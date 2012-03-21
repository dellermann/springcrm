/*
 * purchase-invoice-form.js
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

        $("#vendor").val(item.label);
        $("#vendor\\.id").val(item.value);
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
