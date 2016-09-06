#
# _ui.coffee
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
#= require jquery/jquery
#= require jquery/autosize
#= require jqueryui/core
#= require jqueryui/widget
#= require jqueryui/mouse
#= require jqueryui/draggable
#= require _core
#= require bootstrap/transition
#= require bootstrap/alert
#= require bootstrap/collapse
#= require bootstrap/dropdown
#= require bootstrap/modal
#= require bootstrap/tooltip
#= require bootstrap/popover
#= require bootstrap/datepicker
#= require selectize/selectize
#= require selectize/plugin-no-delete
#= require selectize/plugin-disable-options
#= require _handlebars-ext
#= require templates/tools/js-calc
#= require templates/tools/vat-calc
#= require _js-calc
#= require _vat-calc


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
  confirm: (msg, title, options) ->
    $ = jQuery
    unless $.type(title) is 'string'
      options = title
      title = null
    options ?= {}

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
# @version  2.1
#
JQueryUiExt =

  # Follows the URL in the `href` attribute of the jQuery object if the user
  # confirms a dialog.
  #
  # For more information about the available options refer to
  # `JQueryUiStaticExt.confirm`.
  #
  # @param [String] msg                   the message (prompt) to be displayed
  # @param [String] title                 an optional title of the modal
  # @param [Object] options               any options which are used in the modal and the deferred object; see function description for more information
  # @return [Boolean]                     always `false` to prevent event bubbling
  # @see JQueryUiStaticExt.html#confirm-  JQueryUiStaticExt.confirm
  # @since                                2.0
  #
  confirmLink: (msg, title, options) ->
    $ = jQuery

    $.Deferred()
      .resolve()
      .then( -> $.confirm msg, title, options)
      .done( => window.location.href = $(this).attr 'href')

    false

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

  # Gets the selected text of the input controls in the jQuery object.  For
  # each input control either the selected text is taken or the whole text if
  # nothing is selected.  The obtained texts are concatenated by the given
  # separator.
  #
  # @param [string] separator the separator used to concatenate the texts
  # @return [string]          the selected texts
  # @since 2.1
  #
  getSelection: (separator = '') ->
    $ = jQuery

    selections = []
    @each ->
      if 'selectionStart' of this and 'selectionEnd' of this
        $input = $(this)
        text = $input.val()
        selStart = @selectionStart
        selEnd = @selectionEnd

        if selStart isnt selEnd
          text = text.substring selStart, selEnd

        selections.push text

      return

    selections.join separator

  # Replaces the selection of the input controls in the jQuery object by the
  # given content.
  #
  # @param [string] content the given content
  # @return [jQuery]        this jQuery object
  # @since 2.1
  #
  replaceSelection: (content = '') ->
    $ = jQuery

    @each ->
      if 'selectionStart' of this and 'selectionEnd' of this
        $input = $(this)
        selStart = @selectionStart
        selEnd = @selectionEnd
        text = $input.val()

        text = text.substring(0, selStart) + content + text.substring(selEnd)
        $input.val text

      return

  # Reverses the elements in the jQuery object.
  #
  # @return [jQuery]  this jQuery object with reversed content
  # @since            1.3
  #
  reverse: [].reverse

  # Scrolls this jQuery object to a particular target or offset.
  #
  # @param [Number, String, jQuery] target  either a target or a numeric offset
  # @param [Object] options                 any options (see below)
  # @option options [Number] duration       the duration for scroll animation
  # @option options [String] easing         the easing type of scroll animation
  # @param [Function] callback              an optional callback function which is executed after scrolling
  # @return [jQuery]                        this jQuery object
  # @since 2.0
  #
  scrollTo: (target, options, callback) ->
    $ = jQ = jQuery

    if $.isFunction options and arguments.length is 2
      callback = options
      options = target

    settings = $.extend
        duration: 500
        easing: 'swing'
        scrollTarget: target
      , options

    @each ->
      $ = jQ
      $scrollPane = $(this)

      if $.isNumeric settings.scrollTarget
        scrollTarget = settings.scrollTarget
        scrollY = scrollTarget
      else
        scrollTarget = $(settings.scrollTarget)
        scrollY = scrollTarget.offset().top

      $scrollPane.animate { scrollTop: scrollY },
        parseInt(settings.duration, 10),
        settings.easing,
        -> if $.isFunction callback then callback.call this

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
# @version  2.1
#
class Page

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery

  # @nodoc
  $I18N = $I

  # @nodoc
  $LANG = $L


  #-- Constructor -------------------------------

  # Initializes a page in this application.
  #
  constructor: ->
    $ = jq
    win = window
    $spinner = $('#spinner')

    $(win).on('load', (event) => @_onLoadWindow event)
    $(win.document)
      .on('click', '.go-top-btn', (event) ->
        $ = jQuery
        $('html, body').scrollTo $(event.currentTarget).attr 'href'
        false
      )
      .on(
        'click', '.boilerplate-add-btn',
        (event) => @_onClickAddBoilerplate event
      )
      .on('click', '.markdown-help-btn', => @_onClickMarkdownHelpBtn())
      .on('click', '#spinner', -> $(this).fadeOut())
      .on('click', '.btn-print', -> win.print())
      .on('click', '.btn-action-delete[href]', (event) =>
        @_onClickDeleteBtn event
      )
      .on('click', '.hidden-assessments > header > h3', (event) =>
        @_onClickHiddenAssessmentsHeader event
      )
      .on('focusin', '.textarea-container', (event) =>
        @_onFocusInTextareaContainer event
      )
      .on('focusin', '.form-control-number', (event) =>
        @_onFocusInNumberControl event
      )
      .on('focusout', '.textarea-container', (event) =>
        @_onFocusOutTextareaContainer event
      )
      .on('focusout', '.form-control-number', (event) =>
        @_onFocusOutNumberControl event
      )
      .on('change', '.date-input-control', (event) =>
        @_onChangeDateInput event
      )
      .on('change', '.date-input-time-control', (event) =>
        @_onChangeTimeInput event
      )
      .ajaxSend( -> $spinner.fadeIn())
      .ajaxComplete( -> $spinner.fadeOut())

    $('select')
      .not('.textarea-toolbar select')
        .each (_, element) => @initSelect $(element)
    @_initTextAreas()

    $('.data-form').on('change', '.auto-number input:checkbox', (event) =>
        @_onChangeAutoNumberCheckbox event
      )
    $('.auto-number').trigger 'change'

    # open all links in HTML content in new window
    $('.html-content a').attr 'target', '_blank'

    @_initTools()


  #-- Public methods ----------------------------

  # Initializes the given select control using Selectize.  Depending on
  # attribute data-find-url the control is initialized to load the options from
  # the given URL.
  #
  # @param [jQuery] $select the given select control
  #
  initSelect: ($select) ->
    $ = jq

    plugins =
      disable_options:
        disableOptions: []
    plugins['no-delete'] = {} if $select.attr 'required'

    opts =
      onInitialize: ->
        id = @$input.attr 'id'
        @$control_input.attr 'id', id.replace(/-select$/, '') if id
      plugins: plugins

    url = $select.data 'find-url'
    if url
      $organization = $($select.data 'filter-organization')
      $resetOnChange = $($select.data 'reset-on-change')
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
          if $otherSel.length
            $otherSel.each -> @selectize.clearOptions()

          return
        preload: 'focus'
        searchField: ['name']
        sortField: 'name'
        valueField: 'id'

    $select.selectize opts
    return


  #-- Non-public methods ------------------------

  # Initializes text areas to support boilerplates and auto-sizing.
  #
  # @private
  # @since 2.1
  #
  _initTextAreas: ->
    $ = jq
    $html = $('html')
    loadUrl = $html.data 'load-boilerplates-url'
    getUrl = $html.data 'get-boilerplate-url'

    autosize $('textarea')

    $('.textarea-toolbar select').selectize
      labelField: 'name'
      load: (query, callback) ->
        $.getJSON(loadUrl, name: query)
          .done((data) -> callback data)
          .fail(-> callback())

        return
      onItemAdd: (value) ->
        selectize = this
        $.getJSON(getUrl, id: value)
          .done (data) ->
            selectize.$wrapper
              .closest('.textarea-container')
                .children('textarea')
                  .replaceSelection data.content
            selectize.clear()

            return

        return
      preload: 'focus'
      searchField: ['name']
      sortField: 'name'
      valueField: 'id'

    return

  # Initializes the title/toolbar that it may be treated as fixed when
  # scrolling.
  #
  # @private
  #
  _initToolbar: ->
    @$titleToolbar = $titleToolbar = $('.title-toolbar')
    if $titleToolbar.length
      @yToolbar = $titleToolbar.offset().top
      $titleToolbar.parent().height $titleToolbar.innerHeight()

  # Initializes the tools such as the calculators.
  #
  # @private
  # @since 2.0
  #
  _initTools: ->
    $ = jq
    $I = $I18N
    $L = $LANG

    $('#calculator')
      .draggable()
      .parent()
        .on('show.bs.dropdown', -> $('#calculator > div').jscalc('enable'))
        .on('hide.bs.dropdown', -> $('#calculator > div').jscalc('disable'))
      .end()
      .find('> div')
        .jscalc point: $I.decimalSeparator

    taxRates = $.each $I.taxRates, -> "#{this} %"
    $('#vat-calculator')
      .draggable()
      .find('> div')
        .vatcalc(
          accessKeys:
            calculate: $L('vatCalculator.calculate.hotkey')
            gross: $L('vatCalculator.gross.hotkey')
            input: $L('vatCalculator.input.hotkey')
            net: $L('vatCalculator.net.hotkey')
            vatRate: $L('vatCalculator.vatRate.hotkey')
          currency: $I.currency
          labels:
            calculate: $L('vatCalculator.calculate.label')
            gross: $L('vatCalculator.gross.label')
            net: $L('vatCalculator.net.label')
            vat: $L('vatCalculator.vat.label')
            vatRate: $L('vatCalculator.vatRate.label')
          point: $I.decimalSeparator
          taxRates: taxRates
        )

    return

  # Called when the state of the checkbox at auto number fields has been
  # changed. The method toggles the disabled state of the auto number input
  # field.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangeAutoNumberCheckbox: (event) ->
    $target = $(event.currentTarget)
    $target.closest('.control-container')
      .find('.input-group input')
        .toggleEnable $target, true

  # Called when either the date or time part of a date/time input field has
  # been changed. The method computes a formatted composed value in a hidden
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

  # Called when the time value has been changed.  The method parses and formats
  # particular time values.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangeTimeInput: (event) ->
    $target = $(event.currentTarget)

    val = $target.val()
    if val.match /^\d$/
      val = "0#{val}:00"
    else if val.match /^\d\d$/
      val = "#{val}:00"
    else if val.match /^(\d)(\d\d)$/
      val = "0#{RegExp.$1}:#{RegExp.$2}"
    else if val.match /^(\d\d)(\d\d)$/
      val = "#{RegExp.$1}:#{RegExp.$2}"
    else if val.match /^(\d{1,2})\D(\d{0,2})$/
      hour = RegExp.$1.padLeft 2, '0'
      minute = RegExp.$2.padLeft 2, '0'
      val = "#{hour}:#{minute}"

    $target.val val
    return

  # Called when the button to add a boilerplate has been clicked.  The method
  # displays a modal window containing a form to submit the boilerplate name
  # and content, submits the form and displays a popup indicating success or
  # failure.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickAddBoilerplate: (event) ->
    $ = jq
    $container = $(event.currentTarget).closest '.textarea-container'
    $textarea = $container.find 'textarea'

    getPopoverTemplate = (status) ->
      """
        <div class="popover popover-#{status}" role="tooltip">
          <div class="arrow"></div>
          <h3 class="popover-title"></h3>
          <div class="popover-content"></div>
        </div>
      """

    $('#add-boilerplate-modal')
      .find('#content')
        .val($textarea.getSelection())
      .end()
      .on('click', '.btn-ok', (event) ->
        $modal = $(event.delegateTarget)
        $form = $modal.find 'form'

        return false unless $form[0].reportValidity()

        $.post($form.attr('action'), $form.serialize())
          .done( ->
            $textarea
              .popover(
                content: $L('default.boilerplate.saved')
                template: getPopoverTemplate('success')
              )
              .popover('show')

            return
          )
          .fail( ->
            $textarea
              .popover(
                content: $L('default.boilerplate.error')
                template: getPopoverTemplate('danger')
              )
              .popover('show')

            return
          )
          .always( ->
            $modal.modal 'hide'
            window.setTimeout (-> $textarea.popover 'hide'), 3000

            return
          )

        false
      )
      .modal()

    false

  # Called when the user clicks on a link to delete a record.  This method
  # displays a deletion confirmation dialog.  If the user confirms the link is
  # loaded to the current window.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickDeleteBtn: (event) ->
    $ = jq
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

  # Called when the user clicks on the header of a section with hidden
  # assessments.  The method opens or closes the assessments content area.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  # @since 2.1
  #
  _onClickHiddenAssessmentsHeader: (event) ->
    $(event.currentTarget)
      .closest('.hidden-assessments')
        .find('.assessments-content')
          .slideToggle()

    false

  # Called when the user clicks on the icon to display the Markdown help.
  #
  # @private
  #
  _onClickMarkdownHelpBtn: ->
    $ = jq

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

  # Called when a number control gets the focus.  The method removes all zeros
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

  # Called when any input control in a text area container receives the focus.
  # The method displays a toolbar below the text area.
  #
  # @param [Event] event  any event data
  # @private
  # @since 2.1
  #
  _onFocusInTextareaContainer: (event) ->
    $(event.currentTarget).closest('.textarea-container')
      .addClass('focus')
      .find('.textarea-toolbar')
        .slideDown()

    return

  # Called when a number control looses the focus.  The method formats the
  # number depending on the type of number control (currency, percentage
  # etc.).
  #
  # @param [Event] event  any event data
  # @private
  #
  _onFocusOutNumberControl: (event) ->
    $target = $(event.currentTarget)
    return if $target.data 'allow-null'

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

  # Called when any input control in a text area container loses the focus.
  # The method hides the toolbar below the text area.
  #
  # @param [Event] event  any event data
  # @private
  # @since 2.1
  #
  _onFocusOutTextareaContainer: (event) ->
    $ = jq

    $container = $(event.currentTarget).closest '.textarea-container'
    $focusInput = $(event.relatedTarget)

    if $focusInput.length is 0 or $container.has($focusInput).length is 0
      $container
        .removeClass('focus')
        .find('.textarea-toolbar')
          .slideUp()

    return

  # Called when the window has been finished loading and rendering.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onLoadWindow: (event) ->
    $ = jq

    @_initToolbar()
    $(event.target).on('scroll', (event) => @_onScrollWindow event)
      .triggerHandler 'scroll'

    # I initialize the Bootstrap datepicker widget here because before, the
    # l18n file in lang/bootstrap-datepicker hasn't been loaded yet.
    datePickerDefaults =
      #autoclose: true
      clearBtn: true
      daysOfWeekHighlighted: '0'
      language: $I.lang.split('-')[0]
      todayBtn: true
      todayHighlight: true
    $.extend $.fn.datepicker.defaults, datePickerDefaults
    $('.date-input-date-control').datepicker()

  # Called when the window is scrolling.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onScrollWindow: (event) ->
    $target = $(event.target)
    @$titleToolbar.toggleClass 'fixed', $target.scrollTop() >= @yToolbar


#== Polyfills ===================================

unless HTMLFormElement::reportValidity
  HTMLFormElement::reportValidity = ->
    valid = @checkValidity()
    unless valid
      for btn in @querySelectorAll 'button, input[type=submit]'

        # Filter out <button type="button">, as querySelectorAll can't
        # handle :not filtering
        btn.click() if btn.type is 'submit'

    valid


#== Main ========================================

window.SPRINGCRM.page = new Page()
