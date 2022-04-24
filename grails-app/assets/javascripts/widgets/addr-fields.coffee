#
# addr-fields.coffee
#
# Copyright (c) 2011-2022, Daniel Ellermann
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
#= require jquery/jquery
#= require _core


$ = jQuery


#== Widgets =====================================

# Class `AddrFields` represents a container with two address areas side by side
# that allow some additional operations such as clearing the input fields,
# copying to each other or loading an address from the associated organization.
#
# @author   Daniel Ellermann
# @version  2.1
#
class AddrFields

  #-- Class variables ---------------------------

  @DEFAULTS =
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
      .on('click', '.option-menu-item-loadFromOrganization', (event) =>
        @_onClickLoadFromOrganization event
      )


  #-- Public methods ----------------------------

  # Loads the address with the given prefix of an organization into the fields
  # of the given side.
  #
  # @param [String] side          the side where to fill in the address; either `both`, `left` or `right`
  # @param [Array, String] prefix the prefix of the address of the organization; if side is `both` an array consisting of two prefixes must be specified
  # @return [Promise]             a deferred object representing the state of the operation
  #
  loadFromOrganization: (side = 'both',
                         prefix = ['billingAddr', 'shippingAddr']) ->
    $ = jQuery
    $el = @$element

    addDone = (promise, sd, idx) ->
      return promise unless side is 'both' or side is sd

      pr = prefix
      p = if $.isArray pr then pr[idx] else pr
      promise.then (data) ->
        $.Deferred (d) =>
          @_fillAddress($el.find(".address-#{sd}"), data, p)
            .always(-> d.resolveWith this, [data])

    promise = @_loadOrganization()
    promise = addDone promise, 'left', 0
    addDone promise, 'right', 1

    promise


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
      .data prefix: item.prefix
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

  # Fills the given address data obtained from server into the fields with the
  # given prefix.
  #
  # @param [jQuery] $toAddr the address that fields should be filled
  # @param [Object] data    the given address data
  # @param [String] prefix  the prefix of the organization address fields that should be used
  # @return [Promise]       a promise representing the state of the process
  # @private
  #
  _fillAddress: ($toAddr, data, prefix) ->
    toPrefix = $toAddr.data 'prefix'
    promise = $.Deferred().resolveWith(this, [data]).promise()

    if @_doesExist $toAddr
      msg = $L("default.copyAddressWarning.#{toPrefix}")
      promise = promise.then (data) ->
        $.confirm msg,
          arguments: [data]
          context: this

    promise.done (data) ->
      return unless data

      $to = $toAddr
      prfx = toPrefix
      data = data[prefix]
      return unless data

      for own key, value of data
        $to.find(".column-content :input[name='#{prfx}.#{key}']").val value

  # Loads data of an organization.  The method returns a `Promise` object in
  # state resolved when a URL and ID is available and loading an organization
  # using this data succeeded.  Otherwise the promise is in state rejected.
  #
  # @return [Promise] a promise to execute asynchronously depending on state
  # @private
  #
  _loadOrganization: ->
    $ = jQuery

    options = @options
    url = options.loadOrganizationUrl
    organizationId = options.organizationId

    deferred = $.Deferred()
    if url and organizationId
      id = null
      if $.isFunction organizationId
        id = organizationId.call this
      else if $.type(organizationId) is 'string'
        id = $(organizationId.replace('.', '\\.')).val()
      else if $.type(organizationId) is 'number'
        id = organizationId

      promise = deferred.then ->
        unless id is null
          $.ajax
            context: this
            data:
              id: id
            dataType: 'json'
            url: url
      deferred.resolveWith this
    else
      promise = deferred.promise()
      deferred.rejectWith this

    promise

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
    toPrefix = $toAddr.data 'prefix'

    promise = $.Deferred().resolve().promise()
    if @_doesExist $toAddr
      msg = $L("default.copyAddressWarning.#{toPrefix}")
      promise = promise.then -> $.confirm msg

    promise.done ->
      $fromAddr.find('.column-content :input')
        .each ->
          $this = $(this)
          name = $this.attr('name')
          if name
            name = toPrefix + '.' + name.split('.').pop()
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

    @_loadOrganization().done (data) ->
      $t = $(event.currentTarget)
      @_fillAddress $t.closest('.address'), data, $t.data('prefix')

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
  args = $.makeArray arguments

  @each ->
    $this = $(this)
    data = $this.data 'springcrm.addrfields'
    options = $.extend {}, AddrFields.DEFAULTS, $this.data(),
      typeof option is 'object' and option

    unless data
      $this.data 'springcrm.addrfields', (data = new AddrFields(this, options))
    if typeof option is 'string' and typeof data[option] is 'function'
      args.shift()
      data[option].apply data, args

old = $.fn.addrfields

$.fn.addrfields = Plugin
$.fn.addrfields.Constructor = AddrFields

$.fn.addrfields.noConflict = ->
  $.fn.addrfields = old
  this
