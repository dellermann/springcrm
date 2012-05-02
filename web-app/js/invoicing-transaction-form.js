/*
 * invoicing-transaction-form.js
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


(function (window, $, $L) {

    "use strict";

    var $LANG = $L,
        jQuery = $;

    $.widget("springcrm.invoicingtransaction", {

        getOrganizationId: function () {
            return { organization: $(this.options.organizationId).val() };
        },

        options: {
            checkStageTransition: true,
            organizationId: "#organization\\.id",
            stageValues: null
        },

        _create: function () {
            var $ = jQuery,
                $L = $LANG,
                el = this.element;

            el.find(".invoicing-items")
                    .invoicingitems()
                .end()
                .find("#organization")
                    .autocompleteex({
                        select: $.proxy(this._onSelectOrganization, this)
                    })
                .end()
                .find("#person")
                    .autocompleteex({
                        loadParameters: this.getOrganizationId
                    });
            this.$stillUnpaid = el.find("#still-unpaid")
                .click($.proxy(this._onClickStillUnpaid, this));
            this.$paymentAmount = el.find("#paymentAmount")
                .change($.proxy(this._onChangePaymentAmount, this));
            this.$paymentAmount.trigger("change");
            this.$addresses = el.find("#addresses")
                .addrfields({
                    leftPrefix: "billingAddr",
                    menuItems: [
                        {
                            action: "copy", side: "left",
                            text: $L("invoicingTransaction.billingAddr.copy")
                        },
                        {
                            action: "loadFromOrganization",
                            propPrefix: "billingAddr",
                            side: "left",
                            text: $L("invoicingTransaction.addr.fromOrgBillingAddr")
                        },
                        {
                            action: "copy", side: "right",
                            text: $L("invoicingTransaction.shippingAddr.copy")
                        },
                        {
                            action: "loadFromOrganization",
                            propPrefix: "shippingAddr",
                            side: "right",
                            text: $L("invoicingTransaction.addr.fromOrgShippingAddr")
                        }
                    ],
                    organizationId: this.options.organizationId,
                    rightPrefix: "shippingAddr"
                });
            this.$stage = el.find("#stage");
            this.$shippingDate = $("#shippingDate-date");
            this.$paymentDate = $("#paymentDate-date");

            this._initStageValues();
        },

        _getModifiedClosingBalance: function ($a) {
            $a = $a || this.$stillUnpaid;
            return parseFloat($a.data("closing-balance"));
        },

        _getTotal: function () {
            var $ = jQuery;

            return $.parseNumber($("#invoicing-items-total").text());
        },

        _getUnpaid: function () {
            var sgn = (this.options.type === "C") ? 1 : -1;

            return -sgn * (
                    this._getTotal()
                    + sgn * $.parseNumber(this.$paymentAmount.val())
                    - this._getModifiedClosingBalance(this.$stillUnpaid)
                );
        },

        _initStageValues: function () {
            var $ = jQuery,
                opts = this.options,
                stageValues;

            stageValues = opts.stageValues;
            if (stageValues) {
                this.$stage.change($.proxy(this._onChangeStage, this));
                if (stageValues.shipping) {
                    this.$shippingDate
                        .change($.proxy(this._onChangeShippingDate, this));
                    if (opts.checkStageTransition) {
                        this.element.submit($.proxy(this._onSubmitForm, this));
                    }
                }
                if (stageValues.payment) {
                    this.$paymentDate
                        .change($.proxy(this._onChangePaymentDate, this));
                }
            }

            return this;
        },

        _onChangePaymentAmount: function () {
            var $ = jQuery,
                $stillUnpaid = this.$stillUnpaid,
                val = this._getUnpaid();

            $stillUnpaid
                .removeClass("still-unpaid-unpaid still-unpaid-paid still-unpaid-too-much")
                .addClass(
                    (val > 0) ? "still-unpaid-unpaid"
                        : ((val < 0) ? "still-unpaid-too-much" : "still-unpaid-paid")
                )
                .find("span")
                    .text($.formatCurrency(val));
        },

        _onChangePaymentDate: function (event) {
            var $stage = this.$stage,
                v = this.options.stageValues.payment;

            if ($(event.target).val() !== "" && $stage.val() < v) {
                $stage.val(v);
            }
        },

        _onChangeShippingDate: function (event) {
            var $stage = this.$stage,
                v = this.options.stageValues.shipping;

            if ($(event.target).val() !== "" && $stage.val() < v) {
                $stage.val(v);
            }
        },

        _onChangeStage: function (event) {
            var stageValues = this.options.stageValues;

            switch (parseInt($(event.target).val(), 10)) {
            case stageValues.shipping:
                this.$shippingDate
                    .populateDate();
                break;
            case stageValues.payment:
                this.$paymentDate
                    .populateDate();
                break;
            }
        },

        _onClickStillUnpaid: function (event) {
            var $ = jQuery,
                $paymentAmount = this.$paymentAmount,
                unpaid = this._getUnpaid(),
                val;

            if (unpaid > 0) {
                val = this._getTotal()
                    - this._getModifiedClosingBalance($(event.currentTarget));
                if (this.options.type === "C") {
                    val = 2 * $.parseNumber($paymentAmount.val()) - val;
                }
                if (val) {
                    $paymentAmount.val($.formatCurrency(val))
                        .trigger("change");
                }
            }
            return false;
        },

        _onSelectOrganization: function () {
            this.$addresses
                .addrfields("loadFromOrganizationToLeft", "billingAddr")
                .addrfields("loadFromOrganizationToRight", "shippingAddr");
        },

        _onSubmitForm: function () {
            var $oldStage = $("#old-stage"),
                newVal,
                oldVal,
                res = true,
                shippingStageValue = this.options.stageValues.shipping;

            if ($oldStage.length) {
                oldVal = $oldStage.val();
                if (oldVal > 0) {
                    newVal = this.$stage.val();
                    if ((oldVal < shippingStageValue)
                        && (newVal >= shippingStageValue))
                    {
                        res = window.confirm(
                            $L("invoicingTransaction.changeState.label")
                        );
                    }
                }
            }
            return res;
        }
    });
}(window, jQuery, $L));
