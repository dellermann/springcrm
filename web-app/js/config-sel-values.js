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

        item = item || { id: -1, name: "" };
        $li = $('<li class="ui-state-default"/>').appendTo($ul);
        if (item.id) {
            $li.attr("data-item-id", item.id);
        }
        $('<span class="value"/>').text(item.name)
            .appendTo($li);
        if (item.id) {
            $('<a href="#" class="delete-btn"/>').text($L("default.btn.remove"))
                .appendTo($li);
            $('<a href="#" class="edit-btn"/>').text($L("default.btn.edit"))
                .appendTo($li);
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
            id = $li.attr("data-item-id");
            if (id && (id != "-1")) {
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
    };

    onDblClickItemList = function (event) {
        var $ = jQuery,
            $li = $(event.target).parents("li").andSelf();

        if ($li.find("input").length === 0 && $li.attr("data-item-id")) {
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
            $ul,
            i = -1,
            n = data.length;

        this.empty();
        $ul = $("<ul/>")
            .click(onClickItemList)
            .dblclick(onDblClickItemList)
            .appendTo(this);
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
            .text($L("default.btn.add"))
            .click(onClickAddItem)
            .appendTo(this);
        $('<a href="#" class="button medium orange restore-btn"/>')
            .text($L("config.restoreList.label"))
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
                        itemId = $this.attr("data-item-id");

                    if (itemId) {
                        data.push({
                                id: parseInt(itemId, 10),
                                name: $this.find(".value").text()
                            });
                    }
                });
            while (++i < n) {
                data.push({ id: itr[i], name: null });
            }
            $('<input type="hidden"/>')
                .attr("name", "selValues." + $this.attr("data-list-type"))
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