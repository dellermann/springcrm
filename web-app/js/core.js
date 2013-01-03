/*
 * core.js
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * @fileOverview    Contains core classes which are used within this
 *                  application.
 * @author          Daniel Ellermann
 * @version         1.3
 */


/*jslint nomen: true, plusplus: true, white: true, regexp: true */
/*global jQuery: true, $L: true */


(function (undef, $, $L) {

    "use strict";


    var $html = $("html"),
        $LANG = $L,
        decimalSeparator = $html.data("decimal-separator") || ",",
        groupingSeparator = $html.data("grouping-separator") || ".",
        numFractions = $html.data("num-fraction-digits") || 2;


    //== Core extensions ========================


    /**
     * Formats this date with either the given user-defined format or the
     * localized date and time format as specified in the messages
     * <code>dateFormat</code> and <code>timeFormat</code>.  If using a
     * user-defined format, the following placeholders may be used:
     * <ul>
     *   <li><code>d</code>. the day of month (1..31)</li>
     *   <li><code>dd</code>. the two-digit day of month (1..31), optionally
     *   filled with zeros</li>
     *   <li><code>M</code>. the month number (1..12)</li>
     *   <li><code>MM</code>. the two-digit month number (1..12), optionally
     *   filled with zeros</li>
     *   <li><code>y</code>. the year without century</li>
     *   <li><code>yy</code>. the two-digit year without century</li>
     *   <li><code>yyyy</code>. the four-digit year with century</li>
     *   <li><code>H</code>. the hour (0..23)</li>
     *   <li><code>HH</code>. the two-digit hour (0..23), optionally filled
     *   with zeros</li>
     *   <li><code>m</code>. the minute (0..59)</li>
     *   <li><code>mm</code>. the two-digit minute (0..59), optionally filled
     *   with zeros</li>
     * </ul>
     *
     * @function
     * @name                                Date#format
     * @param {String} [format="datetime"]  the format specification used to
     *                                      format the date.  This may be
     *                                      either a user-defined format using
     *                                      the placeholders listed above or
     *                                      any of the given types
     *                                      "date", "time", and "datetime",
     *                                      which obtain the actual format from
     *                                      the localized strings.
     * @returns {String}                    the formatted date
     */
    Date.prototype.format = function (format) {
        var $L = $LANG,
            d = this,
            delimiter,
            f = function (x) {
                var s = x.toFixed();
                if (s.length < 2) {
                    s = "0" + s;
                }
                return s;
            },
            fmt = "",
            regexp = /[^dMyHm]/,
            res = "",
            token;

        format = format || "datetime";
        if ((format === "date") || (format === "time")
            || (format === "datetime"))
        {
            fmt = "";
            if ((format === "date") || (format === "datetime")) {
                fmt += $L("default.format.date");
            }
            if (format === "datetime") {
                fmt += " ";
            }
            if ((format === "time") || (format === "datetime")) {
                fmt += $L("default.format.time");
            }
            format = fmt;
        }

        while (format) {
            if (regexp.test(format)) {
                token = RegExp.leftContext;
                delimiter = RegExp.lastMatch;
                format = RegExp.rightContext;
            } else {
                token = format;
                delimiter = "";
                format = "";
            }
            switch (token) {
            case 'd':
                res += d.getDate();
                break;
            case 'dd':
                res += f(d.getDate());
                break;
            case 'M':
                res += d.getMonth() + 1;
                break;
            case 'MM':
                res += f(d.getMonth() + 1);
                break;
            case 'y':
                res += d.getYear();
                break;
            case 'yy':
                res += f(d.getYear() % 100);
                break;
            case 'yyy':
            case 'yyyy':
                res += String(d.getFullYear());
                break;
            case 'H':
                res += d.getHours();
                break;
            case 'HH':
                res += f(d.getHours());
                break;
            case 'm':
                res += d.getMinutes();
                break;
            case 'mm':
                res += f(d.getMinutes());
                break;
            }
            res += delimiter;
        }

        return res;
    };

    /**
     * Formats this number with the given number of fraction digits and the
     * decimal and grouping separator as specified in the attributes
     * <code>decimal-separator</code> and <code>grouping-separator</code>,
     * respectively, in the <code>&lt;html></code> tag.  If the separators are
     * not set, a dot (.) is used as decimal separator and a comma (,) as
     * grouping separator.
     *
     * @function
     * @name                Number#format
     * @param {Number} [n]  the precision; if not set or <code>null</code> the
     *                      precision remains unchanged
     * @returns {String}    the formatted number
     * @since               1.3
     */
    Number.prototype.format = function (n) {
        var dotPos,
            frac = "",
            gs = groupingSeparator,
            int,
            num = this,
            pos,
            sgn = num < 0 ? "-" : "";

        n = (n === null) ? undef : Math.abs(n);
        num = Math.abs(+num || 0);
        int = String(
                parseInt(num = isNaN(n) ? num.toString() : num.toFixed(n), 10)
            );
        pos = (pos = int.length) > 3 ? pos % 3 : 0;
        if (n !== 0) {
            dotPos = num.indexOf(".");
            if (dotPos >= 0) {
                frac = Math.abs(num.substring(dotPos));
                frac = isNaN(n) ? frac.toString() : frac.toFixed(n);
                frac = decimalSeparator + frac.substring(2);
            }
        }
        return sgn + (pos ? int.substr(0, pos) + gs : "")
            + int.substr(pos).replace(/(\d{3})(?=\d)/g, "$1" + gs) + frac;
    };

    /**
     * Formats the given number as currency value, that is with the number of
     * fraction digits specified in attribute <code>num-fraction-digits</code>
     * of the <code>&lt;html></code> tag.  If the attribute is not specified
     * a number of 2 fraction digits are used.  Furthermore, all formatting
     * used in method <code>format</code> is used as well.
     *
     * @function
     * @name                Number#formatCurrencyValue
     * @returns {String}    the formatted currency value
     * @see                 Number#format
     * @since               1.3
     */
    Number.prototype.formatCurrencyValue = function () {
        return this.format(numFractions);
    };

    /**
     * Rounds this number to the given number of fraction digits.
     *
     * @function
     * @name                Number#round
     * @param {Number} n    the given number of fraction digits
     * @returns {Number}    the rounded number
     */
    Number.prototype.round = function (n) {
        return parseFloat(this.toFixed(n));
    };

    /**
     * Escapes all characters in the given string which have special meaning in
     * regular expressions.
     *
     * @function
     * @name                RegExp#escape
     * @param {String} s    the given string
     * @returns {String}    the string with escaped special characters suitable
     *                      for a regular expression
     * @since               1.3
     */
    RegExp.escape = function (s) {
        return s.replace(/[\-\/\\\^$*+?.()|\[\]{}]/g, "\\$&");
    };

    /**
     * Parses this string as a date and/or time value in either the given
     * user-defined format or the localized date and time format as specified
     * in the messages <code>dateFormat</code> and <code>timeFormat</code>.  If
     * using a user-defined format, the following placeholders may be used:
     * <ul>
     *   <li><code>d</code> or <code>dd</code>. the day of month (1..31)</li>
     *   <li><code>M</code> or <code>MM</code>. the month number (1..12)</li>
     *   <li><code>y</code>, <code>yy</code>, <code>yyy</code>, or
     *   <code>yyyy</code>. the year without century</li>
     *   <li><code>H</code> or <code>HH</code>. the hour (0..23)</li>
     *   <li><code>m</code> or <code>mm</code>. the minute (0..59)</li>
     * </ul>
     *
     * @function
     * @name                                String#parseDate
     * @param {String} [format="datetime"]  the format specification used to
     *                                      parse the date.  This may be
     *                                      either a user-defined format using
     *                                      the placeholders listed above or
     *                                      any of the given types
     *                                      "date", "time", and "datetime",
     *                                      which obtain the actual format from
     *                                      the localized strings.
     * @param {Number} [baseYear=35]        the year which acts as limit
     *                                      for year specifications without
     *                                      century: years before the base
     *                                      year are treated as after 2000,
     *                                      all other years before 2000
     * @returns {Date}                      the parsed date; <code>null</code>
     *                                      if this string is empty
     * @throws Error                        if this string does not represent a
     *                                      valid date according to the
     *                                      specified or default format
     */
    String.prototype.parseDate = function (format, baseYear) {
        var $L = $LANG,
            day = 1,
            fmt = "",
            hours = 0,
            minutes = 0,
            month = 0,
            part = "",
            pos = 0,
            regexp = /[^dMyHm]/,
            s = this,
            token,
            year = 1970;

        if (!$.trim(s)) {
            return null;
        }
        baseYear = baseYear || 35;

        format = format || "datetime";
        if ((format === "date") || (format === "time")
            || (format === "datetime"))
        {
            if ((format === "date") || (format === "datetime")) {
                fmt += $L("default.format.date");
            }
            if (format === "datetime") {
                fmt += " ";
            }
            if ((format === "time") || (format === "datetime")) {
                fmt += $L("default.format.time");
            }
            format = fmt;
        }

        while (format) {
            if (regexp.test(format)) {
                token = RegExp.leftContext;
                pos = s.indexOf(RegExp.lastMatch);
                if ((pos < 0) || (pos + 1 >= s.length)) {
                    throw new Error("Invalid date or time format: " + s);
                }
                part = s.substring(0, pos);
                s = s.substring(pos + 1);
                format = RegExp.rightContext;
            } else {
                token = format;
                part = s;
                format = "";
            }

            switch (token) {
            case 'd':
            case 'dd':
                day = parseInt(part, 10);
                break;
            case 'M':
            case 'MM':
                month = parseInt(part, 10) - 1;
                break;
            case 'y':
            case 'yy':
            case 'yyy':
            case 'yyyy':
                year = parseInt(part, 10);
                if (year < 100) {
                    year += (year < baseYear) ? 2000 : 1900;
                }
                break;
            case 'H':
            case 'HH':
                hours = parseInt(part, 10);
                break;
            case 'm':
            case 'mm':
                minutes = parseInt(part, 10);
                break;
            }
        }

        if (isNaN(year) || isNaN(month) || isNaN(day) || isNaN(hours)
            || isNaN(minutes))
        {
            throw new Error("Invalid date or time format: " + s);
        }
        return new Date(year, month, day, hours, minutes, 0, 0);
    };

    /**
     * Parses this string as number using the decimal and grouping separator
     * specified in the attributes <code>decimal-separator</code> and
     * <code>grouping-separator</code>, respectively, in the
     * <code>&lt;html></code> tag.  If the separators are not set, a dot (.) is
     * used as decimal separator and a comma (,) as grouping separator.
     *
     * @function
     * @name                String#parseNumber
     * @returns {Number}    the parsed number
     * @since               1.3
     */
    String.prototype.parseNumber = function () {
        var reD = new RegExp(RegExp.escape(decimalSeparator), "g"),
            reG = new RegExp(RegExp.escape(groupingSeparator), "g"),
            s = this;

        return (s === "") ? 0
                : parseFloat(s.replace(reG, "").replace(reD, "."));

    };

}(undefined, jQuery, $L));

// vim:set ts=4 sw=4 sts=4:
