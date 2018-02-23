#
# _ui.coffee
#
# Copyright (c) 2011-2018, Daniel Ellermann
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
#= require polyfills/form-attr.js
#= require _handlebars-ext
#= require templates/tools/js-calc
#= require templates/tools/vat-calc
#= require _js-calc
#= require _vat-calc
#= require _jquery-ext


$ = jQuery


#== Classes =====================================

# Class `Page` handles default components of pages in this application.
#
# @author   Daniel Ellermann
# @version  3.0
#
class Page

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  __$I = $I

  # @nodoc
  #noinspection JSUnresolvedVariable
  __$L = $L


  #-- Constructor -------------------------------

  # Initializes a page in this application.
  #
  constructor: ->
    $ = __jq
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
      .on('click', '.save-link', (event) => @_onClickSaveLink event, false)
      .on(
        'click', '.save-and-close-link',
        (event) => @_onClickSaveLink event, true
      )
      .on('click', '.btn-print', -> win.print())
      .on('click', '.btn-action-delete', (event) => @_onClickDeleteBtn event)
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
      .on('change', '.num-items-per-page-form select', (event) =>
        @_onChangeNumItemsPerPage event
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
    $ = __jq

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
    $ = __jq
    $html = $('html')
    loadUrl = $html.data 'load-boilerplates-url'
    getUrl = $html.data 'get-boilerplate-url'

    autosize $('textarea')

    $('.no-textarea-toolbar')
      .find('.textarea-toolbar')
        .remove()
      .end()
      .find('textarea')
      .removeAttr(
        'aria-controls aria-live aria-relevant data-toggle data-placement ' +
        'data-container data-trigger'
      )

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
    $ = __jq
    $I = __$I
    $L = __$L

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

  # Called when the selector with the number of items per page has been
  # changed.  The method reloads the current URL with another `max` value.
  #
  # @param [Event] event  any event data
  # @private
  # @since 2.1
  #
  _onChangeNumItemsPerPage: (event) ->
    url = new HttpUrl(window.location.href)
    url.overwriteQuery max: $(event.currentTarget).val()

    window.location.href = url.toString()

    return

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
    $ = __jq
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
    $ = __jq
    #noinspection JSUnresolvedVariable
    $L = __$L

    $target = $(event.currentTarget)
    $.confirm(
        $L('default.delete.confirm.msg'),
        $L('default.delete.confirm.title'),
        okBtn:
          color: 'danger'
          icon: 'trash'
          label: $L('default.button.delete.label')
      )
      .done( ->
        $target
          .closest('form')
            .each ->
              @elements['confirmed'].value = '1'
              @submit()
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
    $ = __jq

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

  # Called when the user clicks the link to save and optionally close the form.
  # The method submits the form.
  #
  # @param [Event] event    any event data
  # @param [Boolean] close  if `true` the form is closed; `false` otherwise
  # @return [Boolean]       always `false` to prevent event bubbling
  # @private
  # @since 2.2
  #
  _onClickSaveLink: (event, close) ->
    $ = __jq

    $('#close-form').attr 'value', if close then '1' else ''
    $(event.currentTarget)
      .closest('.btn-group')
        .find('button[type=submit]')
          .click()

    false

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
    $ = __jq

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
    $ = __jq

    @_initToolbar()
    $(event.target).on('scroll', (event) => @_onScrollWindow event)
      .triggerHandler 'scroll'

    # I initialize the Bootstrap datepicker widget here because before, the
    # l18n file in lang/bootstrap-datepicker hasn't been loaded yet.
    datePickerDefaults =
      autoclose: true
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
