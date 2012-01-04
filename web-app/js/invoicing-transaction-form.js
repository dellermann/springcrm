SPRINGCRM.invoicingTransaction = (function (window, $, $L) {

    "use strict";

    var $LANG = $L,
        $addresses = $("#addresses"),
        getOrganizationId,
        init,
        jQuery = $,
        onChangePaymentDate = null,
        onChangeShippingDate = null,
        onChangeStage = null,
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
            stageValues,
            taxes,
            units;

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

        stageValues = config.stageValues;
        if (stageValues) {
            $("#stage").change($.proxy(onChangeStage, this));
            if (stageValues.shipping) {
                $("#shippingDate-date")
                    .change($.proxy(onChangeShippingDate, this));
                if (config.checkStageTransition) {
                    $("#creditMemo-form").submit($.proxy(onSubmitForm, this));
                }
            }
            if (stageValues.payment) {
                $("#paymentDate-date")
                    .change($.proxy(onChangePaymentDate, this));
            }
        }

        return this;
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

        switch ($(event.target).val()) {
        case stageValues.shipping:
            $("#shippingDate-date").populateDate();
            break;
        case stageValues.payment:
            $("#paymentDate-date").populateDate();
            break;
        }
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
        init: init
    };
}(window, jQuery, $L));
