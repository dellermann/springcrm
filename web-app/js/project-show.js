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

    var changePhase,
        jQuery = $,
        loadList = null,
        onChangeTopCheckbox = null,
        onChangeType = null,
        onClick,
        onClickSelectItem = null,
        onLoadedList = null,
        onOpenSelectDlg = null,
        submitSelectedItems;

    changePhase = function (phaseName) {
        var $ = jQuery;

        $.get(
                $("#project-phases").attr("data-set-phase-url"),
                { phase: phaseName }
            );
    };

    loadList = function (url) {
        $("#select-project-item-list").load(url + " #content", onLoadedList);
    };

    onChangeTopCheckbox = function () {
        var $this = $(this);

        $this.parents(".content-table")
            .find("tbody td:first-child input:checkbox")
                .attr("checked", $this.is(":checked"));
    };

    onChangeType = function () {
        var $this = $(this);

        $("#select-project-item-dialog h2").text($this.find(":selected").text());
        loadList($(this).val());
    };

    onClick = function (event) {
        var $ = jQuery,
            $section,
            $target = $(event.target),
            phaseName,
            res = true;

        $section = $target.parents("section");
        phaseName = $section.attr("data-phase");
        if ($target.is("#project-phases h5")) {
            $("#project-phase").text($target.text());
            $section.addClass("current")
                .siblings()
                    .removeClass("current");
            changePhase(phaseName);
            res = false;
        } else if ($target.is(".project-phase-actions-create")) {
            $("#create-project-item-dialog").dialog({ modal: true })
                .find("a")
                    .click(function () {
                        window.location.href =
                            $(this).attr("href") + "&projectPhase=" + phaseName;
                        return false;
                    });
            res = false;
        } else if ($target.is(".project-phase-actions-select")) {
            $("#select-project-item-dialog").dialog({
                    minWidth: 700, minHeight: 400, modal: true,
                    open: onOpenSelectDlg
                })
                .data("phase", phaseName);
            res = false;
        }
        return res;
    };

    onClickSelectItem = function () {
        var itemId;

        itemId = $(this).parents("tr")
            .find("td:first-child input:checkbox")
            .data("id");
        submitSelectedItems([ itemId ]);
        $("#select-project-item-dialog").dialog("close");
        return false;
    };

    onLoadedList = function () {
        var $ = jQuery,
            $a,
            $list = $("#select-project-item-list"),
            $table = $list.find(".content-table");

        $table.find("td:last-child")
                .remove()
            .end()
            .find("th:last-child")
                .remove()
            .end()
            .find("tbody a")
                .each(function () {
                    var $this = $(this);

                    $this.replaceWith($this.html());
                })
            .end()
            .find("th input:checkbox")
                .change(onChangeTopCheckbox);
        $list.find("a")
                .click(function () {
                    loadList($(this).attr("href"));
                    return false;
                });
        $a = $('<a href="#"/>')
            .click(onClickSelectItem);
        $table.find("tbody td:nth-child(2)")
            .wrapInner($a);
    };

    onOpenSelectDlg = function () {
        $("#select-project-item-type-selector").change(onChangeType)
            .trigger("change");
    };

    submitSelectedItems = function (ids) {
        var controller,
            phaseName = $("#select-project-item-dialog").data("phase");

        controller = $("#select-project-item-type-selector :selected")
            .data("controller");
        alert("Selected " + phaseName + "/" + controller + "/" + ids);
    };

    $("#project-phases").click(onClick);
}(window, jQuery));