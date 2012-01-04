(function (SPRINGCRM, $) {

    "use strict";

    var initPurchaseInvoice,
        jQuery = $,
        onClickFileRemove,
        onFocusVendor,
        onLoadVendors,
        onSelectVendor;

    onClickFileRemove = function () {
        var $ = jQuery;

        $("#fileRemove").val(1);
        $(".document-preview").remove();
        $(".document-preview-links").remove();
    };

    onFocusVendor = function (event, ui) {
        $(event.target).val(ui.item.label);
        return false;
    };

    onLoadVendors = function (request, response) {
        $.getJSON(
                this.config.loadVendorsUrl, { name: request.term },
                function (data) {
                    response($.map(data, function (item) {
                        return { label: item.name, value: item.id };
                    }));
                }
            );
    };

    onSelectVendor = function (event, ui) {
        var $ = jQuery,
            item = ui.item;

        $("#vendorName").val(item.label);
        $("#vendor-id").val(item.value);
        return false;
    };

    initPurchaseInvoice = function (config) {
        var $ = jQuery;

        this.init(config);

        $("#vendorName").autocomplete({
                focus: $.proxy(onFocusVendor, this),
                select: $.proxy(onSelectVendor, this),
                source: $.proxy(onLoadVendors, this)
            });
        $(".document-delete").wrapInner(
                $('<a href="#">').click(onClickFileRemove)
            );
    };

    $.extend(SPRINGCRM.invoicingTransaction, {
        initPurchaseInvoice: initPurchaseInvoice
    });

}(SPRINGCRM, jQuery));
