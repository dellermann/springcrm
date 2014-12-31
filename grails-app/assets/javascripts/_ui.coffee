#
# _ui.coffee
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
#= require _jquery-autosize
#= require _core
#= require bootstrap/transition
#= require bootstrap/alert
#= require bootstrap/collapse
#= require bootstrap/dropdown
#= require _handlebars-ext
#= require templates/tools/js-calc
#= require _js-calc


$ = jQuery


# This mixin contains various static extensions for jQuery UI.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.4
#
JQueryUiStaticExt =

  # Displays an alert message to the user.  The method is used to  abstract the
  # access to the actual alert dialog.
  #
  # @param [String] msg the message (prompt) to be displayed
  # @return [jQuery]    this jQuery object
  #
  alert: (msg) ->
    window.alert msg
    this

  # Displays a confirmation message to the user.  The method is used to
  # abstract the access to the actual confirmation dialog.
  #
  # @param [String] msg the message (prompt) to be displayed
  # @return [Boolean]   `true` if the user has confirmed; `false` otherwise
  #
  confirm: (msg) ->
    window.confirm msg

$.extend JQueryUiStaticExt


# Defines jQuery extensions which are used in the user interface (UI).
#
# @mixin
# @author   Daniel Ellermann
# @version  1.3
#
JQueryUiExt =

  # Registers a click handler for each element in this jQuery object which
  # displays a deletion confirmation dialog.  If the user confirms the link is
  # rewritten and followed to allow deletion on the server.
  #
  deleteConfirm: ->
    @filter('[href]')
      .on('click', ->
        res = $.confirm $L('default.delete.confirm.msg')
        if res
          $this = $(this)
          url = $this.attr('href')
          url += (if url.indexOf('?') < 0 then '?' else '&') + 'confirmed=1'
          $this.attr 'href', url
        res
      )
    .end()

  # Disables the elements in the jQuery object.
  #
  # @param [boolean] disable  if true the elements are disabled; otherwise they are enabled.  This parameter is used to change the disable state by a boolean value.
  # @return [jQuery]          this jQuery object
  #
  disable: (disable = true) ->
    $ = jQuery

    if disable
      @each ->
        $(this).attr('disabled', 'disabled')
          .addClass 'disabled'
    else
      @enable()

  # Enables the elements in the jQuery object.
  #
  # @param [boolean] enable if true the elements are enabled; otherwise they are disabled.  This parameter is used to change the enable state by a boolean value.
  # @return [jQuery]        this jQuery object
  #
  enable: (enable = true) ->
    $ = jQuery

    if enable
      @each ->
        $(this).removeAttr('disabled')
          .removeClass 'disabled'
    else
      @disable()

  # Reverses the elements in the jQuery object.
  #
  # @return [jQuery]  this jQuery object with reversed content
  # @since            1.3
  #
  reverse: [].reverse

  # Sorts the jQuery elements or the elements which are returned by the given
  # `getSortable` function.
  #
  # @param [Function] comparator  the comparator function; the function gets the values to compare as two parameters and must return a number less than zero if the first value is less than the second, zero if both the values are equal, and a number greater than zero otherwise
  # @param [Function] getSortable a function which is called to obtain the element to be sorted; if not specified the identity function is used
  # @return [Array]               the sorted array
  #
  sortElements: (comparator, getSortable) ->
    getSortable = getSortable or ->
      this

    document = window.document
    placements = @map( ->
      sortElement = getSortable.call(this)
      parentNode = sortElement.parentNode

      # Since the element itself will change position, we have to have some way
      # of storing its original position in the DOM. The easiest way is to have
      # a 'flag' node:
      nextSibling = parentNode.insertBefore(document.createTextNode(''), sortElement.nextSibling)

      ->
        if parentNode is this
          throw new Error(
            'You can\'t sort elements if any one is a descendant of another.'
          )

        # Insert before flag:
        parentNode.insertBefore this, nextSibling

        # Remove flag:
        parentNode.removeChild nextSibling
    )

    [].sort.call(this, comparator)
      .each (i) ->
        placements[i].call getSortable.call(this)

  # Enables or disables the elements in the jQuery object depending on the
  # given state.
  #
  # @param [Boolean|String|jQuery] enable either a boolean value indicating whether or not to enable the elements, a string representing a selector of a check box, or a jQuery object representing a check box which checked state is obtained
  # @return [jQuery]                      this jQuery object
  #
  toggleEnable: (enable) ->
    b = if $.type(enable) is 'boolean' then enable else $(enable).is(':checked')
    (if b then @enable() else @disable())

$.fn.extend JQueryUiExt


# Renders an autocomplete input field with extended functionality.  The
# `autocompleteex` widget forces the user to select a value.  If not, the input
# field is reset to its old content.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.3
#
AutoCompleteExWidget =
  options:
    combobox: true
    labelProp: 'name'
    loadParameters: {}
    lookupUrl: null
    noSelectValue: 'null'
    url: null
    valueInput: null
    valueProp: 'id'

  # Initializes this widget.
  #
  _create: ->
    $ = jQuery

    parentOpts = {}
    @_prepareOptions()
    opts = @options
    $.extend parentOpts, opts

    el = @element
    el.autocomplete(parentOpts)
      .wrap("<span class='#{@widgetFullName}-combobox'/>")
      .focus(=> @_onFocus())
      .blur => @_onBlur()

    @valueInput = @_getValueInput()
    if opts.combobox
      el.after $('<a/>',
          click: (event) => @_onClickComboboxBtn event
          html: '<i class="fa fa-caret-down"></i>'
        )

  _getValueInput: ->
    $ = jQuery

    valueInput = null
    v = @options.valueInput
    if v
      switch $.type(v)
        when 'string' then valueInput = $(v)
        when 'function' then valueInput = v.call(this)
    else
      el = @element
      name = el.attr('name')
      if name
        valueInput = $(":input[name=#{name}.id]")
      else
        name = el.attr('id')
        if name
          valueInput = $("##{name}\\.id")
          valueInput = $("##{name}-id") unless valueInput.length
    valueInput

  _load: (request, response) ->
    opts = @options
    url = opts.url
    if url
      $ = jQuery

      p = opts.loadParameters
      params = {}
      params = (if $.isFunction(p) then p.call(this) else p) if p
      params.name = request.term

      $.getJSON url, params, (data) =>
        opts = @options
        labelProp = opts.labelProp
        valueProp = opts.valueProp
        response $.map(data, (item) ->
          label: item[labelProp]
          value: item[valueProp]
        )

  _onBlur: ->
    el = @element
    val = el.val()
    valueInput = @valueInput
    if val is ''
      valueInput.val @options.noSelectValue
    else
      valueInput.val @oldValue
      el.val @oldLabel

  _onClickComboboxBtn: (event) ->
    el = @element
    if el.autocomplete('widget').is ':visible'
      el.autocomplete 'close'
      return

    # work around a bug (likely same cause as #5265)
    $(event.target).blur()

    # pass wildcard as value to search for, displaying all results
    el.autocomplete 'search', '%'
    el.focus()

  _onFocus: ->
    @oldValue = @valueInput.val()
    @oldLabel = @element.val()

  _onFocusItem: (event, ui) ->
    $(event.target).val ui.item.label

  _onSelect: (event, ui) ->
    item = ui.item
    s = item.label
    @oldLabel = s
    @element.val s

    s = item.value
    @oldValue = s
    @valueInput.val s

  _prepareOptions: ->
    el = @element
    self = this

    opts = @options
    unless opts.source
      url = opts.url or el.data('find-url')
      if url
        opts.url = url
        opts.source = (request, response) => @_load(request, response)

    select = opts.select
    opts.select = =>
      @_onSelect.apply this, arguments
      select.apply this, arguments if select
      false

    focus = opts.focus
    opts.focus = =>
      @_onFocusItem.apply this, arguments
      focus.apply this, arguments if focus
      false
    this

$.widget 'springcrm.autocompleteex', $.ui.autocomplete, AutoCompleteExWidget


# Handles actions in address fields which allow copying data from left to right
# side and vice versa.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.3
#
AddrFieldsWidget =
  ADDRESS_FIELDS: [
    'street', 'poBox', 'postalCode', 'location', 'state', 'country'
  ]

  options:
    confirm: (msg) ->
      $.confirm msg

    leftMenuSelector: '.left-address .dropdown-menu'
    leftPrefix: 'billingAddr'
    loadOrganizationUrl: null
    menuItems: []
    organizationId: null
    rightMenuSelector: '.right-address .dropdown-menu'
    rightPrefix: 'shippingAddr'

  # Adds a menu item to clear data of the given side.
  #
  # @param [String] side  the side where the menu item should be added; must be either `left` or `right`
  # @param [String] text  the menu item text
  # @since                1.4
  #
  addMenuItemClear: (side, text) ->
    f = (if (side is 'left') then @clearLeft else @clearRight)
    $('<span/>',
        click: => f.call this
        text: text
      )
      .wrap('<li/>')
      .appendTo @_getMenu(side)

  # Adds a menu item to copy data from the one side to the other.
  #
  # @param [String] side  the side where the menu item should be added; must be either `left` or `right`
  # @param [String] text  the menu item text
  #
  addMenuItemCopy: (side, text) ->
    f = (if (side is 'left') then @copyToLeft else @copyToRight)
    $('<span/>',
        click: => f.call this
        text: text
      )
      .wrap('<li/>')
      .appendTo @_getMenu(side)

  # Adds a menu item to load an address from organization.
  #
  # @param [String] side        the side where the menu item should be added; must be either `left` or `right`
  # @param [String] text        the menu item text
  # @param [String] propPrefix  the prefix used for the field names
  #
  addMenuItemLoadFromOrganization: (side, text, propPrefix) ->
    f = (if (side is 'left') then @loadFromOrganizationToLeft else @loadFromOrganizationToRight)
    $('<span/>',
        click: => f.call this, propPrefix
        text: text
      )
      .wrap('<li/>')
      .appendTo @_getMenu(side)

  # Clears the address data on the left side.
  #
  # @since 1.4
  #
  clearLeft: ->
    @_clear @options.leftPrefix

  # Clears the address data on the right side.
  #
  # @since 1.4
  #
  clearRight: ->
    @_clear @options.rightPrefix

  # Copies the address from the right side to the left side.
  #
  copyToLeft: ->
    opts = @options
    @_copyAddress opts.rightPrefix, opts.leftPrefix

  # Copies the address from the left side to the right side.
  #
  copyToRight: ->
    opts = @options
    @_copyAddress opts.leftPrefix, opts.rightPrefix

  # Loads the address of the organization to the left side.
  #
  # @param [String] propPrefix  the prefix used for the field names
  #
  loadFromOrganizationToLeft: (propPrefix) ->
    @_loadFromOrganization @options.leftPrefix, propPrefix

  # Loads the address of the organization to the right side.
  #
  # @param [String] propPrefix  the prefix used for the field names
  #
  loadFromOrganizationToRight: (propPrefix) ->
    @_loadFromOrganization @options.rightPrefix, propPrefix

  # Clears the address fields with the given prefix.
  #
# @param [String] prefix  the prefix of the fields that should be cleared
  #
  _clear: (prefix) ->
    addrFields = @ADDRESS_FIELDS
    gaf = @_getField
    gaf(prefix, f).val '' for f in addrFields
    this

  # Copies the address from one side to the other using the given field name
  # prefixes.
  #
  # @param [String] fromPrefix  the field name prefix of the source fields
  # @param [String] toPrefix    the field name prefix of the destination fields
  # @return [jQuery]            this object
  #
  _copyAddress: (fromPrefix, toPrefix) ->
    addrFields = @ADDRESS_FIELDS
    msg = $L("default.copyAddressWarning.#{toPrefix}")
    gaf = @_getField
    gaf(toPrefix, f).val gaf(fromPrefix, f).val() for f in addrFields if not @_doesExist(toPrefix) or @options.confirm(msg)
    this

  # Initializes this widget.
  #
  _create: ->
    el = @element
    opts = @options
    @leftMenu = el.find(opts.leftMenuSelector)
    @rightMenu = el.find(opts.rightMenuSelector)
    opts.loadOrganizationUrl = el.data('load-organization-url') unless opts.loadOrganizationUrl
    loadOrganizationUrl = opts.loadOrganizationUrl
    orgId = opts.organizationId

    menuItems = opts.menuItems
    for menuItem in menuItems
      switch menuItem.action
        when 'clear'
          @addMenuItemClear menuItem.side, menuItem.text
        when 'copy'
          @addMenuItemCopy menuItem.side, menuItem.text
        when 'loadFromOrganization'
          @addMenuItemLoadFromOrganization menuItem.side, menuItem.text, menuItem.propPrefix if loadOrganizationUrl and orgId

  # Checks whether or not the fields with the given prefix are filled.
  #
  # @param [String] prefix  the given field name prefix
  # @return [Boolean]       `true` if any field with the given prefix is filled; `false` otherwise
  #
  _doesExist: (prefix) ->
    addrFields = @ADDRESS_FIELDS
    gf = @_getField

    res = false
    res = res or gf(prefix, addrField).val() for addrField in addrFields
    res

  # Fills address data obtained from server into the fields with the given
  # prefix.
  #
  # @param [String] prefix      the given field name prefix
  # @param [String] propPrefix  the prefix of the properties in the given data object which are to set into the fields
  # @param [Object] data        the given data
  # @return [jQuery]            this object
  #
  _fillAddress: (prefix, propPrefix, data) ->
    addrFields = @ADDRESS_FIELDS

    msg = $L("default.copyAddressWarning.#{prefix}")
    gf = @_getField
    addr = data[propPrefix]
    gf(prefix, f).val addr[f] for f in addrFields if not @_doesExist(prefix) or @options.confirm(msg)
    this

  # Gets the input field which name is composed by the given prefix and name.
  #
  # @param [String] prefix  the given prefix
  # @param [String] name    the given field name
  # @return [jQuery]        the input field
  #
  _getField: (prefix, name) ->
    $ "##{prefix}\\.#{name}"

  # Gets the menu of the given side of the address fields.  If no such menu
  # exists, it is created.
  #
  # @param [String] side  the given side; must be either `left` or `right`
  # @return [jQuery]      the menu
  #
  _getMenu: (side) ->
    (if side is 'left' then @leftMenu else @rightMenu)

  # Loads address data from the organization stored on the server.
  #
  # @param [String] prefix      the given field name prefix
  # @param [String] propPrefix  the prefix of the properties in the given data object which are to set into the fields
  # @return [jQuery]            this object
  #
  _loadFromOrganization: (prefix, propPrefix) ->
    self = this

    opts = @options
    organizationId = opts.organizationId
    url = opts.loadOrganizationUrl
    if url and organizationId
      $ = jQuery
      id = null
      if $.isFunction organizationId
        id = organizationId.call(this)
      else if $.type(organizationId) is 'string'
        id = $(organizationId).val()
      else if $.type(organizationId) is 'number'
        id = organizationId

      unless id is null
        $.getJSON url, { id: id }, (data) =>
          @_fillAddress prefix, propPrefix, data
    this

$.widget 'springcrm.addrfields', AddrFieldsWidget


RemoteListWidget =
  options:
    container: '> div'
    returnUrl: null

  # Computes the URL to load the remote data either from the given URL or the
  # data attributes.  If attribute `data-load-params` is specified the
  # parameters given there are added to the URL.
  #
  # @param [String] url the given URL to use; if not specified the URL is obtained from attribute `data-load-url`
  # @return [String]    the computed URL
  #
  _computeUrl: (url) ->
    el = @element
    url = new HttpUrl(url or el.data('load-url'))
    params = el.data('load-params')
    url.overwriteQuery params if params
    url.toString()

  # Initializes this widget.
  #
  _create: ->
    url = @element.data('load-url')
    @_loadContent @_computeUrl(url) if url

  # Loads the content of the given URL.
  #
  # @param [String] url         the given URL
  # @return [RemoteListWidget]  this object
  #
  _loadContent: (url) ->
    el = @element
    opts = @options
    el.find(opts.container).load url, =>
      $ = jQuery
      element = el

      element.find('thead a, .paginator a')
        .click (event) =>
          @_loadContent @_computeUrl($(event.currentTarget).attr('href'))
          false

      returnUrl = opts.returnUrl
      if returnUrl
        element.find('tbody .button')
          .each ->
            $this = $(this)
            url = $this.attr('href')
            url += (if url.indexOf('?') < 0 then '?' else '&')
            url += "returnUrl=#{returnUrl}"
            $this.attr 'href', url

      element.find('.delete-btn').deleteConfirm()
    this

$.widget 'springcrm.remotelist', RemoteListWidget


SPRINGCRM.page = (->
  $ = jQuery
  win = window
  doc = win.document
  $document = $(doc)

  $spinner = $('#spinner')
  $toolbar = $('#toolbar-container')
  toolbarOffset = (if $toolbar.length then $toolbar.offset().top else 0)

  # Initializes the page and their elements.
  #
  init = ->
    $ = jQuery

    $document.scroll onScrollDocument if $toolbar.length
    $('#calculator-button').on 'click', ->
      $dlg = $('.calculator-dialog')
      $calculator = $dlg.find('.calculator')
        .jscalc(point: $('html').data('decimal-separator'))
      $jsCalcContainer = $calculator.find '.jscalc-calculator-container'
      $dlg.dialog(
          resizable: false
        ).dialog 'option',
          height: $jsCalcContainer.height() + 65
          width: $jsCalcContainer.width() + 30
      false

    $('#main-container').on('focusin', '.currency :input, :input.currency', ->
        $this = $(this)
        val = $this.val().parseNumber()
        $this.val (if val then val.format() else '')
      )
      .on('focusout', '.currency :input, :input.currency', ->
        $this = $(this)
        val = $this.val().parseNumber()
        $this.val if $this.is '.currency-ext' then val.formatCurrencyValueExt() else val.formatCurrencyValue()
      )
      .on('click', '.button-group .dropdown', ->
        $btnGroup = $(this).parents('.button-group')
        unless $btnGroup.is '.open'
          $('.button-group.open').removeClass 'open'
        $btnGroup.toggleClass 'open'
        $document.one 'click', ->
          $btnGroup.removeClass 'open'
          true
        false
      )
      .on('click', '.submit-btn', ->
        $ = jQuery

        $("##{$(this).data("form")}").submit()
        false
      )
      .on('click', '#print-btn', -> win.print())
      .on('change', '.date-input-date, .date-input-time', onChangeDateInput)
      .on('click', '.markdown-help-btn', ->
        $ = jQuery

        $markdownHelp = $('#markdown-help')
        if $markdownHelp.length
          $markdownHelp.dialog 'open'
        else
          $('<div id="markdown-help"/>').appendTo('body')
            .load $('html').data('load-markdown-help-url'), ->
              $(this).dialog
                title: $L('help.markdown.title')
                width: '35em'
        false
      )
      .on('change', '#autoNumber', ->
        $('#number').toggleEnable not @checked
      )
      .on('click', '#spinner', ->
        $(this).css 'display', 'none'
      )
      .find('#autoNumber')
        .trigger('change')

    $('.delete-btn').deleteConfirm()
    $('.date-input-date').datepicker
        changeMonth: true
        changeYear: true
        gotoCurrent: true
        selectOtherMonths: true
        showButtonPanel: true
        showOtherMonths: true
    $('.date-input-time').autocomplete
        select: onSelectTimeValue
        source: timeValues
    $('textarea').autosize()
      .each ->
        $html = $('html')
        $(this).wrap("""<div class="textarea-container"/>""")
          .after("""<i class="fa fa-question-circle markdown-help-btn"></i>""")
    initAjaxEvents()

  # Initializes the handling of AJAX requests. The method cares about display
  # of a spinner view while loading data.
  #
  initAjaxEvents = ->
    $ = jQuery

    $spinner.ajaxSend(->
        $(this).show()
      ).ajaxComplete(->
        $(this).hide()
      )

  # Called if either the date or time part of a date/time input field has
  # changed. The method computes a formatted composed value in a hidden
  # date/time field.
  #
  onChangeDateInput = ->
    if @id.match /^([\w\-.]+)-(date|time)$/
      els = @form.elements
      baseId = RegExp.$1
      partId = RegExp.$2
      otherPartField = els["#{baseId}_" + (if (partId is 'date') then 'time' else 'date')]

      type = ''
      val = ''
      if partId is 'date'
        val += @value
        type = 'date'
        if otherPartField
          val += " #{otherPartField.value}"
          type += 'time'
      else
        if otherPartField
          val += "#{otherPartField.value} "
          type = 'date'
        val += @value
        type += 'time'
      els[baseId].value = val

  # Called if an item of the quick access selector was selected. The method
  # calls the associated URL.
  #
  onChangeQuickAccess = ->
    $this = $(this)
    val = $this.val()
    $this.val ''
    win.location.href = val if val

  # Called if the document is scrolled.
  #
  onScrollDocument = ->
    if $document.scrollTop() >= toolbarOffset
      $toolbar.addClass 'fixed'
    else
      $toolbar.removeClass 'fixed'

  # Called if the user selects a time from the autocomplete list.
  #
  # @param [Object] event the event data
  # @param [Object] ui    information about the selected item
  #
  onSelectTimeValue = (event, ui) ->
    $this = $(this)
    item = ui.item
    $this.val item.value if item
    $this.trigger 'change'

  timeValues = do ->
    res = []
    for h in [0..23]
      hh = h.toString()
      hh = "0#{hh}" if hh.length < 2
      res.push "#{hh}:00"
      res.push "#{hh}:30"
    res

  init()
)()
