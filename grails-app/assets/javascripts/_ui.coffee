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
#= require bootstrap/modal
#= require _handlebars-ext
#= require templates/tools/js-calc
#= require _js-calc


$ = jQuery


#== Classes =====================================

# Defines jQuery extensions which are used in the user interface (UI).
#
# @mixin
# @author   Daniel Ellermann
# @version  2.0
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
  # @param [boolean] disable  if `true` the elements are disabled; otherwise they are enabled.  This parameter is used to change the disable state by a boolean value.
  # @return [jQuery]          this jQuery object
  #
  disable: (disable = true) ->
    $ = jQuery

    if disable
      @each -> $(this).attr 'disabled', 'disabled'
    else
      @enable()

  # Enables the elements in the jQuery object.
  #
  # @param [boolean] enable if `true` the elements are enabled; otherwise they are disabled.  This parameter is used to change the enable state by a boolean value.
  # @return [jQuery]        this jQuery object
  #
  enable: (enable = true) ->
    $ = jQuery

    if enable
      @each -> $(this).removeAttr 'disabled'
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
      nextSibling = parentNode.insertBefore(
        document.createTextNode(''), sortElement.nextSibling
      )

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
  # @param [Boolean, String, jQuery] enable either a boolean value indicating whether or not to enable the elements, a string representing a selector of a checkbox, or a jQuery object representing a checkbox which checked state is obtained
  # @param [Boolean] [invert]               if `true` the enabled stated is inverted
  # @return [jQuery]                        this jQuery object
  #
  toggleEnable: (enable, invert = false) ->
    b = if $.type(enable) is 'boolean' then enable else $(enable).is ':checked'
    b = not b if invert
    (if b then @enable() else @disable())

$.fn.extend JQueryUiExt


# Class `Page` handles default components of pages in this application.
#
# @author   Daniel Ellermann
# @version  2.0
#
class Page

  #-- Constructor -------------------------------

  # Initializes a page in this application.
  #
  constructor: ->
    $ = jQuery
    win = window
    $spinner = $('#spinner')

    $(win).on('load', (event) => @_onLoadWindow event)
    $(win.document).on(
        'click', '.markdown-help-btn', => @_onClickMarkdownHelpBtn()
      )
      .on('click', '#spinner', -> $(this).fadeOut())
      .ajaxSend( -> $spinner.fadeIn())
      .ajaxComplete( -> $spinner.fadeOut())

    $('textarea').autosize()
      .each ->
        $(this).wrap("""<div class="textarea-container"/>""")
          .after("""<i class="fa fa-question-circle markdown-help-btn"></i>""")

    $('.data-form').on('change', '.auto-number input:checkbox', (event) =>
        @_onChangeAutoNumberCheckbox event
      )
    $('.auto-number').trigger 'change'


  #-- Non-public methods ------------------------

  # Initializes the title/toolbar that it may be treated as fixed when
  # scrolling.
  #
  # @private
  #
  _initToolbar: ->
    @$titleToolbar = $titleToolbar = $('.title-toolbar')
    @yToolbar = $titleToolbar.offset().top
    $titleToolbar.parent().height $titleToolbar.innerHeight()

  # Called if the state of the checkbox at auto number fields has been changed.
  # The method toggles the disabled state of the auto number input field.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangeAutoNumberCheckbox: (event) ->
    $target = $(event.currentTarget)
    $target.closest('.control-container')
      .find('.input-group input')
        .toggleEnable $target, true

  # Called if the user clicks on the icon to display the Markdown help.
  #
  # @private
  #
  _onClickMarkdownHelpBtn: ->
    $ = jQuery

    $markdownHelp = $('#markdown-help')
    if $markdownHelp.length
      $markdownHelp.modal 'show'
    else
      html = '''\
<div id="markdown-help" class="modal fade" tabindex="-1" role="dialog"
  aria-hidden="true"/>
'''
      $(html).appendTo('body')
        .load $('html').data('load-markdown-help-url'), ->
          $(this).modal()

    return

  # Called if the window has been finished loading and rendering.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onLoadWindow: (event) ->
    @_initToolbar()
    $(event.target).on 'scroll', (event) => @_onScrollWindow event

  # Called if the window is scrolling.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onScrollWindow: (event) ->
    $target = $(event.target)
    @$titleToolbar.toggleClass 'fixed', $target.scrollTop() >= @yToolbar


#== Main ========================================

new Page()




#============== TODO ============================


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

#$.widget 'springcrm.autocompleteex', $.ui.autocomplete, AutoCompleteExWidget


SPRINGCRM.page = (->
  $ = jQuery
  win = window
  doc = win.document
  $document = $(doc)

  $spinner = $('#spinner')

  # Initializes the page and their elements.
  #
  init = ->
    $ = jQuery

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
      .on('click', '#print-btn', -> win.print())
      .on('change', '.date-input-date, .date-input-time', onChangeDateInput)

    $('.delete-btn').deleteConfirm()
#    $('.date-input-date').datepicker
#        changeMonth: true
#        changeYear: true
#        gotoCurrent: true
#        selectOtherMonths: true
#        showButtonPanel: true
#        showOtherMonths: true
#    $('.date-input-time').autocomplete
#        select: onSelectTimeValue
#        source: timeValues

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

# vim:set ts=2 sw=2 sts=2:

