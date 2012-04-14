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
        onClickAddBtn = null,
        onClickLink,
        onClickSelectItem = null,
        onLoadedList = null,
        onOpenSelectDlg = null,
        onSubmittedSelectedItems = null,
        submitSelectedItems;

    changePhase = function (phaseName) {
        var $ = jQuery;

        $.get(
                $("#project-phases").attr("data-set-phase-url"),
                { phase: phaseName }
            );
    };

    loadList = function (url) {
        $("#select-project-item-list").load(
                url, { view: 'selector' }, onLoadedList
            );
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

    onClickAddBtn = function () {
        var $ = jQuery,
            ids = [];

        $("#select-project-item-list tbody :checked").each(function () {
                ids.push($(this).parents("tr").data("item-id"));
            });
        submitSelectedItems(ids);
        $("#select-project-item-dialog").dialog("close");
        return false;
    };

    onClickLink = function () {
        loadList($(this).attr("href"));
        return false;
    };

    onClickSelectItem = function () {
        var itemId;

        itemId = $(this).parents("tr")
            .data("item-id");
        submitSelectedItems([ itemId ]);
        $("#select-project-item-dialog").dialog("close");
        return false;
    };

    onLoadedList = function () {
        var $ = jQuery;

        $("#select-project-item-list")
            .find(".content-table th input:checkbox")
                .change(onChangeTopCheckbox)
            .end()
            .find("a")
                .each(function () {
                    var $this = $(this);

                    if ($this.is(".content-table tbody a")) {
                        $this.click(onClickSelectItem);
                    } else {
                        $this.click(onClickLink);
                    }
                });
    };

    onOpenSelectDlg = function () {
        $("#select-project-item-type-selector").change(onChangeType)
            .trigger("change");
        $("#select-project-item-add-btn").click(onClickAddBtn);
    };

    onSubmittedSelectedItems = function () {
        window.location.reload(true);
    };

    submitSelectedItems = function (ids) {
        var $ = jQuery,
            $dialog = $("#select-project-item-dialog"),
            controller,
            phaseName = $dialog.data("phase"),
            url = $dialog.data("submit-url");

        controller = $("#select-project-item-type-selector :selected")
            .data("controller");
        $.post(
                url, {
                    projectPhase: phaseName, controllerName: controller,
                    itemIds: ids.join()
                },
                onSubmittedSelectedItems
            );
    };

    $("#project-phases").click(onClick);
}(window, jQuery));