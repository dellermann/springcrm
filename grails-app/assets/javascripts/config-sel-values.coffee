#
# config-sel-values.coffee
#
# Copyright (c) 2011-2014, Daniel Ellermann
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
#= require _jquery-json
#= require _handlebars-ext
#= require templates/config/sel-values


$ = jQuery


#-- Classes -------------------------------------

# Renders a widget to manage a particular list of select values.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.4
#
ConfigSelValuesWidget =

  # The items which are marked for deletion.
  #
  _itemsToRemove: []

  # Called if this widget should be initialized.
  #
  _create: ->
    @element.on('click', '.edit-btn', (event) => @_onClickEditBtn event)
      .on('click', '.delete-btn', (event) => @_onClickDeleteBtn event)
      .on('click', '.add-btn', => @_onClickAddBtn())
      .on('click', '.restore-btn', => @restore())
      .on('click', '.sort-btn', => @_onClickSortBtn())
      .on('dblclick', 'li', (event) => @_onDblClickItemList event)
      .on('blur', 'input', (event, cancel) => @_onBlurItemInput event, cancel)
      .on('keypress', 'input', (event) => @_onKeyPressItem event)
    @restore()

  # Called if the input control to edit the value has lost its focus.
  #
  # @param {Object} event   the event data
  # @param {Boolean} cancel if `true` editing the value has been cancelled explicitely; `false` otherwise
  # @return {Boolean}       `false` to prevent event bubbling
  #
  _onBlurItemInput: (event, cancel) ->
    return false if @removingRow

    $input = $(event.currentTarget)
    val = $input.val()
    $li = $input.parents 'li'

    # look for other occurrences of this value
    unless cancel
      $li.siblings().each ->
        if $(this).find('.value').text() is val
          cancel = true
          val = ''
          return false
        true

    if val is ''

      # XXX the blur event is triggered again when removing the <li> so we
      # need to set a variable which prevents recursive handling
      @removingRow = true
      $li.remove()
      @removingRow = false
    else
      $span = $li.find('.value').show()
      $span.text val unless cancel
      $li.find('i').fadeIn()
      $input.parent().remove()
    false

  # Called if the add button has been clicked.
  #
  # @return {Boolean} `false` to prevent event bubbling
  #
  _onClickAddBtn: ->
    template = @template
    s = template items: [id: -1]
    $li = $(s).find 'li'

    @element.find('ul')
      .data('dirty', true)
      .append $li
    @_showEditField $li.find('.value')
    false

  # Called if the delete button of an item has been clicked.
  #
  # @param {Object} event the event data
  # @return {Boolean}     `false` to prevent event bubbling
  #
  _onClickDeleteBtn: (event) ->
    $li = $(event.currentTarget).parents('li')
    id = $li.data('item-id')
    @_itemsToRemove.push parseInt(id, 10) if id and (id isnt '-1')
    $li.parent()
        .data('dirty', true)
      .end()
      .remove()
    false

  # Called if the edit button of an item has been clicked.
  #
  # @param {Object} event the event data
  # @return {Boolean}     `false` to prevent event bubbling
  #
  _onClickEditBtn: (event) ->
    @_showEditField($(event.currentTarget).prevAll('.value'))
      .parents('ul')
        .data('dirty', true)
    false

  # Called if the sort button has been clicked.
  #
  # @return {Boolean} `false` to prevent event bubbling
  #
  _onClickSortBtn: ->
    $ = jQuery
    @element.find('li')
      .sortElements (li1, li2) -> $(li1).text().compare $(li2).text()
    false

  # Called if an item has been double clicked.
  #
  # @param {Object} event the event data
  #
  _onDblClickItemList: (event) ->
    $li = $(event.currentTarget)
    if $li.find('input').length is 0 and not $li.data('item-disabled')
      @_showEditField($li.find('.value'))
        .parents('ul')
          .data('dirty', true)

  # Called if a key in the input control has been pressed.
  #
  # @param {Object} event the event data
  # @return {Boolean}     whether or not the event should bubble
  #
  _onKeyPressItem: (event) ->
    $input = $(event.currentTarget)
    switch event.keyCode
      when 13 # Enter/Return
        $input.trigger 'blur'
        return false
      when 27 # Esc
        $input.trigger 'blur', [true]
        return false
    true

  # Called if the select values have been loaded from server.
  #
  # @param {Array} data the loaded data
  #
  _onLoaded: (data) ->
    s = @_renderTemplate items: data

    @element.html(s)
      .find('ul')
        .data('dirty', false)
        .sortable
          change: (event) => $(event.currentTarget).data 'dirty', true
          forcePlaceholderSize: true
          placeholder: 'ui-state-highlight'

  # Creates a hidden input control and stores the values of this list as JSON.
  # The method only works if the list is marked as dirty.
  #
  prepareSubmit: ->
    $ = jQuery

    el = @element
    $ul = el.find('ul')
    if $ul.data 'dirty'
      data = []
      $ul.children('li')
        .each ->
          $this = $(this)
          item = id: parseInt $this.data('item-id'), 10
          unless $this.data 'item-disabled'
            item.name = $this.find('.value').text()
          data.push item

      data.push id: id, remove: true for id in @_itemsToRemove

      $("""<input type="hidden"/>""")
        .attr('name', "selValues.#{el.data "list-type"}")
        .val($.toJSON data)
        .appendTo el
      $ul.data 'dirty', false

  # Renders the Handlebars template with the given data.
  #
  # @param [Object] data    the given data
  # @return [String]        the HTML code of the rendered template
  # @private
  #
  _renderTemplate: (data) ->
    Handlebars.templates['config/sel-values'] data

  # Restores the whole list by loading the previous select values from server.
  #
  restore: ->
    $.getJSON @element.data('load-url'), (data) =>
      @_onLoaded data

  # Displays an input control to edit the value represented by the given
  # `<span>` element.
  #
  # @param {jQuery} $elem the given `<span>` element representing the value
  # @return {jQuery}      the given `<span>` element
  #
  _showEditField: ($elem) ->
    $ = jQuery

    $span = $("""<span class="input"/>""").insertAfter $elem
    $('<input/>',
        type: 'text'
        value: $elem.text()
      )
      .appendTo($span)
      .focus()
    $elem.hide()
      .nextAll('i')
        .fadeOut()
      .end()

$.widget 'springcrm.configSelValues', ConfigSelValuesWidget


#-- Main ----------------------------------------

$lists = $('.sel-values-list')
$lists.configSelValues()
  .parents('form')
    .on 'submit', ->
      $lists.each -> $(this).configSelValues 'prepareSubmit'
      true

