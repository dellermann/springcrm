#
# sales-order-signature.coffee
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
#= require application
#= require _signature-pad


#== Classes =====================================

# Class `SalesOrderSignature` handles the signature functionality of sales
# orders.
#
# @author   Daniel Ellermann
# @version  3.0
#
class SalesOrderSignature

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new widget which handles the signature functionality of sales
  # orders.
  #
  # @param [jQuery] $element  the element containing the form
  # @param [Object] [options] any options
  #
  constructor: ($element, options = {}) ->
    $ = __jq

    defOptions =
      onChangeSignature: ->
    @options = $.extend {}, defOptions, options

    @_initSignature()

    $element
      .on(
        'click', '#set-signature-btn, #signature-canvas > img',
        => @_onClickSetSignatureBtn()
      )
      .on(
        'click', '#delete-signature-btn', => @_onClickDeleteSignatureBtn()
      )
    $('window').on 'resize', => @_onResizeWindow()

    return


  #-- Non-public methods ------------------------

  # Calls the callback function when the signature has been changed.
  #
  # @param [String] value the value to set in the hidden input control
  # @private
  #
  _handleChangeSignature: (value) ->
    $signature = @$signature

    $signature.attr 'value', value
    @_showSignature()

    f = @options.onChangeSignature
    f($signature) if f

    return

  # Initializes the components used to sign the order.
  #
  # @private
  #
  _initSignature: ->
    $ = __jq

    @$signature = $('#signature')
    @$signatureDlg = $dlg = $('#signature-dialog')
    @signatureDlgCanvas = $dlg.find('canvas')[0]

    @_showSignature()

    return

  # Called when the button to clear the signature has been clicked.  The method
  # clears the whole signature canvas.
  #
  # @private
  #
  _onClickClearSignature: ->
    @signaturePad.clear()

    return

  # Called when the button to delete the signature has been clicked.  The
  # method deletes the signature data in the hidden input control.
  #
  # @private
  #
  _onClickDeleteSignatureBtn: ->
    @_handleChangeSignature ''

    return

  # Called when the OK button in the signature dialog has been clicked.  The
  # method stores the SVG code of the signature and closes the dialog.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickDlgOkBtn: (event) ->
    signaturePad = @signaturePad

    data = signaturePad.toData()
    value = if data.length then signaturePad.toDataURL('image/svg+xml') else ''
    $(event.delegateTarget).modal 'hide'

    @_handleChangeSignature value

    return

  # Called when the button to open the signature dialog has been clicked.  The
  # method opens a dialog containing a canvas which is used to write a
  # signature with the mouse, finger or pen.
  #
  # @private
  #
  _onClickSetSignatureBtn: ->
    @signaturePad = new SignaturePad(@signatureDlgCanvas)

    @$signatureDlg
      .on('shown.bs.modal', => @_onShownDlg())
      .on('click', '.clear-btn', => @_onClickClearSignature())
      .on('click', '.ok-btn', (event) => @_onClickDlgOkBtn event)
      .modal backdrop: 'static'

    return

  # Called when the window is resized.  The method resizes the canvas
  # containing the signature.
  #
  # @private
  #
  _onResizeWindow: ->
    @_resizeCanvas()

    return

  # Called when the dialog is visible.  The method restores the signatures
  # which has been previously set.
  #
  # @private
  #
  _onShownDlg: ->
    @_resizeCanvas()

    #    signature = @$signature.val()
    #    @signaturePad.fromDataURL signature if signature

    return

  # Resizes the canvas containing the signature using the device pixel ratio of
  # the screen.
  #
  # @private
  #
  _resizeCanvas: ->
    canvas = @signatureDlgCanvas

    ratio = Math.max window.devicePixelRatio or 1, 1
    canvas.width = canvas.offsetWidth * ratio
    canvas.height = canvas.offsetHeight * ratio
    canvas.getContext('2d').scale ratio, ratio
    @signaturePad.clear()

    return

  # Shows the signature or a placeholder message if no signature has been
  # stored.
  #
  # @private
  #
  _showSignature: ->
    signature = @$signature.val()
    if signature
      $('#signature-canvas')
        .find('img')
          .attr('src', signature)
        .end()
        .show()
      $('#no-signature-msg').hide()
      $('#delete-signature-btn').show()
    else
      $('#signature-canvas, #delete-signature-btn').hide()
      $('#no-signature-msg').show()

    return

window.modules.register 'SalesOrderSignature', SalesOrderSignature
