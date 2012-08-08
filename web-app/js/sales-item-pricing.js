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

    var /*$LANG = $L,*/
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
//        INPUT_FIELD_NAMES: [
//            "id", "number", "quantity", "unit", "name", "description",
//            "unitPrice", "tax"
//        ],

        _$finderRow: null,

        /**
         * Cache for the rows of the pricing table.
         *
         * @name    _$trs
         * @type    jQuery
         * @private
         */
        _$trs: null,

        /**
         * Stores the reference to related items for each pricing item.  The
         * size and order of the elements in this array corresponds with the
         * items in the pricing table.
         *
         * @name    _itemRelations
         * @type    Array
         * @private
         */
        _itemRelations: null,

        /**
         * The options for this widget.
         *
         * @name    options
         * @type    Object
         */
        options: {
            currency: "€",
//            fieldNamePrefix: "items",
            imgPath: $(".pricing-items").data("img-path"),
            units: $(".pricing-items").data("units").split(",")
        },

        _addItem: function (jumpToNewRow) {

        },

        /**
         * Computes the total price of the pricing item with the given
         * position.
         *
         * @function
         * @name                        _computeTotalPrice
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @returns {Number}            the computed total price;
         *                              <code>null</code> if the related item
         *                              was not set
         * @private
         */
        _computeTotalPrice: function (row) {
            var pos = this._getPos(row),
                type = this._getRowType(row),
                unitPrice;

            if (type === "sum") {
                return this._getCurrentSum(pos - 1);
            }

            unitPrice = this._computeUnitPrice(pos);
            return (unitPrice === null) ? null
                    : this._getFieldVal(row, "quantity") * unitPrice;
        },

        /**
         * Computes the unit price of the pricing item with the given position.
         *
         * @function
         * @name                        _computeUnitPrice
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @returns {Number}            the computed unit price;
         *                              <code>null</code> if the type of the
         *                              pricing item is <code>sum</code> or
         *                              unknown or the related item was not set
         * @private
         */
        _computeUnitPrice: function (row) {
            var otherPos,
                pos = this._getPos(row),
                totalPrice = null,
                unitPrice = null;

            switch (this._getRowType(row)) {
            case "absolute":
                unitPrice = this._getFieldVal(row, "unit-price");
                break;
            case "relativeToPos":
                otherPos = this._getFieldVal(row, "relative-to-pos");
                if (otherPos >= 0) {
                    totalPrice = this._computeTotalPrice(otherPos);
                    if (totalPrice !== null) {
                        unitPrice = (
                                this._getFieldVal(row, "unit-percent")
                                * totalPrice / 100
                            ).toFixed(2);
                    }
                }
                break;
            case "relativeToLastSum":
                otherPos = this._getLastSumPos(pos);
                if (otherPos < 0) {
                    unitPrice = (
                            this._getFieldVal(row, "unit-percent")
                            * this._getCurrentSum(pos - 1) / 100
                        ).toFixed(2);
                } else {
                    totalPrice = this._computeTotalPrice(otherPos);
                    if (totalPrice !== null) {
                        unitPrice = (
                                this._getFieldVal(row, "unit-percent")
                                * totalPrice / 100
                        ).toFixed(2);
                    }
                }
                break;
            case "relativeToCurrentSum":
                unitPrice = (
                        this._getFieldVal(row, "unit-percent")
                        * this._getCurrentSum(pos - 1) / 100
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
                itemRelations = [],
                self = this;

            this._$trs = this._getRows();
            this.element
                .change($.proxy(this._onChange, this))
                .click($.proxy(this._onClick, this))
                .find("> .pricing-items-body > tr")
                    .each(function () {
                            var $tr = $(this),
                                inst = self,
                                ref = -1;

                            inst._prepareRow.call(inst, this);
                            if (inst._getRowType($tr) === "relativeToPos") {
                                ref = inst._getFieldVal($tr, "relative-to-pos");
                            }
                            itemRelations.push(ref);
                        });
            this._itemRelations = itemRelations;
            $("#add-pricing-item-btn").click(function () {
                        self._addItem(self, true);
                    }
                );
        },

        /**
         * Deletes the item relation entry at the given position.
         *
         * @function
         * @name                _deleteItemRelation
         * @param {Number} pos  the given zero-based item position
         * @private
         */
        _deleteItemRelation: function (pos) {
            this._itemRelations.splice(pos, 1);
        },

        /**
         * Gets the sum of all items' total prices at the given position and
         * before.
         *
         * @function
         * @name                    _getCurrentSum
         * @param {Number} [pos]    the given zero-based item position;
         *                          defaults to the last item position
         * @returns {Number}        the current sum
         * @private
         */
        _getCurrentSum: function (pos) {
            var $trs = this._$trs,
                self = this,
                sum = 0;

            if (pos === undefined) {
                pos = $trs.length - 1;
            }
            $trs.slice(0, pos + 1)
                .each(function (i) {
                    if (self._getRowType($(this)) !== "sum") {
                        sum += self._computeTotalPrice(i);
                    }
                });
            return sum;
        },

        /**
         * Gets the input control in the table cell with the given name in the
         * given row.  In case of name <code>total</code> the
         * <code>&lt;span></code> object is returned.
         *
         * @function
         * @name                        _getField
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @param {String} name         the given name of the table cell
         * @returns {jQuery}            the input control or the
         *                              <code>&lt;span></code> object
         * @private
         */
        _getField: function (row, name) {
            var sel = (name === "total") ? ".value" : ":input";

            row = this._getRow(row);
            return $("> .pricing-items-" + name + " > " + sel, row);
        },

        /**
         * Gets the value of the input control in the table cell with the given
         * name in the given row.  In case of name <code>total</code> the text
         * of the <code>&lt;span></code> object is returned.  Numeric values
         * are parsed before returned.
         *
         * @function
         * @name                        _getFieldVal
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @param {String} name         the given name of the table cell
         * @returns {String|Number}     the value of the input control or the
         *                              text of the <code>&lt;span></code>
         *                              object
         * @private
         */
        _getFieldVal: function (row, name) {
            var $field = this._getField(row, name),
                val = (name === "total") ? $field.text() : $field.val();

            if ((name === "quantity") || (name === "unit-percent")
                || (name === "unit-price") || (name === "total"))
            {
                val = $.parseNumber(val);
            } else if (name === "relative-to-pos") {
                val = (val === "") ? -1 : $.parseNumber(val);
            }
            return val;
        },

        /**
         * Gets the last position of the item of type <code>SUM</code>.
         *
         * @function
         * @name                    _getLastSumPos
         * @param {Number} [pos]    the given zero-based position; defaults to
         *                          the last item position
         * @return {Number}         the zero-based position of the last
         *                          subtotal sum; -1 if no such an item exists
         * @private
         */
        _getLastSumPos: function (pos) {
            var $trs = this._$trs,
                res = -1,
                self = this;

            if (pos === undefined) {
                pos = $trs.length - 1;
            }
            $trs.slice(0, pos + 1)
                .reverse()
                .each(function (i) {
                    if (self._getRowType($(this)) === "sum") {
                        res = pos - i;
                        return false;
                    }
                    return true;
                });

            return res;
        },

        /**
         * Gets the position of the item with the given table row or position
         * in the table.
         *
         * @function
         * @name                        _getPos
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @returns {Number}            the zero-based position or -1 if the
         *                              item was not found
         * @private
         */
        _getPos: function (row) {
            return (typeof row === "number") ? row : this._$trs.index(row);
        },

        /**
         * Gets a list of items which refer to the item with the given
         * position.
         *
         * @function
         * @name                _getReferrers
         * @param {Number} pos  the given zero-based position
         * @returns {Array}     the zero-based positions of the items referring
         *                      the item with the given position
         * @private
         */
        _getReferrers: function (pos) {
            var i = -1,
                itemRelations = this._itemRelations,
                n = itemRelations.length,
                res = [];

            while (++i < n) {
                if (itemRelations[i] === pos) {
                    res.push(i);
                }
            }
            return res;
        },

        /**
         * Gets the table row of the item with the given position.
         *
         * @function
         * @name                        _getRow
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @returns {jQuery}            the table row
         * @private
         */
        _getRow: function (row) {
            return (typeof row === "number") ? this._$trs.eq(row) : row;
        },

        /**
         * Gets all rows of the pricing table.
         *
         * @function
         * @name                    _getRows
         * @param {Number} [pos]    the zero-based position up to but not
         *                          including the rows are to return; defaults
         *                          to all rows
         * @returns {jQuery}        all table rows, optionally up to but not
         *                          including the given position
         * @private
         */
        _getRows: function (pos) {
            var $trs = $("> .pricing-items-body > tr", this.element);

            if (arguments.length > 0) {
                $trs = $trs.slice(0, pos);
            }
            return $trs;
        },

        /**
         * Gets the type of the item with the given position or table row.
         *
         * @function
         * @name                        _getRowType
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @returns {String}            the type of the item
         * @private
         */
        _getRowType: function (row) {
            return this._getFieldVal(row, "type");
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
                pos;

            if ($td.hasClass("pricing-items-quantity")
                || $td.hasClass("pricing-items-unit-percent")
                || $td.hasClass("pricing-items-unit-price"))
            {
                this._updateRows();
            } else if ($td.hasClass("pricing-items-type")) {
                this._prepareRow.call(this, $tr);
                if ($target.val() === "relativeToPos") {
                    pos = this._getFieldVal($tr, "relative-to-pos");
                    pos = (pos < 0) ? "" : String(pos + 1) + ".";
                    $tr.find("> .pricing-items-relative-to-pos > span")
                        .fadeIn();
                } else {
                    $tr.find("> .pricing-items-relative-to-pos > span")
                        .fadeOut();
                }
                this._updateRows();
            }
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
                $target = $(event.target),
                $td,
                $tr,
                k,
                v;

            if ($finderRow) {
                $tr = $target.parents("tr")
                    .andSelf();
                if (!$tr.is($finderRow)) {
                    k = this._getPos($finderRow);
                    v = this._getPos($tr);
                    this._itemRelations[k] = v;
                    $finderRow.find("> .pricing-items-relative-to-pos > span")
                        .find("> strong")
                            .text(String(v + 1))
                        .end()
                        .find("> input")
                            .val(v);
                    this._setFieldVal($finderRow, "relative-to-pos", v);
                    this._updateRows();
                }
                this._stopFinderMode();
                return false;
            }

            $td = $target.parents("td");
            $tr = $td.parent();
            if ($td.hasClass("pricing-items-relative-to-pos")) {
                this._startFinderMode($tr);
                return false;
            }
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
         * Prepares the given table row by enabling or disabling the input
         * controls depending on the type of the item in the row.
         *
         * @function
         * @name                        _prepareRow
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @private
         */
        _prepareRow: function (row) {
            var type = this._getRowType(row);

            this._getField(row, "quantity").toggleEnable(type !== "sum");
            this._getField(row, "unit").toggleEnable(type !== "sum");
            this._getField(row, "name").toggleEnable(type !== "sum");
            this._getField(row, "unit-percent")
                .toggleEnable((type !== "absolute") && (type !== "sum"));
            this._getField(row, "unit-price").toggleEnable(type === "absolute");
        },

        /**
         * Sets the value of the input control in the table cell with the given
         * name in the given row.  In case of name <code>total</code> the text
         * of the <code>&lt;span></code> object is set.  Numeric values are
         * formatted before returned.
         *
         * @function
         * @name                        _setFieldVal
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @param {String} name         the given name of the table cell
         * @param {String|Number} val   the value of the input control or the
         *                              text of the <code>&lt;span></code>
         *                              object to set
         * @private
         */
        _setFieldVal: function (row, name, val) {
            var $field = this._getField(row, name);

            if ((name === "quantity") || (name === "unit-percent")) {
                val = $.formatNumber(val);
            }
            if ((name === "unit-price") || (name === "total")) {
                val = $.formatCurrency(val);
            }
            if (name === "total") {
                $field.text(val);
            } else {
                $field.val(val);
            }
        },

        /**
         * Starts the mode where the user should select a related row.
         *
         * @function
         * @name                        _startFinderMode
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @see                         #_stopFinderMode
         * @private
         */
        _startFinderMode: function (row) {
            var $tr = this._getRow(row);

            this._$finderRow = $tr;
            $tr.addClass("non-selectable")
                .siblings()
                    .addClass("selectable");
            $(window.document).keydown($.proxy(this._onKeyDown, this));
        },

        /**
         * Stops the mode where the user should select a related row.
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
         * Updates the computable fields in the given item.
         *
         * @function
         * @name                        _updateRow
         * @param {jQuery|Number} row   either the given zero-based position or
         *                              the table row representing the item
         * @private
         */
        _updateRow: function (row) {
            var totalPrice = this._computeTotalPrice(row),
                unitPrice = this._computeUnitPrice(row);

            if (unitPrice !== null) {
                this._setFieldVal(row, "unit-price", unitPrice);
            }
            if (totalPrice !== null) {
                this._setFieldVal(row, "total", totalPrice);
            }
        },

        /**
         * Updates the computable fields of all items and updates the total sum
         * of the pricing table.
         *
         * @function
         * @name        _updateRows
         * @private
         */
        _updateRows: function () {
            var self = this;

            this._$trs.each(function () {
                    self._updateRow($(this));
                });
            $("#pricing-items-total").text($.formatCurrency(this._getCurrentSum()));
        }
    });
    /**#@-*/

    $("#pricing-items").salesitempricing();
}(window, jQuery, $L));
