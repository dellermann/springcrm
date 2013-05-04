#
# sales-item-pricing.coffee
#
# Copyright (c) 2011-2013, Daniel Ellermann
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


$ = jQuery
win = window
$doc = $(win.document)


# Defines a jQuery widget which handles pricings in sales items such as
# products or services.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.3
#
SalesItemPricing =

  # The names of the input controls of a table row.
  #
  INPUT_FIELD_NAMES: [
      "quantity", "unit", "name", "type", "relToPos", "unitPercent", "unitPrice"
  ]

  # During finder mode the row that referring item currently is searched.
  #
  _$finderRow: null

  # The form associated to the pricing table.
  #
  _form: null

  # Stores whether or not pricing is enabled when the form is displayed.
  #
  _initialPricingEnabled: false

  # A regular expression which is applied to the names of input fields in order
  # to obtain the index and field name.
  #
  _inputRegExp: null

  # The form element indicating whether or not pricing is enabled for this
  # sales item.
  #
  _pricingEnabled: null

  # The options for this widget.
  #
  options:
    currency: "€"
    fieldNamePrefix: "pricing.items"
    imgPath: null
    units: null

  # Adds a row for a new item to the pricing table.
  #
  # @param {Boolean} jumpToNewRow `true` if the document is to scroll that the new row is visible; `false` otherwise
  #
  _addItem: (jumpToNewRow) ->
    opts = @options
    currency = opts.currency
    imgPath = opts.imgPath
    index = @_getRows().length

    s = """
      <tr>
        <td class="pos number">#{String(index + 1)}.</td>
        <td class="quantity number">
          <input type="text" name="#{@_getInputName(index, "quantity")}" size="6" />
        </td>
        <td class="unit">
          <input type="text" name="#{@_getInputName(index, "unit")}" size="8" />
        </td>
        <td class="name">
          <input type="text" name="#{@_getInputName(index, "name")}" size="30" />
        </td>
        <td class="type">
          <select name="#{@_getInputName(index, "type")}">
            <option value="absolute">#{$L("salesItem.pricing.type.absolute")}</option>
            <option value="relativeToPos">#{$L("salesItem.pricing.type.relativeToPos")}</option>
            <option value="relativeToLastSum">#{$L("salesItem.pricing.type.relativeToLastSum")}</option>
            <option value="relativeToCurrentSum">#{$L("salesItem.pricing.type.relativeToCurrentSum")}</option>
            <option value="sum">#{$L("salesItem.pricing.type.sum")}</option>
          </select>
        </td>
        <td class="relative-to-pos">
          <input type="hidden" name="#{@_getInputName(index, "relToPos")}" />
          <span style="display: none;"><img src="#{imgPath}/target.png" alt="#{$L("salesItem.pricing.relativeToPos.finder")}" title="#{$L("salesItem.pricing.relativeToPos.finder")}" width="16" height="16" /><strong></strong></span>
        </td>
        <td class="unit-percent percentage number">
          <input type="text" name="#{@_getInputName(index, "unitPercent")}" size="5" class="percent" />
        </td>
        <td class="unit-price currency number">
          <input type="text" name="#{@_getInputName(index, "unitPrice")}" size="8" />&nbsp;#{currency}
        </td>
        <td class="total-price currency number">
          <output>#{(0).formatCurrencyValue()}</output>&nbsp;#{currency}
        </td>
        <td class="action-buttons">
      """
    if imgPath
      s += """
          <img class="up-btn" src="#{imgPath}/up.png"
            alt="#{$L("default.btn.up")}" title="#{$L("default.btn.up")}"
            width="16" height="16"
          /><img class="down-btn" src="#{imgPath}/down.png"
            alt="#{$L("default.btn.down")}" title="#{$L("default.btn.down")}"
            width="16" height="16"
          /><img class="remove-btn" src="#{imgPath}/remove.png"
            alt="#{$L("default.btn.remove")}" title="#{$L("default.btn.remove")}"
            width="16" height="16" />
        """
    s += """
        </td>
      </tr>
      """

    $row = $(s)
    @_initItemCtrls $row
    @element.find("> .items").append $row

    @_initUnitAutocomplete $row.find(".unit input")

    if jumpToNewRow
      pos = $row.position().top - $("#toolbar").outerHeight()
      $("html, body").animate { scrollTop: pos }, "slow"

  # Computes the total price of the given item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {Number}            the computed total price; `null` if the referred item was not set
  #
  _computeTotalPrice: (item) ->
    idx = @_getIndex item
    type = @_getRowType item
    return @_getCurrentSum(idx - 1) if type is "sum"

    unitPrice = @_computeUnitPrice idx
    (if (unitPrice is null) then null else @_getFieldVal(item, "quantity") * unitPrice)

  # Computes the unit price of the given item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {Number}            the computed unit price; `null` if the type of the pricing item is `sum` or unknown or the referred item was not set
  #
  _computeUnitPrice: (item) ->
    idx = @_getIndex item

    unitPrice = null
    switch @_getRowType item
      when "absolute"
        unitPrice = @_getFieldVal item, "unit-price"
      when "relativeToPos"
        otherIndex = @_getFieldVal item, "relative-to-pos"
        if otherIndex >= 0
          totalPrice = @_computeTotalPrice otherIndex
          unitPrice = (@_getFieldVal(item, "unit-percent") * totalPrice / 100.0).round(2) if totalPrice isnt null
      when "relativeToLastSum"
        otherIndex = @_getLastSumIndex idx
        if otherIndex < 0
          unitPrice = (@_getFieldVal(item, "unit-percent") * @_getCurrentSum(idx - 1) / 100.0).round(2)
        else
          totalPrice = @_computeTotalPrice otherIndex
          unitPrice = (@_getFieldVal(item, "unit-percent") * totalPrice / 100.0).round(2) if totalPrice isnt null
      when "relativeToCurrentSum"
        unitPrice = (@_getFieldVal(item, "unit-percent") * @_getCurrentSum(idx - 1) / 100.0).round(2)
    unitPrice

  # Initializes this widget.
  #
  _create: ->
    $ = jQuery

    el = @element
    opts = @options
    opts.imgPath = opts.imgPath or el.data("img-path")
    opts.units = opts.units or el.data("units").split(",")
    @_inputRegExp = new RegExp("^#{opts.fieldNamePrefix}\\[(\\d+)\\]\\.(\\w+)$")

    $("#start-pricing").click (event) => @_onClickStartPricing(event)
    $("#remove-pricing").click (event) => @_onClickRemovePricing(event)

    $form = el.parents("form")
    @_form = form = $form.get(0)
    @_pricingEnabled = pricingEnabled = form.pricingEnabled
    @_initialPricingEnabled = initialPricingEnabled = !!pricingEnabled.value
    $(".hidden :input").attr "disabled", "disabled"
    @_toggleVisibility() if initialPricingEnabled

    el.change((event) => @_onChange(event))
      .click((event) => @_onClick(event))
    $(win).focusin((event) => @_onFocusIn(event))
      .focusout((event) => @_onFocusOut(event))

    $trs = @_getRows()
    $trs.each (index, elem) =>
      @_initItemCtrls $(elem)
    @_updateReferenceClasses()

    @_initUnitAutocomplete() if $trs.length isnt 0
    $(".add-pricing-item-btn").click =>
      @_addItem true
      false

    $("#step1-pricing-quantity").change (event) => @_onChangeStep1PricingQuantity(event)
    $("#step1-pricing-unit").change (event) => @_onChangeStep1PricingUnit(event)
    $("#step2").change (event) => @_onChangeStep2(event)
    $("#step3-quantity").change (event) => @_onChangeStep3Quantity(event)

  # Disables all options of the type selector in the given item which are not
  # available in the current state.  The method disables options if the item
  # is referenced by at least one another item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {jQuery|Number}     the given item
  #
  _disableTypeOptions: (item) ->
    @_enableTypeOptions item
    $tr = @_getRow item
    $select = $tr.find "td.type select"

    disableOption = (name) ->
      $select.children("option[value=#{name}]")
        .attr("disabled", "disabled")

    idx = @_getIndex item
    referrers = @_getReferrers idx
    if referrers.length
      disableOption "relativeToPos"

    for referrer in referrers
      if referrer < idx
        disableOption "relativeToLastSum"
        disableOption "relativeToCurrentSum"
        disableOption "sum"
        break
    item

  # Enables all options of the type selector in the given item.  The method is
  # needed after options have been disabled during a reference phase.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {jQuery|Number}     the given item
  #
  _enableTypeOptions: (item) ->
    @_getRow(item).find("td.type option:disabled")
      .removeAttr("disabled")
    item

  # Gets the sum of all items' total prices at the given index and before.
  #
  # @param {Number} idx the given zero-based item index; defaults to the last item index
  # @return {Number}    the current sum
  #
  _getCurrentSum: (idx) ->
    $ = jQuery
    $trs = @_getRows()
    idx = $trs.length - 1 if !idx?

    sum = 0
    $trs.slice(0, idx + 1)
      .each (i, elem) =>
        sum += @_computeTotalPrice(i) if @_getRowType($(elem)) isnt "sum"
    sum

  # Gets the input control in the table cell with the given name in the given
  # item.  In case of name `total-price` the `<output>` object is returned.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @param {String} name        the given name of the table cell
  # @return {jQuery}            the input control or the `<output>` object
  #
  _getField: (item, name) ->
    sel = (if (name is "total-price") then "output" else ":input")
    @_getRow(item).find("> .#{name} > #{sel}")

  # Gets the value of the input control in the table cell with the given name
  # in the given item.  In case of name `total-price` the text of the
  # `<output>` object is returned.  Numeric values are parsed before returned.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @param {String} name        the given name of the table cell
  # @return {String|Number}     the value of the input control or the text of the `<span>` object
  #
  _getFieldVal: (item, name) ->
    $field = @_getField item, name
    val = (if (name is "total-price") then $field.text() else $field.val())
    if (name is "quantity") or (name is "unit-percent") or (name is "unit-price") or (name is "total-price")
      val = val.parseNumber()
    else if name is "relative-to-pos"
      val = (if (val is "") then -1 else val.parseNumber())
    val

  # Gets the index of the item with the given table item or index in the table.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {Number}            the zero-based index or -1 if the item was not found
  #
  _getIndex: (item) ->
    (if (typeof item is "number") then item else @_getRows().index(item))

  # Returns the input field with the given name and item index.
  #
  # @param {Number} index   the zero-based index of the item
  # @param {String} name    the name of the field
  # @param {String} suffix  a suffix which is to append to the field name prefix defined in the options
  # @return {Object}        the DOM element representing the input field; `null` if no such input field exists
  #
  _getInput: (index, name = "", suffix = "") ->
    @_form.elements[@_getInputName index, name, suffix]

  # Computes the name of an input field for the given name and item index.
  #
  # @param {Number} index   the zero-based index of the item
  # @param {String} name    the name of the field
  # @param {String} suffix  a suffix which is to append to the field name prefix defined in the options
  # @return {String}        the computed field name
  #
  _getInputName: (index, name = "", suffix = "") ->
    "#{@options.fieldNamePrefix}#{suffix}[#{index}].#{name}"

  # Gets the last index of the item of type <code>SUM</code>.
  #
  # @param {Number} idx the given zero-based index; defaults to the last item index
  # @return {Number}    the zero-based index of the last subtotal sum; -1 if no such an item exists
  #
  _getLastSumIndex: (idx) ->
    $ = jQuery
    $trs = @_getRows()
    idx = $trs.length - 1 if !idx?

    res = -1
    $trs.slice(0, idx + 1)
      .reverse()
      .each (i, elem) =>
        if @_getRowType($(elem)) is "sum"
          res = idx - i
          return false
        true
    res

  # Gets the table row which is referred by the given item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {jQuery}            the referred table row; `null` if no reference is defined
  #
  _getReferredRow: (item) ->
    refIdx = @_getFieldVal item, "relative-to-pos"
    (if refIdx is -1 then null else @_getRow refIdx)

  # Gets a list of items which refer to the item with the given index.
  #
  # @param {Number} idx the given zero-based index
  # @return {Array}     the zero-based indices of the items referring the item with the given index
  #
  _getReferrers: (idx) ->
    res = []
    @_getRows().each (i, tr) =>
      $tr = $(tr)
      if @_getFieldVal($tr, "type") is "relativeToPos"
        refIdx = @_getFieldVal $tr, "relative-to-pos"
        res.push i if refIdx is idx
    res

  # Gets the table row of the given item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {jQuery}            the table row
  #
  _getRow: (item) ->
    (if (typeof item is "number") then @_getRows().eq(item) else item)

  # Gets all rows of the pricing table.
  #
  # @param {Number} idx the zero-based index up to but not including the rows are to return; defaults to all rows
  # @return {jQuery}    all table rows, optionally up to but not including the given index
  #
  _getRows: (idx) ->
    $trs = @element.find("> .items > tr")
    $trs = $trs.slice(0, idx) if idx?
    $trs

  # Gets the type of the given item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @return {String}            the type of the item: `absolute`, `relativeToPos`, `relativeToLastSum`, `relativeToCurrentSum`, or `sum`
  #
  _getRowType: (item) ->
    @_getFieldVal item, "type"

  # Initializes the given item by enabling or disabling the input controls
  # depending on the item type.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  #
  _initItemCtrls: (item) ->
    type = @_getRowType item
    @_getField(item, "quantity").toggleEnable type isnt "sum"
    @_getField(item, "unit").toggleEnable type isnt "sum"
    @_getField(item, "name").toggleEnable type isnt "sum"
    @_getField(item, "unit-percent").toggleEnable (type isnt "absolute") and (type isnt "sum")
    @_getField(item, "unit-price").toggleEnable type is "absolute"

  # Augments the given input control with the autocomplete feature to select
  # units.
  #
  # @param {jQuery} $input  the given input control
  #
  _initUnitAutocomplete: ($input) ->
    units = @options.units
    if units
      data = source: units
      $input = @element.find(".unit input") unless $input
      $input.autocomplete data

  # Moves the item with the given table row in to the given direction.
  #
  # @param {jQuery} $tr the table row to move
  # @param {Number} dir the direction to move; negative values move the row up, positive values down
  #
  _moveItem: ($tr, dir) ->
    checkReferee = ($tr, dir) =>
      if dir < 0 and @_getRowType($tr) is "relativeToPos"
        $refTr = @_getReferredRow $tr
        if $refTr isnt null and @_getRowType($refTr) isnt "absolute" and @_getIndex($tr) - 1 <= @_getIndex($refTr)
          win.alert $L("salesItem.pricing.error.notMovable.refBeforeReferee")
          return false
      true

    # obtain destination row
    $destTr = if dir < 0 then $tr.prev() else $tr.next()

    # check validation rules
    return unless $destTr.length and checkReferee($tr, dir) and checkReferee($destTr, -dir)

    # swap current row with previous or next row
    if dir < 0
      $destTr.before $tr
    else
      $destTr.after $tr

    # swap input name positions, item positions, and references
    @_swapInputItemPos $tr, $destTr
    @_swapItemPos $tr, $destTr
    @_swapItemReferences $tr, $destTr

    # fix type options after swapping
    @_disableTypeOptions $tr
    @_disableTypeOptions $destTr

    # update all values
    @_updateItems()

  # Called if an input control in the pricing table has been changed.
  #
  # @param {Object} event the event data
  #
  _onChange: (event) ->
    $target = $(event.target)
    $td = $target.parents("td")
    $tr = $td.parent()

    if $td.hasClass("quantity") or $td.hasClass("unit-percent") or $td.hasClass("unit-price")
      @_updateItems()
    else if $td.hasClass("type")
      @_initItemCtrls $tr
      if $target.val() is "relativeToPos"
        idx = @_getFieldVal $tr, "relative-to-pos"
        idx = (if (idx < 0) then "" else String(idx + 1) + ".")
        $tr.find("> .relative-to-pos > span").fadeIn()
      else
        $tr.find("> .relative-to-pos > span").fadeOut()
      @_updateReferenceClasses()
      @_updateItems()

  # Called if the pricing quantity in step 1 has been changed.
  #
  _onChangeStep1PricingQuantity: ->
    @_updateItems()

  # Called if the pricing unit in step 1 has been changed.
  #
  _onChangeStep1PricingUnit: ->
    @_updateItems()

  # Called if an input control in step 2 has been changed.
  #
  _onChangeStep2: ->
    @_updateSalesPricing()

  # Called if the quantity in step 3 has been changed.
  #
  _onChangeStep3Quantity: ->
    @_updateSalesPricing()

  # Called if an element in the pricing table has been clicked.
  #
  # @param {Object} event the event data
  # @return {boolean}     `true` to perform event bubbling; `false` otherwise
  # @see                  #_updateReferenceClasses-mixin _updateReferenceClasses
  #
  _onClick: (event) ->
    $finderRow = @_$finderRow
    $target = $(event.target)
    if $finderRow
      @_onClickReferenceItem $target
      return false

    $img = $target.closest("img")
    $tr = $target.closest("td").parent()
    if $img.hasClass "up-btn"
      @_moveItem $tr, -1
      return false
    if $img.hasClass "down-btn"
      @_moveItem $tr, 1
      return false
    if $img.hasClass "remove-btn"
      if $tr.hasClass "not-removable"
        win.alert $L("salesItem.pricing.error.notRemovable")
      else
        @_removeItem $tr
      return false
    if $img.is ".relative-to-pos img"
      @_startFinderMode $tr
      false

  # Called if the user is in "find reference item" mode and has clicked the
  # reference item.  If the clicked row has the CSS class `selectable` a
  # reference to it is stored in the finder row as defined in
  # `this._$finderRow`.  In each case, at last, the finder mode is deactivated.
  #
  # @param {jQuery} $target the clicked element
  # @see            #_startFinderMode-mixin _startFinderMode
  # @see            #_stopFinderMode-mixin _stopFinderMode
  #
  _onClickReferenceItem: ($target) ->
    $tr = $target.closest "tr"
    if $tr.hasClass "selectable"
      $finderRow = @_$finderRow
      idx = @_getIndex $finderRow
      refIdx = @_getIndex $tr
      oldIdx = @_getFieldVal $finderRow, "relative-to-pos"
      @_enableTypeOptions oldIdx unless oldIdx is -1
      @_setItemReference idx, refIdx
      @_disableTypeOptions $tr
      @_updateReferenceClasses()
      @_updateItems()
    @_stopFinderMode()

  # Called if the button to remove pricing has been clicked.
  #
  # @return {boolean} always `false` to prevent event bubbling
  # @since            1.3
  #
  _onClickRemovePricing: ->
    if @_initialPricingEnabled
      ok = win.confirm($L("salesItem.pricing.removePricing.confirm"))
      return false unless ok

    @_pricingEnabled.value = ""
    @_toggleVisibility()
    $("#quantity").val $("#step3-quantity").val()
    $("#unit").val $("#step3-unit").val()
    $("#unitPrice").val $("#step3-unit-price").text().parseNumber().formatCurrencyValue()
    false

  # Called if the button to start pricing has been clicked.
  #
  # @return {boolean} always `false` to prevent event bubbling
  # @since            1.3
  #
  _onClickStartPricing: ->
    @_pricingEnabled.value = "1"
    @_toggleVisibility()
    @_addItem false
    false

  # Called if an element in the pricing table has got the focus.
  #
  # @param {Object} event the event data
  #
  _onFocusIn: (event) ->
    $target = $(event.target)
    if $target.is ".number input"
      val = $target.val().parseNumber()
      $target.val (if val then val.format() else "")

  # Called if an element in the pricing table has lost the focus.
  #
  # @param {Object} event the event data
  #
  _onFocusOut: (event) ->
    $target = $(event.target)
    if $target.is ".currency input"
      $target.val $target.val().parseNumber().formatCurrencyValue()
    else if $target.is ".percentage input"
      $target.val $target.val().parseNumber().format(2)

  # Called if a key has been pressed.
  #
  # @param {Object} event the event data
  #
  _onKeyDown: (event) ->
    # Esc
    @_stopFinderMode() if @_$finderRow and (event.which is 27)

  # Removes the given pricing item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  #
  _removeItem: (item) ->
    $ = jQuery
    fieldPrefix = @options.fieldNamePrefix
    index = @_getIndex item
    re = @_inputRegExp

    # fix row position labels and input names of all successing rows
    @_getRow(item)
      .nextAll()
        .each((i, tr) =>
          $tr = $(tr)

          idx = index
          $tr.find("td:first-child").text(String(idx + i + 1) + ".")

          type = @_getFieldVal $tr, "type"
          if type is "relativeToPos"
            $tr.find("td.relative-to-pos")
              .find("input")
                .each( ->
                  $this = $(this)
                  $this.val $this.val() - 1
                )
              .end()
              .find("strong")
                .each( ->
                  $this = $(this)
                  $this.text parseInt($this.text(), 10) - 1
                )

          prefix = fieldPrefix
          regexp = re
          $tr.find(":input").each( ->
            parts = @name.match regexp
            @name = "#{prefix}[#{idx + i}].#{parts[2]}" if parts
          )
        )
      .end()
      .remove()
    @_$trs = @_getRows()
    @_updateItems()

  # Sets the value of the input control in the table cell with the given name
  # in the given item.  In case of name `total-price` the text of the
  # `<output>` object is set.  Numeric values are formatted before returned.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @param {String} name        the given name of the table cell
  # @param {String|Number} val  the value of the input control or the text of the `<span>` object to set
  #
  _setFieldVal: (item, name, val) ->
    $field = @_getField item, name
    val = val.format() if (name is "quantity") or (name is "unit-percent")
    val = val.formatCurrencyValue() if (name is "unit-price") or (name is "total-price")

    if name is "total-price"
      $field.text val
    else
      $field.val val

  # Sets the referred item for the given item in the associated table row.  The
  # method displays the index of the referred item and stores the index in the
  # hidden input field in the table row.
  #
  # @param {jQuery|Number} item     either the given zero-based index or the table row representing the referring item
  # @param {jQuery|Number} refItem  either the given zero-based index or the table row representing the referred item
  #
  _setItemReference: (item, refItem) ->
    idx = @_getIndex refItem
    @_getRow(item)
      .find("> .relative-to-pos")
        .find("strong")
          .text(String(idx + 1))
        .end()
        .find("> input")
          .val idx

  # Starts the mode where the user should select a referred item.  The method
  # marks all rows in the table as selectable or non-selectable depending on
  # the following criteria.  The following list of criteria is checked in that
  # order.  The first matching criterion is used.
  #
  # 1.  the current row, that is, the referring row, is always non-selectable
  #     because a row cannot refer itself
  # 2.  rows of type `relativeToPos` are always non-selectable because
  #     transient references are not (yet) implemented
  # 3.  rows of type `absolute` are always selectable because their unit price
  #     and total price can be computed definitely
  # 4.  rows before the current row are always selectable (if not limited by
  #     any criterion above) and rows after the current row are always
  #     non-selectable (if not permitted by any criterion above)
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  # @see                        #_stopFinderMode-mixin _stopFinderMode
  # @see                        #_onClickReferenceItem-mixin _onClickReferenceItem
  #
  _startFinderMode: (item) ->
    $tr = @_getRow item
    @_$finderRow = $tr
    $tr.addClass("non-selectable")
      .prevAll()
        .each((index, tr) =>
          $tr = $(tr)
          $tr.addClass (if @_getRowType($tr) is "relativeToPos" then "non-" else "") + "selectable"
        )
      .end()
      .nextAll()
        .each (index, tr) =>
          $tr = $(tr)
          $tr.addClass (if @_getRowType($tr) is "absolute" then "" else "non-") + "selectable"
    $doc.keydown (event) => @_onKeyDown(event)

  # Stops the mode where the user should select a referred item.
  #
  # @see  #_startFinderMode-mixin _startFinderMode
  #
  _stopFinderMode: ->
    @_$finderRow = null
    @_getRows().removeClass "selectable non-selectable"
    $doc.unbind "keydown"

  # Swaps the indices of the input controls of both the given table rows.
  #
  # @param {jQuery} $tr     the given source table row
  # @param {jQuery} $destTr the given destination table row
  #
  _swapInputItemPos: ($tr, $destTr) ->
    form = @_form
    index = @_getIndex $tr
    destIndex = @_getIndex $destTr
    fieldNames = @INPUT_FIELD_NAMES
    swap = (name, newName) ->
      elems = form.elements
      for fieldName in fieldNames
        el = elems[name + fieldName]
        el.name = newName + fieldName if el
      null

    name1 = @_getInputName(index)
    name2 = @_getInputName(destIndex)
    name3 = @_getInputName(destIndex, "", "-dest")
    swap name1, name3
    swap name2, name1
    swap name3, name2

  # Swaps the position numbers of both the given table rows.
  #
  # @param {jQuery} $tr     the given source table row
  # @param {jQuery} $destTr the given destination table row
  #
  _swapItemPos: ($tr, $destTr) ->
    $td = $tr.find "td:first-child"
    $destTd = $destTr.find "td:first-child"

    s = $td.text()
    $td.text $destTd.text()
    $destTd.text s

  # Swaps the references to both the given items.
  #
  # @param {jQuery|Number} item     either the given zero-based index or the table row representing the one item
  # @param {jQuery|Number} destItem either the given zero-based index or the table row representing the other item
  # @return {jQuery|Number}         the given first item
  #
  _swapItemReferences: (item, destItem) ->
    idx = @_getIndex item
    destIdx = @_getIndex destItem

    refs = @_getReferrers idx
    for ref in refs
      @_setItemReference (if (ref is destIdx) then idx else ref), destIdx
    destRefs = @_getReferrers destIdx
    for ref in destRefs
      @_setItemReference (if (ref is idx) then destIdx else ref), idx
    item

  # Toggles the visibility of the pricing form section.
  #
  _toggleVisibility: ->
    $(".toggle-visibility")
      .filter(".hidden")
        .find(":input")
          .removeAttr("disabled")
        .end()
      .end()
      .toggleClass("hidden")
        .filter(".hidden")
          .find(":input")
            .attr "disabled", "disabled"

  # Updates the computable fields in the given item.
  #
  # @param {jQuery|Number} item either the given zero-based index or the table row representing the item
  #
  _updateItem: (item) ->
    totalPrice = @_computeTotalPrice item
    unitPrice = @_computeUnitPrice item
    @_setFieldVal item, "unit-price", unitPrice unless unitPrice is null
    @_setFieldVal item, "total-price", totalPrice unless totalPrice is null

  # Updates the computable fields of all items and updates the total sum of the
  # pricing table.  Furthermore, the function updates the computable fields in
  # the sales pricing section.
  #
  # @see  #_updateSalesPricing
  #
  _updateItems: ->
    sum = @_getCurrentSum()
    sumText = sum.formatCurrencyValue()
    @_getRows().each (idx, elem) =>
      @_updateItem $(elem)

    $("#step1-total-price").text sumText
    $("#step2-total-price").text sumText
    quantity = $("#step1-pricing-quantity").val().parseNumber()
    s = quantity.format()
    $("#step2-quantity").text s
    $("#step2-total-quantity").text s
    s = $("#step1-pricing-unit option:selected").text()
    $("#step2-unit").text s
    $("#step2-total-unit").text s
    s = (sum / quantity).formatCurrencyValue()
    $("#step1-unit-price").text s
    $("#step2-unit-price").text s
    @_updateSalesPricing()

  # Updates the class names for each row in the pricing table.  If a row is
  # referenced by another one class "not-removable" is added.
  #
  # @see  #_onClick-mixin _onClick
  #
  _updateReferenceClasses: ->
    @_getRows().each (i, elem) =>
      $elem = $(elem)
      referrers = @_getReferrers(i)
      if referrers.length
        $elem.addClass "not-removable"
      else
        $elem.removeClass "not-removable"

  # Updates the computed fields in the sales pricing section.
  #
  _updateSalesPricing: ->
    discountPercent = $("#step2-discount-percent").val().parseNumber()
    totalPrice = $("#step2-total-price").text().parseNumber()
    discountPercentAmount = discountPercent * totalPrice / 100
    $("#step2-discount-percent-amount").text discountPercentAmount.formatCurrencyValue()

    adjustment = $("#step2-adjustment").val().parseNumber()
    totalPrice += adjustment - discountPercentAmount
    s = totalPrice.formatCurrencyValue()
    $("#step2-total").text s
    $("#step3-total-price").text s

    qty = $("#step1-pricing-quantity").val().parseNumber()
    $("#step2-total-unit-price").text if qty is 0 then "---" else (totalPrice / qty).formatCurrencyValue()
    step3Qty = $("#step3-quantity").val().parseNumber()
    $("#step3-unit-price").text if step3Qty is 0 then "---" else (totalPrice / step3Qty).formatCurrencyValue()


$.widget "springcrm.salesitempricing", SalesItemPricing

$("#step1-pricing-items").salesitempricing()
