#
# _invoicing-items.coffee
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
#= require application
#= require _handlebars-ext
#= require _typeahead


$ = jQuery


# Class `InvoicingItems` defines a widget which handles the items in an
# invoicing transaction such as quotes, invoices, dunnings etc.
#
# @author   Daniel Ellermann
# @version  2.0
#
class InvoicingItems

  #-- Class variables ---------------------------

  @DEFAULTS =
    currency: $I.currencySymbol
    fieldNamePrefix: 'items'
    productListUrl: $('.price-table').data('product-list-url')
    serviceListUrl: $('.price-table').data('service-list-url')
    taxes: $('.price-table').data('tax-items').split(',')
    units: $('.price-table').data('units').split(',')

  @INPUT_FIELD_NAMES = [
    'quantity'
    'unit'
    'name'
    'description'
    'unitPrice'
    'tax'
  ]


  #-- Constructor -------------------------------

  # Creates a new widget instance to handle invoicing transaction items within
  # the given element.
  #
  # @param [jQuery] $element  the given element
  # @param [Object] options   any options
  #
  constructor: ($element, options) ->
    $ = jQuery
    @$element = $element
    @options = options = $.extend {}, InvoicingItems.DEFAULTS, options

    @subtotalNet = 0.0
    @taxTotal = 0.0
    @subtotalGross = 0.0
    @total = 0.0

    @form = $element.parents('form')[0]
    $element
      .on('click', '.up-btn', (event) => @_onClickMoveItem event, -1)
      .on('click', '.down-btn', (event) => @_onClickMoveItem event, 1)
      .on('click', '.remove-btn', (event) => @_onClickRemoveItem event)
      .on('click', '.btn-select-sales-item', (event) =>
        @_onClickSelectSalesItem event
      )
      .on('change', (event) => @_onChange event)

    @units = options.units
    @taxes = @_prepareTaxes options.taxes
    @inputRegExp = /// ^
        #{options.fieldNamePrefix}      # the prefix
        \[(\d+)\]                       # the numerical index
        \.                              # followed by a dot
        (\w+)                           # the name of the field
        $
      ///

    $('.add-invoicing-item-btn').on 'click', =>
      @_addItem true
      false

    numItems = $element.find('.items tr').length
    if numItems is 0
      @_addItem false
    else
      @_initUnitTypeahead()
      @_initTaxTypeahead()
    @_computeFooterValues()


  #-- Non-public methods ------------------------

  # Adds a new row with empty input fields to the end of the items table.
  #
  # @param [Boolean] jumpToNewRow if `true` the view is scrolled to the new row
  # @private
  #
  _addItem: (jumpToNewRow) ->
    $ = jQuery

    # prepare Handlebars template
    template = @addItemTemplate
    unless template
      template = Handlebars.compile $('#add-item-template').html()
      @addItemTemplate = template

    index = @_getNumRows()
    s = template
      index: index
      pos: index + 1
      zero: (0).formatCurrencyValue()
    $row = $(s)
    @$element.find('.items').append $row

    @_initUnitTypeahead $row.find '.col-unit input'
    @_initTaxTypeahead $row.find '.col-tax input'
    $row.find('textarea')
      .autosize()
    if jumpToNewRow
      $('html').scrollTop(
          $row.position().top - $('.title-toolbar').outerHeight()
        )

    return

  # Adds a new tax rate and the associated tax value to the given array of tax
  # rates.
  #
  # @param [Array] taxRates an array containing the tax rates to add to
  # @param [Number] taxRate the given tax rate
  # @param [Number] tax     the given tax value
  # @private
  #
  _addTaxRate: (taxRates, taxRate, tax) ->
    found = false
    for tr in taxRates
      if tr.taxRate is taxRate
        tr.tax += tax
        found = true
        break

    taxRates.push taxRate: taxRate, tax: tax unless found
    return

  # Computes the values in the footer of the price table, such as subtotal,
  # tax rates, total etc.
  #
  # @private
  #
  _computeFooterValues: ->
    $ = jQuery

    subtotalNet = 0
    @$element.find('.items .col-total-price input').each ->
      subtotalNet += $(this).val().parseNumber()

    shippingCosts = $('#shippingCosts').val().parseNumber()
    subtotalNet += shippingCosts
    @subtotalNet = subtotalNet
    $('#subtotal-net').val subtotalNet.formatCurrencyValue()
    @_computeTaxValues()
    subtotalGross = @subtotalGross
    discountPercent = $('#discountPercent').val().parseNumber()
    discount = subtotalGross * discountPercent / 100
    $('#discount-from-percent').val discount.formatCurrencyValue()
    discount += $('#discountAmount').val().parseNumber()
    adjustment = $('#adjustment').val().parseNumber()
    @total = total = subtotalGross - discount + adjustment
    $('#total-price').val total.formatCurrencyValue()
    $('#paymentAmount').trigger 'change'

    return

  # Computes a list of tax values from the tax rates the user specified in the
  # input fields.  After that, the list of tax rates is displayed ordered in
  # the footer of the price table.
  #
  # @return [Number]  the computed subtotal gross value
  # @private
  #
  _computeTaxValues: ->
    $ = jQuery

    # compute a map of tax rates
    taxRates = []
    @$element.find('input[name$=".tax"]')
      .each (_, elem) =>
        els = @form.elements
        name = elem.name.replace /\.tax$/, '.quantity'
        qty = els[name].value.parseNumber()
        name = elem.name.replace /\.tax$/, '.unitPrice'
        unitPrice = els[name].value.parseNumber()

        taxRate = elem.value.parseNumber()
        if taxRate isnt 0
          tax = qty * unitPrice * taxRate / 100.0
          @_addTaxRate taxRates, taxRate, tax

    # add the shipping tax to the tax rate map
    shippingCosts = $('#shippingCosts').val().parseNumber()
    shippingTax = $('#shippingTax').val().parseNumber()
    if shippingCosts isnt 0 and shippingTax isnt 0
      @_addTaxRate taxRates, shippingTax, shippingCosts * shippingTax / 100.0
    taxRates.sort (a, b) -> a.taxRate - b.taxRate

    # prepare Handlebars template
    template = @taxRateSumTemplate
    unless template
      template = Handlebars.compile $('#tax-rate-sum-template').html()
      @taxRateSumTemplate = template

    # display the tax rates
    currency = @options.currency
    taxTotal = 0
    s = ""
    for tr, i in taxRates
      t = tr.tax
      taxTotal += t
      label = $L('invoicingTransaction.taxRate.label')
        .replace /\{0\}/, tr.taxRate.format(1)
      s += template
        i: i
        label: label
        value: t.formatCurrencyValue()
    $('.row-tax-rate-sum').remove()
    $('tfoot tr:first').after s

    # compute values
    @taxTotal = taxTotal
    @subtotalGross = subtotalGross = @subtotalNet + taxTotal
    $('#subtotal-gross').val subtotalGross.formatCurrencyValue()

    subtotalGross

  # Gets the input field with the given position, name, and an optional suffix.
  #
  # @param [Number] pos     the given zero-based position
  # @param [String] name    the name of the input control
  # @param [String] suffix  an optional suffix which is added to the field name prefix
  # @return [String]        the input field; `null` if no field with the computed name exists
  # @private
  #
  _getInput: (pos, name = '', suffix = '') ->
    @form.elements[@_getInputName pos, name, suffix]

  # Gets the name of the input field with the given index, name, and an
  # optional suffix.
  #
  # @param [Number] index   the given zero-based index
  # @param [String] name    the name of the input control
  # @param [String] suffix  an optional suffix which is added to the field name prefix
  # @returns [String]       the computed input field name
  # @private
  #
  _getInputName: (index, name = '', suffix = '') ->
    "#{@options.fieldNamePrefix}#{suffix}[#{index}].#{name}"

  # Gets the position and name of the given input field.
  #
  # @param [Object] input the given input field
  # @returns [Array]      an array containing the zero-based position as first and the name as second element
  # @private
  #
  _getInputPosAndName: (input) ->
    parts = input.name.match @inputRegExp
    if parts
      parts.shift()
      parts
    else
      null

  # Gets the number of item rows in the price table.
  #
  # @returns [Number] the number of rows
  # @private
  #
  _getNumRows: -> @$element.find('.items tr').length

  # Gets the position of the given price table row.  The position is the
  # zero-based sequence number of the row in the table.
  #
  # @param [jQuery] $tr the given row
  # @returns [Number]   the zero-based position of the given row
  # @private
  #
  _getRowPosition: ($tr) -> $tr.index()

  # Initialize the autocomplete fields for tax rates.
  #
  # @param [jQuery] $input  a selector representing the input fields that should be initialized; if not defined, all tax input fields are initialized
  # @private
  #
  _initTaxTypeahead: ($input = $('.col-tax input')) ->
    taxes = @taxes
    if taxes
      $input.typeahead
          highlight: true
          hint: true
          minLength: 1
        ,
          displayKey: 'value'
          name: 'taxes'
          source: (q, cb) ->
            matches = []
            re = new RegExp(RegExp.escape(q), 'i')
            $.each taxes, (_, t) -> matches.push value: t if re.test String(t)
            cb matches

  # Initialize the autocomplete fields for units.
  #
  # @param [jQuery] $input  a selector representing the input fields that should be initialized; if not defined, all unit input fields are initialized
  #
  _initUnitTypeahead: ($input = $('.col-unit input')) ->
    units = @units
    if units
      $input.typeahead
          highlight: true
          hint: true
          minLength: 1
        ,
          displayKey: 'value'
          name: 'units'
          source: (q, cb) ->
            matches = []
            re = new RegExp("^#{RegExp.escape(q)}", 'i')
            $.each units, (_, t) -> matches.push value: t if re.test t
            cb matches

  # Loads the content of the sales item selector via AJAX.
  #
  # @param [String] type    the type of sales items which should be retrieved; must be either `product` or `service`
  # @param [Number] pos     the zero-based position of the row that should be filled in the price table
  # @param [String] url     the URL which is called to load the sales items via AJAX
  # @param [Object] params  any parameters which are to send (optional)
  # @private
  #
  _loadSalesItemSelector: (type, pos, url, params) ->
    $modal = $("#inventory-selector-#{type}")
    $modal.find('.modal-body')
      .load url, params, =>
        @_onLoadedSalesItemSelector $modal, type, pos

  # Called if an input field is changed.  The method re-computes the total
  # value if either the quantity or the unit prices has been changed.  In each
  # case the footer values are re-computed.
  #
  # @param [Event] event any event data
  # @private
  #
  _onChange: (event) ->
    input = event.target
    parts = @_getInputPosAndName input
    if parts
      [index, name] = parts
      if name is 'quantity' or name is 'unitPrice'
        if name is 'quantity'
          qty = input.value.parseNumber()
          unitPrice = @_getInput(index, 'unitPrice').value.parseNumber()
        if name is "unitPrice"
          unitPrice = input.value.parseNumber()
          qty = @_getInput(index, 'quantity').value.parseNumber()
        $(input).parents('tr')
          .find('.col-total-price input')
            .val (qty * unitPrice).formatCurrencyValue()

    @_computeFooterValues()

  # Called if a button to moves a row in the price table up- or downwards has
  # been clicked.
  #
  # @param [Event] event  any event data
  # @param [Number] dir   a negative value moves the row upwards; otherwise it moves it downwards
  # @private
  #
  _onClickMoveItem: (event, dir) ->
    $tr = $(event.currentTarget).parents 'tr'

    # swap current row with previous or next row
    pos = @_getRowPosition $tr
    if dir < 0 and pos > 0
      $destTr = $tr.prev()
      $destTr.before $tr
    else if pos < @_getNumRows() - 1
      $destTr = $tr.next()
      $destTr.after $tr

    # swap input name positions and item positions
    if $destTr
      @_swapInputItemPos $tr, $destTr
      @_swapItemPos $tr, $destTr

    return

  # Called if the button has been clicked to remove a row in the pricing
  # table.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickRemoveItem: (event) ->
    if @_getNumRows() > 1
      @_removeRow $(event.currentTarget).parents 'tr'
      @_computeFooterValues()

    return

  # Called if a button has been clicked to display the sales item selector of
  # the given type and loads its content
  # via AJAX.
  #
  # @param [Event] event  any event data
  # @param [String] url   an optional URL which is called to load the sales items via AJAX; if not specified the URL is obtained from the options
  # @private
  #
  _onClickSelectSalesItem: (event, url) ->
    $target = $(event.currentTarget)
    type = $target.data 'type'

    opts = @options
    url ?= opts["#{type}ListUrl"]
    if url
      pos = @_getRowPosition $target.parents 'tr'
      @_loadSalesItemSelector type, pos, url

    return

  # Called if the the content of the sales item selector has been successfully
  # loaded via AJAX.
  #
  # @param [jQuery] $modal  the modal where the selector content has been loaded
  # @param [String] type    the type of sales items which should be retrieved; must be either `product` or `service`
  # @param [Number] pos     the zero-based position of the row that should be filled in the price table
  # @private
  #
  _onLoadedSalesItemSelector: ($modal, type, pos) ->
    $ = jQuery

    getData = ->
      search = $modal.find('[name=search]').val()
      if search is '' then null else search: search

    $modal
      .off('click')
      .on('click', '.modal-header .close', (event) -> $modal.modal 'hide')
      .on('click', '.modal-body a.select-link', (event) =>
        @_retrieveSalesItem $modal, pos, $(event.currentTarget).attr('href')
        false
      )
      .on('click', '.modal-body a:not(.select-link)', (event) =>
        @_loadSalesItemSelector type, pos,
          $(event.currentTarget).attr('href'), getData()
        false
      )
      .find('form')
        .on('submit', (event) =>
          @_loadSalesItemSelector type, pos, event.target.action, getData()
          false
        )
      .end()
      .modal()

  # Converts all tax rates in the given array to formatted strings.
  #
  # @param [Array] taxes  the given tax rates as percentage values
  # @returns [Array]      an array of strings containing the formatted tax rates
  # @private
  #
  _prepareTaxes: (taxes) ->
    $ = jQuery

    if taxes
      (tax * 100).format() for tax in taxes when $.isNumeric tax
    else
      null

  # Removes the given row from the pricing table.
  #
  # @param [jQuery] $tr the given table row
  # @private
  #
  _removeRow: ($tr) ->
    $ = jQuery
    fieldPrefix = @options.fieldNamePrefix
    re = @inputRegExp

    # fix row position labels and input names of all successing rows
    pos = @_getRowPosition $tr
    $tr.nextAll()
        .each((i) ->
          $this = $(this)

          p = pos
          $this.find("td:first-child")
            .text String(p + i + 1) + "."

          prefix = fieldPrefix
          regexp = re
          $this.find(":input")
            .each ->
              parts = @name.match regexp
              @name = "#{prefix}[#{p + i}].#{parts[2]}" if parts
        )
      .end()
      .remove()

  # Retrieves the sales item of the given type and URL and fills in the input
  # fields of the price table row with the given position.
  #
  # @param [jQuery] $modal  the modal where the selector content has been loaded
  # @param [Number] pos     the zero-based position of the row that should be filled in the price table
  # @param [String] url     the URL used to retrieve the sales item data from the server
  # @return [Promise]       a promise representing the state of loading data
  # @private
  #
  _retrieveSalesItem: ($modal, pos, url) ->
    $.ajax(
        context: this
        dataType: 'json'
        url: url
      )
      .done (data) =>
        prefix = @_getInputName pos
        els = @form.elements

        item = data.inventoryItem
        qty = item.quantity
        els[prefix + 'quantity'].value = qty.format()
        els[prefix + 'unit'].value = item.unit.name
        els[prefix + 'salesItem.id'].value = item.id
        els[prefix + 'name'].value = item.name
        textArea = els[prefix + 'description']
        textArea.value = item.description
        $textArea = $(textArea).trigger 'autosize.resize'
        unitPrice = item.unitPrice
        unitPriceInput = els[prefix + 'unitPrice']
        unitPriceInput.value = unitPrice.formatCurrencyValue()
        total = (qty * unitPrice).formatCurrencyValue()
        $textArea.closest('tr')
          .find('.col-total-price input').val total
        els[prefix + 'tax'].value = (item.taxRate.taxValue * 100.0).format(1)

        @_computeFooterValues()
        $modal.modal 'hide'

  # Swaps the positions in the input field names for both the given rows.
  #
  # @param [jQuery] $tr     the given row
  # @param [jQuery] $destTr the other row
  # @private
  #
  _swapInputItemPos: ($tr, $destTr) ->
    fieldNames = InvoicingItems.INPUT_FIELD_NAMES
    form = @form

    swap = (name, newName) ->
      elems = form.elements
      for fieldName in fieldNames
        el = elems[name + fieldName]
        el.name = newName + fieldName

    pos = @_getRowPosition $tr
    destPos = @_getRowPosition $destTr
    swap @_getInputName(pos), @_getInputName(destPos, '', '-dest')
    swap @_getInputName(destPos), @_getInputName(pos)
    swap @_getInputName(destPos, '', '-dest'), @_getInputName(destPos)

    return

  # Swaps the position number in the first cell of both the given rows.
  #
  # @param [jQuery] $tr     the given row
  # @param [jQuery] $destTr the other row
  # @private
  #
  _swapItemPos: ($tr, $destTr) ->
    $td = $tr.find 'td:first-child'
    $destTd = $destTr.find 'td:first-child'

    s = $td.text()
    $td.text $destTd.text()
    $destTd.text s

    return

SPRINGCRM.InvoicingItems = InvoicingItems

# vim:set ts=2 sw=2 sts=2:
