(function ($, $L) {

    "use strict";

    var $LANG = $L,
        jQuery = $,
        addItem,
        onBlurItemInput,
        onClickAddItem,
        onClickItemList,
        onClickRestoreList,
        onDblClickItemList,
        onKeyPressItem = null,
        onLoadedSelValues,
        onSubmitForm,
        restoreList = null,
        showEditField = null,
        submitList = null;


    //-- Functions ------------------------------

    addItem = function ($ul, item) {
        var $ = jQuery,
            $L = $LANG,
            $li;

        item = item || { id: -1, name: "" };
        $li = $("<li/>").addClass("ui-state-default")
            .appendTo($ul);
        if (item.id) {
            $li.attr("data-item-id", item.id);
        }
        $("<span/>").text(item.name)
            .appendTo($li);
        $('<a href="#"/>').text($L("default.btn.remove"))
            .addClass("delete-btn")
            .appendTo($li);
        $('<a href="#"/>').text($L("default.btn.edit"))
            .addClass("edit-btn")
            .appendTo($li);
        return $li;
    };

    onBlurItemInput = function () {
        var $this = $(this),
            val = $this.val();

        if (val === "") {
            $this.parents("li")
                .remove();
        } else {
            $this.prevAll("span")
                    .text(val)
                    .show()
                .end()
                .nextAll(".edit-btn")
                    .show()
                .end()
                .remove();
        }
    };

    onClickAddItem = function () {
        var $li;

        $li = addItem($(this).parents(".sel-values-list").find("ul"));
        showEditField.call($li.find("span"));
    };

    onClickItemList = function (event) {
        var $target = $(event.target);

        if ($target.hasClass("edit-btn")) {
            showEditField.call($target.prevAll("span"));
            return false;
        } else if ($target.hasClass("delete-btn")) {
            $target.parents("li")
                .remove();
        }

        return true;
    };

    onClickRestoreList = function () {
        restoreList.call($(this).parents(".sel-values-list"));
    };

    onDblClickItemList = function (event) {
        var $li = $(event.target).parents("li").andSelf();

        if ($li.find("input").length === 0) {
            showEditField.call(
                    $li.find("span")
                );
        }
    };

    onKeyPressItem = function (event) {
        if (event.which === 13) {
            $(this).triggerHandler("blur");
            return false;
        }
    };

    onLoadedSelValues = function (data) {
        var $ = jQuery,
            $ul,
            i = -1,
            n = data.length;

        this.empty();
        $ul = $("<ul/>")
            .click(onClickItemList)
            .dblclick(onDblClickItemList)
            .appendTo(this);
        while (++i < n) {
            addItem($ul, data[i]);
        }
        $ul.sortable({
                forcePlaceholderSize: true,
                placeholder: "ui-state-highlight"
            });
        $('<a href="#"/>').text($L("default.btn.add"))
            .addClass("button medium green add-btn")
            .click(onClickAddItem)
            .appendTo(this);
        $('<a href="#"/>').text($L("config.restoreList.label"))
            .addClass("button medium orange restore-btn")
            .click(onClickRestoreList)
            .appendTo(this);
    };

    onSubmitForm = function () {
        $(".sel-values-list").each(submitList);
    };

    restoreList = function () {
        var $ = jQuery,
            $this = $(this);

        $.getJSON(
                $this.attr("data-load-sel-values-url"), null,
                $.proxy(onLoadedSelValues, $this)
            );
    };

    submitList = function () {
        var $ = jQuery,
            $this = $(this),
            data = [];

        $this.find("li")
            .each(function () {
                var $this = $(this),
                    itemId = $this.attr("data-item-id");

                if (itemId) {
                    data.push({
                            id: parseInt(itemId, 10),
                            name: $this.find("span").text()
                        });
                }
            });
        $.ajax({
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify(data),
                dataType: "json",
                type: "POST",
                url: $this.attr("data-save-sel-values-url")
            });
    };

    showEditField = function () {
        $("<input/>", { type: "text", value: this.text() })
            .blur(onBlurItemInput)
            .keypress(onKeyPressItem)
            .insertAfter(this)
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