(function (SPRINGCRM, $) {

    "use strict";

    var OverviewPanels = null;


    //== Classes ================================

    /**
     * The class <code>OverviewPanels</code> handles the display and the drag
     * and drop feature of the panels on the overview page.
     *
     * @param {Object} config               the configuration data instance
     * @param {String} [config.columnSel]   the selector representing the
     *                                      columns where the panels reside;
     *                                      defaults to ".overview-column"
     * @param {String} [config.panelSel]    the selector representing the
     *                                      panels; defaults to ".panel"
     * @param {String} config.addPanelUrl   the URL which is called to add a
     *                                      panel
     * @param {String} config.movePanelUrl  the URL which is called to store
     *                                      the new location after a panel has
     *                                      been moved
     * @param {String} config.removePanelUrl
     *                                      the URL which is called after a
     *                                      panel has been removed
     * @param {String|JQueryObject} [config.addPanelLink]
     *                                      either the selector or a JQuery
     *                                      object representing the link to
     *                                      display the panel list in order to
     *                                      add a panel; defaults to
     *                                      "#add-panel"
     * @param {String|JQueryObject} [config.panelList]
     *                                      either the selector or a JQuery
     *                                      object representing the panel list;
     *                                      defaults to "#panel-list"
     */
    OverviewPanels = function (config) {
        var s;

        if (!(this instanceof OverviewPanels)) {
            return new OverviewPanels();
        }


        //-- Instance variables -----------------

        s = config.columnSel || ".overview-column";

        /**
         * The selector representing the columns.
         *
         * @type String
         * @default ".overview-column"
         */
        this._columnSel = s;

        /**
         * The JQuery object representing the columns where the panels reside.
         *
         * @type JQueryObject
         * @default $(".overview-column")
         */
        this._$columns = $(s);

        s = config.panelSel || ".panel";

        /**
         * The selector representing the panels.
         *
         * @type String
         * @default ".panel"
         */
        this._panelSel = s;

        /**
         * The JQuery object representing all the panels.
         *
         * @type JQueryObject
         * @default $(".panel")
         */
        this._$panels = $(s);

        /**
         * The URL which is called to add a panel.
         *
         * @type String
         */
        this._addPanelUrl = config.addPanelUrl;

        /**
         * The URL which is called to store the new location after a panel has
         * been moved.
         *
         * @type String
         */
        this._movePanelUrl = config.movePanelUrl;

        /**
         * The URL which is called to after a panel has been removed.
         *
         * @type String
         */
        this._removePanelUrl = config.removePanelUrl;

        /**
         * A JQuery object representing the link to display the panel list in
         * order to add a panel.
         *
         * @type JQueryObject
         * @default $("#add-panel")
         */
        this._$addPanelLink = $(config.addPanelLink || "#add-panel");

        /**
         * A JQuery object representing the panel list.
         *
         * @type JQueryObject
         * @default $("#panel-list")
         */
        this._$panelList = $(config.panelList || "#panel-list");

        /**
         * The JQuery object representing the container of the columns.
         *
         * @type JQueryObject
         */
        this._$columnParent = this._$columns.parent();

        /**
         * The panel before that the dragged panel is to drop. If not specified
         * the dragged panel is to drop after all panels in the column.
         *
         * @type JQueryObject
         */
        this._$dropBefore = null;

        /**
         * The panel which was moved at last.
         *
         * @type Object
         */
        this._lastMovedPanel = null;

        /**
         * The Y list which stores the bottom coordinates of all panels in the
         * active column. It is used to determine the panel where the dragged
         * panel is over.
         *
         * @type Array
         */
        this._yList = null;

        /**
         * The currently active column.
         *
         * @type JQueryObject
         */
        this._$activeCol = null;

        /**
         * The available panels from the repository.
         *
         * @type Object
         */
        this._availablePanels = null;
    };

    OverviewPanels.prototype = {

        //-- Public methods ---------------------

        /**
         * Initializes the columns and panels. The method loads the content of
         * the panels via AJAX and initializes drag and drop.
         */
        initialize: function () {
            var instance = this;

            this._$panels
                .each(function () {
                    instance._initPanel(this);
                });
            this._$columns
                .droppable({
                    drop: function (event, ui) {
                        instance._onDrop($(this), event, ui);
                    },
                    hoverClass: "drag-hover",
                    out: function (event, ui) {
                        instance._onDropOut($(this), event, ui);
                    },
                    over: function (event, ui) {
                        instance._onDropOver($(this), event, ui);
                    }
                });
            this._$addPanelLink
                .click($.proxy(this._onClickAddPanel, this));
            return this;
        },


        //-- Non-public methods -----------------

        /**
         * Computes the Y list for the given column.
         *
         * @param {JQueryObject} $col   the given column
         * @protected
         */
        _computeYList: function ($col) {
            var yList = [ 0 ];

            $col.find(this._panelSel)
                .each(function () {
                    var $this = $(this);

                    yList.push($this.position().top + $this.outerHeight(true));
                });
            return yList;
        },

        /**
         * Initializes the given panel. The method obtains the content of the
         * panel via AJAX.
         *
         * @param {Object|JQueryObject} panel   the given panel
         * @private
         */
        _initPanel: function (panel) {
            var $a,
                $panel = $(panel),
                url;

            url = $panel.find("> link")
                .attr("href");
            if (url) {
                $.ajax({
                    dataType: "html",
                    success: function (html) {
                        $panel.find("> .panel-content")
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
            $a = $panel.find(".panel-close-btn");
            if ($a.length !== 0) {
                $a.click($.proxy(this._onRemovePanel, this));
            }
        },

        /**
         * Called if the user clicks the "Add panel" button. The method starts
         * loading the panel data from the repository on the server.
         *
         * @param {Object} event    the event data
         * @returns {Boolean}       always <code>false</code>
         * @protected
         */
        _onClickAddPanel: function (event) {
            var url;

            if (this._availablePanels) {
                this._onShowPanelList();
            } else {
                url = $(event.target).attr("href");
                $.ajax({
                    dataType: "json",
                    success: $.proxy(this._onPanelsLoaded, this),
                    url: url
                });
            }
            return false;
        },

        /**
         * Called if the user moves around a panel. The method cares about
         * reserving space for the dragged panel.
         *
         * @param {Object} event    the original event data
         * @param {Object} ui       the UI event data
         * @protected
         */
        _onDrag: function (event, ui) {
            var $col = this._$activeCol,
                $panel = null,
                $panels,
                helper = ui.helper,
                i = -1,
                n,
                offset = ui.offset,
                panel,
                top,
                yList = this._yList;

            $panels = $col.find(this._panelSel);
            top = offset.top - $col.offset().top + $col.scrollTop();
            for (n = yList.length - 1; ++i < n; ) {
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
                    && (this._lastMovedPanel !== panel))
                {
                    $panel.prevAll(this._panelSel)
                            .not(helper)
                                .stop(true, true)
                                .animate({ top: 0 }, "slow")
                            .end()
                        .end()
                        .nextAll(this._panelSel)
                        .andSelf()
                        .not(helper)
                            .stop(true, true)
                            .animate({ top : helper.outerHeight(true) }, "slow");
                    this._lastMovedPanel = $panel.get(0);
                }
            } else if (top >= yList[n]) {
                $panels.not(helper)
                    .stop(true, true)
                    .animate({ top: 0 }, "slow");
            }
            this._$dropBefore = $panel;
        },

        /**
         * Called if the user starts moving around panels. The method extends
         * the height of all columns in order to allow the user to add the
         * draggable panel to the end.
         *
         * @param {Object} event    the original event data
         * @param {Object} ui       the UI event data
         * @protected
         */
        _onDragStart: function (event, ui) {
            this._$columns
                .css("padding-bottom", ui.helper.outerHeight(true) + 10);
        },

        /**
         * Called if the user finishes moving around panels. The method resets
         * the height of all column which was applied in method
         * <code>_onDragStart</code>.
         *
         * @protected
         */
        _onDragStop: function () {
            this._$columns
                .css("padding-bottom", 0);
        },

        /**
         * Called if the user drops a panel. The method inserts the panel at
         * the right position and submits the new position to the server.
         *
         * @param {JQueryObject} $col   the column where the panel was dropped
         * @param {Object} event        the original event data
         * @param {Object} ui           the UI event data
         * @protected
         */
        _onDrop: function ($col, event, ui) {
            var $dropBefore = this._$dropBefore,
                $panel,
                $panelList = this._$panelList,
                col,
                helper = ui.helper,
                html = "",
                panelAdded,
                panelDef,
                panelId,
                pos;

            /* check whether a new panel has been dropped */
            panelAdded = helper.attr("id").match(/^add-panel-([\w\-]+)$/);
            if (panelAdded) {
                panelId = RegExp.$1;
                panelDef = this._availablePanels[panelId];
                html = '<div id="' + panelId + '" class="panel" ' +
                    'itemscope="itemscope" ' +
                    'itemtype="http://www.amc-world.de/data/xml/springcrm/panel-vocabulary">' +
                    '<div class="panel-header"><h3>' + panelDef.title + '</h3>' +
                    '<a href="' + this._removePanelUrl + '" class="panel-close-btn">×</a></div>' +
                    '<link itemprop="panel-link" href="' + panelDef.url + '" />' +
                    '<div class="panel-content" style="' + panelDef.style + '"></div>' +
                    '</div>';
                $panel = $(html);
                this._initPanel($panel);
                helper.remove();
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

            this._$panels
                .stop(true, true)
                .animate({ top: 0 }, "slow");

            col = this._$columnParent
                .find(".overview-column")
                .index($col);
            pos = $col.find(this._panelSel)
                .index($panel);
            $.ajax({
                data: {
                    panelId: $panel.attr("id"),
                    col: col,
                    pos: pos
                },
                dataType: "json",
                url: panelAdded ? this._addPanelUrl : this._movePanelUrl
            });
        },

        /**
         * Called if the user leaves a column while dragging a panel. The
         * method resets the position of the moved panels in the left column
         *
         * @param {JQueryObject} $col   the column which has been left
         * @param {Object} event        the original event data
         * @param {Object} ui           the UI event data
         * @protected
         */
        _onDropOut: function ($col, event, ui) {
            var helper = ui.helper;

            $col.find(this._panelSel)
                .not(helper)
                .stop(true, true)
                .animate({ top: 0 }, "slow");
        },

        /**
         * Called if the user enters a column while dragging a panel. The
         * method stores the column and computes the Y list.
         *
         * @param {JQueryObject} $col   the column which has been entered
         * @param {Object} event        the original event data
         * @protected
         */
        _onDropOver: function ($col, event) {
            this._yList = this._computeYList($col);
            this._$activeCol = $col;
        },

        /**
         * Called if the panel data have been loaded from the repository. The
         * method generates the panel list and initializes drag & drop.
         *
         * @param {Object} data the panel data
         * @protected
         */
        _onPanelsLoaded: function (data) {
            this._availablePanels = data;
            this._refreshPanelList();
            this._onShowPanelList();
        },

        /**
         * Called if a panel is to remove.
         *
         * @param {Object} event    the event data
         * @returns {Boolean}       always <code>false</code>
         * @protected
         */
        _onRemovePanel: function (event) {
            var $a = $(event.target),
                $parent,
                panelId,
                url;

            url = $a.attr("href");
            $parent = $a.parents(".panel");
            panelId = $parent.attr("id");
            $parent.remove();
            $.ajax({
                data: { panelId: panelId },
                dataType: "json",
                url: url
            });
            this._refreshPanelList();
            return false;
        },

        /**
         * Called if the panel list is to show or to hide.
         *
         * @protected
         */
        _onShowPanelList: function () {
            this._$panelList
                .slideToggle();
        },

        /**
         * Refreshes the panel list by re-reading the available panels. All
         * panels which are already in use are not displayed in the list.
         *
         * @protected
         */
        _refreshPanelList: function () {
            var d = null,
                data = this._availablePanels,
                html = "";

            for (d in data) {
                if (data.hasOwnProperty(d)) {
                    if ($("#" + d).length === 0) {
                        html += '<li id="add-panel-' + d + '">' +
                            data[d].title + "</li>";
                    }
                }
            }
            this._$panelList
                .html(html ? "<ol>" + html + "</ol>" : "")
                .find("li")
                    .draggable({
                        containment: "#content",
                        revert: "invalid"
                    });
        }
    };
    SPRINGCRM.OverviewPanels = OverviewPanels;

}(SPRINGCRM, jQuery));
