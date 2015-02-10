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
#= require _bootstrap-datepicker
#= require _selectize
#= require _handlebars-ext
#= require templates/tools/js-calc
#= require _js-calc


$ = jQuery


#== Classes =====================================

# This mixin contains various static extensions for jQuery UI.
#
# @mixin
# @author   Daniel Ellermann
# @version  2.0
#
JQueryUiStaticExt =

  # Displays an alert message in a modal to the user.  The method is used to
  # abstract the access to the actual alert modal.
  #
  # @param [String] msg   the message (prompt) to be displayed
  # @param [String] title an optional title of the modal
  # @return [jQuery]      this jQuery object
  #
  alert: (msg, title) ->
    $dlg = $('#alert-modal')
    $dlg.find('.modal-title').text title if title

    btnOpts = options.okBtn
    if btnOpts
      $btn = $dlg.find '.btn-ok'
      s = btnOpts.color
      $btn.removeClass('btn-default').addClass "btn-#{s}" if s
      s = btnOpts.label
      $btn.find('span').text s if s
      s = btnOpts.icon
      $btn.find('i').attr 'class', "fa fa-#{s}" if s

    $dlg.find('.model-body p')
        .text(msg)
      .end()
      .modal()

    this

  # Displays a confirmation message to the user.  The method is used to
  # abstract the access to the actual confirmation dialog.
  #
  # The function accepts the following options:
  #
  # #### Dialog options
  #
  # * `okBtn`.  Options concerning the OK button
  #
  #   - `color`.  The color of the button.
  #
  #   - `label`.  The label of the button.
  #
  #   - `icon`.  The icon to display without the `fa-` prefix.
  #
  # * `cancelBtn`.  Options concerning the cancel button
  #
  #   - `color`.  The color of the button.
  #
  #   - `label`.  The label of the button.
  #
  #   - `icon`.  The icon to display without the `fa-` prefix.
  #
  # #### Options for deferred object
  #
  # * `context`.  The context object to set when resolving or rejecting the
  #   returned deferred object.
  # * `arguments`.  Any arguments to pass to the deferred object when
  #   resolving or rejecting the returned deferred object.
  #
  # @param [String] msg     the message (prompt) to be displayed
  # @param [String] title   an optional title of the modal
  # @param [Object] options any options which are used in the modal and the deferred object; see function description for more information
  # @return [Promise]       a deferred object which is resolved if the user confirms; otherwise it is rejected
  #
  confirm: (msg, title, options = {}) ->
    $ = jQuery
    unless $.type(title) is 'string'
      options = title
      title = null

    $.Deferred((d) ->
        opts = options

        $dlg = $('#confirm-modal')
        $dlg.find('.modal-title').text title if title

        btnOpts = opts.okBtn
        if btnOpts
          $btn = $dlg.find '.btn-ok'
          s = btnOpts.color
          $btn.removeClass('btn-success').addClass "btn-#{s}" if s
          s = btnOpts.label
          $btn.find('span').text s if s
          s = btnOpts.icon
          $btn.find('i').attr 'class', "fa fa-#{s}" if s

        btnOpts = opts.cancelBtn
        if btnOpts
          $btn = $dlg.find '.btn-cancel'
          s = btnOpts.color
          $btn.removeClass('btn-default').addClass "btn-#{s}" if s
          s = btnOpts.label
          $btn.find('span').text s if s
          s = btnOpts.icon
          $btn.find('i').attr 'class', "fa fa-#{s}" if s

        method = 'rejectWith'
        $dlg.find('.modal-body p')
            .text(msg)
          .end()
          .on('click', '.btn-ok', -> method = 'resolveWith')
          .on('hidden.bs.modal', ->
            d[method].apply d, [opts.context ? $dlg, opts.arguments ? []]
          )
          .modal()
      ).promise()

$.extend JQueryUiStaticExt


# Defines jQuery extensions which are used in the user interface (UI).
#
# @mixin
# @author   Daniel Ellermann
# @version  2.0
#
JQueryUiExt =

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
      .on('click', '.btn-action-delete[href]', (event) =>
        @_onClickDeleteBtn event
      )
      .on('focusin', '.form-control-number', (event) =>
        @_onFocusInNumberControl event
      )
      .on('focusout', '.form-control-number', (event) =>
        @_onFocusOutNumberControl event
      )
      .on('change', '.date-input-control', (event) =>
        @_onChangeDateInput event
      )
      .ajaxSend( -> $spinner.fadeIn())
      .ajaxComplete( -> $spinner.fadeOut())

    $('select').each (index, element) => @_initSelect index, element
    $('textarea').autosize()
      .each ->
        $(this).wrap("""<div class="textarea-container"/>""")
          .after("""<i class="fa fa-question-circle markdown-help-btn"></i>""")

    $('.data-form').on('change', '.auto-number input:checkbox', (event) =>
        @_onChangeAutoNumberCheckbox event
      )
    $('.auto-number').trigger 'change'


  #-- Non-public methods ------------------------
  
  # Initializes the given select control using Selectize.  Depending on
  # attribute data-find-url the control is initialized to load the options from
  # the given URL.
  #
  # @param [Number] index
  # @param [Element] element  the given select control
  # @private
  #
  _initSelect: (index, element) ->
    $element = $(element)

    opts =
      onInitialize: ->
        id = @$input.attr 'id'
        @$control_input.attr 'id', id.replace(/-select$/, '') if id

    url = $element.data 'find-url'
    if url
      $organization = $($element.data 'filter-organization')
      $resetOnChange = $($element.data 'reset-on-change')
      $.extend opts,
        labelField: 'name'
        load: (query, callback) ->
          $org = $organization

          data = name: query
          data.organization = $org.val() if $org.length

          $.getJSON(url, data)
            .done((data) -> callback data)
            .fail(-> callback())

          return
        onItemAdd: ->
          $otherSel = $resetOnChange
          $otherSel[0].selectize.clearOptions() if $otherSel.length
          return
        preload: 'focus'
        searchField: ['name']
        sortField: 'name'
        valueField: 'id'

    $element.selectize opts
    return

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

  # Called if either the date or time part of a date/time input field has been
  # changed. The method computes a formatted composed value in a hidden
  # date/time field.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangeDateInput: (event) ->
    $target = $(event.currentTarget)

    if $target.attr('id').match /^([\w\-.]+)-(date|time)$/
      baseId = RegExp.$1
      type = RegExp.$2

      input = $target[0]
      elements = input.form.elements

      otherPartField =
        elements["#{baseId}_#{if (type is 'date') then 'time' else 'date'}"]

      val = ''
      if type is 'date'
        val += input.value
        val += " #{otherPartField.value}" if otherPartField
      else
        val += "#{otherPartField.value} " if otherPartField
        val += input.value

      elements[baseId].value = val

  # Called if the user clicks on a link to delete a record.  This method
  # displays a deletion confirmation dialog.  If the user confirms the link is
  # loaded to the current window.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickDeleteBtn: (event) ->
    $ = jQuery
    $LANG = $L

    $target = $(event.currentTarget)
    $.confirm(
        $LANG('default.delete.confirm.msg'),
        $LANG('default.delete.confirm.title'),
        okBtn:
          color: 'danger'
          icon: 'trash'
          label: $LANG('default.button.delete.label')
      )
      .done( ->
        url = $target.attr 'href'
        url += (if url.indexOf('?') < 0 then '?' else '&') + 'confirmed=1'
        window.location.assign url
      )

    false

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

  # Called if a number control gets the focus.  The method removes all zeros
  # after the decimal point.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onFocusInNumberControl: (event) ->
    $target = $(event.currentTarget)

    val = $target.val().parseNumber()
    $target.val (if val then val.format(null, false) else '')

    return

  # Called if a number control looses the focus.  The method formats the
  # number depending on the type of number control (currency, percentage
  # etc.).
  #
  # @param [Event] event  any event data
  # @private
  #
  _onFocusOutNumberControl: (event) ->
    $target = $(event.currentTarget)

    numDigits = $I.numFractions
    if $target.hasClass 'form-control-currency-ext'
      numDigits = $I.numFractionsExt
    else if $target.hasClass 'form-control-percentage'
      numDigits = 1
    d = $target.data('num-fraction-digits')
    numDigits = parseInt d, 10 if d?
    numDigits = null if $target.data 'suppress-reformat'

    val = $target.val().parseNumber()
    $target.val val.format numDigits

  # Called if the window has been finished loading and rendering.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onLoadWindow: (event) ->
    @_initToolbar()
    $(event.target).on('scroll', (event) => @_onScrollWindow event)
      .triggerHandler 'scroll'

    # I initialize the Bootstrap datepicker widget here because before, the
    # l18n file in lang/bootstrap-datepicker hasn't been loaded yet.
    datePickerDefaults =
      autoclose: true
      clearBtn: true
      language: $I.lang.split('-')[0]
      todayBtn: true
      todayHighlight: true
    $.extend $.fn.datepicker.defaults, datePickerDefaults
    $('.date-input-date-control').datepicker()

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

SPRINGCRM.page = (->
  $ = jQuery
  win = window
  doc = win.document
  $document = $(doc)

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

    $('#main-container').on('click', '#print-btn', -> win.print())

#    $('.date-input-time').autocomplete
#        select: onSelectTimeValue
#        source: timeValues

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

