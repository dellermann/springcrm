/*
 * overview.js
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

    var jQuery = $;

    $.widget("springcrm.overviewpanels", {
        options: {
            addPanelLink: "#add-panel",
            addPanelUrl: null,
            columns: ".overview-column",
            movePanelUrl: null,
            panelList: "#panel-list",
            panels: ".springcrm-overviewpanels-panel",
            removePanelUrl: null
        },

        _computeYList: function ($col) {
            var yList = [ 0 ];

            $col.find(this.options.panels)
                .each(function () {
                    var $this = $(this);

                    yList.push($this.position().top + $this.outerHeight(true));
                });
            return yList;
        },

        _create: function () {
            var $ = jQuery,
                $columns,
                elem = this.element,
                opts = this.options,
                self = this;

            opts.addPanelUrl = opts.addPanelUrl
                || elem.data("add-panel-url");
            opts.movePanelUrl = opts.movePanelUrl
                || elem.data("move-panel-url");
            opts.removePanelUrl = opts.removePanelUrl
                || elem.data("remove-panel-url");
            this.$panels = $(opts.panels, this.element).each(function () {
                    self._initPanel(this);
                });
            $columns = $(opts.columns, this.element).droppable({
                    drop: function (event, ui) {
                        self._onDrop($(this), event, ui);
                    },
                    hoverClass: "drag-hover",
                    out: function (event, ui) {
                        self._onDropOut($(this), event, ui);
                    },
                    over: function (event, ui) {
                        self._onDropOver($(this), event, ui);
                    }
                });
            this.$columns = $columns;
            this.$addPanelLink = $(opts.addPanelLink).click(
                    $.proxy(this._onClickAddPanel, this)
                );
            this.$columnParent = $columns.parent();
        },

        _initPanel: function (panel) {
            var $ = jQuery,
                $a,
                $panel = $(panel),
                baseClass = this.widgetBaseClass,
                url;

            url = $panel.data("panel-url");
            if (url) {
                $.ajax({
                    dataType: "html",
                    success: function (html) {
                        $panel.find("> ." + baseClass + "-panel-content")
                            .html(html);
                    },
                    url: url
                });
            }
            $panel.draggable({
                    containment: "document",
                    drag: $.proxy(this._onDrag, this),
                    handle: "h3",
                    opacity: 0.5,
                    start: $.proxy(this._onDragStart, this),
                    stop: $.proxy(this._onDragStop, this)
                });
            $a = $panel.find("." + baseClass + "-panel-close-btn");
            if ($a.length !== 0) {
                $a.click($.proxy(this._onRemovePanel, this));
            }
        },

        _onClickAddPanel: function (event) {
            var $ = jQuery,
                url;

            if (this.availablePanels) {
                this._onShowPanelList();
            } else {
                url = $(event.target).attr("href");
                $.getJSON(url, $.proxy(this._onPanelsLoaded, this));
            }
            return false;
        },

        _onDrag: function (event, ui) {
            var $col = this.$activeCol,
                $panel = null,
                $panels,
                helper = ui.helper,
                i = -1,
                n,
                offset = ui.offset,
                opts = this.options,
                panel,
                panelSel = opts.panels,
                top,
                yList = this.yList;

            $panels = $col.find(panelSel);
            top = offset.top - $col.offset().top + $col.scrollTop();
            n = yList.length - 1;
            while (++i < n) {
                if ((top >= yList[i]) && (top < yList[i + 1])) {
                    $panel = $panels.eq(i);
                    if (top > $panel.position().top + $panel.height() / 2) {
                        $panel = $panels.eq(i + 1);
                    }
                    break;
                }
            }

            if ($panel && ($panel.length > 0)) {
                panel = $panel.get(0);
                if ((helper.get(0) !== panel)
                    && (this.lastMovedPanel !== panel))
                {
                    $panel.prevAll(panelSel)
                            .not(helper)
                                .stop(true, true)
                                .animate({ top: 0 }, "slow")
                            .end()
                        .end()
                        .nextAll(panelSel)
                        .andSelf()
                        .not(helper)
                            .stop(true, true)
                            .animate({ top : helper.outerHeight(true) }, "slow");
                    this.lastMovedPanel = $panel.get(0);
                }
            } else if (top >= yList[n]) {
                $panels.not(helper)
                    .stop(true, true)
                    .animate({ top: 0 }, "slow");
            }
            this.$dropBefore = $panel;
        },

        _onDragStart: function (event, ui) {
            this.$columns
                .css("padding-bottom", ui.helper.outerHeight(true) + 10);
        },

        _onDragStop: function () {
            this.$columns
                .css("padding-bottom", 0);
        },

        _onDrop: function ($col, event, ui) {
            var $dropBefore = this.$dropBefore,
                $panel,
                $panelList,
                baseClass = this.widgetBaseClass,
                col,
                helper = ui.helper,
                html = "",
                opts = this.options,
                panelAdded,
                panelDef,
                panelId,
                pos,
                removePanelUrl = opts.removePanelUrl,
                url;

            /* check whether a new panel has been dropped */
            panelAdded = helper.attr("id")
                .match(/^add-panel-([\w\-]+)$/);
            if (panelAdded) {
                panelId = RegExp.$1;
                panelDef = this.availablePanels[panelId];
                html = '<div id="' + panelId +
                    '" class="' + baseClass + '-panel" data-panel-url="' +
                    panelDef.url + '"><div class="' + baseClass +
                    '-panel-header"><h3>' + panelDef.title + '</h3>';
                if (removePanelUrl) {
                    html += '<a href="' + removePanelUrl +
                    '" class="' + baseClass + '-panel-close-btn">×</a>';
                }
                html += '</div><div class="' + baseClass +
                    '-panel-content" style="' + panelDef.style +
                    '"></div></div>';
                $panel = $(html);
                this._initPanel($panel);
                helper.remove();
                $panelList = $(opts.panelList);
                if ($panelList.find("li").length === 0) {
                    $panelList.slideUp();
                }
            } else {
                $panel = helper;
                helper.css({ left: 0, top: 0 });
            }

            /* place the moved panel */
            if ($dropBefore) {
                if ($dropBefore.get(0) !== $panel.get(0)) {
                    $dropBefore.before($panel);
                }
            } else {
                $col.append($panel);
            }

            this.$panels
                .stop(true, true)
                .animate({ top: 0 }, "slow");

            col = this.$columnParent
                .find(opts.columns)
                .index($col);
            pos = $col.find(opts.panels)
                .index($panel);
            url = panelAdded ? opts.addPanelUrl : opts.movePanelUrl;
            if (url) {
                $.getJSON(
                        url, {
                            panelId: $panel.attr("id"),
                            col: col,
                            pos: pos
                        }
                    );
            }
        },

        _onDropOut: function ($col, event, ui) {
            var helper = ui.helper;

            $col.find(this.options.panels)
                .not(helper)
                .stop(true, true)
                .animate({ top: 0 }, "slow");
        },

        _onDropOver: function ($col, event) {
            this.yList = this._computeYList($col);
            this.$activeCol = $col;
        },

        _onPanelsLoaded: function (data) {
            this.availablePanels = data;
            this._refreshPanelList();
            this._onShowPanelList();
        },

        _onRemovePanel: function (event) {
            var $ = jQuery,
                $a = $(event.target),
                $parent = $a.parents(this.options.panels),
                panelId = $parent.attr("id"),
                url = $a.attr("href");

            $parent.remove();
            $.getJSON(url, { panelId: panelId });
            this._refreshPanelList();
            return false;
        },

        _onShowPanelList: function () {
            $(this.options.panelList).slideToggle();
        },

        _refreshPanelList: function () {
            var $ = jQuery,
                d = null,
                data = this.availablePanels,
                html = "";

            for (d in data) {
                if (data.hasOwnProperty(d)) {
                    if ($("#" + d).length === 0) {
                        html += '<li id="add-panel-' + d + '">' +
                            data[d].title + "</li>";
                    }
                }
            }
            $(this.options.panelList).html(html ? "<ol>" + html + "</ol>" : "")
                .find("li")
                    .draggable({
                        containment: "#content",
                        revert: "invalid"
                    });
        }
    });

}(jQuery));
