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


(function ($) {

    "use strict";

    var jQuery = $;

    $.widget("springcrm.purchaseinvoice", $.springcrm.invoicingtransaction, {

        options: {
            loadVendorsUrl: null
        },

        _create: function () {
            var $ = jQuery;

            $.springcrm.invoicingtransaction.prototype._create.call(this);
            this.element
                .find(".price-table")
                    .invoicingitems(
                        "option",
                        { productListUrl: null, serviceListUrl: null }
                    );
            $("#vendorName").autocomplete({
                    focus: this._onFocusVendor,
                    select: this._onSelectVendor,
                    source: $.proxy(this._onLoadVendors, this)
                });
            $(".document-delete").wrapInner(
                    $('<a href="#">').click(this._onClickFileRemove)
                );
        },

        _onClickFileRemove: function () {
            var $ = jQuery;

            $("#fileRemove").val(1);
            $(".document-preview").remove();
            $(".document-preview-links").remove();
        },

        _onFocusVendor: function (event, ui) {
            $(event.target).val(ui.item.label);
            return false;
        },

        _onLoadVendors: function (request, response) {
            var $ = jQuery,
                url = this.options.loadVendorsUrl;

            if (url) {
                $.getJSON(
                        url, { name: request.term },
                        function (data) {
                            response($.map(data, function (item) {
                                return { label: item.name, value: item.id };
                            }));
                        }
                    );
            }
        },

        _onSelectVendor: function (event, ui) {
            var $ = jQuery,
                item = ui.item;

            $("#vendor").val(item.label);
            $("#vendor\\.id").val(item.value);
            return false;
        }
    });
}(jQuery));
