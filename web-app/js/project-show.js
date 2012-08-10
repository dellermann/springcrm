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


/**
 * @fileOverview    Contains functions which are loaded in the show view of the
 *                  project.
 * @author          Daniel Ellermann
 * @version         1.2
 * @since           1.0
 */


/*global window: false, jQuery: false */
/*jslint nomen: true, plusplus: true, white: true */


/**
 * @name        project-show
 * @namespace   Contains functions which are loaded in the show view of the
 *              project.
 * @author      Daniel Ellermann
 * @version     1.2
 * @since       1.0
 */
/**#@+
 * @memberOf    project-show
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
        onSelectProjectStatus,
        onSubmittedSelectedItems = null,
        showFileSelector,
        submitSelectedDocuments = null,
        submitSelectedItems;

    /**
     * Changes the current project phase.  The method submits the project phase
     * to the server.
     *
     * @param {String} phaseName    the name of the current project phase
     */
    changePhase = function (phaseName) {
        var $ = jQuery;

        $.get(
                $("#project-phases").data("set-phase-url"),
                { phase: phaseName }
            );
    };

    /**
     * Loads the list of items with the given URL into the selector dialog.
     *
     * @param {String} url  the given URL
     */
    loadList = function (url) {
        $("#select-project-item-list").load(
                url, { view: 'selector' }, onLoadedList
            );
    };

    /**
     * Called if the checkbox to select or unselect all entries in the item
     * table is changed.
     */
    onChangeTopCheckbox = function () {
        var $this = $(this);

        $this.parents(".content-table")
            .find("tbody td:first-child input:checkbox")
                .attr("checked", $this.is(":checked"));
    };

    /**
     * Called if the type selector is changed.
     */
    onChangeType = function () {
        var $option,
            $this = $(this);

        $option = $this.find(":selected");
        $("#select-project-item-dialog h2").text($option.text());
        if ($option.data("controller") === "document") {
            showFileSelector($this.val());
            $("#select-project-item-add-btn").fadeOut();
            $(".selector-toolbar-search").fadeOut();
            $("#select-project-item-list").fadeOut();
            $("#select-project-document-list").fadeIn();
        } else {
            loadList($this.val());
            $("#select-project-item-add-btn").show();
            $(".selector-toolbar-search").show();
            $("#select-project-item-list").fadeIn();
            $("#select-project-document-list").fadeOut();
        }
    };

    /**
     * Called if an element is clicked.
     *
     * @param {Object} event    the event data
     */
    onClick = function (event) {
        var $ = jQuery,
            $section,
            $target = $(event.target),
            phaseName,
            res = true;

        $section = $target.parents("section");
        phaseName = $section.data("phase");
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
                    minWidth: 900, minHeight: 520, modal: true,
                    open: onOpenSelectDlg
                })
                .data("phase", phaseName);
            res = false;
        }
        return res;
    };

    /**
     * Called if the button to add an item is clicked.
     */
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

    /**
     * Called if any link in the item selector dialog is clicked.  The method
     * loads the link into the same dialog.
     */
    onClickLink = function () {
        loadList($(this).attr("href"));
        return false;
    };

    /**
     * Called if an item in the item selector dialog is clicked and thus the
     * item is selected.
     */
    onClickSelectItem = function () {
        var itemId;

        itemId = $(this).parents("tr")
            .data("item-id");
        submitSelectedItems([ itemId ]);
        $("#select-project-item-dialog").dialog("close");
        return false;
    };

    /**
     * Called if the data for the item selector dialog are received from the
     * server and are to display.
     */
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

    /**
     * Called if the item selector dialog is to display.
     */
    onOpenSelectDlg = function () {
        $("#select-project-item-type-selector").change(onChangeType)
            .trigger("change");
        $("#select-project-item-add-btn").click(onClickAddBtn);
    };

    /**
     * Called if the project status is changed.
     */
    onSelectProjectStatus = function () {
        var $this = $(this),
            status = $this.val();

        $.get($this.data("submit-url"), { status: status });
        $("#project-status-indicator")
            .attr("class", "project-status-" + status)
            .text($this.find(":selected").text());
    };

    /**
     * Called after the item selected from the item selector dialog has been
     * submitted to the server.
     */
    onSubmittedSelectedItems = function () {
        window.location.reload(true);
    };

    /**
     * Shows the selector to select documents.
     *
     * @param {String} url  the URL used to obtain the documents from the
     *                      server
     */
    showFileSelector = function (url) {
        $("#select-project-document-list").elfinder({
            commands: [
                'open', 'reload', 'home', 'up', 'back', 'forward', 'getfile',
                'quicklook', 'download', 'rm', 'duplicate', 'rename', 'mkdir',
                'mkfile', 'upload', 'copy', 'cut', 'paste', 'edit',
                /*'extract', 'archive', */'search', 'info', 'view', 'help',
                /*'resize', */'sort'
            ],
            commandsOptions: {
                getfile: {
                    onlyURL: false
                }
            },
            contextmenu: {
                files: [
                    'getfile', '|','open', 'quicklook', '|', 'download', '|',
                    'copy', 'cut', 'paste', 'duplicate', '|',
                    'rm', '|', 'edit', 'rename', '|', 'info'
                ]
            },
            getFileCallback: submitSelectedDocuments,
            lang: "de",
            uiOptions: {
                toolbar: [
                    ['back', 'forward'],
                    ['mkdir', 'mkfile', 'upload'],
                    ['open', 'download', 'getfile'],
                    ['info', 'quicklook'],
                    ['copy', 'cut', 'paste'],
                    ['rm'],
                    ['duplicate', 'rename', 'edit'],
                    ['search'],
                    ['view', 'sort'],
                    ['help']
                ]
            },
            url: url
        })
        .elfinder("instance");
    };

    /**
     * Submits the selected document to the server to associate them to the
     * current project.
     *
     * @param {Object} file the data of the selected document
     */
    submitSelectedDocuments = function (file) {
        var $ = jQuery,
            $dialog = $("#select-project-item-dialog"),
            phaseName = $dialog.data("phase"),
            url = $dialog.data("submit-url");

        $.post(
                url, {
                    projectPhase: phaseName, controllerName: "document",
                    documents: file.hash
                },
                onSubmittedSelectedItems
            );
    };

    /**
     * Submits the selected items to the server to associate them to the
     * current project.
     *
     * @param {Array} ids   the IDs of the selected items
     */
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
    $("#project-status").selectmenu({
            dropdown: false,
            menuWidth: 200,
            select: onSelectProjectStatus,
            style: "popup"
        });
}(window, jQuery));
/**#@-*/
