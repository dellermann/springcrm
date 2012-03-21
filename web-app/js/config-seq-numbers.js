/*
 * config-seq-numbers.js
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

    var checkAddOrgNumber,
        computeRndNumber,
        onChangeSeqNumberData,
        renderExample = null;

    checkAddOrgNumber = function () {
        var ctrlName = "," + $(this).attr("data-controller-name") + ",",
            ctrlNames = ",quote,salesOrder,invoice,dunning,creditMemo,";

        return ctrlNames.indexOf(ctrlName) >= 0;
    };

    computeRndNumber = function () {
        var diff,
            end,
            start;

        start = parseInt(
                this.find("td[headers*=seq-numbers-header-start-value] input")
                    .val(),
                10
            );
        end = parseInt(
                this.find("td[headers*=seq-numbers-header-end-value] input")
                    .val(),
                10
            );
        if (isNaN(start) || isNaN(end)) {
            return "";
        } else {
            diff = Math.abs(end - start);
            return String(Math.round(Math.random() * diff) + start);
        }
    };

    onChangeSeqNumberData = function (event) {
        var $target = $(event.target),
            $tr = $target.parents("tr");

        renderExample.call($tr);
        if ($tr.attr("data-controller-name") === "organization") {
            $tr.siblings()
                .each(function () {
                    var $this = $(this);

                    if (checkAddOrgNumber.call($this)) {
                        renderExample.call($this);
                    }
                });
        }
    };

    renderExample = function () {
        var $this = $(this),
            s = "",
            suffix;

        s = $this.find("td[headers*=seq-numbers-header-prefix] input")
            .val();
        if (s !== "") {
            s += "-";
        }
        s += computeRndNumber.call($this);
        suffix = $this.find("td[headers*=seq-numbers-header-suffix] input")
            .val();
        if (suffix !== "") {
            s += "-" + suffix;
        }
        if (checkAddOrgNumber.call($this)) {
            s += "-" + computeRndNumber.call(
                    $this.siblings("tr[data-controller-name=organization]")
                );
        }
        $this.find("td[headers*=seq-numbers-header-example]")
            .text(s);
    };

    $("#seq-numbers").change(onChangeSeqNumberData)
        .find("tbody tr")
            .each(renderExample);
}(jQuery));