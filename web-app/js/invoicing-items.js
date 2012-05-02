/*
 * invoicing-items.js
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


/**
 * @fileOverview    Contains a class to handle invoicing items in quotes,
 *                  sales order, invoices etc.
 * @author          Daniel Ellermann
 * @version         1.0
 */


(function ($, $L) {

    "use strict";

    var $LANG = $L,
        jQuery = $;

    $.widget("springcrm.invoicingitems", {
        INPUT_FIELD_NAMES: [
            "id", "number", "quantity", "unit", "name", "description",
            "unitPrice", "tax"
        ],

        options: {
            currency: "€",
            fieldNamePrefix: "items",
            imgPath: $(".invoicing-items").data("img-path"),
            productListUrl: $(".invoicing-items").data("product-list-url"),
            serviceListUrl: $(".invoicing-items").data("service-list-url"),
            taxes: $(".invoicing-items").data("tax-items").split(","),
            units: $(".invoicing-items").data("units").split(",")
        },

        _addItem: function (jumpToNewRow) {
            var $ = jQuery,
                $L = $LANG,
                $row,
                $tbody = this.$tbodyItems,
                currency,
                imgPath,
                index = this.nextIndex++,
                opts = this.options,
                s;

            currency = opts.currency;
            imgPath = opts.imgPath;
            s = '<tr><td headers="invoicing-items-header-pos" ' +
                'class="invoicing-items-pos">' + String(index + 1) +
                '.</td><td headers="-number" class="invoicing-items-number">' +
                '<input type="text" name="' +
                this._getInputName(index, "number") + '" size="10" /></td>' +
                '<td headers="invoicing-items-header-quantity" ' +
                'class="invoicing-items-quantity"><input type="text" name="' +
                this._getInputName(index, "quantity") + '" size="4" /></td>' +
                '<td headers="invoicing-items-header-unit" ' +
                'class="invoicing-items-unit"><input type="text" name="' +
                this._getInputName(index, "unit") + '" size="5" /></td>' +
                '<td headers="invoicing-items-header-name" ' +
                'class="invoicing-items-name"><input type="text" name="' +
                this._getInputName(index, "name") + '" size="28" />';
            if (opts.productListUrl && imgPath) {
                s += '&nbsp;<a href="javascript:void 0;" ' +
                    'class="select-btn-products"><img src="' + imgPath +
                    '/products.png" alt="' +
                    $L("invoicingTransaction.product.sel") + '" title="' +
                    $L("invoicingTransaction.product.sel") +
                    '" width="16" height="16" ' +
                    'style="vertical-align: middle;" /></a>';
            }
            if (opts.serviceListUrl && imgPath) {
                s += '&nbsp;<a href="javascript:void 0;" ' +
                    'class="select-btn-services">' +
                    '<img src="' + imgPath + '/services.png" alt="' +
                    $L("invoicingTransaction.service.sel") + '" title="' +
                    $L("invoicingTransaction.service.sel") +
                    '" width="16" height="16" ' +
                    'style="vertical-align: middle;" /></a>';
            }
            s += '<br /><textarea name="' +
                this._getInputName(index, "description") +
                '" cols="30" rows="3"></textarea></td>' +
                '<td headers="invoicing-items-header-unit-price" ' +
                'class="invoicing-items-unit-price"><input type="text" name="' +
                this._getInputName(index, "unitPrice") +
                '" size="8" value="0,00" class="currency" />&nbsp;' + currency +
                '</td><td headers="invoicing-items-header-total" ' +
                'class="invoicing-items-total">' +
                '<span class="value">0,00</span>&nbsp;' + currency + '</td>' +
                '<td headers="invoicing-items-header-tax" ' +
                'class="invoicing-items-tax">' +
                '<input type="text" name="' + this._getInputName(index, "tax") +
                '" size="4" />&nbsp;%</td>' +
                '<td class="invoicing-items-buttons">';
            if (imgPath) {
                s += '<a href="javascript:void 0;" class="up-btn"><img src="' +
                    imgPath + '/up.png" alt="' + $L("default.btn.up") +
                    '" title="' + $L("default.btn.up") +
                    '" width="16" height="16" /></a>' +
                    '<a href="javascript:void 0;" class="down-btn"><img src="' +
                    imgPath + '/down.png" alt="' + $L("default.btn.down") +
                    '" title="' + $L("default.btn.down") +
                    '" width="16" height="16" /></a>' +
                    '<a href="javascript:void 0;" class="remove-btn">' +
                    '<img src="' + imgPath + '/remove.png" alt="' +
                    $L("default.delete.label") + '" title="' +
                    $L("default.delete.label") +
                    '" width="16" height="16" /></a>';
            }
            s += '</td></tr>';
            $row = $(s);
            $row.appendTo($tbody);
            this._initUnitAutocomplete(
                $row.find(".invoicing-items-unit input")
            );
            this._initTaxAutocomplete(
                $row.find(".invoicing-items-tax input")
            );
            if (jumpToNewRow) {
                $("html").scrollTop(
                        $row.position().top - $("#toolbar").outerHeight()
                    );
            }
        },

        _addTaxRate: function (taxRates, taxRate, tax) {
            var found = false,
                i = -1,
                n = taxRates.length;

            while (++i < n) {
                if (taxRates[i].taxRate === taxRate) {
                    taxRates[i].tax += tax;
                    found = true;
                    break;
                }
            }
            if (!found) {
                taxRates.push({ taxRate: taxRate, tax: tax });
            }
        },

        _computeFooterValues: function () {
            var $ = jQuery,
                adjustment,
                discount,
                discountPercent,
                shippingCosts,
                subtotalNet = 0,
                total;

            $(".invoicing-items-total .value", this.$tbodyItems)
                .each(function () {
                    subtotalNet += $.parseNumber($(this).text());
                });
            shippingCosts = $.parseNumber(this.$shippingCosts.val());
            this.subtotalNet = subtotalNet + shippingCosts;
            this.$subtotalNet.text($.formatCurrency(this.subtotalNet));

            this._computeTaxValues();

            discountPercent = $.parseNumber(this.$discountPercent.val());
            discount = this.subtotalGross * discountPercent / 100;
            this.$discountFromPercent.text($.formatCurrency(discount));
            discount += $.parseNumber(this.$discountAmount.val());

            adjustment = $.parseNumber(this.$adjustment.val());
            total = this.subtotalGross - discount + adjustment;
            this.total = total;
            this.$total.text($.formatCurrency(total));
            $("#paymentAmount").trigger("change");
        },

        _computeTaxValues: function () {
            var $ = jQuery,
                $L = $LANG,
                currency = this.options.currency,
                i = -1,
                n,
                s = "",
                self = this,
                shippingCosts,
                shippingTax,
                subtotalGross,
                taxRates = [],
                taxTotal = 0,
                tr;

            /* compute a map of tax rates */
            $("input:text[name$='.tax']").each(function () {
                    var els = self.form.elements,
                        name,
                        qty,
                        tax,
                        taxRate,
                        tr = taxRates,
                        unitPrice;

                    name = this.name.replace(/\.tax$/, ".quantity");
                    qty = $.parseNumber(els[name].value);
                    name = this.name.replace(/\.tax$/, ".unitPrice");
                    unitPrice = $.parseNumber(els[name].value);
                    taxRate = $.parseNumber(this.value);
                    if (taxRate !== 0) {
                        tax = qty * unitPrice * taxRate / 100.0;
                        self._addTaxRate(tr, taxRate, tax);
                    }
                });

            /* add the shipping tax to the tax rate map */
            shippingCosts = $.parseNumber(this.$shippingCosts.val());
            shippingTax = $.parseNumber(this.$shippingTax.val());
            if (shippingCosts !== 0 && shippingTax !== 0) {
                this._addTaxRate(
                    taxRates, shippingTax, shippingCosts * shippingTax / 100.0
                );
            }

            taxRates.sort(function (a, b) { return a.taxRate - b.taxRate; });

            /* display the tax rates */
            n = taxRates.length;
            while (++i < n) {
                tr = taxRates[i];
                taxTotal += tr.tax;
                s += '<tr class="tax-rate-sum">' +
                    '<td headers="invoicing-items-header-name" ' +
                    'colspan="5" class="invoicing-items-label"><label>' +
                    $L("invoicingTransaction.taxRate.label").replace(
                        /\{0\}/, $.formatNumber(tr.taxRate, 1)
                    ) +
                    '</label></td>' +
                    '<td headers="invoicing-items-header-unitPrice"></td>' +
                    '<td headers="invoicing-items-header-total" ' +
                    'class="invoicing-items-total">' +
                    $.formatCurrency(tr.tax) + '&nbsp;' + currency + '</td>' +
                    '<td headers="invoicing-items-header-tax"></td><td></td>' +
                    '</tr>';
            }
            $(".tax-rate-sum").remove();
            $("tfoot tr:first").after(s);

            /* compute values */
            this.taxTotal = taxTotal;
            subtotalGross = this.subtotalNet + taxTotal;
            this.subtotalGross = subtotalGross;
            this.$subtotalGross.text($.formatCurrency(subtotalGross));
            return subtotalGross;
        },

        _create: function () {
            var $ = jQuery,
                $form,
                $tbodyItems,
                el = this.element,
                numItems,
                opts = this.options;

            $tbodyItems = el.find(".invoicing-items-body");
            this.$tbodyItems = $tbodyItems;
            numItems = $tbodyItems.find("tr").length;
            this.nextIndex = numItems;

            $form = el.parents("form")
                .submit($.proxy(this._onSubmit, this));
            this.form = $form.get(0);

            this.$shippingCosts = $("#shippingCosts");
            this.$shippingTax = $("#shippingTax");
            this.$subtotalNet = $("#invoicing-items-subtotal-net");
            this.$subtotalGross = $("#invoicing-items-subtotal-gross");
            this.$discountPercent = $("#discountPercent");
            this.$discountFromPercent =
                $("#invoicing-items-discount-from-percent");
            this.$discountAmount = $("#discountAmount");
            this.$adjustment = $("#adjustment");
            this.$total = $("#invoicing-items-total");
            this.subtotalNet = 0.0;
            this.taxTotal = 0.0;
            this.subtotalGross = 0.0;
            this.total = 0.0;

            this.units = opts.units;
            this.taxes = this._prepareTaxes(opts.taxes);

            this.inputRegExp = new RegExp(
                    "^" + opts.fieldNamePrefix + "\\[(\\d+)\\]\\.(\\w+)$"
                );

            el.click($.proxy(this._onClick, this))
                .change($.proxy(this._onChange, this))
                .focusin($.proxy(this._onFocusIn, this))
                .focusout($.proxy(this._onFocusOut, this));
            $(".add-invoicing-item-btn")
                .click($.proxy(
                    function () {
                        this._addItem(true);
                    },
                    this
                ));
            if (numItems === 0) {
                this._addItem(false);
            } else {
                this._initUnitAutocomplete();
                this._initTaxAutocomplete();
            }
            this._computeFooterValues();
        },

        _getInput: function (index, name, suffix) {
            return this.form.elements[this._getInputName(index, name, suffix)];
        },

        _getInputIndexAndName: function (input) {
            var parts,
                res = null;

            parts = input.name.match(this.inputRegExp);
            if (parts) {
                parts.shift();
                res = parts;
            }
            return res;
        },

        _getInputName: function (index, name, suffix) {
            return this.options.fieldNamePrefix + (suffix || "") + "[" +
                index + "]." + (name || "");
        },

        _getNumRows: function () {
            return this.$tbodyItems.find("tr").length;
        },

        _getRowIndex: function ($tr) {
            var input = $tr.find(":input").get(0);

            return Number(this._getInputIndexAndName(input)[0]);
        },

        _getRowPosition: function ($tr) {
            return $tr.parent()
                .children()
                .index($tr);
        },

        _initTaxAutocomplete: function ($input) {
            var taxes = this.taxes;

            if (taxes) {
                if (!$input) {
                    $input = $(".quote-items-tax input");
                }
                $input.autocomplete({ source: taxes });
            }
        },

        _initUnitAutocomplete: function ($input) {
            var units = this.units;

            if (units) {
                if (!$input) {
                    $input = $(".invoicing-items-unit input");
                }
                $input.autocomplete({ source: units });
            }
        },

        _loadInventorySelector: function (type, url, index, params) {
            var self = this;

            $.ajax({
                url: url,
                data: params,
                success: function (html) {
                    self._onLoadInventorySelector(html, type, index);
                }
            });
        },

        _moveItem: function ($a, dir) {
            var $destTr = null,
                $tr = $a.parents("tr"),
                pos;

            /* swap current row with previous or next row */
            pos = this._getRowPosition($tr);
            if ((dir < 0) && (pos > 0)) {
                $destTr = $tr.prev();
                $destTr.before($tr);
            } else if (pos < this._getNumRows() - 1) {
                $destTr = $tr.next();
                $destTr.after($tr);
            }

            /* swap input name positions and item positions */
            if ($destTr) {
                this._swapInputItemPos($tr, $destTr);
                this._swapItemPos($tr, $destTr);
            }
        },

        _onChange: function (event) {
            var $ = jQuery,
                $tr,
                input = event.target,
                parts,
                index,
                qty,
                unitPrice;

            parts = this._getInputIndexAndName(input);
            if (parts) {
                index = parts[0];
                $tr = $(input).parents("tr");
                switch (parts[1]) {
                case "quantity":
                    qty = $.parseNumber(input.value);
                    unitPrice = $.parseNumber(
                        this._getInput(index, "unitPrice").value
                    );
                    $tr.find(".invoicing-items-total .value")
                        .text($.formatCurrency(qty * unitPrice));
                    break;
                case "unitPrice":
                    unitPrice = $.parseNumber(input.value);
                    qty = $.parseNumber(
                        this._getInput(index, "quantity").value
                    );
                    $tr.find(".invoicing-items-total .value")
                        .text($.formatCurrency(qty * unitPrice));
                    break;
                }
            }

            this._computeFooterValues();
        },

        _onClick: function (event) {
            var $a = $(event.target).closest("a");

            if ($a.hasClass("up-btn")) {
                this._moveItem($a, -1);
            } else if ($a.hasClass("down-btn")) {
                this._moveItem($a, 1);
            } else if ($a.hasClass("remove-btn")) {
                this._removeItem($a);
            } else if ($a.hasClass("select-btn-products")) {
                this._showInventorySelector($a, "products");
            } else if ($a.hasClass("select-btn-services")) {
                this._showInventorySelector($a, "services");
            }
            return true;
        },

        _onFocusIn: function (event) {
            var $ = jQuery,
                $target = $(event.target),
                val;

            if ($target.hasClass("currency")) {
                val = $.parseNumber($target.val());
                $target.val(val ? $.formatNumber(val, null) : "");
            }
        },

        _onFocusOut: function (event) {
            var $ = jQuery,
                $target = $(event.target);

            if ($target.hasClass("currency")) {
                $target.val($.formatCurrency($.parseNumber($target.val())));
            }
        },

        _onLoadInventorySelector: function (html, type, index) {
            var $ = jQuery,
                $form = null,
                $selector,
                getData = function () {
                    var data = null,
                        search = $form.get(0).search.value;

                    if ((search !== $L("default.search.label")) &&
                        (search !== ""))
                    {
                        data = { search: search };
                    }
                    return data;
                };

            $selector = $("#inventory-selector-" + type);
            $selector.html(html)
                .unbind("click")
                .click($.proxy(
                    function (event) {
                        var $target,
                            target = event.target;

                        if (target.tagName.toLowerCase() !== "a") {
                            return true;
                        }
                        $target = $(target);
                        if ($target.hasClass("select-link")) {
                            this._retrieveInventoryItem(
                                type, $target.attr("href"), index
                            );
                        } else {
                            this._loadInventorySelector(
                                type, $target.attr("href"), index, getData()
                            );
                        }
                        return false;
                    },
                    this
                ));
            $form = $selector.find("form");
            $form.submit($.proxy(
                function (event) {
                    this._loadInventorySelector(
                        type, event.target.action, index, getData()
                    );
                    return false;
                },
                this
            ));
            $("#selector-search").hint($L("default.search.label"));
            $selector.dialog({ minWidth: 700, minHeight: 400, modal: true });
        },

        _onSubmit: function () {
            var $name = null,
                b,
                del,
                e,
                elems = this.form.elements,
                fieldName,
                fieldNames = this.INPUT_FIELD_NAMES,
                i = -1,
                name,
                n = fieldNames.length,
                val,
                x;

            while (++i < this.nextIndex) {
                name = this._getInputName(i);

                del = true;
                x = -1;
                while (++x < n) {
                    fieldName = fieldNames[x];
                    e = elems[name + fieldName];
                    if (e && (fieldName !== "id")) {
                        val = e.value;
                        b = val === "";
                        if ((fieldName === "quantity")
                            || (fieldName === "unitPrice")
                            || (fieldName === "tax"))
                        {
                            b = b || $.parseNumber(val) === 0;
                        } else if (fieldName === "name") {
                            $name = $(e);
                        }
                        del = del && b;
                        if (!del) {
                            break;
                        }
                    }
                }

                if (del) {
                    if ($name) {
                        this._removeRow($name.parents("tr"));
                    }
                    e = elems[name + "id"];
                    if (e) {
                        e.value = "null";
                    }
                }
            }
            return true;
        },

        _prepareTaxes: function (taxes) {
            var $,
                i = -1,
                n,
                tax;

            if (taxes) {
                $ = jQuery;
                n = taxes.length;
                while (++i < n) {
                    tax = taxes[i];
                    if (typeof(tax) === "number") {
                        taxes[i] = $.formatNumber(tax * 100);
                    }
                }
            }
            return taxes;
        },

        _removeItem: function ($a) {
            var numRows;

            numRows = this.$tbodyItems
                .find("tr")
                .size();
            if (numRows > 1) {
                this._removeRow($a.parents("tr"));
                this._computeFooterValues();
            }
        },

        _removeRow: function ($tr) {
            var $ = jQuery,
                el,
                fieldPrefix = this.options.fieldNamePrefix,
                index = this._getRowIndex($tr),
                pos = this._getRowPosition($tr),
                re = this.inputRegExp;

            /* unset the ID value to cause Grails delete the record */
            el = this._getInput(index, "id");
            if (el) {
                el.value = "null";
            }

            /* fix row position labels and input names of all successing rows */
            $tr.nextAll()
                    .each(function (i) {
                        var $this = $(this),
                            idx = index,
                            p = pos,
                            prefix = fieldPrefix,
                            regexp = re;

                        $this.find("td:first-child")
                            .text(String(p + i + 1) + ".");

                        /*
                         * input names are fixed after deleting new items, only
                         */
                        if (!el) {
                            $this.find(":input")
                                .each(function () {
                                    var parts = this.name.match(regexp);

                                    if (parts) {
                                        this.name = prefix + "["
                                            + String(idx + i) + "]."
                                            + parts[2];
                                    }
                                });
                        }
                    })
                .end()
                .remove();
        },

        _retrieveInventoryItem: function (type, url, index) {
            var self = this;

            $.ajax({
                url: url,
                dataType: "json",
                success: function (data) {
                    var els,
                        item,
                        prefix,
                        qty,
                        unitPrice,
                        unitPriceInput;

                    prefix = self._getInputName(index);
                    els = self.form.elements;
                    els[prefix + "number"].value = data.fullNumber;
                    item = data.inventoryItem;
                    qty = item.quantity;
                    els[prefix + "quantity"].value = $.formatNumber(qty, null);
                    els[prefix + "unit"].value = item.unit.name;
                    els[prefix + "name"].value = item.name;
                    els[prefix + "description"].value = item.description;
                    unitPrice = item.unitPrice;
                    unitPriceInput = els[prefix + "unitPrice"];
                    unitPriceInput.value = $.formatCurrency(unitPrice);
                    $(unitPriceInput).parents("tr")
                        .find(".invoicing-items-total .value")
                            .text($.formatCurrency(qty * unitPrice));
                    els[prefix + "tax"].value =
                        $.formatNumber(item.taxRate.taxValue * 100.0, 1);
                    self._computeFooterValues();
                    $("#inventory-selector-" + type).dialog("close");
                }
            });
        },

        _showInventorySelector: function ($a, type, url) {
            var index,
                opts = this.options;

            if (!url) {
                url = (type === "products") ? opts.productListUrl
                        : opts.serviceListUrl;
            }
            if (url) {
                index = this._getRowIndex($a.parents("tr"));
                this._loadInventorySelector(type, url, index);
            }
        },

        _swapInputItemPos: function($tr, $destTr) {
            var destIndex,
                f,
                form = this.form,
                index,
                self = this;

            index = this._getRowIndex($tr);
            destIndex = this._getRowIndex($destTr);

            f = function (name, newName) {
                var el,
                    elems = form.elements,
                    fieldName,
                    fieldNames = self.INPUT_FIELD_NAMES,
                    i = -1,
                    n = fieldNames.length;

                while (++i < n) {
                    fieldName = fieldNames[i];
                    el = elems[name + fieldName];
                    if (el) {
                        el.name = newName + fieldName;
                    }
                }
            };
            f(
                this._getInputName(index),
                this._getInputName(destIndex, "", "-dest")
            );
            f(
                this._getInputName(destIndex),
                this._getInputName(index)
            );
            f(
                this._getInputName(destIndex, "", "-dest"),
                this._getInputName(destIndex)
            );
        },

        _swapItemPos: function ($tr, $destTr) {
            var $destTd = $destTr.find("td:first-child"),
                $td = $tr.find("td:first-child"),
                s = $td.text();

            $td.text($destTd.text());
            $destTd.text(s);
        }
    });

}(jQuery, $L));
