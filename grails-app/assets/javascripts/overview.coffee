#
# overview.coffee
#
# Copyright (c) 2011-2016, Daniel Ellermann
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
#= require application
#= require _handlebars-ext
#= require templates/overview/available-panels
#= require templates/overview/panel


$ = jQuery


#== Widgets =====================================

class OverviewPanels

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Class fields ------------------------------

  @DEFAULTS =
    addPanelBtn: '.add-panel-btn'
    availablePanels: '.available-panels'


  #-- Constructor -------------------------------

  # Creates a new overview page panel view using the given element and
  # options.
  #
  # @param [Element|jQuery] element the given element
  # @param [Object] options         any options
  #
  constructor: (element, options) ->
    $ = jq

    @$element = $el = $(element)
    @options = opts = options

    $el.on('click', '.panel-heading .up-btn', (event) =>
        @_onClickUpBtn event
      )
      .on('click', '.panel-heading .down-btn', (event) =>
        @_onClickDownBtn event
      )
      .on('click', '.panel-heading .close-btn', (event) =>
        @_onClickCloseBtn event
      )
      .find('.panel')
        .each (idx, panel) =>
          @_initPanel $(panel)

    @_updateNoPanelsMessage()

    @$addPanelBtn = $(opts.addPanelBtn).on 'click', =>
      @_toggleAvailablePanels()
    @$availablePanels = availablePanels = $(opts.availablePanels)
    url = availablePanels.data 'load-available-panels-url'
    $.getJSON url, (data) => @_onLoadedPanels data

    @_initChangelogModal()


  #-- Non-public methods ------------------------

  # Enables or disables the button to add panels depending on the number of
  # available panels.
  #
  # @return   `true` if the button has been enabled; `false` otherwise
  # @private
  #
  _enableDisableAddPanelBtn: ->
    if @_getNumOfAvailablePanels()
      @$addPanelBtn.removeAttr 'disabled'
      true
    else
      @$addPanelBtn.attr 'disabled', 'disabled'
      false

  # Gets the number of available panels.
  #
  # @return the number of available panels
  # @private
  #
  _getNumOfAvailablePanels: ->
    @$availablePanels.find('.panel').length

  # Initializes the modal containing the changelog.
  #
  # @private
  # @since 2.0
  #
  _initChangelogModal: ->
    $ = jq

    $('#changelog-modal')
      .find('.modal-body a')
        .each( -> $(this).attr 'target', '_blank')
      .end()
      .modal()
      .on('hide.bs.modal', (event) ->
        $modal = $(event.currentTarget)
        if $modal.find('#dont-show-again:checked').length
          $.get $modal.data('url')

        return
      )

    return

  # Initializes the given overview page panel.
  #
  # @param [jQuery] $panel  the given panel
  # @private
  #
  _initPanel: ($panel) ->
    url = $panel.data('panel-url')
    if url
      $.ajax
        dataType: 'html'
        success: (html) ->
          $panel.find('> .panel-heading').after html
        url: url

    null

  # Called if a panel should be added to the overview page.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickAddPanel: (event) ->
    options = @options
    $panel = $(event.currentTarget).closest '.panel'
    id = $panel.data 'panel-id'

    html = Handlebars.templates['overview/panel']
      id: id
      panel: @availablePanels[id]
      closeUrl: options.closePanelUrl
    $newPanel = $(html)
    @$element.append $newPanel
    @_initPanel $newPanel

    $panel.parent()
      .remove()
    @_toggleAvailablePanels() unless @_enableDisableAddPanelBtn()
    @_updateNoPanelsMessage()

    $.getJSON @options.addPanelUrl,
      panelId: id
      pos: $newPanel.index()

    false

  # Called if the user toggles the button to add a panel.  The method loads
  # the list of available panels, if necessary and toggles the available panel
  # list area.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickAddPanelBtn: (event) ->
    @_toggleAvailablePanels() if @availablePanels

    false

  # Called if the user clicks the button to close a panel.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickCloseBtn: (event) ->
    that = this
    $target = $(event.currentTarget)
    url = $target.attr 'href'
    $target.closest('.panel')
      .slideUp 400, ->
        $this = $(this)
        panelId = $this.attr 'id'
        $this.remove()
        $.getJSON url, panelId: panelId
        that._refreshPanelList()

    @_updateNoPanelsMessage()

    false

  # Called if the user clicks the button to move a panel downwards.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickDownBtn: (event) ->
    $panel = $(event.currentTarget).closest '.panel'
    $targetPanel = $panel.next()
    $panel.insertAfter $targetPanel if $targetPanel.length
    @_submitPanelPos $panel, $targetPanel

    false

  # Called if the user clicks the button to move a panel upwards.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickUpBtn: (event) ->
    $panel = $(event.currentTarget).closest '.panel'
    $targetPanel = $panel.prev()
    $panel.insertBefore $targetPanel if $targetPanel.length
    @_submitPanelPos $panel, $targetPanel

    false

  # Called if the list of available panels has been loaded from the server.
  #
  # @param [Array] data the available panels
  # @private
  #
  _onLoadedPanels: (data) ->
    @availablePanels = data
    @$availablePanels.on 'click', '.add-btn', (event) =>
      @_onClickAddPanel event
    @_refreshPanelList()

  # Refreshes the list of available panels.
  #
  # @private
  #
  _refreshPanelList: ->
    panels = $.extend {}, @availablePanels
    @$element.find('.panel').each -> delete panels[$(this).attr('id')]

    html = Handlebars.templates['overview/available-panels']
      panels: panels
    @$availablePanels.html html
    @_enableDisableAddPanelBtn()

  # Submits the current positions of both the given panels.
  #
  # @param [jQuery] $panel        the one panel
  # @param [jQuery] $targetPanel  the other panel
  # @private
  #
  _submitPanelPos: ($panel, $targetPanel) ->
    url = @options.movePanelUrl
    if url
      $.ajax
        data:
          panelId1: $panel.attr('id')
          pos1: $panel.index()
          panelId2: $targetPanel.attr('id')
          pos2: $targetPanel.index()
        url: url

    null

  # Opens or closes the list of available panels.
  #
  # @private
  #
  _toggleAvailablePanels: ->
    numPanels = @_getNumOfAvailablePanels()
    $panelList = @$availablePanels
    $panelList.slideToggle() if numPanels > 0 or $panelList.is ':visible'

    return

  # Shows or hides the message that no panels are available.
  #
  # @private
  # @since 2.0
  #
  _updateNoPanelsMessage: ->
    $('.empty-list').toggleClass 'hidden', @$element.find('.panel').length > 0

    return


Plugin = (option) ->
  @each ->
    $this = $(this)
    data = $this.data 'springcrm.overviewpanels'
    options = $.extend {}, OverviewPanels.DEFAULTS, $this.data(),
      typeof option is 'object' and option

    unless data
      $this.data 'springcrm.overviewpanels',
        (data = new OverviewPanels(this, options))
    data[option]() if typeof option is 'string'

old = $.fn.overviewpanels

$.fn.overviewpanels = Plugin
$.fn.overviewpanels.Constructor = OverviewPanels

$.fn.overviewpanels.noConflict = ->
  $.fn.overviewpanels = old
  this


#== Main ========================================

$('.overview-panels').overviewpanels()
