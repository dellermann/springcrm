/**
 * invoicing-items.js
 * Scripting for invoicing items in quotes, orders, and invoices.
 *
 * $Id: styles.css 1234 2011-06-21 20:24:44Z dellermann $
 *
 * Copyright (c) 2011, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


/**
 * @fileOverview    Contains a class to handle invoicing items in quotes,
 *                  sales order, invoices etc.
 * @author          Daniel Ellermann
 * @version         0.9
 */


(function (window, SPRINGCRM, $) {

    "use strict";

    var InvoicingItems;


    //== Classes ================================

    /**
     * Creates a new invoicing item list.
     *
     * @class                                   Represents a list of invoicing
     *                                          items used in quotes, orders,
     *                                          or invoices.
     * @constructor
     * @param {Object} config                   configuration data for this
     *                                          invoicing items list
     * @param {String} config.baseName          the base name used to generate
     *                                          the table ID and form name
     * @param {String} config.tableId           the ID of the table containing
     *                                          the invoicing items; defaults
     *                                          to "{baseName}-items"
     * @param {String} config.formName          the name of the form containing
     *                                          the invoicing items; defaults
     *                                          to "{baseName}-form"
     * @param {String} config.imgPath           the base path to the folder
     *                                          containing the images which are
     *                                          used for rendering the
     *                                          invoicing items
     * @param {String} config.productListUrl    the URL used to retrieve the
     *                                          products of the CRM
     * @param {String} config.serviceListUrl    the URL used to retrieve the
     *                                          services of the CRM
     */
    InvoicingItems = function InvoicingItems(config) {
        var formName = config.formName || config.baseName + "-form";

        /* handle function call without new */
        if (!(this instanceof InvoicingItems)) {
            return new InvoicingItems(config);
        }


        //-- Instance variables -----------------

        /**
         * The ID of the table containing the invoicing items.
         *
         * @type String
         * @default "{baseName}-items"
         */
        this._tableId = config.tableId || config.baseName + "-items";

        /**
         * The base path to the folder containing the images which are used for
         * rendering the invoicing items.
         *
         * @type String
         */
        this._imgPath = config.imgPath;

        /**
         * The URL used to retrieve the products of the CRM.
         *
         * @type String
         */
        this._productListUrl = config.productListUrl;

        /**
         * The URL used to retrieve the services of the CRM.
         *
         * @type String
         */
        this._serviceListUrl = config.serviceListUrl;

        /**
         * The form containing the invoicing items.
         *
         * @type Object
         */
        this._form = window.document.forms[formName];

        /**
         * The table containing the invoicing items.
         *
         * @type JQueryObject
         */
        this._$table = $("#" + this._tableId);

        /**
         * The table body containing the invoicing items.
         *
         * @type JQueryObject
         */
        this._$tbodyItems = $("#invoicing-items");

        /**
         * The field containing the subtotal net value.
         *
         * @type JQueryObject
         */
        this._$subtotalNet = $("#invoicing-items-subtotal-net");

        /**
         * The field containing the subtotal gross value.
         *
         * @type JQueryObject
         */
        this._$subtotalGross = $("#invoicing-items-subtotal-gross");

        /**
         * The field containing the discount percentage value.
         *
         * @type JQueryObject
         */
        this._$discountPercent = $("#discountPercent");

        /**
         * The field containing the discount value computed from the discount
         * percentage value.
         *
         * @type JQueryObject
         */
        this._$discountFromPercent = $("#invoicing-items-discount-from-percent");

        /**
         * The field containing the fixed discount amount value.
         *
         * @type JQueryObject
         */
        this._$discountAmount = $("#discountAmount");

        /**
         * The field containing the shipping costs value.
         *
         * @type JQueryObject
         */
        this._$shippingCosts = $("#shippingCosts");

        /**
         * The field containing the shipping tax value.
         *
         * @type JQueryObject
         */
        this._$shippingTax = $("#shippingTax");

        /**
         * The field containing the adjustment value.
         *
         * @type JQueryObject
         */
        this._$adjustment = $("#adjustment");

        /**
         * The field containing the total value.
         *
         * @type JQueryObject
         */
        this._$total = $("#invoicing-items-total");

        /**
         * The currently computed subtotal net value.
         *
         * @type Number
         * @default 0.0
         */
        this._subtotalNet = 0.0;

        /**
         * The currently computed subtotal gross value.
         *
         * @type Number
         * @default 0.0
         */
        this._subtotalGross = 0.0;

        /**
         * The currently computed total taxes.
         *
         * @type Number
         * @default 0.0
         */
        this._taxTotal = 0.0;

        /**
         * The currently computed total (gross) value.
         *
         * @type Number
         * @default 0.0
         */
        this._total = 0.0;
    };
    InvoicingItems.prototype = {

        //-- Public methods ---------------------

        /**
         * Initializes the invoicing items table.
         */
        init: function () {
            this._$table
                .click($.proxy(this._onClick, this))
                .change($.proxy(this._onChange, this))
                .focusin($.proxy(this._onFocusIn, this))
                .focusout($.proxy(this._onFocusOut, this));
            $("#add-invoicing-item-btn")
                .click($.proxy(this._addInvoicingItem, this));
            if (this._$tbodyItems.find("tr").length == 0) {
                this._addInvoicingItem();
            }
            this._computeFooterValues();
        },


        //-- Non-public methods -----------------

        /**
         * Adds a table row for a new invoicing item and appends it to the end
         * of the table body.
         *
         * @protected
         */
        _addInvoicingItem: function () {
            var $tbody = this._$tbodyItems,
                gm = SPRINGCRM.getMessage,
                imgPath,
                pos,
                s,
                sPos,
                table;

            table = this._tableId;
            imgPath = this._imgPath;
            pos = $tbody.find("tr")
                .length;
            sPos = String(pos);
            s = '<tr><td headers="' + table +
                '-pos" class="invoicing-items-pos">' + String(pos + 1) +
                '.</td><td headers="' + table +
                '-number" class="invoicing-items-number">' +
                '<input type="text" name="items[' + sPos +
                '].number" size="10" /></td><td headers="' + table +
                '-quantity" class="invoicing-items-quantity">' +
                '<input type="text" name="items[' + sPos +
                '].quantity" size="4" /></td><td headers="' + table +
                '-unit" class="invoicing-items-unit">' +
                '<input type="text" name="items[' + sPos +
                '].unit" size="5" /></td><td headers="' + table +
                '-name" class="invoicing-items-name">' +
                '<input type="text" name="items[' + sPos +
                '].name" size="28" />&nbsp;<a href="javascript:void 0;" ' +
                'class="select-btn-products"><img src="' + imgPath +
                '/products.png" alt="' + gm("productSel") + '" title="' +
                gm("productSel") +
                '" width="16" height="16" style="vertical-align: middle;" /></a>' +
                '&nbsp;<a href="javascript:void 0;" class="select-btn-services">' +
                '<img src="' + imgPath + '/services.png" alt="' +
                gm("serviceSel") + '" title="' + gm("serviceSel") +
                '" width="16" height="16" style="vertical-align: middle;" /></a>' +
                '<br /><textarea name="items[' + sPos +
                '].description" cols="30" rows="3"></textarea></td><td headers="' +
                table + '-unit-price" class="invoicing-items-unit-price">' +
                '<input type="text" name="items[' + sPos +
                '].unitPrice" size="8" value="0,00" class="currency" />&nbsp;€</td>' +
                '<td headers="' + table + '-total" class="invoicing-items-total">' +
                '<span class="value">0,00</span>&nbsp;€</td>' +
                '<td headers="' + table + '-tax" class="invoicing-items-tax">' +
                '<input type="text" name="items[' + sPos +
                '].tax" size="4" />&nbsp;%</td>' +
                '<td class="invoicing-items-buttons">' +
                '<a href="javascript:void 0;" class="up-btn"><img src="' +
                imgPath + '/up.png" alt="' + gm("upBtn") + '" title="' +
                gm("upBtn") + '" width="16" height="16" /></a>' +
                '<a href="javascript:void 0;" class="down-btn"><img src="' +
                imgPath + '/down.png" alt="' + gm("downBtn") + '" title="' +
                gm("downBtn") + '" width="16" height="16" /></a>' +
                '<a href="javascript:void 0;" class="remove-btn"><img src="' +
                imgPath + '/remove.png" alt="' + gm("removeBtn") + '" title="' +
                gm("removeBtn") + '" width="16" height="16" /></a></td></tr>';
            $tbody.append(s);
        },

        /**
         * Computes the values in the table footer such as subtotal, taxes,
         * total etc. and prints the values in the table footer.
         *
         * @protected
         */
        _computeFooterValues: function () {
            var adjustment,
                discount,
                discountPercent,
                shippingCosts,
                subtotalNet = 0,
                total;

            $(".invoicing-items-total .value", this._$tbodyItems)
                .each(function () {
                    subtotalNet += $.parseNumber($(this).text());
                });
            shippingCosts = $.parseNumber(this._$shippingCosts.val());
            this._subtotalNet = subtotalNet + shippingCosts;
            this._$subtotalNet.text($.formatCurrency(this._subtotalNet));

            this._computeTaxValues();

            discountPercent = $.parseNumber(this._$discountPercent.val());
            discount = this._subtotalGross * discountPercent / 100;
            this._$discountFromPercent.text($.formatCurrency(discount));
            discount += $.parseNumber(this._$discountAmount.val());

            adjustment = $.parseNumber(this._$adjustment.val());
            total = this._subtotalGross - discount + adjustment;
            this._total = total;
            this._$total.text($.formatCurrency(total));
        },

        /**
         * Computes the tax classes and the tax values.
         *
         * @returns {Number}    the computed subtotal gross value
         * @protected
         */
        _computeTaxValues: function () {
            var gm = SPRINGCRM.getMessage,
                i,
                n,
                s = "",
                shippingCosts,
                shippingTax,
                subtotalGross,
                taxRates = [],
                taxTotal = 0,
                tr;

            /* compute a map of tax rates */
            $("input:text[name$='.tax']").each(function () {
                    var els,
                        form = this.form,
                        name,
                        qty,
                        tax,
                        taxRate,
                        tr = taxRates,
                        unitPrice;

                    els = form.elements;
                    name = this.name.replace(/\.tax$/, ".quantity");
                    qty = $.parseNumber(els[name].value);
                    name = this.name.replace(/\.tax$/, ".unitPrice");
                    unitPrice = $.parseNumber(els[name].value);
                    taxRate = $.parseNumber(this.value);
                    if (taxRate !== 0) {
                        tax = qty * unitPrice * taxRate / 100.0;
                        InvoicingItems._addTaxRate(tr, taxRate, tax);
                    }
                });

            /* add the shipping tax to the tax rate map */
            shippingCosts = $.parseNumber(this._$shippingCosts.val());
            shippingTax = $.parseNumber(this._$shippingTax.val());
            if (shippingCosts !== 0 && shippingTax !== 0) {
                InvoicingItems._addTaxRate(
                    taxRates, shippingTax, shippingCosts * shippingTax / 100.0
                );
            }

            taxRates.sort(function (a, b) { return a.taxRate - b.taxRate; });

            /* display the tax rates */
            for (i = -1, n = taxRates.length; ++i < n; ) {
                tr = taxRates[i];
                taxTotal += tr.tax;
                s += '<tr class="tax-rate-sum"><td headers="quote-items-name" ' +
                    'colspan="5" class="invoicing-items-label"><label>' +
                    gm("taxRateLabel").replace(
                        /\{0\}/, $.formatNumber(tr.taxRate, 1)
                    ) +
                    '</label></td>' +
                    '<td headers="quote-items-unitPrice"></td>' +
                    '<td headers="quote-items-total" class="invoicing-items-total">' +
                    $.formatCurrency(tr.tax) + '&nbsp;€</td>' +
                    '<td headers="quote-items-tax"></td>' +
                    '<td></td>' +
                    '</tr>';
            }
            $(".tax-rate-sum").remove();
            $("tfoot tr:first").after(s);

            /* compute values */
            this._taxTotal = taxTotal;
            subtotalGross = this._subtotalNet + taxTotal;
            this._subtotalGross = subtotalGross;
            this._$subtotalGross.text($.formatCurrency(subtotalGross));
            return subtotalGross;
        },

        /**
         * Displays a dialog window and load inventory items such as products
         * or services for display within the dialog.
         *
         * @param {String} type the type of inventory items which are to load;
         *                      possible values are "products" and "services"
         * @param {String} url  the URL used to load the inventory items
         * @param {Number} pos  the zero-based index of the row in which the
         *                      selected inventory item is to place
         * @private
         */
        _loadInventorySelector: function (type, url, pos) {
            $.ajax({
                url: url,
                context: { invoicingItems: this, type: type, pos: pos },
                success: function (html) {
                    var pos = this.pos,
                        type = this.type;

                    $("#inventory-selector-" + type).html(html)
                        .unbind("click")
                        .click($.proxy(
                            function (e) {
                                var $target,
                                    target;

                                target = e.target;
                                if (target.tagName.toLowerCase() !== "a") {
                                    return true;
                                }
                                $target = $(target);
                                if ($target.hasClass("select-link")) {
                                    this._retrieveInventoryItem(
                                        type, $target.attr("href"), pos
                                    );
                                } else {
                                    this._loadInventorySelector(
                                        type, $target.attr("href"), pos
                                    );
                                }
                                return false;
                            },
                            this.invoicingItems
                        ))
                        .dialog({ minWidth: 700, minHeight: 400, modal: true });
                }
            });
        },

        /**
         * Moves the invoicing item belonging to the given up or down link.
         *
         * @param {JQueryObject} $a the link to move the item
         * @param {Number} dir      the direction to move the item; -1 indicates
         *                          movement upwards, 1 movement downwards
         * @private
         */
        _moveInvoicingItem: function ($a, dir) {
            var $allTrs,
                $destTr = null,
                $tr,
                pos;

            /* obtain current row, all rows, and row position */
            $tr = $a.parents("tr");
            $allTrs = $tr.parent()
                .children();
            pos = $allTrs.index($tr);

            /* swap current row with previous or next row */
            if ((dir < 0) && (pos > 0)) {
                $destTr = $tr.prev();
                $destTr.before($tr);
            } else if (pos < $allTrs.length - 1) {
                $destTr = $tr.next();
                $destTr.after($tr);
            }

            /* swap input name positions and item positions */
            if ($destTr) {
                InvoicingItems._swapInputItemPos(pos, dir);
                InvoicingItems._swapItemPos($tr, $destTr);
            }
        },

        /**
         * Called if the user changes the value of an input field.
         *
         * @param {Object} e    the event object
         * @private
         */
        _onChange: function (e) {
            var $input,
                $tr,
                els,
                field,
                input = e.target,
                name,
                parts,
                pos,
                qty,
                unitPrice;

            $input = $(input);
            name = $input.attr("name");
            els = input.form.elements;
            parts = name.match(/^items\[(\d+)\]\.(\w+)$/);
            if (parts) {
                pos = parts[1];
                field = parts[2];
                $tr = $input.parents("tr");
                switch (field) {
                case "quantity":
                    qty = $.parseNumber($input.val());
                    unitPrice = $.parseNumber(
                        $(els["items[" + pos + "].unitPrice"]).val()
                    );
                    $tr.find(".invoicing-items-total .value")
                        .text($.formatCurrency(qty * unitPrice));
                    break;
                case "unitPrice":
                    unitPrice = $.parseNumber($input.val());
                    qty = $.parseNumber(
                        $(els["items[" + pos + "].quantity"]).val()
                    );
                    $tr.find(".invoicing-items-total .value")
                        .text($.formatCurrency(qty * unitPrice));
                    break;
                }
            }

            this._computeFooterValues();
        },

        /**
         * Called if the user clicks a link.
         *
         * @param {Object} e    the event object
         * @returns {Boolean}   always <code>true</code>
         * @private
         */
        _onClick: function (e) {
            var $a = $(e.target).closest("a");

            switch ($a.attr("class")) {
            case "up-btn":
                this._moveInvoicingItem($a, -1);
                return true;
            case "down-btn":
                this._moveInvoicingItem($a, 1);
                return true;
            case "remove-btn":
                this._removeInvoicingItem($a);
                return true;
            case "select-btn-products":
                this._showInventorySelector($a, "products");
                return true;
            case "select-btn-services":
                this._showInventorySelector($a, "services");
                return true;
            default:
                return true;
            }
        },

        /**
         * Called if an input control gets the focus. The method clears the
         * content of the control if the numeric value is zero.
         *
         * @param {Object} e    the event object
         * @private
         */
        _onFocusIn: function (e) {
            var $target = $(e.target),
                val;

            if ($target.hasClass("currency")) {
                val = $.parseNumber($target.val());
                $target.val(val ? $.formatNumber(val, null) : "");
            }
        },

        /**
         * Called if an input control loses the focus. The method formats the
         * entered numeric value.
         *
         * @param {Object} e    the event object
         * @private
         */
        _onFocusOut: function (e) {
            var $target = $(e.target);

            if ($target.hasClass("currency")) {
                $target.val($.formatCurrency($.parseNumber($target.val())));
            }
        },

        /**
         * Removes the invoicing item which belongs to the given anchor.
         *
         * @param {JQueryObject} $a the link anchor which was clicked to remove
         *                          the invoicing item
         * @private
         */
        _removeInvoicingItem: function ($a) {
            var instance = this;

            $a.parents("tr")
                .each(function () {
                    var $this = $(this),
                        el,
                        pos;

                    /* obtain current row position */
                    pos = $this.parent()
                        .children()
                        .index($this);

                    /* unset the ID value to cause Grails delete the record */
                    el = instance.form
                        .elements["items[" + String(pos) + "].id"];
                    if (el) {
                        el.value = "null";
                    }

                    /* fix row position labels of all successing rows */
                    $this.nextAll()
                        .each(function (i) {
                            $(this).find("td:first-child")
                                .text(String(pos + i + 1) + ".");
                        });
                })
                .remove();
        },

        /**
         * Retrieves an inventory item (product, service etc.) from the server
         * and places it in the table row with the given position.
         *
         * @param {String} type the type of inventory item which is to load;
         *                      possible values are "products" and "services"
         * @param {String} url  the URL used to load the inventory item
         * @param {Number} pos  the zero-based index of the row in which the
         *                      selected inventory item is to place
         * @private
         */
        _retrieveInventoryItem: function (type, url, pos) {
            $.ajax({
                url: url,
                context: { invoicingItems: this, type: type, pos: pos },
                dataType: "json",
                success: function (data) {
                    var els,
                        instance = this.invoicingItems,
                        item,
                        prefix,
                        pos = this.pos,
                        qty,
                        type = this.type,
                        unitPrice,
                        unitPriceInput;

                    prefix = "items[" + pos + "].";
                    els = instance._form.elements;
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
                        $.formatNumber(item.taxClass.taxValue * 100.0, 1);
                    instance._computeFooterValues();
                    $("#inventory-selector-" + type).dialog("close");
                }
            });
        },

        /**
         * Shows a dialog window to select products or services.
         *
         * @param {JQueryObject} $a the link which is clicked to load the
         *                          items; it is used to determine in which
         *                          table row the selected item is to store
         * @param {String} type     the type of inventory items which are to
         *                          load; possible values are "products" and
         *                          "services"
         * @param {String} [url]    the URL which is called to load the
         *                          inventory items
         * @protected
         */
        _showInventorySelector: function ($a, type, url) {
            var $tr,
                pos;

            $tr = $a.parents("tr");
            pos = $tr.parent()
                .children()
                .index($tr);
            if (!url) {
                url = (type === "products") ? this._productListUrl
                        : this._serviceListUrl;
            }
            this._loadInventorySelector(type, url, pos);
        }
    };

    /**
     * Adds the given tax rate to the map of tax rates. The method handles
     * duplicates.
     *
     * @param {Array} taxRates  the list of tax rates
     * @param {Number} taxRate  the tax rate to add
     * @param {Number} tax      the tax value to add
     * @protected
     */
    InvoicingItems._addTaxRate = function _addTaxRate(taxRates, taxRate, tax) {
        var found = false,
            i = -1,
            n = taxRates.length;

        for (; ++i < n; ) {
            if (taxRates[i].taxRate === taxRate) {
                taxRates[i].tax += tax;
                found = true;
                break;
            }
        }
        if (!found) {
            taxRates.push({ taxRate: taxRate, tax: tax });
        }
    };

    /**
     * Swaps the positional names of the input controls of the row with the
     * given position and the preceding or following row, depending on the
     * given direction.
     *
     * @name                InvoicingItems#_swapInputItemPos
     * @param {Number} pos  the zero-based position of the item to move
     * @param {Number} dir  the direction to move the item; must be either -1
     *                      or 1
     * @protected
     */
    InvoicingItems._swapInputItemPos = function(pos, dir) {
        var destPos = pos + dir,

            /** @ignore */
            f = function (el, name, pos) {
                var $el = $(el),
                    parts;

                parts = $el.attr("name")
                    .match(/^items(?:-dest)?\[\d+\]\.(\w+)$/);
                $el.attr("name", name + "[" + String(pos) + "]." + parts[1]);
            };

        $(':input[name^="items[' + String(pos) + ']."]').each(function () {
                f(this, "items-dest", destPos);
            });
        $(':input[name^="items[' + String(destPos) + ']."]').each(function () {
                f(this, "items", pos);
            });
        $(':input[name^="items-dest[' + String(destPos) + ']."]')
            .each(function () {
                f(this, "items", destPos);
            });
    };

    /**
     * Swaps the position label of the item in the given table row with the
     * one of the given destination table row. The position label is stored
     * in the first cell of the table rows.
     *
     * @name                            InvoicingItems#_swapItemPos
     * @param {JQueryObject} $tr        the table row to swap
     * @param {JQueryObject} $destTr    the table row to swap with
     * @protected
     */
    InvoicingItems._swapItemPos = function ($tr, $destTr) {
        var $destTd,
            $td,
            s;

        $td = $tr.find("td:first-child");
        $destTd = $destTr.find("td:first-child");
        s = $td.text();
        $td.text($destTd.text());
        $destTd.text(s);
    };

    SPRINGCRM.InvoicingItems = InvoicingItems;
}(this, SPRINGCRM, jQuery));
