#
# addr-fields.coffee
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
#= require _jquery
#= require _core


$ = jQuery


#== Widgets =====================================

# Class `AddrFields` represents a container with two address areas side by side
# that allow some additional operations such as clearing the input fields,
# copying to each other or loading an address from the associated organization.
#
# @author   Daniel Ellermann
# @version  2.0
#
class AddrFields

  #-- Class variables ---------------------------

  @DEFAULTS =
    confirm: (msg) -> $.confirm msg
    loadOrganizationUrl: null
    menuItems: []
    organizationId: null


  #-- Constructor -------------------------------

  # Creates a field of two addresses within the given element.
  #
  # @param [Element, jQuery] element  the given element
  # @param [Object] options           any options
  #
  constructor: (element, options) ->
    @$element = $el = $(element)
    @options = options

    @$leftAddr = $el.find '.address-left'
    @$rightAddr = $el.find '.address-right'

    menuItems = options.menuItems
    @_renderMenuItems menuItems, 'left'
    @_renderMenuItems menuItems, 'right'

    $el.on('click', '.option-menu-item-clear', (event) =>
        @_onClickClearAddress event
      )
      .on('click', '.option-menu-item-copy', (event) =>
        @_onClickCopyAddress event
      )
      .on('click', '.option-menu-item-load', (event) =>
        @_onClickLoadFromOrganization event
      )


  #-- Non-public methods ------------------------

  # Adds an item to the option menu of the given address.
  #
  # @param [jQuery] $menu the menu the item should be added to
  # @param [Object] item  the menu item that should be added
  # @private
  # @since                2.0
  #
  _addMenuItem: ($menu, item) ->
    $ = jQuery

    if item.action is 'loadFromOrganization'
      options = @options
      return unless options.loadOrganizationUrl and options.organizationId

    $a = $('<a/>',
        class: "option-menu-item-#{item.action}"
        href: '#'
        text: item.text
      )
    $('<li/>').append($a)
      .appendTo $menu

  # Checks whether or not the fields of the given address are filled.
  #
  # @param [jQuery] $address  the address that fields should be checked
  # @return [Boolean]         `true` if any field is filled; `false` otherwise
  # @private
  #
  _doesExist: ($address) ->
    res = false

    $address.find(':input')
      .each ->
        res = $.trim($(this).val()) isnt ''
        not res

    res

  # Fills address data obtained from server into the fields with the given
  # prefix.
  #
  # @param [jQuery] $toAddr the address that fields should be filled
  # @param [Object] data    the given data
  # @private
  #
  _fillAddress: ($toAddr, data) ->
    toPrefix = $toAddr.data 'prefix'

    if (@_doesExist $toAddr)
      msg = $L("default.copyAddressWarning.#{toPrefix}")
      return unless @options.confirm msg

    for own key, value of data
      name = "#{toPrefix}.#{key}"
      $toAddr.find(".column-content :input[name='#{name}']")
        .val $this.val()

    return

  # Called if the user selects the menu item to clear the input fields of the
  # address the menu item belongs to.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickClearAddress: (event) ->
    event.preventDefault()

    $(event.currentTarget).closest('.address')
        .find('.column-content :input')
          .val('')

    return

  # Called if the user selects the menu item to copy the content of the input
  # fields of the other address to the one the menu item belongs to.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickCopyAddress: (event) ->
    event.preventDefault()

    $target = $(event.currentTarget)
    $toAddr = $target.closest '.address'
    $fromAddr = $toAddr.siblings '.address'

    toPrefix = $toAddr.data('prefix')

    if (@_doesExist $toAddr)
      msg = $L("default.copyAddressWarning.#{toPrefix}")
      return unless @options.confirm msg

    $fromAddr.find('.column-content :input')
      .each ->
        $this = $(this)
        name = toPrefix + '.' + $this.attr('name').split('.').pop()
        $toAddr.find(":input[name='#{name}']")
          .val $this.val()

    return

  # Called if the user selects the menu item to load the address of the
  # organization into the address where the menu item belongs to.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickLoadFromOrganization: (event) ->
    event.preventDefault()

    options = @options
    url = options.loadOrganizationUrl
    organizationId = options.organizationId

    if url and organizationId
      $ = jQuery

      id = null
      if $.isFunction organizationId
        id = organizationId.call this
      else if $.type(organizationId) is 'string'
        id = $(organizationId).val()
      else if $.type(organizationId) is 'number'
        id = organizationId

      unless id is null
        $toAddr = $(event.currentTarget).closest '.address'
        $.getJSON url, { id: id }, (data) => @_fillAddress $toAddr, data

    return

  # Renders the menu items of the given side.
  #
  # @param [Object] menuItems the map containing all menu items
  # @param [String] side      the given side; must be either `left` or `right`
  # @private
  #
  _renderMenuItems: (menuItems, side) ->
    $menu = this["$#{side}Addr"].find '.dropdown-menu'
    @_addMenuItem $menu, item for item in menuItems[side]
    return


Plugin = (option) ->
  @each ->
    $this = $(this)
    data = $this.data 'springcrm.addrfields'
    options = $.extend {}, AddrFields.DEFAULTS, $this.data(),
      typeof option is 'object' and option

    unless data
      $this.data 'springcrm.addrfields', (data = new AddrFields(this, options))
    data[option]() if typeof option is 'string'

old = $.fn.addrfields

$.fn.addrfields = Plugin
$.fn.addrfields.Constructor = AddrFields

$.fn.addrfields.noConflict = ->
  $.fn.addrfields = old
  this

# vim:set ts=2 sw=2 sts=2:

