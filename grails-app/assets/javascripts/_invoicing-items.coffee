#
# invoicing-items.coffee
#
# Copyright (c) 2011-2014, Daniel Ellermann
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


$ = jQuery


# Defines a jQuery widget which handles the items in an invoicing transaction
# such as quotes, invoices, dunnings etc.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.4
#
InvoicingItemsWidget =

  INPUT_FIELD_NAMES: [
    "number", "quantity", "unit", "name", "description", "unitPrice", "tax"
  ]

  options:
    currency: $("html").data("currency-symbol") or "€"
    fieldNamePrefix: "items"
    productListUrl: $(".price-table").data("product-list-url")
    serviceListUrl: $(".price-table").data("service-list-url")
    taxes: $(".price-table").data("tax-items").split(",")
    units: $(".price-table").data("units").split(",")

  # Adds a new row with empty input fields to the end of the items table.
  #
  # @param {Boolean} jumpToNewRow if `true` the view is scrolled to the new row
  #
  _addItem: (jumpToNewRow) ->
    $ = jQuery

    # prepare Handlebars template
    template = @addItemTemplate
    unless template
      template = Handlebars.compile $("#add-item-template").html()
      @addItemTemplate = template

    index = @_getNumRows()
    s = template
      index: index
      pos: index + 1
      zero: (0).formatCurrencyValue()
    $row = $(s)
    @element.find(".items").append $row

    @_initUnitAutocomplete $row.find(".unit input")
    @_initTaxAutocomplete $row.find(".tax input")
    $row.find("textarea")
      .autosize()
    if jumpToNewRow
      $("html").scrollTop $row.position().top - $("#toolbar").outerHeight()

  # Adds a new tax rate and the associated tax value to the given array of tax
  # rates.
  #
  # @param {Array<Number>} taxRates an array containing the tax rates to add to
  # @param {Number} taxRate         the given tax rate
  # @param {Number} tax             the given tax value
  #
  _addTaxRate: (taxRates, taxRate, tax) ->
    found = false
    for tr in taxRates
      if tr.taxRate is taxRate
        tr.tax += tax
        found = true
        break

    unless found
      taxRates.push
        taxRate: taxRate
        tax: tax

  # Computes the values in the footer of the price table, such as subtotal,
  # tax rates, total etc.
  #
  _computeFooterValues: ->
    $ = jQuery

    subtotalNet = 0
    @element.find(".items .total-price output").each ->
      subtotalNet += $(this).text().parseNumber()

    shippingCosts = $("#shippingCosts").val().parseNumber()
    subtotalNet += shippingCosts
    @subtotalNet = subtotalNet
    $("#subtotal-net").text subtotalNet.formatCurrencyValue()
    @_computeTaxValues()
    subtotalGross = @subtotalGross
    discountPercent = $("#discountPercent").val().parseNumber()
    discount = subtotalGross * discountPercent / 100
    $("#discount-from-percent").text discount.formatCurrencyValue()
    discount += $("#discountAmount").val().parseNumber()
    adjustment = $("#adjustment").val().parseNumber()
    @total = total = subtotalGross - discount + adjustment
    $("#total-price").text total.formatCurrencyValue()
    $("#paymentAmount").trigger "change"

  # Computes a list of tax values from the tax rates the user specified in the
  # input fields.  After that, the list of tax rates is displayed ordered in
  # the footer of the price table.
  #
  # @return {Number}  the computed subtotal gross value
  #
  _computeTaxValues: ->
    $ = jQuery

    # compute a map of tax rates
    taxRates = []
    @element.find("input:text[name$='.tax']")
      .each (index, elem) =>
        els = @form.elements
        name = elem.name.replace /\.tax$/, ".quantity"
        qty = els[name].value.parseNumber()
        name = elem.name.replace /\.tax$/, ".unitPrice"
        unitPrice = els[name].value.parseNumber()

        taxRate = elem.value.parseNumber()
        if taxRate isnt 0
          tax = qty * unitPrice * taxRate / 100.0
          @_addTaxRate taxRates, taxRate, tax

    # add the shipping tax to the tax rate map
    shippingCosts = $("#shippingCosts").val().parseNumber()
    shippingTax = $("#shippingTax").val().parseNumber()
    if shippingCosts isnt 0 and shippingTax isnt 0
      @_addTaxRate taxRates, shippingTax, shippingCosts * shippingTax / 100.0
    taxRates.sort (a, b) ->
      a.taxRate - b.taxRate

    # prepare Handlebars template
    template = @taxRateSumTemplate
    unless template
      template = Handlebars.compile $("#tax-rate-sum-template").html()
      @taxRateSumTemplate = template

    # display the tax rates
    currency = @options.currency
    taxTotal = 0
    s = ""
    for tr in taxRates
      t = tr.tax
      taxTotal += t
      label = $L("invoicingTransaction.taxRate.label").replace /\{0\}/, tr.taxRate.format(1)
      s += template
        label: label
        value: t.formatCurrencyValue()
    $(".tax-rate-sum").remove()
    $("tfoot tr:first").after s

    # compute values
    @taxTotal = taxTotal
    @subtotalGross = subtotalGross = @subtotalNet + taxTotal
    $("#subtotal-gross").text subtotalGross.formatCurrencyValue()
    subtotalGross

  # Initializes this widget.
  #
  _create: ->
    $ = jQuery
    el = @element
    @form = el.parents("form")
      .get(0)

    @subtotalNet = 0.0
    @taxTotal = 0.0
    @subtotalGross = 0.0
    @total = 0.0

    opts = @options
    @units = opts.units
    @taxes = @_prepareTaxes opts.taxes
    @inputRegExp = new RegExp("^#{opts.fieldNamePrefix}\\[(\\d+)\\]\\.(\\w+)$")

    el.on("click", ".up-btn", (event) =>
        @_moveItem $(event.currentTarget), -1
        false
      )
      .on("click", ".down-btn", (event) =>
        @_moveItem $(event.currentTarget), 1
        false
      )
      .on("click", ".remove-btn", (event) =>
        @_removeItem $(event.currentTarget)
        false
      )
      .on("click", ".select-btn-products", (event) =>
        @_showSalesItemSelector $(event.currentTarget), "products"
        false
      )
      .on("click", ".select-btn-services", (event) =>
        @_showSalesItemSelector $(event.currentTarget), "services"
        false
      )
      .on("change", (event) => @_onChange(event))
    $(".add-invoicing-item-btn").click =>
      @_addItem true
      false

    numItems = el.find(".items tr").length
    if numItems is 0
      @_addItem false
    else
      @_initUnitAutocomplete()
      @_initTaxAutocomplete()
    @_computeFooterValues()

  # Gets the input field with the given position, name, and an optional suffix.
  #
  # @param {Number} pos     the given zero-based position
  # @param {String} name    the name of the input control
  # @param {String} suffix  an optional suffix which is added to the field name prefix
  # @return {String}        the input field; `null` if no field with the computed name exists
  #
  _getInput: (pos, name = "", suffix = "") ->
    @form.elements[@_getInputName pos, name, suffix]

  # Gets the name of the input field with the given index, name, and an
  # optional suffix.
  #
  # @param {Number} index   the given zero-based index
  # @param {String} name    the name of the input control
  # @param {String} suffix  an optional suffix which is added to the field name prefix
  # @returns {String}       the computed input field name
  #
  _getInputName: (index, name = "", suffix = "") ->
    "#{@options.fieldNamePrefix}#{suffix}[#{index}].#{name}"

  # Gets the number of item rows in the price table.
  #
  # @returns {Number} the number of rows
  #
  _getNumRows: ->
    @element.find(".items tr").length

  # Gets the position and name of the given input field.
  #
  # @param {Object} input the given input field
  # @returns {Array}      an array containing the zero-based position as first and the name as second element
  #
  _getInputPosAndName: (input) ->
    parts = input.name.match(@inputRegExp)
    if parts
      parts.shift()
      parts
    else
      null

  # Gets the position of the given price table row.  The position is the
  # zero-based sequence number of the row in the table.
  #
  # @param {jQuery} $tr the given row
  # @returns {Number}   the zero-based position of the given row
  #
  _getRowPosition: ($tr) ->
    $tr.index()

  # Initialize the autocomplete fields for tax rates.
  #
  # @param {jQuery} $input  a selector which input fields are to initialize; if not defined, all tax input fields are initialized
  #
  _initTaxAutocomplete: ($input) ->
    taxes = @taxes
    if taxes
      $input = $(".tax input") unless $input
      $input.autocomplete source: taxes

  # Initialize the autocomplete fields for units.
  #
  # @param {jQuery} $input  a selector which input fields are to initialize; if not defined, all unit input fields are initialized
  #
  _initUnitAutocomplete: ($input) ->
    units = @units
    if units
      $input = $(".unit input") unless $input
      $input.autocomplete source: units

  # Loads the content of the sales item selector via AJAX.
  #
  # @param {String} type  the type of sales items which are to retrieve; may be either `products` or `services`
  # @param {String} url   the URL called to load the sales items via AJAX; if not specified the URL is obtained from the options
  # @param {Number} pos   the zero-based position of the row in the price table to fill
  # @param {Array} params any parameters which are to send (optional)
  #
  _loadSalesItemSelector: (type, url, pos, params) ->
    $dialog = $("#inventory-selector-#{type}")
    $dialog.load url, params, =>
      @_onLoadSalesItemSelector $dialog, type, pos

  # Moves a row in the price table up- or downwards.
  #
  # @param {jQuery} $icon the symbol which was clicked to move the row
  # @param {Number} dir   a negative value moves the row upwards; otherwise it moves it downwards
  _moveItem: ($icon, dir) ->
    $tr = $icon.parents("tr")

    # swap current row with previous or next row
    pos = @_getRowPosition($tr)
    if (dir < 0) and (pos > 0)
      $destTr = $tr.prev()
      $destTr.before $tr
    else if pos < @_getNumRows() - 1
      $destTr = $tr.next()
      $destTr.after $tr

    # swap input name positions and item positions
    if $destTr
      @_swapInputItemPos $tr, $destTr
      @_swapItemPos $tr, $destTr

  # Called if an input field is changed.  The method re-computes the total
  # value if either the quantity or the unit prices has been changed.  In each
  # case the footer values are re-computed.
  #
  # @param {Object} event the event data
  #
  _onChange: (event) ->
    input = event.target
    parts = @_getInputPosAndName input
    if parts
      [index, name] = parts
      if name is "quantity" or name is "unitPrice"
        if name is "quantity"
          qty = input.value.parseNumber()
          unitPrice = @_getInput(index, "unitPrice").value.parseNumber()
        if name is "unitPrice"
          unitPrice = input.value.parseNumber()
          qty = @_getInput(index, "quantity").value.parseNumber()
        $(input).parents("tr")
          .find(".total-price output")
            .text (qty * unitPrice).formatCurrencyValue()
    @_computeFooterValues()

  # Called if the the content of the sales item selector has been successfully
  # loaded via AJAX.
  #
  # @param {jQuery} $dialog the dialog where the selector content has been loaded
  # @param {String} type    the type of sales items which are to retrieve; may be either `products` or `services`
  # @param {Number} pos     the zero-based position of the row in the price table to fill
  #
  _onLoadSalesItemSelector: ($dialog, type, pos) ->
    $ = jQuery

    getData = ->
      search = $dialog.find("[name=search]").val()
      if search is "" then null else search: search

    $dialog.off("click")
      .on("click", "a.select-link", (event) =>
        @_retrieveSalesItem type, $(event.currentTarget).attr("href"), pos
        false
      )
      .on("click", "a:not(.select-link)", (event) =>
        @_loadSalesItemSelector type, $(event.currentTarget).attr("href"), pos, getData()
        false
      )
      .find("form")
        .submit((event) =>
          @_loadSalesItemSelector type, event.target.action, pos, getData()
          false
        )
      .end()
      .dialog(
        minWidth: 700
        minHeight: 400
        modal: true
      )

  # Converts all tax rates in the given array to formatted strings.
  #
  # @param {Array} taxes  the given tax rates as percentage values
  # @returns {Array}      an array of strings containing the formatted tax rates
  #
  _prepareTaxes: (taxes) ->
    if taxes
      (tax * 100).format() for tax in taxes when typeof tax is "number"
    else
      null

  # Removes the row in the pricing table that remove symbol was clicked.
  #
  # @param {jQuery} $icon the clicked remove symbol
  #
  _removeItem: ($icon) ->
    if @_getNumRows() > 1
      @_removeRow $icon.parents("tr")
      @_computeFooterValues()

  # Removes the given row from the pricing table.
  #
  # @param {jQuery} $tr the given table row
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
  # @param {String} type  the type of sales item which is to retrieve; may be either `products` or `services`
  # @param {String} url   the URL used to retrieve the sales item data from the server
  # @param {Number} pos   the zero-based position of the row in the pricing table which is to fill with the retrieved data
  #
  _retrieveSalesItem: (type, url, pos) ->
    self = this
    $.ajax
      url: url
      dataType: "json"
      success: (data) =>
        prefix = @_getInputName pos
        els = @form.elements
        els[prefix + "number"].value = data.fullNumber
        item = data.inventoryItem
        qty = item.quantity
        els[prefix + "quantity"].value = qty.format()
        els[prefix + "unit"].value = item.unit.name
        els[prefix + "name"].value = item.name
        textArea = els[prefix + "description"]
        textArea.value = item.description
        $(textArea).trigger 'autosize.resize'
        unitPrice = item.unitPrice
        unitPriceInput = els[prefix + "unitPrice"]
        unitPriceInput.value = unitPrice.formatCurrencyValue()
        total = (qty * unitPrice).formatCurrencyValue()
        $(unitPriceInput).parents("tr")
          .find(".total-price output").text total
        els[prefix + "tax"].value = (item.taxRate.taxValue * 100.0).format(1)
        @_computeFooterValues()
        $("#inventory-selector-" + type).dialog "close"

  # Displays the sales item selector of the given type and loads its content
  # via AJAX.
  #
  # @param {jQuery} $icon the selector symbol which was clicked
  # @param {String} type  the type of sales items which are to retrieve; may be either `products` or `services`
  # @param {String} url   the URL called to load the sales items via AJAX; if not specified the URL is obtained from the options
  #
  _showSalesItemSelector: ($icon, type, url) ->
    opts = @options
    unless url
      url = (if (type is "products") then opts.productListUrl else opts.serviceListUrl)
    if url
      pos = @_getRowPosition $icon.parents("tr")
      @_loadSalesItemSelector type, url, pos

  # Swaps the positions in the input field names for both the given rows.
  #
  # @param {jQuery} $tr     the given row
  # @param {jQuery} $destTr the other row
  #
  _swapInputItemPos: ($tr, $destTr) ->
    fieldNames = @INPUT_FIELD_NAMES
    form = @form
    f = (name, newName) ->
      elems = form.elements
      for fieldName in fieldNames
        el = elems[name + fieldName]
        el.name = newName + fieldName

    pos = @_getRowPosition($tr)
    destPos = @_getRowPosition($destTr)
    f @_getInputName(pos), @_getInputName(destPos, "", "-dest")
    f @_getInputName(destPos), @_getInputName(pos)
    f @_getInputName(destPos, "", "-dest"), @_getInputName(destPos)

  # Swaps the position number in the first cell of both the given rows.
  #
  # @param {jQuery} $tr     the given row
  # @param {jQuery} $destTr the other row
  #
  _swapItemPos: ($tr, $destTr) ->
    $td = $tr.find("td:first-child")
    $destTd = $destTr.find("td:first-child")

    s = $td.text()
    $td.text $destTd.text()
    $destTd.text s

$.widget "springcrm.invoicingitems", InvoicingItemsWidget
