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


(function (window, pkg, $) {

    "use strict";

    var InvoicingItems;


    /*== Classes ==============================*/

    /**
     * Creates a new invoicing item list.
     * 
     * @class                           Represents a list of invoicing items
     *                                  used in quotes, orders, or invoices.
     * @constructor
     * @param {String} tableId                  the ID of the table containing
     *                                          the invoicing items
     * @param {String} formName                 the name of the form containing
     *                                          the invoicing items
     * @param {String} imgPath                  the base path to the folder 
     *                                          containing the images which are
     *                                          used for rendering the
     *                                          invoicing items
     * @param {String} productListUrl           the URL used to retrieve the
     *                                          products of the CRM
     * @param {String} serviceListUrl           the URL used to retrieve the
     *                                          services of the CRM
     * @property {String} tableId               the ID of the table containing
     *                                          the invoicing items
     * @property {String} imgPath               the base path to the folder 
     *                                          containing the images which are
     *                                          used for rendering the 
     *                                          invoicing items
     * @property {String} productListUrl        the URL used to retrieve the
     *                                          products of the CRM
     * @property {String} serviceListUrl        the URL used to retrieve the
     *                                          services of the CRM
     * @property {Object} form                  the form containing the
     *                                          invoicing items
     * @property {Object} $table                the table containing the 
     *                                          invoicing items
     * @property {Object} $subtotalNet          the field containing the 
     *                                          subtotal net value
     * @property {Object} $subtotalGross        the field containing the 
     *                                          subtotal gross value
     * @property {Object} $discountPercent      the field containing the 
     *                                          discount percentage value
     * @property {Object} $discountFromPercent  the field containing the 
     *                                          discount value computed from
     *                                          the discount percentage value
     * @property {Object} $discountAmount       the field containing the 
     *                                          fixed discount amount value
     * @property {Object} $shippingCosts        the field containing the 
     *                                          shippings costs value
     * @property {Object} $shippingTax          the field containing the 
     *                                          shipping tax value
     * @property {Object} $adjustment           the field containing the 
     *                                          adjustment value
     * @property {Number} subtotalNet           the currently computed subtotal
     *                                          net value
     * @property {Number} subtotalGross         the currently computed subtotal
     *                                          gross value
     * @property {Number} taxTotal              the currently computed total
     *                                          taxes
     * @property {Number} subtotalNet           the currently computed total
     *                                          (gross) value
     */
    InvoicingItems = function InvoicingItems(tableId, formName, imgPath,
                                             productListUrl, serviceListUrl) {
        
        this.tableId = tableId;
        this.imgPath = imgPath;
        this.productListUrl = productListUrl;
        this.serviceListUrl = serviceListUrl;

        this.form = window.document.forms[formName];
        this.$table = $("#" + tableId);
        this.$subtotalNet = $("#invoicing-items-subtotal-net");
        this.$subtotalGross = $("#invoicing-items-subtotal-gross");
        this.$discountPercent = $("#discountPercent");
        this.$discountFromPercent = $("#invoicing-items-discount-from-percent");
        this.$discountAmount = $("#discountAmount");
        this.$shippingCosts = $("#shippingCosts");
        this.$shippingTax = $("#shippingTax");
        this.$adjustment = $("#adjustment");
        this.$total = $("#invoicing-items-total");
        this.subtotalNet = 0.0;
        this.subtotalGross = 0.0;
        this.taxTotal = 0.0;
        this.total = 0.0;
    };
    InvoicingItems.prototype = {

        /**
         * Adds a table row for a new invoicing item and appends it to the end
         * of the table body.
         */
        addInvoicingItem: function () {
            var $tbody = $("#invoicing-items"),
                imgPath,
                msgs = pkg.messages,
                pos,
                s,
                sPos,
                table;

            table = this.tableId;
            imgPath = this.imgPath;
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
                '/products.png" alt="' + msgs.productSel + '" title="' + 
                msgs.productSel + 
                '" width="16" height="16" style="vertical-align: middle;" /></a>' +
                '&nbsp;<a href="javascript:void 0;" class="select-btn-services">' +
                '<img src="' + imgPath + '/services.png" alt="' + 
                msgs.serviceSel + '" title="' + msgs.serviceSel +
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
                imgPath + '/up.png" alt="' + msgs.upBtn + '" title="' + 
                msgs.upBtn + '" width="16" height="16" /></a>' +
                '<a href="javascript:void 0;" class="down-btn"><img src="' + 
                imgPath + '/down.png" alt="' + msgs.downBtn + '" title="' + 
                msgs.downBtn + '" width="16" height="16" /></a>' +
                '<a href="javascript:void 0;" class="remove-btn"><img src="' + 
                imgPath + '/remove.png" alt="' + msgs.removeBtn + '" title="' + 
                msgs.removeBtn + '" width="16" height="16" /></a></td></tr>';
            $tbody.append(s);
        },

        /**
         * Computes the values in the table footer such as subtotal, taxes,
         * total etc. and prints the values in the table footer.
         */
        computeFooterValues: function () {
            var adjustment,
                discount,
                discountPercent,
                shippingCosts,
                subtotalNet = 0,
                total;

            $("#invoicing-items .invoicing-items-total .value")
                .each(function () {
                    subtotalNet += $.parseNumber($(this).text());
                });
            shippingCosts = $.parseNumber(this.$shippingCosts.val());
            this.subtotalNet = subtotalNet + shippingCosts;
            this.$subtotalNet.text($.formatCurrency(this.subtotalNet));

            this.computeTaxValues();

            discountPercent = $.parseNumber(this.$discountPercent.val());
            discount = this.subtotalGross * discountPercent / 100;
            this.$discountFromPercent.text($.formatCurrency(discount));
            discount += $.parseNumber(this.$discountAmount.val());

            adjustment = $.parseNumber(this.$adjustment.val());
            total = this.subtotalGross - discount + adjustment;
            this.total = total;
            this.$total.text($.formatCurrency(total));
        },

        /**
         * Computes the tax classes and the tax values.
         */
        computeTaxValues: function () {
            var $subtotalNetRow,
                i,
                n,
                s = "",
                shippingCosts,
                shippingTax,
                subtotalGross,
                taxRates = [],
                taxTotal = 0,
                tr;

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
                        pkg.InvoicingItems._addTaxRate(tr, taxRate, tax);
                    }
                });
            shippingCosts = $.parseNumber(this.$shippingCosts.val());
            shippingTax = $.parseNumber(this.$shippingTax.val());
            if (shippingCosts !== 0 && shippingTax !== 0) {
                pkg.InvoicingItems._addTaxRate(
                    taxRates, shippingTax, shippingCosts * shippingTax / 100.0
                );
            }
            taxRates.sort(function (a, b) { return a.taxRate - b.taxRate; });

            $subtotalNetRow = $("tfoot tr:first");
            for (i = -1, n = taxRates.length; ++i < n; ) {
                tr = taxRates[i];
                taxTotal += tr.tax;
                s += '<tr class="tax-rate-sum"><td headers="quote-items-name" ' +
                    'colspan="5" class="invoicing-items-label"><label>' + 
                    pkg.messages.taxRateLabel.replace(/\{0\}/, $.formatNumber(tr.taxRate, 1)) + 
                    '</label></td>' +
                    '<td headers="quote-items-unitPrice"></td>' +
                    '<td headers="quote-items-total" class="invoicing-items-total">' + 
                    $.formatCurrency(tr.tax) + '&nbsp;€</td>' +
                    '<td headers="quote-items-tax"></td>' +
                    '<td></td>' +
                    '</tr>';
            }
            $(".tax-rate-sum").remove();
            $subtotalNetRow.after(s);
            this.taxTotal = taxTotal;
            subtotalGross = this.subtotalNet + taxTotal;
            this.subtotalGross = subtotalGross;
            this.$subtotalGross.text($.formatCurrency(subtotalGross));
            return subtotalGross;
        },

        /**
         * Initializes the invoicing items.
         */
        init: function () {
            this.$table
                .click($.proxy(this.onClick, this))
                .change($.proxy(this.onChange, this))
                .focusin($.proxy(this.onFocusIn, this))
                .focusout($.proxy(this.onFocusOut, this));
            $("#add-invoicing-item-btn")
                .click($.proxy(this.addInvoicingItem, this));
            this.computeFooterValues();
        },

        /**
         * Moves the invoicing item belonging to the given up or down link.
         * 
         * @param {Object} $a   the link to move the item
         * @param {Number} dir  the direction to move the item; -1 indicates
         *                      movement upwards, 1 movement downwards
         */
        moveInvoicingItem: function ($a, dir) {
            var $allTrs,
                $destTr,
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
            InvoicingItems._swapInputItemPos(pos, dir);
            InvoicingItems._swapItemPos($tr, $destTr);
        },

        /**
         * Called if the user changes the value of an input field.
         * 
         * @param {Object} e    the event object 
         */
        onChange: function (e) {
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

            this.computeFooterValues();
        },

        onClick: function (e) {
            var $a = $(e.target).closest("a");

            switch ($a.attr("class")) {
            case "up-btn":
                this.moveInvoicingItem($a, -1);
                return true;
            case "down-btn":
                this.moveInvoicingItem($a, 1);
                return true;
            case "remove-btn":
                this.removeInvoicingItem($a);
                return true;
            case "select-btn-products":
                this.showInventorySelector($a, "products");
                return true;
            case "select-btn-services":
                this.showInventorySelector($a, "services");
                return true;
            default:
                return true;
            }
        },

        onFocusIn: function (e) {
            var $target = $(e.target),
                val;

            if ($target.hasClass("currency")) {
                val = $.parseNumber($target.val());
                $target.val(val ? $.formatNumber(val, null) : "");
            }
        },

        onFocusOut: function (e) {
            var $target = $(e.target);

            if ($target.hasClass("currency")) {
                $target.val($.formatCurrency($.parseNumber($target.val())));
            }
        },

        /**
         * Removes the invoicing item which belongs to the given anchor.
         * 
         * @param {Object} $a   the jQuery object representing the link anchor
         *                      which was clicked to remove the invoicing item
         */
        removeInvoicingItem: function ($a) {
            var ii = this;

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
                    el = ii.form.elements["items[" + String(pos) + "].id"];
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

        showInventorySelector: function ($a, type, url) {
            var $tr,
                pos;

            $tr = $a.parents("tr");
            pos = $tr.parent()
                .children()
                .index($tr);
            if (!url) {
                url = (type === "products") ? this.productListUrl 
                        : this.serviceListUrl;
            }
            this._loadInventorySelector(type, url, pos);
        },


        //-- Non-public methods ---------------------

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

        _retrieveInventoryItem: function (type, url, pos) {
            $.ajax({
                url: url,
                context: { invoicingItems: this, type: type, pos: pos },
                dataType: "json",
                success: function (data) {
                    var els,
                        item,
                        items = this.invoicingItems,
                        prefix,
                        pos = this.pos,
                        qty,
                        type = this.type,
                        unitPrice,
                        unitPriceInput;

                    prefix = "items[" + pos + "].";
                    els = items.form.elements;
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
                    items.computeFooterValues();
                    $("#inventory-selector-" + type).dialog("close");
                }
            });
        }
    };

    /* static methods */
    InvoicingItems._addTaxRate = function (taxRates, taxRate, tax) {
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
     * @method              _swapInputItemPos
     * @class               InvoicingItems
     * @param {Number} pos  the zero-based position of the item to move
     * @param {Number} dir  the direction to move the item; must be either -1
     *                      or 1
     * @protected
     * @static
     */
    InvoicingItems._swapInputItemPos = function(pos, dir) {
        var destPos = pos + dir,
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
     * @method                  _swapItemPos
     * @class                   InvoicingItems
     * @param {Object} $tr      jQuery object representing the table row to
     *                          swap
     * @param {Object} $destTr  jQuery object representing the table row to
     *                          swap with
     * @protected
     * @static
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

    pkg.InvoicingItems = InvoicingItems;
}(this, springcrm, jQuery));
