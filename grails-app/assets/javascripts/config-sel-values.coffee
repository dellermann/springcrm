#
# config-sel-values.coffee
#
# Copyright (c) 2011-2015, Daniel Ellermann
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
#= require jquery/json
#= require jqueryui/core
#= require jqueryui/widget
#= require jqueryui/mouse
#= require jqueryui/sortable
#= require jqueryui/touch-punch
#= require _handlebars-ext
#= require templates/config/sel-value-input
#= require templates/config/sel-values


$ = jQuery


#== Classes =====================================

# Renders a widget to manage a particular list of select values.
#
# @author   Daniel Ellermann
# @version  2.0
# @since    1.4
#
class ConfigSelValuesWidget

  #-- Internal variables ------------------------

  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new widget to handle selection values.
  #
  # @param [jQuery] $elem the element containing the selection values
  #
  constructor: ($elem) ->
    @itemsToRemove = []
    @dirty = false

    @$element = $elem
      .on('click', '.value', (event) => @_onClickValue event)
      .on('click', '.accept-btn', (event) => @_onClickAcceptBtn event)
      .on('click', '.cancel-btn', (event) => @_onClickCancelBtn event)
      .on('click', '.delete-btn', (event) => @_onClickDeleteBtn event)
      .on('click', '.add-btn', => @_onClickAddBtn())
      .on('click', '.restore-btn', => @_onClickRestoreBtn())
      .on('click', '.sort-btn', => @_onClickSortBtn())
      .on('keypress', 'input', (event) => @_onKeyPressItem event)

    @restore()


  #-- Public methods ----------------------------

  # Populates the hidden input control with the values of this list converted
  # to JSON if the list is marked as dirty.
  #
  prepareSubmit: ->
    $elem = @$element

    if @dirty
      data = @_gatherData $elem.find 'ul'
      $elem.find('input:hidden')
        .val $.toJSON data
      @dirty = false

    return

  # Restores the whole list by loading the previous select values from server.
  #
  restore: ->
    $.ajax(dataType: 'json', url: @$element.data('load-url'))
      .then (data) => @_onLoaded data

    return


  #-- Non-public methods ------------------------

  # Finishs editing the input in the given item.  The method hides the input
  # control and shows the value as well as the action buttons.
  #
  # @param [jQuery] $li the item that input control should be affected
  # @param [String] val the value to be set; if `undefined` the old value remains unchanged
  # @return [jQuery]    the item
  # @private
  #
  _finishInput: ($li, val) ->
    $li
      .find('.input-group')
        .remove()
      .end()
      .find('.value')
        .show()
        .text(if val then val else `undefined`)
      .end()
      .find('.action-buttons')
        .show()
      .end()

  # Gathers all data of the given list.
  #
  # @param [jQuery] $ul the given list
  # @return [Array]     the gathered data; one item for each entry
  # @private
  # @since 2.0
  #
  _gatherData: ($ul) ->
    $ = jq
    data = []

    $ul.children('li')
      .each ->
        $this = $(this)
        item = id: parseInt $this.data('item-id'), 10
        unless $this.data 'item-disabled'
          item.name = $.trim $this.find('.value').text()
        data.push item

    data.push id: id, remove: true for id in @itemsToRemove

    data

  # Called when the button to accept the input has been clicked.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickAcceptBtn: (event) ->
    $ = jq
    $li = $(event.currentTarget).closest 'li'
    $input = $li.find 'input'
    val = $.trim $input.val()

    # look for other occurrences of this value
    $li.siblings()
      .each ->
        if $.trim($(this).find('.value').text()) is val
          val = ''        # causes item to be deleted
          return false

        true

    if val is ''
      $li.remove()
    else
      @_finishInput $li, val
    @dirty = true

    return

  # Called when the add button has been clicked.
  #
  # @private
  #
  _onClickAddBtn: ->
    html = @_renderTemplate items: [id: -1]
    $li = $(html).find 'li'

    @$element.find('ul')
      .append $li
    @_showInputControl $li.find '.value'

    return

  # Called when the button to cancel the input has been clicked.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickCancelBtn: (event) ->
    $li = $(event.currentTarget).closest 'li'
    unless $.trim $li.find('.value').text()
      $li.remove()    # should only happen after add and succeeding cancel
      return

    @_finishInput $li

    return

  # Called when the delete button of an item has been clicked.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickDeleteBtn: (event) ->
    $li = $(event.currentTarget).closest 'li'
    id = $li.data 'item-id'
    @itemsToRemove.push parseInt(id, 10) if id and (id isnt '-1')

    $li.remove()
    @dirty = true

    return

  # Called when the restore button has been clicked.
  #
  # @private
  #
  _onClickRestoreBtn: -> @restore()

  # Called when the sort button has been clicked.
  #
  # @private
  #
  _onClickSortBtn: ->
    $ = jq

    @$element
      .find('li')
        .sortElements (li1, li2) ->
          $(li1).text().compareIgnoreCase $(li2).text()
    @dirty = true

    return

  # Called when the value of an item has been clicked in order to edit it.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickValue: (event) ->
    $value = $(event.currentTarget)

    @_showInputControl $value unless $value.closest('li').data 'item-disabled'

    return

  # Called if a key in the input control has been pressed.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     whether or not the event should bubble
  # @private
  #
  _onKeyPressItem: (event) ->
    $input = $(event.currentTarget)
    $inputGroup = $input.closest '.input-group'

    switch event.keyCode
      when 13 # Enter/Return
        $inputGroup.find('.accept-btn')
          .trigger 'click'
        return false
      when 27 # Esc
        $inputGroup.find('.cancel-btn')
          .trigger 'click'
        return false

    true

  # Called when the select values have been loaded from server.
  #
  # @param [Array] data any data loaded from server
  # @private
  #
  _onLoaded: (data) ->
    html = @_renderTemplate items: data

    @$element
      .find('.sel-values-list')
        .html(html)
        .find('ul')
          .sortable
            change: (event) => @dirty = true
            forcePlaceholderSize: true
            handle: '.move-btn'
    @dirty = false

    return

  # Renders the Handlebars template with the given data.
  #
  # @param [Object] data  the given data
  # @return [String]      the HTML code of the rendered template
  # @private
  #
  _renderTemplate: (data) ->
    ConfigSelValuesWidget.selValuesTemplate data

  # Displays an input control to edit the value represented by the given
  # `<span>` element.
  #
  # @param [jQuery] $elem the given `<span>` element representing the value
  # @return [jQuery]      the given `<span>` element
  # @private
  #
  _showInputControl: ($elem) ->
    html = ConfigSelValuesWidget.selValueInputTemplate
      value: $elem.text()

    $elem
      .hide()
      .after(html)
      .nextAll('.action-buttons')
        .hide()
      .end()
      .closest('li')
        .find('.form-control')
          .focus()
        .end()
      .end()

ConfigSelValuesWidget.selValueInputTemplate = Handlebars.templates['config/sel-value-input']
ConfigSelValuesWidget.selValuesTemplate = Handlebars.templates['config/sel-values']


#== Main ========================================

widgets = []
$('.sel-values-list-container').each ->
  widgets.push new ConfigSelValuesWidget $(this)
$('#config-form').on 'submit', ->
  widget.prepareSubmit() for widget in widgets

  true
