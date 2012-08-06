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


(function ($, $L) {

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

        _finderMode: false,

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
         * @name                _computeTotalPrice
         * @param {Number} pos  the zero-based position of the item which total
         *                      price is to compute
         * @returns {Number}    the computed total price
         * @private
         */
        _computeTotalPrice: function (pos) {
            var $tr = this._getRow(pos),
                type = this._getRowType($tr);

            if (type === "sum") {
                return this._getCurrentSum(pos - 1);
            } else {
                return this._getFieldVal($tr, "quantity") * this._computeUnitPrice(pos);
            }
        },

        /**
         * Computes the unit price of the pricing item with the given position.
         *
         * @function
         * @name                _computeUnitPrice
         * @param {Number} pos  the zero-based position of the item which unit
         *                      price is to compute
         * @returns {Number}    the computed unit price; <code>null</code> if
         *                      the type of the pricing item is
         *                      <code>sum</code> or unknown
         * @private
         */
        _computeUnitPrice: function (pos) {
            var $tr = this._getRow(pos),
                otherPos,
                type = this._getRowType($tr),
                unitPrice = null;

            switch (type) {
            case "absolute":
                unitPrice = this._getFieldVal($tr, "unit-price");
                break;
            case "relativeToPos":
                otherPos = this._getFieldVal($tr, "relative-to-pos");
                unitPrice = (
                        this._getFieldVal($tr, "unit-percent")
                        * this._computeTotalPrice(otherPos) / 100
                    ).toFixed(2);
                break;
            case "relativeToLastSum":
                otherPos = this._getLastSumPos(pos);
                if (otherPos >= 0) {
                    unitPrice = (
                            this._getFieldVal($tr, "unit-percent")
                            * this._computeTotalPrice(otherPos) / 100
                        ).toFixed(2);
                }
                // fall through
            case "relativeToCurrentSum":
                unitPrice = (
                        this._getFieldVal($tr, "unit-percent")
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
         * @name                _getCurrentSum
         * @param {Number} pos  the given zero-based item position
         * @returns {Number}    the current sum
         * @private
         */
        _getCurrentSum: function (pos) {
            var self = this,
                sum = 0;

            this._getRows().each(function (i) {
                    var $tr = $(this);

                    if (self._getRowType($tr) !== "sum") {
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
         * @name                _getField
         * @param {jQuery} $tr  the given table row
         * @param {String} name the given name of the table cell
         * @returns {jQuery}    the input control or the <code>&lt;span></code>
         *                      object
         * @private
         */
        _getField: function ($tr, name) {
            var sel = (name === "total") ? ".value" : ":input";

            return $("> .pricing-items-" + name + " > " + sel, $tr);
        },

        /**
         * Gets the value of the input control in the table cell with the given
         * name in the given row.  In case of name <code>total</code> the text
         * of the <code>&lt;span></code> object is returned.  Numeric values
         * are parsed before returned.
         *
         * @function
         * @name                    _getFieldVal
         * @param {jQuery} $tr      the given table row
         * @param {String} name     the given name of the table cell
         * @returns {String|Number} the value of the input control or the text
         *                          of the <code>&lt;span></code> object
         * @private
         */
        _getFieldVal: function ($tr, name) {
            var $field = this._getField($tr, name),
                val;

            val = (name === "total") ? $field.val() : $field.text();
            if ((name === "quantity") || (name === "unit-percent")
                || (name === "unit-price") || (name === "total"))
            {
                val = $.parseNumber(val);
            }
            return val;
        },

        /**
         * Gets the last position of the item of type <code>SUM</code>.
         *
         * @function
         * @name                _getLastSumPos
         * @param {Number} pos  the given zero-based position
         * @return {Number}     the zero-based position of the last subtotal
         *                      sum; -1 if no such an item exists
         * @private
         */
        _getLastSumPos: function (pos) {
            var res = -1,
                self = this;

            this._getRows()
                .each(function (i) {
                    if (self._getRowType($(this)) === "sum") {
                        res = i;
                        return false;
                    }
                    return true;
                });

            return res;
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
                if (itemRelations[i] == pos) {
                    res.push(i);
                }
            }
            return res;
        },

        /**
         * Gets the table row of the item with the given position.
         *
         * @function
         * @name                _getRow
         * @param {Number} pos  the given zero-based position
         * @returns {jQuery}    the table row
         * @private
         */
        _getRow: function (pos) {
            return this._getRows()
                .eq(pos);
        },

        /**
         * Gets all rows of the pricing table.
         *
         * @function
         * @name                _getRows
         * @returns {jQuery}    all table rows
         * @private
         */
        _getRows: function () {
            return $("> .pricing-items-body > tr", this.element);
        },

        /**
         * Gets the type of the item with the given position or table row.
         *
         * @function
         * @name                            _getRowType
         * @param {jQuery|Number} trOrPos   either the given zero-based
         *                                  position or the table row
         *                                  representing the item
         * @returns {String}                the type of the item
         * @private
         */
        _getRowType: function (trOrPos) {
            if (typeof(trOrPos) === "number") {
                trOrPos = this._getRow(trOrPos);
            }
            return this._getFieldVal(trOrPos, "type");
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
                pos = this._getRows().index($tr);

            if ($td.hasClass("pricing-items-quantity")
                || $td.hasClass("pricing-items-unit-percent")
                || $td.hasClass("pricing-items-unit-price"))
            {
                this._setFieldVal(
                        $tr, "unit-price", this._computeUnitPrice(pos)
                    );
            } else if ($td.hasClass("pricing-items-type")) {
                this._prepareRow.call(this, $tr);
                if ($target.val() === "relativeToPos") {
                    $tr.find(".pricing-items-relative-to-pos")
                        .append(
                            '<a href="#">Select reference row</a><span>1.</span>'
                        );
                }
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
            var $target = $(event.target),
                $td = $target.parents("td"),
                $tr = $td.parent();

            if ($td.hasClass("pricing-items-relative-to-pos")) {
                this._finderMode = true;
                $tr.addClass("non-selectable")
                    .siblings()
                        .addClass("selectable");
                return false;
            }
        },

        /**
         * Prepares the given table row by enabling or disabling the input
         * controls depending on the type of the item in the row.
         *
         * @function
         * @name                _prepareRow
         * @param {Object} tr   the given table row
         * @private
         */
        _prepareRow: function (tr) {
            var $tr = $(tr),
                type = this._getRowType($tr);

            this._getField($tr, "quantity").toggleEnable(type !== "sum");
            this._getField($tr, "unit").toggleEnable(type !== "sum");
            this._getField($tr, "name").toggleEnable(type !== "sum");
            this._getField($tr, "unit-percent")
                .toggleEnable((type !== "absolute") && (type !== "sum"));
            this._getField($tr, "unit-price").toggleEnable(type === "absolute");
        },

        /**
         * Sets the value of the input control in the table cell with the given
         * name in the given row.  In case of name <code>total</code> the text
         * of the <code>&lt;span></code> object is set.  Numeric values are
         * formatted before returned.
         *
         * @function
         * @name                        _setFieldVal
         * @param {jQuery} $tr          the given table row
         * @param {String} name         the given name of the table cell
         * @param {String|Number} val   the value of the input control or the
         *                              text of the <code>&lt;span></code>
         *                              object to set
         * @private
         */
        _setFieldVal: function ($tr, name, val) {
            var $field = this._getField($tr, name);

            if ((name === "quantity") || (name === "unit-percent")) {
                val = $.formatNumber(val);
            }
            if ((name === "unit-price") || (name === "total")) {
                val = $.parseCurrency(val);
            }
            if (name === "total") {
                $field.text(val);
            } else {
                $field.val(val);
            }
        }
    });
    /**#@-*/

    $("#pricing-items").salesitempricing();
}(jQuery, $L));
