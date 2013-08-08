#
# overview.coffee
#
# Copyright (c) 2011-2013, Daniel Ellermann
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#


$ = jQuery


OverviewPanelsWidget =
  options:
    addPanelLink: "#add-panel"
    addPanelUrl: null
    columns: ".overview-column"
    movePanelUrl: null
    panelList: "#panel-list"
    panels: ".springcrm-overviewpanels-panel"
    removePanelUrl: null

  # Computes a list of Y-positions of all panels within the given column.
  #
  # @param {jQuery} $col  the given column
  # @return {Array}       the Y-positions
  #
  _computeYList: ($col) ->
    yList = [ 0 ]
    $col.find(@options.panels).each ->
      $this = $(this)
      yList.push $this.position().top + $this.outerHeight(true)
    yList

  # Initializes this overview panels widget.
  #
  _create: ->
    $ = jQuery
    elem = @element

    opts = @options
    opts.addPanelUrl ?= elem.data("add-panel-url")
    opts.movePanelUrl ?= elem.data("move-panel-url")
    opts.removePanelUrl ?= elem.data("remove-panel-url")

    @$panels = elem.find(opts.panels)
      .each (idx, panel) =>
        @_initPanel $(panel)
    @$columns = $columns = elem.find(opts.columns)
      .droppable
        drop: (event, ui) => @_onDrop event, ui
        hoverClass: "drag-hover"
        out: (event, ui) => @_onDropOut event, ui
        over: (event, ui) => @_onDropOver event, ui
    
    @$addPanelLink = $(opts.addPanelLink).on "click", (event) => 
      @_onClickAddPanel event
    @$columnParent = $columns.parent()

  # Initializes the given overview page panel.
  # 
  # @param {jQuery} $panel  the given panel
  #
  _initPanel: ($panel) ->
    $ = jQuery

    url = $panel.data("panel-url")
    if url
      $.ajax
        dataType: "html"
        success: (html) ->
          $panel.find("> div").html html
        url: url
    $panel.draggable
      containment: "document"
      drag: (event, ui) => @_onDrag event, ui
      handle: "h3"
      opacity: 0.5
      start: (event, ui) => @_onDragStart event, ui
      stop: => @_onDragStop
    $a = $panel.find("header a")
    $a.on "click", (event) => @_onRemovePanel(event) if $a.length
    null

  # Called if the user clicks on the button to add panels.  The method shows
  # the area to select available panels.  If the available panel list is not
  # loaded yet it is requested via AJAX.
  #
  # @param {Object} event the event data
  # @return {Boolean}     always `false` to prevent event bubbling
  #
  _onClickAddPanel: (event) ->
    $ = jQuery

    if @availablePanels
      @_onShowPanelList()
    else
      url = $(event.target).attr("href")
      $.getJSON url, (data) => @_onPanelsLoaded data
    false

  # Called if a panel is dragged.
  #
  # @param {Object} event the event data
  # @param {Object} ui    information about the dragged panel
  #
  _onDrag: (event, ui) ->
    opts = @options

    $col = @$activeCol
    panelSel = opts.panels
    $panels = $col.find(panelSel)
    offset = ui.offset
    top = offset.top - $col.offset().top + $col.scrollTop()

    yList = @yList
    n = yList.length - 1
    $panel = null
    for i in [0..n]
      if (top >= yList[i]) and (top < yList[i + 1])
        $panel = $panels.eq(i)
        if top > $panel.position().top + $panel.height() / 2
          $panel = $panels.eq(i + 1)
        break

    helper = ui.helper
    if $panel and ($panel.length > 0)
      panel = $panel.get(0)
      if (helper.get(0) isnt panel) and (@lastMovedPanel isnt panel)
        $panel.prevAll(panelSel)
            .not(helper)
              .stop(true, true)
              .animate({ top: 0 }, "slow")
            .end()
          .end()
          .nextAll(panelSel)
          .addBack()
            .not(helper)
              .stop(true, true)
              .animate { top: helper.outerHeight(true) }, "slow"
        @lastMovedPanel = $panel.get(0)
    else if top >= yList[n]
      $panels.not(helper)
        .stop(true, true)
        .animate { top: 0 }, "slow"
    @$dropBefore = $panel

  # Called if a panel is about to be dragged.
  #
  # @param {Object} event the event data
  # @param {Object} ui    information about the dropped panel
  #
  _onDragStart: (event, ui) ->
    @$columns.css "padding-bottom", ui.helper.outerHeight(true) + 10

  # Called if a panel has been stopped dragging.
  #
  _onDragStop: ->
    @$columns.css "padding-bottom", 0

  # Called if the panel has been dropped on a column.
  #
  # @param {Object} event the event data
  # @param {Object} ui    information about the dropped panel
  #
  _onDrop: (event, ui) ->
    $col = $(event.target)
    opts = @options
    
    # check whether a new panel has been dropped 
    helper = ui.helper
    panelAdded = helper.attr("id").match /^add-panel-([\w\-]+)$/
    if panelAdded
      panelId = RegExp.$1
      panelDef = @availablePanels[panelId]
      html = """
        <div id="#{panelId}" class="#{@widgetBaseClass}-panel"
          data-panel-url="#{panelDef.url}">
          <header>
            <h3>#{panelDef.title}</h3>
"""
      removePanelUrl = opts.removePanelUrl
      if removePanelUrl
        html += """
            <a href="#{removePanelUrl}"><i class="icon-remove icon-large"></i></a>
"""
      html += """
          </header>
          <div style="#{panelDef.style}"></div>
        </div>
"""
      $panel = $(html)
      @_initPanel $panel

      helper.remove()
      $panelList = $(opts.panelList)
      $panelList.slideUp() unless $panelList.find("li").length
    else
      $panel = helper
      helper.css
        left: 0
        top: 0
    
    # place the moved panel 
    $dropBefore = @$dropBefore
    if $dropBefore
      $dropBefore.before $panel unless $dropBefore.get(0) is $panel.get(0)
    else
      $col.append $panel
    @$panels.stop(true, true)
      .animate { top: 0 }, "slow"
    col = @$columnParent.find(opts.columns).index($col)
    pos = $col.find(opts.panels).index($panel)
    url = (if panelAdded then opts.addPanelUrl else opts.movePanelUrl)
    if url
      $.getJSON url,
        col: col
        panelId: $panel.attr("id")
        pos: pos

  # Called if a panel is dragged out of a column.
  #
  # @param {Object} event the event data
  # @param {Object} information about the panel
  #
  _onDropOut: (event, ui) ->
    $(event.target).find(@options.panels)
      .not(ui.helper)
        .stop(true, true)
        .animate { top: 0 }, "slow"

  # Called if a panel is dragged over a column.  The method stores the
  # selected column as active column.
  #
  # @param {Object} event the event data
  #
  _onDropOver: (event) ->
    $col = $(event.target)
    @yList = @_computeYList $col
    @$activeCol = $col

  # Called if the list of available panels has been loaded from the server.
  #
  # @param {Array} data the available panels
  #
  _onPanelsLoaded: (data) ->
    @availablePanels = data
    @_refreshPanelList()
    @_onShowPanelList()

  # Called if a panel is to remove from the overview page.
  #
  # @param {Object} event the event data
  # @return {Boolean}     always `false` to prevent event bubbling
  #
  _onRemovePanel: (event) ->
    $ = jQuery
    $a = $(event.target).closest "a"
    $panel = $a.parents @options.panels
    panelId = $panel.attr "id"
    url = $a.attr("href")
    $panel.remove()
    $.getJSON url,
      panelId: panelId
    @_refreshPanelList()
    false

  # Opens the list of available panels.
  #
  _onShowPanelList: ->
    $(@options.panelList).slideToggle()

  # Refreshes the list of available panels.
  #
  _refreshPanelList: ->
    $ = jQuery

    data = @availablePanels
    html = ""
    for d of data
      if data.hasOwnProperty(d) and $("##{d}").length is 0
        html += """<li id="add-panel-#{d}">#{data[d].title}</li>"""

    $(@options.panelList).html(if html then "<ol>#{html}</ol>" else "")
      .find("li")
        .draggable
          containment: "#content"
          revert: "invalid"

$.widget "springcrm.overviewpanels", OverviewPanelsWidget


