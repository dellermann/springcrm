/*
 * config-sel-values.js
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


(function ($, $L) {

    "use strict";

    var $LANG = $L,
        jQuery = $,
        addItem,
        hideEditField,
        itemsToRemove = [],
        onBlurItemInput,
        onChangeSortable,
        onClickAddItem,
        onClickItemList,
        onClickRestoreList,
        onClickSortList,
        onDblClickItemList,
        onKeyPressItem = null,
        onLoadedSelValues,
        onSubmitForm,
        restoreList = null,
        setDirty = null,
        showEditField = null,
        submitList = null;


    //-- Functions ------------------------------

    addItem = function ($ul, item) {
        var $ = jQuery,
            $L = $LANG,
            $li;

        item = item || { id: -1, name: "", disabled: false };
        $li = $('<li class="ui-state-default"/>')
            .data("item-id", item.id)
            .appendTo($ul);
        $('<span class="value"/>').text(item.name)
            .appendTo($li);
        if (!item.disabled) {
            $('<a href="#" class="delete-btn"/>').text($L("default.btn.remove"))
                .appendTo($li);
            $('<a href="#" class="edit-btn"/>').text($L("default.btn.edit"))
                .appendTo($li);
        } else {
            $li.data("item-disabled", "true");
        }
        return $li;
    };

    hideEditField = function () {
        this.parent()
            .remove();
    };

    onBlurItemInput = function (event, cancel) {
        var $li,
            $span,
            $this = $(this),
            val = $this.val();

        $li = $this.parents("li");
        if (!cancel) {
            $li.siblings()
                .each(function () {
                    if ($(this).find(".value").text() === val) {
                        cancel = true;
                        val = "";
                        return false;
                    }
                    return true;
                });
        }
        if ((val === "") && !cancel) {
            $li.remove();
        } else {
            $span = $li.find(".value")
                .show();
            if (!cancel) {
                $span.text(val);
            }
            $li.find(".edit-btn")
                .show();
            hideEditField.call($this);
        }
    };

    onChangeSortable = function () {
        setDirty.call($(this), true);
    };

    onClickAddItem = function () {
        var $li,
            $ul;

        $ul = $(this).parents(".sel-values-list")
            .find("ul");
        setDirty.call($ul, true);
        $li = addItem($ul);
        showEditField.call($li.find(".value"));
        return false;
    };

    onClickItemList = function (event) {
        var $ = jQuery,
            $li,
            $target = $(event.target),
            $this = $(this),
            id;

        if ($target.hasClass("edit-btn")) {
            showEditField.call($target.prevAll(".value"));
            setDirty.call($this, true);
            return false;
        } else if ($target.hasClass("delete-btn")) {
            $li = $target.parents("li");
            id = $li.data("item-id");
            if (id && (id !== "-1")) {
                itemsToRemove.push(parseInt(id, 10));
            }
            $li.remove();
            setDirty.call($this, true);
            return false;
        }

        return true;
    };

    onClickRestoreList = function () {
        restoreList.call($(this).parents(".sel-values-list"));
        return false;
    };

    onClickSortList = function () {
        var $ = jQuery,
            $li;

        $li = $(this).parents(".sel-values-list")
            .find("li");
        $li.sortElements(function (li1, li2) {
                return $(li1).text() > $(li2).text() ? 1 : -1;
            });
        return false;
    };

    onDblClickItemList = function (event) {
        var $ = jQuery,
            $li = $(event.target).parents("li").andSelf();

        if ($li.find("input").length === 0 && !$li.data("item-disabled")) {
            showEditField.call($li.find(".value"));
            setDirty.call($(this), true);
        }
    };

    onKeyPressItem = function (event) {
        var $this = $(this);

        switch (event.keyCode) {
        case 13:        // Enter/Return
            $this.trigger("blur");
            return false;
        case 27:        // Esc
            $this.trigger("blur", [ true ]);
            return false;
        }

        return true;
    };

    onLoadedSelValues = function (data) {
        var $ = jQuery,
            $div,
            $ul,
            i = -1,
            n = data.length;

        this.empty();
        $div = $('<div class="scroll-pane"/>').appendTo(this);
        $ul = $("<ul/>")
            .click(onClickItemList)
            .dblclick(onDblClickItemList)
            .appendTo($div);
        setDirty.call($ul, false);
        while (++i < n) {
            addItem($ul, data[i]);
        }
        $ul.sortable({
                change: onChangeSortable,
                forcePlaceholderSize: true,
                placeholder: "ui-state-highlight"
            });
        $('<a href="#" class="button medium green add-btn"/>')
            .text($L("default.btn.add.short"))
            .click(onClickAddItem)
            .appendTo(this);
        $('<a href="#" class="button medium orange restore-btn"/>')
            .text($L("config.restoreList.label"))
            .click(onClickRestoreList)
            .appendTo(this);
        $('<a href="#" class="button medium white sort-btn"/>')
            .text($L("default.btn.sort.short"))
            .click(onClickSortList)
            .appendTo(this);
    };

    onSubmitForm = function () {
        $(".sel-values-list").each(submitList);
    };

    restoreList = function () {
        var $ = jQuery,
            $this = $(this);

        $.getJSON(
                $this.data("load-sel-values-url"), null,
                $.proxy(onLoadedSelValues, $this)
            );
    };

    setDirty = function (dirty) {
        this.data("dirty", dirty);
    };

    submitList = function () {
        var $ = jQuery,
            $this = $(this),
            $ul = $this.find("ul"),
            data = [],
            i = -1,
            itr = itemsToRemove,
            n = itr.length;

        if ($ul.data("dirty")) {
            $ul.find("li")
                .each(function () {
                    var $this = $(this),
                        item = {};

                    item.id = parseInt($this.data("item-id"), 10);
                    if (!$this.data("item-disabled")) {
                        item.name = $this.find(".value").text();
                    }
                    data.push(item);
                });
            while (++i < n) {
                data.push({ id: itr[i], remove: true });
            }
            $('<input type="hidden"/>')
                .attr("name", "selValues." + $this.data("list-type"))
                .val($.toJSON(data))
                .appendTo($this);
            setDirty.call($ul, false);
        }
    };

    showEditField = function () {
        var $span;

        $span = $('<span class="input"/>').insertAfter(this);
        $("<input/>", { type: "text", value: this.text() })
            .blur(onBlurItemInput)
            .keypress(onKeyPressItem)
            .appendTo($span)
            .focus();
        this.hide()
            .nextAll(".edit-btn")
                .hide();
    };


    //-- Initialization -------------------------

    $(".sel-values-list").each(restoreList)
        .parents("form")
            .submit(onSubmitForm);
}(jQuery, $L));