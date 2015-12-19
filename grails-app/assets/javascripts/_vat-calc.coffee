###

  vat-calc.coffee

  Copyright (c) 2014-2015, Daniel Ellermann

  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:

  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
  THE SOFTWARE.

###


# @nodoc
$ = jQuery


# Class `VatCalculator` represents the client-side V.A.T calculator.
#
# @author   Daniel Ellermann
# @version  0.9
# @since    0.9
#
class VatCalculator

  #-- Private variables -------------------------

  $ = jQuery


  #-- Instance variables ------------------------

  DEFAULT_OPTIONS =
    accessKeys:
      calculate: 'c'
      gross: 'g'
      input: 'i'
      net: 'n'
      vatRate: 'r'
    currency: '€'
    gross: false
    labels:
      calculate: 'Calculate'
      gross: 'Gross'
      net: 'Net'
      vat: 'V.A.T'
      vatRate: 'Rate'
    point: '.'
    precision: 2
    taxRates: [7, 19]


  #-- Constructor -------------------------------

  # Creates a new calculator within the given element.
  #
  # @param [Element] element  the given container element
  # @param [Object] options   any options that overwrite the default options
  #
  constructor: (element, options = {}) ->
    @$element = $(element)
    @options = $.extend {}, DEFAULT_OPTIONS, options
    @disabled = false

    @_renderTemplate()


  #-- Non-public methods ------------------------

  # Calculates either the net or gross value depending on the input values.
  #
  # @private
  #
  _calculate: ->
    options = @options
    point = options.point
    precision = options.precision

    input = @$input.val().replace(point, '.')
    input = '0' unless input
    input = parseFloat input
    rate = parseFloat @$rates.val()

    if @$netGrossSwitch.is ':checked'
      v = 100.0 + rate
      vat = rate * input / v
      res = 100.0 * input / v
    else
      vat = input * rate / 100.0
      res = input + vat

    @$vat.val @_format vat
    @$result.val @_format res

    return

  # Formats the given numeric value respecting the decimal point and precision
  # from the options.
  #
  # @param [Number] value the given numeric value
  # @return [String]      the formatted number
  # @private
  #
  _format: (value) ->
    options = @options
    precision = options.precision

    value = value.toFixed precision if precision >= 0
    value.replace /\./, options.point

  # Called if the toggle switch for net/gross values has been changed.  The
  # method changes the label of the output field and calculates the result.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangeNetGross: (event) ->
    $this = $(event.currentTarget)
    $label = $this.parent()
    if $this.is ':checked'
      @$netGrossLabel.text $label.prev().text()
    else
      @$netGrossLabel.text $label.next().text()

    @_calculate()

  # Renders the Handlebars template that displays the calculator.
  #
  # @private
  #
  _renderTemplate: ->
    html = Handlebars.templates['tools/vat-calc']
      options: @options
    $el = @$element
      .empty()
      .html(html)
      .on('click', 'button', =>
        @_calculate()

        false
      )
      .on('click', (event) -> event.stopPropagation())
      .on('change', '.vatcalc-net-gross-switch', (event) =>
        @_onChangeNetGross event
      )
      .on('change', '.vatcalc-vat-rates', => @_calculate())
      .on('change', '.vatcalc-input', => @_calculate())
      .on('change keyup', '.vatcalc-input', => @_calculate())

    @$input = $el.find '.vatcalc-input'
    @$netGrossSwitch = $el.find '.vatcalc-net-gross-switch'
    @$rates = $el.find '.vatcalc-vat-rates'
    @$vat = $el.find '.vatcalc-vat'
    @$netGrossLabel = $el.find '.vatcalc-net-gross-label'
    @$result = $el.find '.vatcalc-result'


Plugin = (option) ->
  args = arguments
  @each ->
    $this = $(this)
    data = $this.data 'bs.vatcalc'

    unless data
      $this.data 'bs.vatcalc', (data = new VatCalculator(this, args[0]))

# @nodoc
old = $.fn.vatcalc

# @nodoc
$.fn.vatcalc = Plugin
# @nodoc
$.fn.vatcalc.Constructor = VatCalculator

# @nodoc
$.fn.vatcalc.noConflict = ->
  $.fn.vatcalc = old
  this
