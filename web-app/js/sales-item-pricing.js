/*
 * sales-item-pricing.js
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
 * @fileOverview    Contains a widget for displaying pricing items.
 * @author          Daniel Ellermann
 * @version         1.3
 * @since           1.3
 */


/*global window: false, jQuery: false, $L: false */
/*jslint nomen: true, plusplus: true, white: true */


(function (window, $, $L) {

    "use strict";

    var $LANG = $L,
        jQuery = $;

    /**
     * @name        salesitempricing
     * @class       Widget for rending an editable table for pricing of sales
     *              items such as products and services.
     * @memberOf    springcrm
     * @author      Daniel Ellermann
     * @version     1.3
     * @since       1.3
     */
    /**#@+
     * @memberOf    springcrm.salesitempricing#
     */
    $.widget("springcrm.salesitempricing", {

        /**
         * The names of the input controls of a table row.
         *
         * @constant
         * @name        INPUT_FIELD_NAMES
         * @type        Array
         * @private
         */
        INPUT_FIELD_NAMES: [
            "id", "quantity", "unit", "name", "type", "relToPos",
            "unitPercent", "unitPrice"
        ],

        /**
         * During finder mode the row that referring item currently is
         * searched.
         *
         * @name    _$finderRow
         * @type    jQuery
         * @private
         */
        _$finderRow: null,

        /**
         * The table body which contains the pricing items.
         *
         * @name    _$tbody
         * @type    jQuery
         * @private
         */
        _$tbody: null,

        /**
         * Cache for the rows of the pricing table.
         *
         * @name    _$trs
         * @type    jQuery
         * @private
         */
        _$trs: null,

        /**
         * The form associated to the pricing table.
         *
         * @name    _form
         * @type    Object
         * @private
         */
        _form: null,

        /**
         * A regular expression which is applied to the names of input fields
         * in order to obtain the index and field name.
         *
         * @name    _inputRegExp
         * @type    RegExp
         * @private
         */
        _inputRegExp: null,

        /**
         * Stores the reference to items for each pricing item.  The size and
         * order of the elements in this array corresponds with the items in
         * the pricing table.
         *
         * @name    _itemReferences
         * @type    Array
         * @private
         */
        _itemReferences: null,

        /**
         * The zero-based index of the next pricing item which is created.
         *
         * @name    _nextIndex
         * @type    Number
         * @private
         * @see     #_addItem
         */
        _nextIndex: 0,

        /**
         * The options for this widget.
         *
         * @name    options
         * @type    Object
         */
        options: {
            currency: "€",
            fieldNamePrefix: "items",
            imgPath: null,
            units: null
        },

        /**
         * Adds a row for a new item to the pricing table.
         *
         * @function
         * @name                            _addItem
         * @param {Boolean} jumpToNewRow    <code>true</code> if the document
         *                                  is to scroll that the new row is
         *                                  visible; <code>false</code>
         *                                  otherwise
         * @private
         */
        _addItem: function (jumpToNewRow) {
            var $L = $LANG,
                $row,
                $tbody = this._$tbody,
                currency,
                imgPath,
                index = this._nextIndex++,
                opts = this.options,
                pos,
                s;

            currency = opts.currency;
            s = '<tr><td class="pos number">' + String(index + 1) +
                '.</td><td class="quantity number"><input type="text" name="' +
                this._getInputName(index, "quantity") + '" size="6" /></td>' +
                '<td class="unit"><input type="text" name="' +
                this._getInputName(index, "unit") + '" size="7" /></td>' +
                '<td class="name"><input type="text" name="' +
                this._getInputName(index, "name") + '" size="30" /></td>' +
                '<td class="type"><select name="' +
                this._getInputName(index, "type") + '">' +
                '<option value="absolute">' +
                $L("salesItem.pricing.type.absolute") +
                '</option><option value="relativeToPos">' +
                $L("salesItem.pricing.type.relativeToPos") +
                '</option><option value="relativeToLastSum">' +
                $L("salesItem.pricing.type.relativeToLastSum") +
                '</option><option value="relativeToCurrentSum">' +
                $L("salesItem.pricing.type.relativeToCurrentSum") +
                '</option><option value="sum">' +
                $L("salesItem.pricing.type.sum") + '</option></select></td>' +
                '<td class="relative-to-pos"><input type="hidden" ' +
                'name="' + this._getInputName(index, "relToPos") +
                '" /><span style="display: none"><a href="#">' +
                $L("salesItem.pricing.relativeToPos.finder") +
                '</a><strong></strong></span></td>' +
                '<td class="unit-percent percentage number">' +
                '<input type="text" name="' +
                this._getInputName(index, "unitPercent") +
                '" size="5" class="percent" /></td>' +
                '<td class="unit-price currency number">' +
                '<input type="text" name="' +
                this._getInputName(index, "unitPrice") +
                '" size="8" class="currency" />&nbsp;' + currency + '</td>' +
                '<td class="total-price currency number">' +
                '<output></output>&nbsp;' + currency + '</td>' +
                '<td class="action-buttons">';
            imgPath = opts.imgPath;
            if (imgPath) {
                s += '<img class="up-btn" src="' + imgPath + '/up.png" alt="' +
                    $L("default.btn.up") + '" title="' + $L("default.btn.up") +
                    '" width="16" height="16" />' +
                    '<img class="down-btn" src="' + imgPath +
                    '/down.png" alt="' + $L("default.btn.down") + '" title="' +
                    $L("default.btn.down") + '" width="16" height="16" />' +
                    '<img class="remove-btn" src="' + imgPath +
                    '/remove.png" alt="' + $L("default.btn.remove") +
                    '" title="' + $L("default.btn.remove") +
                    '" width="16" height="16" />';
            }
            s += '</td></tr>';

            $row = $(s);
            this._initItemCtrls($row);
            $row.appendTo($tbody);
            this._$trs = this._getRows();
            this._itemReferences.push(-1);
            this._initUnitAutocomplete($row.find(".unit input"));
            if (jumpToNewRow) {
                pos = $row.position().top - $("#toolbar").outerHeight();
                $("html, body").animate({ scrollTop: pos }, "slow");
            }
        },

        /**
         * Computes the total price of the given item.
         *
         * @function
         * @name                        _computeTotalPrice
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @returns {Number}            the computed total price;
         *                              <code>null</code> if the referred item
         *                              was not set
         * @private
         */
        _computeTotalPrice: function (item) {
            var idx = this._getIndex(item),
                type = this._getRowType(item),
                unitPrice;

            if (type === "sum") {
                return this._getCurrentSum(idx - 1);
            }

            unitPrice = this._computeUnitPrice(idx);
            return (unitPrice === null) ? null
                    : this._getFieldVal(item, "quantity") * unitPrice;
        },

        /**
         * Computes the unit price of the given item.
         *
         * @function
         * @name                        _computeUnitPrice
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @returns {Number}            the computed unit price;
         *                              <code>null</code> if the type of the
         *                              pricing item is <code>sum</code> or
         *                              unknown or the referred item was not
         *                              set
         * @private
         */
        _computeUnitPrice: function (item) {
            var otherIndex,
                idx = this._getIndex(item),
                totalPrice = null,
                unitPrice = null;

            switch (this._getRowType(item)) {
            case "absolute":
                unitPrice = this._getFieldVal(item, "unit-price");
                break;
            case "relativeToPos":
                otherIndex = this._getFieldVal(item, "relative-to-pos");
                if (otherIndex >= 0) {
                    totalPrice = this._computeTotalPrice(otherIndex);
                    if (totalPrice !== null) {
                        unitPrice = (
                                this._getFieldVal(item, "unit-percent")
                                * totalPrice / 100
                            ).toFixed(2);
                    }
                }
                break;
            case "relativeToLastSum":
                otherIndex = this._getLastSumIndex(idx);
                if (otherIndex < 0) {
                    unitPrice = (
                            this._getFieldVal(item, "unit-percent")
                            * this._getCurrentSum(idx - 1) / 100
                        ).toFixed(2);
                } else {
                    totalPrice = this._computeTotalPrice(otherIndex);
                    if (totalPrice !== null) {
                        unitPrice = (
                                this._getFieldVal(item, "unit-percent")
                                * totalPrice / 100
                        ).toFixed(2);
                    }
                }
                break;
            case "relativeToCurrentSum":
                unitPrice = (
                        this._getFieldVal(item, "unit-percent")
                        * this._getCurrentSum(idx - 1) / 100
                    ).toFixed(2);
                break;
            }
            return unitPrice;
        },

        /**
         * Initializes this widget.
         *
         * @function
         * @name        _create
         * @constructs
         * @private
         */
        _create: function () {
            var $ = jQuery,
                $form,
                $trs,
                el = this.element,
                itemReferences = [],
                numItems,
                opts = this.options,
                self = this;

            opts.imgPath = opts.imgPath || el.data("img-path");
            opts.units = opts.units || el.data("units").split(",");
            this._inputRegExp = new RegExp(
                    "^" + opts.fieldNamePrefix + "\\[(\\d+)\\]\\.(\\w+)$"
                );

            $form = el.parents("form")
                /*.submit($.proxy(this._onSubmit, this))*/;
            this._form = $form.get(0);

            el.change($.proxy(this._onChange, this))
                .click($.proxy(this._onClick, this));
            this._$tbody = el.find("> .items");
            this._$trs = $trs = this._getRows();
            $trs.each(function () {
                    var $tr = $(this),
                        inst = self,
                        ref = -1;

                    inst._initItemCtrls.call(inst, this);
                    if (inst._getRowType($tr) === "relativeToPos") {
                        ref = inst._getFieldVal($tr, "relative-to-pos");
                    }
                    itemReferences.push(ref);
                });
            this._itemReferences = itemReferences;
            this._updateReferenceClasses();
            this._nextIndex = numItems = $trs.length;
            if (numItems !== 0) {
                this._initUnitAutocomplete();
            }
            $(".add-pricing-item-btn").click(function () {
                        self._addItem.call(self, true);
                        return false;
                    }
                );
            $("#sales-item-pricing-quantity").change(
                    $.proxy(this._onChangePricingQuantity, this)
                );
            $("#pricing-sales-pricing").change(
                    $.proxy(this._onChangeSalesPricing, this)
                );
        },

        /**
         * Gets the sum of all items' total prices at the given index and
         * before.
         *
         * @function
         * @name                    _getCurrentSum
         * @param {Number} [idx]    the given zero-based item index;
         *                          defaults to the last item index
         * @returns {Number}        the current sum
         * @private
         */
        _getCurrentSum: function (idx) {
            var $trs = this._$trs,
                self = this,
                sum = 0;

            if (idx === undefined) {
                idx = $trs.length - 1;
            }
            $trs.slice(0, idx + 1)
                .each(function (i) {
                    if (self._getRowType($(this)) !== "sum") {
                        sum += self._computeTotalPrice(i);
                    }
                });
            return sum;
        },

        /**
         * Gets the input control in the table cell with the given name in the
         * given item.  In case of name <code>total-price</code> the
         * <code>&lt;output></code> object is returned.
         *
         * @function
         * @name                        _getField
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @param {String} name         the given name of the table cell
         * @returns {jQuery}            the input control or the
         *                              <code>&lt;output></code> object
         * @private
         */
        _getField: function (item, name) {
            var sel = (name === "total-price") ? "output" : ":input";

            item = this._getRow(item);
            return $("> ." + name + " > " + sel, item);
        },

        /**
         * Gets the value of the input control in the table cell with the given
         * name in the given item.  In case of name <code>total-price</code>
         * the text of the <code>&lt;output></code> object is returned.
         * Numeric values are parsed before returned.
         *
         * @function
         * @name                        _getFieldVal
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @param {String} name         the given name of the table cell
         * @returns {String|Number}     the value of the input control or the
         *                              text of the <code>&lt;span></code>
         *                              object
         * @private
         */
        _getFieldVal: function (item, name) {
            var $field = this._getField(item, name),
                val = (name === "total-price") ? $field.text() : $field.val();

            if ((name === "quantity") || (name === "unit-percent")
                || (name === "unit-price") || (name === "total-price"))
            {
                val = $.parseNumber(val);
            } else if (name === "relative-to-pos") {
                val = (val === "") ? -1 : $.parseNumber(val);
            }
            return val;
        },

        /**
         * Gets the index of the item with the given table item or index in the
         * table.
         *
         * @function
         * @name                        _getIndex
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @returns {Number}            the zero-based index or -1 if the item
         *                              was not found
         * @private
         */
        _getIndex: function (item) {
            return (typeof item === "number") ? item : this._$trs.index(item);
        },

        /**
         * Returns the input field with the given name and item index.
         *
         * @function
         * @name                        _getInput
         * @param {Number} index        the zero-based index of the item
         * @param {String} [name=""]    the name of the field
         * @param {String} [suffix=""]  a suffix which is to append to the
         *                              field name prefix defined in the
         *                              options
         * @returns {Object}            the DOM element representing the input
         *                              field; <code>null</code> if no such
         *                              input field exists
         * @private
         */
        _getInput: function (index, name, suffix) {
            return this._form.elements[this._getInputName(index, name, suffix)];
        },

        /**
         * Computes the name of an input field for the given name and item
         * index.
         *
         * @function
         * @name                        _getInputName
         * @param {Number} index        the zero-based index of the item
         * @param {String} [name=""]    the name of the field
         * @param {String} [suffix=""]  a suffix which is to append to the
         *                              field name prefix defined in the
         *                              options
         * @returns {String}            the computed field name
         * @private
         */
        _getInputName: function (index, name, suffix) {
            return this.options.fieldNamePrefix + (suffix || "") + "[" +
                index + "]." + (name || "");
        },

        /**
         * Gets the last index of the item of type <code>SUM</code>.
         *
         * @function
         * @name                    _getLastSumIndex
         * @param {Number} [idx]    the given zero-based index; defaults to
         *                          the last item index
         * @return {Number}         the zero-based index of the last
         *                          subtotal sum; -1 if no such an item exists
         * @private
         */
        _getLastSumIndex: function (idx) {
            var $trs = this._$trs,
                res = -1,
                self = this;

            if (idx === undefined) {
                idx = $trs.length - 1;
            }
            $trs.slice(0, idx + 1)
                .reverse()
                .each(function (i) {
                    if (self._getRowType($(this)) === "sum") {
                        res = idx - i;
                        return false;
                    }
                    return true;
                });

            return res;
        },

        /**
         * Gets a list of items which refer to the item with the given index.
         *
         * @function
         * @name                _getReferrers
         * @param {Number} idx  the given zero-based index
         * @returns {Array}     the zero-based indices of the items referring
         *                      the item with the given index
         * @private
         */
        _getReferrers: function (idx) {
            var i = -1,
                itemReferences = this._itemReferences,
                n = itemReferences.length,
                res = [];

            while (++i < n) {
                if (itemReferences[i] === idx) {
                    res.push(i);
                }
            }
            return res;
        },

        /**
         * Gets the table row of the given item.
         *
         * @function
         * @name                        _getRow
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @returns {jQuery}            the table row
         * @private
         */
        _getRow: function (item) {
            return (typeof item === "number") ? this._$trs.eq(item) : item;
        },

        /**
         * Gets all rows of the pricing table.
         *
         * @function
         * @name                    _getRows
         * @param {Number} [idx]    the zero-based index up to but not
         *                          including the rows are to return; defaults
         *                          to all rows
         * @returns {jQuery}        all table rows, optionally up to but not
         *                          including the given index
         * @private
         */
        _getRows: function (idx) {
            var $trs = this._$tbody.find("> tr");

            if (idx !== undefined) {
                $trs = $trs.slice(0, idx);
            }
            return $trs;
        },

        /**
         * Gets the type of the given item.
         *
         * @function
         * @name                        _getRowType
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @returns {String}            the type of the item
         * @private
         */
        _getRowType: function (item) {
            return this._getFieldVal(item, "type");
        },

        /**
         * Initializes the given item by enabling or disabling the input
         * controls depending on the item type.
         *
         * @function
         * @name                        _initItemCtrls
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @private
         */
        _initItemCtrls: function (item) {
            var type = this._getRowType(item);

            this._getField(item, "quantity").toggleEnable(type !== "sum");
            this._getField(item, "unit").toggleEnable(type !== "sum");
            this._getField(item, "name").toggleEnable(type !== "sum");
            this._getField(item, "unit-percent")
                .toggleEnable((type !== "absolute") && (type !== "sum"));
            this._getField(item, "unit-price").toggleEnable(type === "absolute");
        },

        /**
         * Augments the given input control with the autocomplete feature to
         * select units.
         *
         * @function
         * @name                    _initUnitAutocomplete
         * @param {jQuery} $input   the given input control
         * @private
         */
        _initUnitAutocomplete: function ($input) {
            var data,
                units = this.options.units;

            if (units) {
                data = { source: units };
                if (!$input) {
                    $input = this.element.find(".unit input");
                }
                $input.autocomplete(data);
                $("#sales-item-pricing-unit").autocomplete(data);
                $("#sales-pricing-unit").autocomplete(data)
                    .bind(
                        "autocompletechange",
                        $.proxy(this._onChangeSalesPricing, this)
                    );
            }
        },

        /**
         * Moves the item with the given table row in to the given direction.
         *
         * @function
         * @name                _moveItem
         * @param {jQuery} $tr  the table row to move
         * @param {Number} dir  the direction to move; negative values move the
         *                      row up, positive values down
         * @private
         */
        _moveItem: function ($tr, dir) {
            var $destTr = null,
                pos = this._getIndex($tr);

            /* swap current row with previous or next row */
            if ((dir < 0) && (pos > 0)) {
                $destTr = $tr.prev();
                $destTr.before($tr);
            } else if (pos < this._$trs.length - 1) {
                $destTr = $tr.next();
                $destTr.after($tr);
            }

            /* swap input name positions, item positions, and references */
            if ($destTr) {
                this._swapInputItemPos($tr, $destTr);
                this._swapItemPos($tr, $destTr);
                this._swapItemReferences($tr, $destTr);
                this._$trs = this._getRows();
                this._updateItems();
            }
        },

        /**
         * Called if an input control in the pricing table has been changed.
         *
         * @function
         * @name                    _onChange
         * @param {Object} event    the event data
         * @private
         */
        _onChange: function (event) {
            var $target = $(event.target),
                $td = $target.parents("td"),
                $tr = $td.parent(),
                idx;

            if ($td.hasClass("quantity") || $td.hasClass("unit-percent")
                || $td.hasClass("unit-price"))
            {
                this._updateItems();
            } else if ($td.hasClass("type")) {
                this._initItemCtrls.call(this, $tr);
                if ($target.val() === "relativeToPos") {
                    idx = this._getFieldVal($tr, "relative-to-pos");
                    idx = (idx < 0) ? "" : String(idx + 1) + ".";
                    $tr.find("> .relative-to-pos > span")
                        .fadeIn();
                } else {
                    $tr.find("> .relative-to-pos > span")
                        .fadeOut();
                }
                this._updateItems();
            }
        },

        /**
         * Called if the quantity in the unit pricing section has been changed.
         *
         * @function
         * @name        _onChangePricingQuantity
         * @private
         */
        _onChangePricingQuantity: function () {
            this._updateItems();
        },

        /**
         * Called if an input control in the sales pricing section has been
         * changed.
         *
         * @function
         * @name        _onChangeSalesPricing
         * @private
         */
        _onChangeSalesPricing: function () {
            this._updateSalesPricing();
        },

        /**
         * Called if an element in the pricing table has been clicked.
         *
         * @function
         * @name                    _onClick
         * @param {Object} event    the event data
         * @private
         */
        _onClick: function (event) {
            var $finderRow = this._$finderRow,
                $img,
                $target = $(event.target),
                $tr;

            if ($finderRow) {
                this._onClickReferenceItem($target);
                return false;
            }

            $img = $target.closest("img");
            $tr = $target.closest("td").parent();
            if ($img.hasClass("up-btn")) {
                this._moveItem($tr, -1);
                return false;
            }
            if ($img.hasClass("down-btn")) {
                this._moveItem($tr, 1);
                return false;
            }
            if ($img.hasClass("remove-btn")) {
                if (!$tr.hasClass("not-removable")) {
                    this._removeItem($tr);
                }
                return false;
            }
            if ($img.is(".relative-to-pos img")) {
                this._startFinderMode($tr);
                return false;
            }
        },

        /**
         * Called if the user is in "find reference item" mode and has clicked
         * the reference item.
         *
         * @function
         * @name                    _onClickReferenceItem
         * @param {jQuery} $target  the clicked element
         * @private
         */
        _onClickReferenceItem: function ($target) {
            var $finderRow = this._$finderRow,
                $tr = $target.closest("tr"),
                k,
                v;

            if (!$tr.is($finderRow)) {
                k = this._getIndex($finderRow);
                v = this._getIndex($tr);
                this._itemReferences[k] = v;
                $finderRow.find("> .relative-to-pos > span")
                    .find("> strong")
                        .text(String(v + 1))
                    .end()
                    .find("> input")
                        .val(v);
                this._setFieldVal($finderRow, "relative-to-pos", v);
                this._updateReferenceClasses();
                this._updateItems();
            }
            this._stopFinderMode();
        },

        /**
         * Called if a key has been pressed.
         *
         * @function
         * @name                    _onKeyPress
         * @param {Object} event    the event data
         * @private
         */
        _onKeyDown: function (event) {
            if (this._$finderRow && (event.which === 27)) {     // Esc
                this._stopFinderMode();
            }
        },

        /**
         * Removes the given pricing item.
         *
         * @function
         * @name                        _removeItem
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @private
         */
        _removeItem: function (item) {
            var $ = jQuery,
                el,
                fieldPrefix = this.options.fieldNamePrefix,
                index = this._getIndex(item),
                re = this._inputRegExp;

            /* unset the ID value to cause Grails delete the record */
            el = this._getInput(index, "id");
            if (el) {
                el.value = "null";
            }

            /* fix row position labels and input names of all successing rows */
            this._getRow(item)
                .nextAll()
                    .each(function (i) {
                        var $this = $(this),
                            idx = index,
                            prefix = fieldPrefix,
                            regexp = re;

                        $this.find("td:first-child")
                            .text(String(idx + i + 1) + ".");

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
            this._$trs = this._getRows();
            this._removeItemReference(item);
            this._updateItems();
        },

        /**
         * Removes the item reference entry at the given index.
         *
         * @function
         * @name                _deleteItemReference
         * @param {Number} idx  the given zero-based item index
         * @private
         */
        _removeItemReference: function (idx) {
            var i = -1,
                refs = this._itemReferences,
                n;

            refs.splice(idx, 1);
            n = refs.length;
            while (++i < n) {
                if (refs[i] > idx) {
                    --refs[i];
                }
            }
        },

        /**
         * Sets the value of the input control in the table cell with the given
         * name in the given item.  In case of name <code>total-price</code>
         * the text of the <code>&lt;output></code> object is set.  Numeric
         * values are formatted before returned.
         *
         * @function
         * @name                        _setFieldVal
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @param {String} name         the given name of the table cell
         * @param {String|Number} val   the value of the input control or the
         *                              text of the <code>&lt;span></code>
         *                              object to set
         * @private
         */
        _setFieldVal: function (item, name, val) {
            var $field = this._getField(item, name);

            if ((name === "quantity") || (name === "unit-percent")) {
                val = $.formatNumber(val);
            }
            if ((name === "unit-price") || (name === "total-price")) {
                val = $.formatCurrency(val);
            }
            if (name === "total-price") {
                $field.text(val);
            } else {
                $field.val(val);
            }
        },

        /**
         * Sets the referred item for the given item in the associated table
         * row.  The method displays the index of the referred item and stores
         * the index in the hidden input field in the table row.
         *
         * @function
         * @name                            _setItemReference
         * @param {jQuery|Number} item      either the given zero-based index
         *                                  or the table row representing the
         *                                  referring item
         * @param {jQuery|Number} refItem   either the given zero-based index
         *                                  or the table row representing the
         *                                  referred item
         * @private
         */
        _setItemReference: function (item, refItem) {
            var $tr = this._getRow(item),
                idx = this._getIndex(refItem);

            $tr.find("> .relative-to-pos")
                .find("> strong")
                    .text(String(idx + 1))
                .end()
                .find("> input")
                    .val(idx);
        },

        /**
         * Starts the mode where the user should select a referred item.
         *
         * @function
         * @name                        _startFinderMode
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @see                         #_stopFinderMode
         * @private
         */
        _startFinderMode: function (item) {
            var $tr = this._getRow(item);

            this._$finderRow = $tr;
            $tr.addClass("non-selectable")
                .siblings()
                    .addClass("selectable");
            $(window.document).keydown($.proxy(this._onKeyDown, this));
        },

        /**
         * Stops the mode where the user should select a referred item.
         *
         * @function
         * @name        _stopFinderMode
         * @see         #_startFinderMode
         * @private
         */
        _stopFinderMode: function () {
            this._$finderRow = null;
            this._$trs.removeClass("selectable non-selectable");
            $(window.document).unbind("keydown");
        },

        /**
         * Swaps the indices of the input controls of both the given table
         * rows.
         *
         * @function
         * @name                    _swapInputItemPos
         * @param {jQuery} $tr      the given source table row
         * @param {jQuery} $destTr  the given destination table row
         * @private
         */
        _swapInputItemPos: function($tr, $destTr) {
            var destIndex,
                form = this._form,
                index,
                self = this,
                swap;

            index = this._getIndex($tr);
            destIndex = this._getIndex($destTr);

            swap = function (name, newName) {
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
            swap(
                this._getInputName(index),
                this._getInputName(destIndex, "", "-dest")
            );
            swap(
                this._getInputName(destIndex),
                this._getInputName(index)
            );
            swap(
                this._getInputName(destIndex, "", "-dest"),
                this._getInputName(destIndex)
            );
        },

        /**
         * Swaps the position numbers of both the given table rows.
         *
         * @function
         * @name                    _swapItemPos
         * @param {jQuery} $tr      the given source table row
         * @param {jQuery} $destTr  the given destination table row
         * @private
         */
        _swapItemPos: function ($tr, $destTr) {
            var $destTd = $destTr.find("td:first-child"),
                $td = $tr.find("td:first-child"),
                s = $td.text();

            $td.text($destTd.text());
            $destTd.text(s);
        },

        /**
         * Swaps the referrences to both the given items.
         *
         * @function
         * @name                            _swapItemReferences
         * @param {jQuery|Number} item      either the given zero-based index or
         *                                  the table row representing the one
         *                                  item
         * @param {jQuery|Number} destItem  either the given zero-based index
         *                                  or the table row representing the
         *                                  other item
         * @private
         */
        _swapItemReferences: function (item, destItem) {
            var destIdx = this._getIndex(destItem),
                destRefs,
                i = -1,
                idx = this._getIndex(item),
                itemRefs = this._itemReferences,
                n,
                refs;

            refs = this._getReferrers(idx);
            n = refs.length;
            while (++i < n) {
                this._setItemReference(refs[i], destIdx);
            }

            i = -1;
            destRefs = this._getReferrers(destIdx);
            n = destRefs.length;
            while (++i < n) {
                this._setItemReference(destRefs[i], idx);
            }

            i = -1;
            n = refs.length;
            while (++i < n) {
                itemRefs[refs[i]] = destIdx;
            }

            i = -1;
            n = destRefs.length;
            while (++i < n) {
                itemRefs[destRefs[i]] = idx;
            }
        },

        /**
         * Updates the computable fields in the given item.
         *
         * @function
         * @name                        _updateItem
         * @param {jQuery|Number} item  either the given zero-based index or
         *                              the table row representing the item
         * @private
         */
        _updateItem: function (item) {
            var totalPrice = this._computeTotalPrice(item),
                unitPrice = this._computeUnitPrice(item);

            if (unitPrice !== null) {
                this._setFieldVal(item, "unit-price", unitPrice);
            }
            if (totalPrice !== null) {
                this._setFieldVal(item, "total-price", totalPrice);
            }
        },

        /**
         * Updates the computable fields of all items and updates the total sum
         * of the pricing table.  Furthermore, the function updates the
         * computable fields in the sales pricing section.
         *
         * @function
         * @name        _updateItems
         * @private
         * @see         #_updateSalesPricing
         */
        _updateItems: function () {
            var quantity,
                self = this,
                sum = this._getCurrentSum(),
                sumText = $.formatCurrency(sum),
                unitPrice;

            this._$trs.each(function () {
                    self._updateItem($(this));
                });
            $("#pricing-total-price").text(sumText);

            quantity = $.parseNumber($("#sales-item-pricing-quantity").val());
            unitPrice = (sum / quantity).toFixed(2);
            $("#calculated-unit-price").text($.formatCurrency(unitPrice));
            $("#sales-pricing-unit-price").text($.formatCurrency(unitPrice));
            this._updateSalesPricing();
        },

        /**
         * Updates the class names for each row in the pricing table.  If a row
         * is references by another one class "not-removable" is added.
         *
         * @function
         * @name        _updateReferenceClasses
         * @private
         */
        _updateReferenceClasses: function () {
            var $L = $LANG,
                self = this,
                textNotRemovable = $L("salesItem.pricing.button.notRemovable"),
                textRemovable = $L("default.btn.remove");

            this._$trs.each(function (i) {
                    var $this = $(this),
                        referrers = self._getReferrers(i);

                    if (referrers.length) {
                        $this.addClass("not-removable")
                            .find(".remove-btn img")
                                .attr("title", textNotRemovable);
                    } else {
                        $this.removeClass("not-removable")
                            .find(".remove-btn img")
                                .attr("title", textRemovable);
                    }
                });
        },

        /**
         * Updates the computed fields in the sales pricing section.
         *
         * @function
         * @name        _updateSalesPricing
         * @private
         */
        _updateSalesPricing: function () {
            var $ = jQuery,
                adjustment = $.parseNumber(
                        $("#sales-pricing-adjustment").val()
                    ),
                discountPercent = $.parseNumber(
                        $("#sales-pricing-discount-percent").val()
                    ),
                discountPercentAmount,
                qty = $.parseNumber($("#sales-pricing-quantity").val()),
                totalPrice,
                unit = $("#sales-pricing-unit").val(),
                unitPrice = $.parseNumber(
                        $("#sales-pricing-unit-price").text()
                    );

            totalPrice = qty * unitPrice;
            $("#sales-pricing-total-price").text($.formatCurrency(totalPrice));
            discountPercentAmount = discountPercent * totalPrice / 100;
            $("#sales-pricing-discount-percent-amount")
                .text($.formatCurrency(discountPercentAmount));
            totalPrice -= discountPercentAmount;
            totalPrice += adjustment;
            $("#sales-pricing-total").text($.formatCurrency(totalPrice));
            $("#sales-pricing-total-unit-price")
                .text($.formatCurrency(totalPrice / qty));

            $("#sales-pricing-total-quantity").text(qty);
            $("#sales-pricing-total-unit").text(unit);
        }
    });
    /**#@-*/

    $("#pricing-items").salesitempricing();
}(window, jQuery, $L));
