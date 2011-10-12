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

    var InvoicingItems = null;


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
     * @param {String} [config.tableId]         the ID of the table containing
     *                                          the invoicing items; defaults
     *                                          to "{baseName}-items"
     * @param {String} [config.formName]        the name of the form containing
     *                                          the invoicing items; defaults
     *                                          to "{baseName}-form"
     * @param {String} [config.fieldName]       the base name of the input
     *                                          fields; defaults to "items"
     * @param {String} config.imgPath           the base path to the folder
     *                                          containing the images which are
     *                                          used for rendering the
     *                                          invoicing items
     * @param {String} [config.productListUrl]  the URL used to retrieve the
     *                                          products of the CRM; if unset
     *                                          no link is displayed
     * @param {String} [config.serviceListUrl]  the URL used to retrieve the
     *                                          services of the CRM; if unset
     *                                          no link is displayed
     * @param {Array} [config.units]            an array of available options
     *                                          which are displayed as
     *                                          autocomplete values when
     *                                          entering a unit
     * @param {Array} [config.taxes]            an array of available options
     *                                          which are displayed as
     *                                          autocomplete values when
     *                                          entering a tax value
     */
    InvoicingItems = function InvoicingItems(config) {
        var formName = config.formName || config.baseName + "-form";

        /* handle function call without new */
        if (!(this instanceof InvoicingItems)) {
            return new InvoicingItems(config);
        }


        //-- Instance variables -----------------

        /**
         * The base name used to generate various names.
         *
         * @type String
         */
        this._baseName = config.baseName;

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
         * The base name of the input controls of the invoicing items in the
         * form.
         *
         * @type String
         * @default "items"
         */
        this._fieldName = config.fieldName || "items";

        /**
         * The next index to use when adding new invoicing items.
         *
         * @type Number
         * @default 0
         */
        this._nextIndex = 0;

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
         * The regular expression which is used to obtain the row index and the
         * input field name.
         *
         * @type RegExp
         */
        this._inputRegExp =
            new RegExp("^" + this._fieldName + "\\[(\\d+)\\]\\.(\\w+)$");

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

        /**
         * An array of available options which are displayed as autocomplete
         * values when entering a unit.
         *
         * @type Array
         */
        this._units = config.units;

        /**
         * An array of available options which are displayed as autocomplete
         * values when entering a tax value.
         *
         * @type Array
         */
        this._taxes = this._prepareTaxes(config.taxes);
    };

    /**
     * The names of the input fields for an invoicing item.
     *
     * @static
     */
    InvoicingItems.INPUT_FIELD_NAMES = [
        "id", "number", "quantity", "unit", "name", "description", "unitPrice",
        "tax"
    ];

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

    InvoicingItems.prototype = {

        //-- Public methods ---------------------

        /**
         * Initializes the invoicing items table.
         */
        init: function () {
            var numItems;

            numItems = this._$tbodyItems.find("tr").length;
            this._nextIndex = numItems;
            $(this._form).submit($.proxy(this._onSubmit, this));
            this._$table
                .click($.proxy(this._onClick, this))
                .change($.proxy(this._onChange, this))
                .focusin($.proxy(this._onFocusIn, this))
                .focusout($.proxy(this._onFocusOut, this));
            $(".add-invoicing-item-btn")
                .click($.proxy(
                    function () {
                        this._addInvoicingItem(true);
                    },
                    this
                ));
            if (numItems === 0) {
                this._addInvoicingItem(false);
            } else {
                this._initUnitAutocomplete();
                this._initTaxAutocomplete();
            }
            this._computeFooterValues();
        },


        //-- Non-public methods -----------------

        /**
         * Adds a table row for a new invoicing item and appends it to the end
         * of the table body.
         *
         * @param {boolean} jumpToNewRow    if <code>true</code> the new
         *                                  created row won't be scrolled into
         *                                  view
         * @protected
         */
        _addInvoicingItem: function (jumpToNewRow) {
            var $row,
                $tbody = this._$tbodyItems,
                gm = SPRINGCRM.getMessage,
                imgPath = this._imgPath,
                index = this._nextIndex++,
                s,
                table = this._tableId;

            s = '<tr><td headers="' + table +
                '-pos" class="invoicing-items-pos">' + String(index + 1) +
                '.</td><td headers="' + table +
                '-number" class="invoicing-items-number">' +
                '<input type="text" name="' +
                this._getInputName(index, "number") +
                '" size="10" /></td><td headers="' + table +
                '-quantity" class="invoicing-items-quantity">' +
                '<input type="text" name="' +
                this._getInputName(index, "quantity") +
                '" size="4" /></td><td headers="' + table +
                '-unit" class="invoicing-items-unit">' +
                '<input type="text" name="' + this._getInputName(index, "unit") +
                '" size="5" /></td><td headers="' + table +
                '-name" class="invoicing-items-name">' +
                '<input type="text" name="' + this._getInputName(index, "name") +
                '" size="28" />';
            if (this._productListUrl) {
                s +=
                    '&nbsp;<a href="javascript:void 0;" ' +
                    'class="select-btn-products"><img src="' + imgPath +
                    '/products.png" alt="' + gm("productSel") + '" title="' +
                    gm("productSel") + '" width="16" height="16" ' +
                    'style="vertical-align: middle;" /></a>';
            }
            if (this._serviceListUrl) {
                s +=
                    '&nbsp;<a href="javascript:void 0;" ' +
                    'class="select-btn-services">' +
                    '<img src="' + imgPath + '/services.png" alt="' +
                    gm("serviceSel") + '" title="' + gm("serviceSel") +
                    '" width="16" height="16" ' +
                    'style="vertical-align: middle;" /></a>';
            }
            s +=
                '<br /><textarea name="' +
                this._getInputName(index, "description") +
                '" cols="30" rows="3"></textarea></td><td headers="' +
                table + '-unit-price" class="invoicing-items-unit-price">' +
                '<input type="text" name="' +
                this._getInputName(index, "unitPrice") +
                '" size="8" value="0,00" class="currency" />&nbsp;€</td>' +
                '<td headers="' + table + '-total" class="invoicing-items-total">' +
                '<span class="value">0,00</span>&nbsp;€</td>' +
                '<td headers="' + table + '-tax" class="invoicing-items-tax">' +
                '<input type="text" name="' +this._getInputName(index, "tax") +
                '" size="4" />&nbsp;%</td>' +
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
            $row = $(s);
            $row.appendTo($tbody);
            this._initUnitAutocomplete(
                $row.find(".invoicing-items-unit input")
            );
            this._initTaxAutocomplete(
                $row.find(".invoicing-items-tax input")
            );
            if (jumpToNewRow) {
//                $("html").scrollTop($tbody.find("tr:last").position().top);
                $("html").scrollTop($row.position().top);
            }
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
                s += '<tr class="tax-rate-sum"><td headers="' +
                    this._baseName + '-items-name" ' +
                    'colspan="5" class="invoicing-items-label"><label>' +
                    gm("taxRateLabel").replace(
                        /\{0\}/, $.formatNumber(tr.taxRate, 1)
                    ) +
                    '</label></td><td headers="' + this._baseName +
                    '-items-unitPrice"></td>' +
                    '<td headers="' + this._baseName +
                    '-items-total" class="invoicing-items-total">' +
                    $.formatCurrency(tr.tax) + '&nbsp;€</td>' +
                    '<td headers="' + this._baseName +
                    '-items-tax"></td><td></td></tr>';
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
         * Obtains the index and the name of the given input control which
         * resides in an invoicing item row.
         *
         * @param {Object} input    the given input control
         * @returns {Array}         an array containing the zero-based index in
         *                          the first element and the name in the
         *                          second element; <code>null</code> if the
         *                          given input control does not belong to an
         *                          invoicing item row
         * @protected
         */
        _getInputIndexAndName: function (input) {
            var parts;

            parts = input.name.match(this._inputRegExp);
            if (parts) {
                parts.shift();
                return parts;
            } else {
                return null;
            }
        },

        /**
         * Get the input control with the given index and name.
         *
         * @param {Number} index    the zero-based index
         * @param {String} [name]   the name
         * @param {String} [suffix] an optional suffix which is appended to the
         *                          name stem
         * @returns {Object}        the input control
         * @protected
         */
        _getInput: function (index, name, suffix) {
            return this._form.elements[this._getInputName(index, name, suffix)];
        },

        /**
         * Composes the name of the input control from the given index and
         * name.
         *
         * @param {Number} index    the zero-based index
         * @param {String} [name]   the name
         * @param {String} [suffix] an optional suffix which is appended to the
         *                          name stem
         * @returns {String}        the computed input control name
         * @protected
         */
        _getInputName: function (index, name, suffix) {
            return this._fieldName + (suffix || "") + "[" + index + "]." +
                (name || "");
        },

        /**
         * Gets the number of invoicing item rows.
         *
         * @returns {Number}    the number of rows
         * @protected
         */
        _getNumRows: function () {
            return this._$tbodyItems.find("tr").length;
        },

        /**
         * Gets the zero-based index of the input controls in the given row.
         *
         * @param {JQueryObject} $tr    the given table row
         * @returns {Number}            the zero-based index
         * @protected
         */
        _getRowIndex: function ($tr) {
            var input = $tr.find(":input").get(0);

            return Number(this._getInputIndexAndName(input)[0]);
        },

        /**
         * Gets the zero-based position of the given row within all the
         * invoicing item rows.
         *
         * @param {JQueryObject} $tr    the given table row
         * @returns {Number}            the zero-based position
         * @protected
         */
        _getRowPosition: function ($tr) {
            return $tr.parent()
                .children()
                .index($tr);
        },

        /**
         * Initializes the given input or, if not set, all tax inputs with the
         * predefined tax options.
         *
         * @param {JQueryObject} [$input]   the given input
         * @protected
         */
        _initTaxAutocomplete: function ($input) {
            if (this._taxes) {
                if (!$input) {
                    $input = $(".quote-items-tax input");
                }
                $input.autocomplete({ source: this._taxes });
            }
        },

        /**
         * Initializes the given input or, if not set, all unit inputs with the
         * predefined unit options.
         *
         * @param {JQueryObject} [$input]   the given input
         * @protected
         */
        _initUnitAutocomplete: function ($input) {
            if (this._units) {
                if (!$input) {
                    $input = $(".invoicing-items-unit input");
                }
                $input.autocomplete({ source: this._units });
            }
        },

        /**
         * Displays a dialog window and load inventory items such as products
         * or services for display within the dialog.
         *
         * @param {String} type     the type of inventory items which are to
         *                          load; possible values are "products" and
         *                          "services"
         * @param {String} url      the URL used to load the inventory items
         * @param {Number} index    the zero-based index of the input controls
         *                          which are to fill with the selected
         *                          inventory item is to place
         * @private
         */
        _loadInventorySelector: function (type, url, index, params) {
            $.ajax({
                url: url,
                context: { invoicingItems: this, type: type, index: index },
                data: params,
                success: function (html) {
                    this.invoicingItems._onLoadInventorySelector(
                        html, this.type, this.index
                    );
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
            var $tr,
                input = e.target,
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
         * Called if the data of the inventory selector was loaded
         * successfully. The method displays the data and rewrites links.
         *
         * @param {String} html     the HTML content retrieved from the server
         * @param {String} type     the type of inventory items which are to
         *                          load; possible values are "products" and
         *                          "services"
         * @param {Number} index    the zero-based index of the input controls
         *                          which are to fill with the selected
         *                          inventory item is to place
         * @private
         */
        _onLoadInventorySelector: function (html, type, index) {
            var $form = null,
                $selector,
                getData = function () {
                    var data = null,
                        search = $form.get(0).search.value;

                    if ((search !== SPRINGCRM.getMessage("search")) &&
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
                    function (e) {
                        var $target,
                            target = e.target;

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
                function (e) {
                    this._loadInventorySelector(
                        type, e.target.action, index, getData()
                    );
                    return false;
                },
                this
            ));
            SPRINGCRM.Page.enableSearchFieldHints();
            $selector.dialog({ minWidth: 700, minHeight: 400, modal: true });
        },

        /**
         * Called if the form is to submit. The method removes empty rows from
         * the invoicing items table and re-indexes the input controls in order
         * to prevent gaps in the indices.
         *
         * @private
         */
        _onSubmit: function () {
            var b,
                del,
                e,
                elems = this._form.elements,
                fieldName,
                fieldNames = InvoicingItems.INPUT_FIELD_NAMES,
                i = 0,
                input = null,
                j,
                name,
                newName,
                n = fieldNames.length,
                val,
                x;

            while (i < this._nextIndex) {
                name = this._getInputName(i);

                del = true;
                for (x = -1; ++x < n; ) {
                    fieldName = fieldNames[x];
                    e = elems[name + fieldName];
                    if (e) {
                        val = e.value;
                        b = val === "";
                        if ((fieldName === "id")
                            || (fieldName === "quantity")
                            || (fieldName === "unitPrice")
                            || (fieldName === "tax"))
                        {
                            b = b || $.parseNumber(val) === 0;
                        } else if (fieldName === "number") {
                            input = e;
                        }
                        del = del && b;
                        if (!del) {
                            break;
                        }
                    }
                }

                if (del) {
                    for (j = i; ++j < this._nextIndex; ) {
                        name = this._getInputName(j);
                        newName = this._getInputName(j - 1);
                        for (x = -1; ++x < n; ) {
                            fieldName = fieldNames[x];
                            e = elems[name + fieldName];
                            if (e) {
                                e.name = newName + fieldName;
                            }
                        }
                    }
                    --this._nextIndex;
                } else {
                    ++i;
                }
            }
            return true;
        },

        /**
         * Converts the given taxes to strings which are suitable for display
         * in an autocomplete field.
         *
         * @param {Array} taxes the given taxes; the array may contain either
         *                      numbers which are multiplied by 100 or strings
         * @returns {Array}     the processed taxes as strings
         * @protected
         */
        _prepareTaxes: function (taxes) {
            var i = -1,
                n = taxes.length,
                tax;

            if (taxes) {
                while (++i < n) {
                    tax = taxes[i];
                    if (typeof(tax) === "number") {
                        taxes[i] = $.formatNumber(tax * 100);
                    }
                }
            }
            return taxes;
        },

        /**
         * Removes the invoicing item which belongs to the given anchor.
         *
         * @param {JQueryObject} $a the link anchor which was clicked to remove
         *                          the invoicing item
         * @private
         */
        _removeInvoicingItem: function ($a) {
            var numRows;

            numRows = this._$tbodyItems
                .find("tr")
                .size();
            if (numRows > 1) {
                this._removeRow($a.parents("tr"));
                this._computeFooterValues();
            }
        },

        /**
         * Removes the given row containing an invoicing item and changes the
         * ID values.
         *
         * @param {JQueryObject} $tr    the row containing the invoicing item
         *                              which is to delete
         * @private
         */
        _removeRow: function ($tr) {
            var el,
                index = this._getRowIndex($tr),
                pos = this._getRowPosition($tr);

            /* unset the ID value to cause Grails delete the record */
            el = this._getInput(index, "id");
            if (el) {
                el.value = "null";
            }

            /* fix row position labels of all successing rows */
            $tr.nextAll()
                    .each(function (i) {
                        $(this).find("td:first-child")
                            .text(String(pos + i + 1) + ".");
                    })
                .end()
                .remove();
        },

        /**
         * Retrieves an inventory item (product, service etc.) from the server
         * and places it in the table row with the given position.
         *
         * @param {String} type     the type of inventory item which is to
         *                          load; possible values are "products" and
         *                          "services"
         * @param {String} url      the URL used to load the inventory item
         * @param {Number} index    the zero-based index of the input controls
         *                          in which the selected inventory item data
         *                          are to fill in
         * @private
         */
        _retrieveInventoryItem: function (type, url, index) {
            $.ajax({
                url: url,
                context: { invoicingItems: this, type: type, index: index },
                dataType: "json",
                success: function (data) {
                    var els,
                        instance = this.invoicingItems,
                        item,
                        prefix,
                        index = this.index,
                        qty,
                        type = this.type,
                        unitPrice,
                        unitPriceInput;

                    prefix = instance._getInputName(index);
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
            var index;

            if (!url) {
                url = (type === "products") ? this._productListUrl
                        : this._serviceListUrl;
            }
            if (url) {
                index = this._getRowIndex($a.parents("tr"));
                this._loadInventorySelector(type, url, index);
            }
        },

        /**
         * Swaps the positional names of the input controls of both the given
         * rows.
         *
         * @param {JQueryObject} $tr        the table row to swap
         * @param {JQueryObject} $destTr    the table row to swap with
         * @protected
         */
        _swapInputItemPos: function($tr, $destTr) {
            var destIndex,
                f,
                form = this._form,
                index;

            index = this._getRowIndex($tr);
            destIndex = this._getRowIndex($destTr);

            f = function (name, newName) {
                var el,
                    elems = form.elements,
                    fieldName,
                    fieldNames = InvoicingItems.INPUT_FIELD_NAMES,
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

    SPRINGCRM.InvoicingItems = InvoicingItems;
}(this, SPRINGCRM, jQuery));
