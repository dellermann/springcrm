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


SPRINGCRM.invoicingTransaction = (function (window, $, $L) {

    "use strict";

    var $LANG = $L,
        $addresses = $("#addresses"),
        getOrganizationId,
        init,
        initConfig = null,
        initStageValues = null,
        jQuery = $,
        onChangePaymentAmount = null,
        onChangePaymentDate = null,
        onChangeShippingDate = null,
        onChangeStage = null,
        onClickStillUnpaid = null,
        onSelectOrganization = null,
        onSubmitForm = null,
        organizationId = "#organization\\.id";

    getOrganizationId = function () {
        return { organization: $(organizationId).val() };
    };

    init = function (config) {
        var $ = jQuery,
            $L = $LANG,
            $invoicingItems = $(".invoicing-items"),
            taxes,
            units;

        config = initConfig.call(this, config);

        taxes = $invoicingItems.attr("data-tax-items")
            .split(",");
        units = $invoicingItems.attr("data-units")
            .split(",");
        $invoicingItems.invoicingitems({
                imgPath: config.imgPath,
                productListUrl: config.productListUrl,
                serviceListUrl: config.serviceListUrl,
                taxes: taxes,
                units: units
            });

        $("#organization").autocompleteex({
                select: onSelectOrganization
            });
        $("#person").autocompleteex({
                loadParameters: getOrganizationId
            });
        $("#paymentAmount").change(onChangePaymentAmount)
            .trigger("change");
        $("#still-unpaid").click(onClickStillUnpaid);

        $addresses.addrfields({
                leftPrefix: "billingAddr",
                loadOrganizationUrl: config.loadOrganizationUrl,
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
                organizationId: organizationId,
                rightPrefix: "shippingAddr"
            });

        initStageValues.call(this);

        return this;
    };

    initConfig = function (config) {
        config = $.extend(
                {
                    checkStageTransition: true,
                    form: null,
                    imgPath: null,
                    loadOrganizationUrl: null,
                    productListUrl: null,
                    serviceListUrl: null,
                    stageValues: {
                        payment: null,
                        shipping: null
                    }
                },
                config
            );
        this.config = config;
        return config;
    };

    initStageValues = function (config) {
        var $ = jQuery,
            stageValues;

        config = config ? initConfig.call(this, config) : this.config;
        stageValues = config.stageValues;
        if (stageValues) {
            $("#stage").change($.proxy(onChangeStage, this));
            if (stageValues.shipping) {
                $("#shippingDate-date")
                    .change($.proxy(onChangeShippingDate, this));
                if (config.checkStageTransition) {
                    config.form.submit($.proxy(onSubmitForm, this));
                }
            }
            if (stageValues.payment) {
                $("#paymentDate-date")
                    .change($.proxy(onChangePaymentDate, this));
            }
        }

        return this;
    };

    onChangePaymentAmount = function () {
        var $ = jQuery,
            totalVal = $("#invoicing-items-total").text(),
            val = $(this).val();

        val = $.parseNumber(totalVal) - $.parseNumber(val);
        $("#still-unpaid")
            .removeClass("still-unpaid-unpaid still-unpaid-paid still-unpaid-too-much")
            .addClass(
                (val > 0) ? "still-unpaid-unpaid"
                    : ((val < 0) ? "still-unpaid-too-much" : "still-unpaid-paid")
            )
            .find("span")
                .text($.formatCurrency(val));
    };

    onChangePaymentDate = function (event) {
        var $stage = $("#stage"),
            v = this.config.stageValues.payment;

        if ($(event.target).val() !== "" && $stage.val() < v) {
            $stage.val(v);
        }
    };

    onChangeShippingDate = function (event) {
        var $stage = $("#stage"),
            v = this.config.stageValues.shipping;

        if ($(event.target).val() !== "" && $stage.val() < v) {
            $stage.val(v);
        }
    };

    onChangeStage = function (event) {
        var stageValues = this.config.stageValues;

        switch (parseInt($(event.target).val(), 10)) {
        case stageValues.shipping:
            $("#shippingDate-date").populateDate();
            break;
        case stageValues.payment:
            $("#paymentDate-date").populateDate();
            break;
        }
    };

    onClickStillUnpaid = function () {
        $("#paymentAmount").val($("#invoicing-items-total").text())
            .trigger("change");
        return false;
    };

    onSelectOrganization = function () {
        $addresses.addrfields("loadFromOrganizationToLeft", "billingAddr")
            .addrfields("loadFromOrganizationToRight", "shippingAddr");
    };

    onSubmitForm = function () {
        var $oldStage = $("#old-stage"),
            newVal,
            oldVal,
            res = true,
            shippingStageValue = this.config.stageValues.shipping;

        if ($oldStage.length) {
            oldVal = $oldStage.val();
            if (oldVal > 0) {
                newVal = $("#stage").val();
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
    };

    return {
        getOrganizationId: getOrganizationId,
        init: init,
        initStageValues: initStageValues
    };
}(window, jQuery, $L));
