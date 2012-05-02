/*
 * report-sales-journal.js
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


(function (window, $) {

    "use strict";

    var onChangeYearSelector,
        onClickMonthSelector,
        submitFilter = null;

    onChangeYearSelector = function () {
        submitFilter($("#month-selector").find(".current"));
    };

    onClickMonthSelector = function (event) {
        var $li = $(event.target).parent("li");

        $li.siblings()
                .removeClass("current")
            .end()
            .addClass("current");
        submitFilter($li);
        return false;
    };

    submitFilter = function ($monthLink) {
        var params,
            url = $monthLink.find("a").attr("href");

        params = {
            month: $monthLink.data("month"),
            year: $("#year-selector").val()
        };
        url += (/\?/.test(url) ? "&" : "?") + $.param(params);
        window.location.href = url;
    };

    $("#month-selector").click(onClickMonthSelector);
    $("#year-selector").selectmenu()
        .change(onChangeYearSelector);
}(window, jQuery));