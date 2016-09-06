#
# _jquery-ext.coffee
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
#= require bootstrap/modal


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

    $dlg
      .find('.model-body p')
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
      $dlg
        .find('.modal-body p')
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

        text = text.substring(selStart, selEnd) if selStart isnt selEnd

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
    getSortable = getSortable or -> this

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

    if b then @enable() else @disable()

$.fn.extend JQueryUiExt
