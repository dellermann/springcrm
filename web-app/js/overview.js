(function (SPRINGCRM, $) {

    "use strict";

    var OverviewPanels = null;


    //== Classes ================================

    /**
     * The class <code>OverviewPanels</code> handles the display and the drag
     * and drop feature of the panels on the overview page.
     *
     * @param {String|JQueryObject} columns either the selector or the JQuery
     *                              object which represents the columns where
     *                              the panels reside
     * @param {String|JQueryObject} panels  either the selector or the JQuery
     *                              object representing the panels
     */
    OverviewPanels = function (columns, panels, movePanelUrl) {
        if (!(this instanceof OverviewPanels)) {
            return new OverviewPanels();
        }


        //-- Instance variables -----------------

        /**
         * The JQuery object which represents the columns where the panels
         * reside.
         *
         * @type JQueryObject
         */
        this._$columns = columns ? $(columns) : $(".overview-column");

        /**
         * The JQuery object representing the panels.
         *
         * @type JQueryObject
         */
        this._$panels = panels ? $(panels) : $(".panel");

        this._movePanelUrl = movePanelUrl;

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
                })
                .draggable({
                    containment: "document",
                    drag: $.proxy(this._onDrag, this),
                    handle: "h3",
                    opacity: 0.5,
                    start: $.proxy(this._onDragStart, this),
                    stop: $.proxy(this._onDragStop, this)
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

            $col.find(".panel").each(function () {
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
            var $panel = $(panel),
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

            $panels = $col.find(".panel");
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
                    $panel.prevAll(".panel")
                            .not(helper)
                                .stop(true, true)
                                .animate({ top: 0 }, "slow")
                            .end()
                        .end()
                        .nextAll(".panel")
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
                col,
                helper = ui.helper,
                pos;

            if ($dropBefore) {
                if ($dropBefore.get(0) !== helper.get(0)) {
                    $dropBefore.before(helper);
                }
            } else {
                $col.append(helper);
            }
            helper.css({ left: 0, top: 0 });
            this._$panels
                .stop(true, true)
                .animate({ top: 0 }, "slow");

            col = $col.parent(".overview-columns")
                .find(".overview-column")
                .index($col);
            pos = $col.find(".panel")
                .index(helper);
            $.ajax({
                data: {
                    panelId: helper.attr("id"),
                    col: col,
                    pos: pos
                },
                dataType: "json",
                url: this._movePanelUrl
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

            $col.find(".panel")
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
        }
    };
    SPRINGCRM.OverviewPanels = OverviewPanels;

}(SPRINGCRM, jQuery));
