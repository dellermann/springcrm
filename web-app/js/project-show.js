/*
 * project-show.js
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

    var jQuery = $,
        onClick;

    onClick = function (event) {
        var $ = jQuery,
            $target = $(event.target),
            phaseName,
            res = true;

        if ($target.is(".project-phases h5")) {
            $("#project-phase").text($target.text());
            phaseName = $target.parents("section")
                .siblings()
                    .removeClass("current")
                .end()
                .addClass("current")
                .attr("data-phase");
            $.get(
                    $target.parents(".project-phases")
                        .attr("data-set-phase-url"),
                    { phase: phaseName }
                );
            res = false;
        } else if ($target.is(".project-phase-actions-create")) {
            $("#create-project-item-dialog").dialog({
                    modal: true
                })
                .find("a")
                    .click(function () {
                        var $this = $(this),
                            phaseName,
                            url = $this.attr("href");

                        phaseName = $target.parents("section")
                            .attr("data-phase");
                        window.location.href = url + "&projectPhase="
                            + phaseName;
                        return false;
                    });
            res = false;
        }
        return res;
    };

    $(".project-phases").click(onClick);
}(window, jQuery));