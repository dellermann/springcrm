#
# sales-item-pricing.coffee
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
#= require _selectize


$ = jQuery


#== Classes =====================================

class SalesItemPricing

  #-- Internal variables ------------------------
  
  $ = jq = jQuery


  #-- Class variables ---------------------------

  @DEFAULTS =
    fieldNamePrefix: 'pricing.items'
    units: $('.price-table').data('units')?.split(',') ? []

  # The names of the input controls of a table row.
  #
  @INPUT_FIELD_NAMES = [
    'quantity', 'unit', 'name', 'type', 'relToPos', 'unitPercent',
    'unitPrice'
  ]


  #-- Constructor -------------------------------

  # Creates a new sales item pricing area within the given element.
  #
  # @param [jQuery] $element    the given element
  # @param [Object] options     any options
  #
  constructor: ($element, options) ->
    $ = jq

    @page = SPRINGCRM.page
    @$element = $element
    @options = options = $.extend {}, SalesItemPricing.DEFAULTS, options
    @_inputRegExp = new RegExp '^' +
      RegExp.escape(options.fieldNamePrefix) + '\\[(\\d+)\\]\\.(\\w+)$'

    @$step1Table = $step1Table = $element.find '#step1-pricing-items'
    @$pricingEnabled = $pricingEnabled = $element.find '#pricing-enabled'

    updateColumns = ['quantity', 'unit-percent', 'unit-price']
    updateSel = (".col-#{c} :input" for c in updateColumns).join ', '
    updateSel += ', #step1-pricing-quantity, #step1-pricing-unit-select'

    $element
      .find('.hidden :input:not(:button)')
        .attr('disabled', 'disabled')
      .end()
      .on('click', '.btn-start-pricing', => @_onClickStartPricing())
      .on('click', '.btn-remove-pricing', => @_onClickRemovePricing())
      .on('click', '.btn-add-pricing-item', => @_addItem true)
      .on('change', updateSel, => @_updateItems())
      .on(
        'change',
        '#step2-discount-percent, #step2-adjustment, #step3-quantity, ' +
          '#step3-unit',
        => @_updateSalesPricing()
      )
      .on('change', '.col-type :input', (event) => @_onChangeItemType event)
    @_registerClickEvents()

    @initialPricingEnabled = initialPricingEnabled = !!$pricingEnabled.val()
    @_toggleVisibility() if initialPricingEnabled

    @_getRows().each (_, tr) => @_initItemCtrls $(tr)
    @_updateReferenceClasses()

    $form = $element.closest 'form'
    @_form = $form[0]

    @units = options.units
    @_initUnitTypeahead() if @_getRows().length

    @_updateItems()


  #-- Non-public methods ------------------------

  # Adds a row for a new item to the pricing table.
  #
  # @param [Boolean] jumpToNewRow `true` if the document is to scroll that the new row is visible; `false` otherwise
  # @private
  #
  _addItem: (jumpToNewRow) ->

    # prepare Handlebars template
    template = @addItemTemplate
    unless template
      template = Handlebars.compile $('#add-pricing-item-template').html()
      @addItemTemplate = template

    index = @_getRows().length
    s = template
      index: index
      pos: index + 1
      zero: (0).formatCurrencyValue()

    $row = $(s)
    @page.initSelect @_getField $row, 'type'
    @_initItemCtrls $row
    @$step1Table.find('> .items')
      .append $row
    @_initUnitTypeahead $row.find '.col-unit input'

    if jumpToNewRow
      $('html').scrollTop(
          $row.position().top - $('.title-toolbar').outerHeight()
        )

    return

  # Computes the total price of the given item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [Number]              the computed total price; `null` if the referred item was not set
  # @private
  #
  _computeTotalPrice: (item) ->
    idx = @_getIndex item
    type = @_getRowType item
    return @_getCurrentSum(idx - 1) if type is 'sum'

    unitPrice = @_computeUnitPrice idx
    if unitPrice is null then null else @_getFieldVal(item, 'quantity') * unitPrice

  # Computes the unit price of the given item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [Number]              the computed unit price; `null` if the type of the pricing item is `sum` or unknown or the referred item was not set
  # @private
  #
  _computeUnitPrice: (item) ->
    idx = @_getIndex item

    unitPrice = null
    switch @_getRowType item
      when 'absolute'
        unitPrice = @_getFieldVal item, 'unit-price'
      when 'relativeToPos'
        otherIndex = @_getFieldVal item, 'relative-to-pos'
        if otherIndex >= 0
          totalPrice = @_computeTotalPrice otherIndex
          unitPrice = (@_getFieldVal(item, 'unit-percent') * totalPrice / 100.0).round(2) if totalPrice isnt null
      when 'relativeToLastSum'
        otherIndex = @_getLastSumIndex idx
        if otherIndex < 0
          unitPrice = (@_getFieldVal(item, 'unit-percent') * @_getCurrentSum(idx - 1) / 100.0).round(2)
        else
          totalPrice = @_computeTotalPrice otherIndex
          unitPrice = (@_getFieldVal(item, 'unit-percent') * totalPrice / 100.0).round(2) if totalPrice isnt null
      when 'relativeToCurrentSum'
        unitPrice = (@_getFieldVal(item, 'unit-percent') * @_getCurrentSum(idx - 1) / 100.0).round(2)

    unitPrice

  # Disables all options of the type selector in the given item which are not
  # available in the current state.  The method disables options if the item
  # is referenced by at least one another item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [jQuery]              the table row representing the item
  # @private
  #
  _disableTypeOptions: (item) ->
    $tr = @_getRow item
    idx = @_getIndex item

    referrers = @_getReferrers idx
    optionsToDisable = []
    optionsToDisable.push 'relativeToPos' if referrers.length

    for referrer in referrers
      if referrer < idx
        optionsToDisable.push 'relativeToLastSum'
        optionsToDisable.push 'relativeToCurrentSum'
        optionsToDisable.push 'sum'
        break

    @_setOptionsToDisable $tr.find('td.col-type select'), optionsToDisable

    $tr

  # Enables all options of the type selector in the given item.  The method is
  # needed after options have been disabled during a reference phase.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [jQuery]              the table row representing the item
  # @private
  #
  _enableTypeOptions: (item) ->
    $tr = @_getRow item
    @_setOptionsToDisable $tr.find('td.col-type select'), []

    $tr

  # Gets the sum of all items' total prices at the given index and before.
  #
  # @param [Number] idx the given zero-based item index; defaults to the last item index
  # @return [Number]    the current sum
  # @private
  #
  _getCurrentSum: (idx) ->
    $ = jq
    $trs = @_getRows()
    idx ?= $trs.length - 1

    sum = 0
    $trs.slice(0, idx + 1)
      .each (i, elem) =>
        sum += @_computeTotalPrice i unless @_getRowType($(elem)) is 'sum'
    sum

  # Gets the input control in the table cell with the given name in the given
  # item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @param [String] name          the given name of the table cell
  # @return [jQuery]              the input control
  # @private
  #
  _getField: (item, name) ->
    @_getRow(item).find "> .col-#{name} :input"

  # Gets the value of the input control in the table cell with the given name
  # in the given item.  Numeric values are parsed before returned.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @param [String] name          the given name of the table cell
  # @return [String, Number]      the value of the input control
  # @private
  #
  _getFieldVal: (item, name) ->
    $field = @_getField item, name
    val = $field.val()

    switch name
      when 'quantity', 'unit-percent', 'unit-price', 'total-price'
        val.parseNumber()
      when 'relative-to-pos'
        if val is '' then -1 else val.parseNumber()
      else val

  # Gets the index of the item with the given table item or index in the table.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [Number]              the zero-based index or -1 if the item was not found
  # @private
  #
  _getIndex: (item) ->
    if $.isNumeric item then item else @_getRows().index item

  # Computes the name of an input field for the given name and item index.
  #
  # @param [Number] index   the zero-based index of the item
  # @param [String] name    the name of the field
  # @param [String] suffix  a suffix which is to append to the field name prefix defined in the options
  # @return [String]        the computed field name
  # @private
  #
  _getInputName: (index, name = '', suffix = '') ->
    "#{@options.fieldNamePrefix}#{suffix}[#{index}].#{name}"

  # Gets the last index of the item of type `SUM`.
  #
  # @param [Number] idx the given zero-based index; defaults to the last item index
  # @return [Number]    the zero-based index of the last subtotal sum; -1 if no such an item exists
  # @private
  #
  _getLastSumIndex: (idx) ->
    $ = jq
    $trs = @_getRows()
    idx ?= $trs.length - 1

    res = -1
    $trs
      .slice(0, idx + 1)
      .reverse()
      .each (i, elem) =>
        if @_getRowType($(elem)) is 'sum'
          res = idx - i
          return false

        true

    res

  # Gets the table row which is referred by the given item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [jQuery]              the referred table row; `null` if no reference is defined
  # @private
  #
  _getReferredRow: (item) ->
    refIdx = @_getFieldVal item, 'relative-to-pos'
    if refIdx is -1 then null else @_getRow refIdx

  # Gets a list of items which refer to the item with the given index.
  #
  # @param [Number] idx the given zero-based index
  # @return [Array]     the zero-based indices of the items referring to the item with the given index
  # @private
  #
  _getReferrers: (idx) ->
    $ = jq

    res = []
    @_getRows().each (i, tr) =>
      $tr = $(tr)
      if @_getFieldVal($tr, 'type') is 'relativeToPos'
        refIdx = @_getFieldVal $tr, 'relative-to-pos'
        res.push i if refIdx is idx

    res

  # Gets the table row of the given item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [jQuery]              the table row
  # @private
  #
  _getRow: (item) ->
    if $.isNumeric item then @_getRows().eq(item) else item

  # Gets all or particular rows of the pricing table.
  #
  # @param [Number] idx the zero-based index up to but not including the rows are to return; defaults to all rows
  # @return [jQuery]    all table rows, optionally up to but not including the given index
  # @private
  #
  _getRows: (idx) ->
    $trs = @$step1Table.find '> .items > tr'
    $trs = $trs.slice 0, idx if idx?

    $trs

  # Gets the type of the given item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @return [String]              the type of the item: `absolute`, `relativeToPos`, `relativeToLastSum`, `relativeToCurrentSum`, or `sum`
  # @private
  #
  _getRowType: (item) -> @_getFieldVal item, 'type'

  # Gets the labels of the selected items.
  #
  # @param [jQuery] $select the given select control
  # @return [Array]         the labels of the selected items
  # @private
  #
  _getSelLabels: ($select) ->
    selectize = $select[0].selectize
    labels = (selectize.getItem(item).text() for item in selectize.items)

    if labels.length is 1 then labels[0] else labels

  # Initializes the given item by enabling or disabling the input controls
  # depending on the item type.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @private
  #
  _initItemCtrls: (item) ->
    type = @_getRowType item

    @_disableTypeOptions item

    notSum = type isnt 'sum'
    notAbs = type isnt 'absolute'
    @_getField(item, 'quantity').toggleEnable notSum
    @_getField(item, 'unit').toggleEnable notSum
    @_getField(item, 'name').toggleEnable notSum
    @_getField(item, 'unit-percent').toggleEnable notAbs and notSum
    @_getField(item, 'unit-price').toggleEnable not notAbs

    return

  # Initialize the autocomplete fields for units.
  #
  # @param [jQuery] $input  a selector representing the input fields that should be initialized; if not defined, all unit input fields are initialized
  # @private
  #
  _initUnitTypeahead: ($input = $('.col-unit > input')) ->
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

  # Called if the type of an item in step 1 of the pricing table has been
  # changed.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangeItemType: (event) ->
    $target = $(event.currentTarget)
    $tr = $target.closest 'tr'

    @_initItemCtrls $tr
    if $target.val() is 'relativeToPos'
      idx = @_getFieldVal $tr, 'relative-to-pos'
      idx = (if (idx < 0) then '' else String(idx + 1) + '.')
      $tr.find('> .col-relative-to-pos > span').removeClass 'hidden'
    else
      $tr.find('> .col-relative-to-pos > span').addClass 'hidden'
    @_updateReferenceClasses()
    @_updateItems()

  # Called if a button to moves a row in the price table up- or downwards has
  # been clicked.
  #
  # @param [Event] event  any event data
  # @param [Number] dir   a negative value moves the row upwards; otherwise it moves it downwards
  # @private
  #
  _onClickMoveItem: (event, dir) ->
    $tr = $(event.currentTarget).closest 'tr'

    checkReferee = ($tr, dir) =>
      if dir < 0 and @_getRowType($tr) is 'relativeToPos'
        $refTr = @_getReferredRow $tr
        unless $refTr is null or @_getRowType($refTr) is 'absolute' or @_getIndex($tr) - 1 > @_getIndex($refTr)
          $.alert $L('salesItem.pricing.error.notMovable.refBeforeReferee')
          return false

      true

    # obtain destination row
    $destTr = if dir < 0 then $tr.prev() else $tr.next()

    # check validation rules
    return unless $destTr.length and checkReferee($tr, dir) and checkReferee($destTr, -dir)

    # swap current row with previous or next row
    if dir < 0 then $destTr.before $tr else $destTr.after $tr

    # swap input name positions, item positions, and references
    @_swapInputItemPos $tr, $destTr
    @_swapItemPos $tr, $destTr
    @_swapItemReferences $tr, $destTr

    # fix type options after swapping
    @_disableTypeOptions $tr
    @_disableTypeOptions $destTr

    # update all values
    @_updateItems()

    return

  # Called if the user is in "find reference item" mode and has clicked the
  # reference item.  If the clicked row has the CSS class `selectable` a
  # reference to it is stored in the finder row as defined in
  # `this.$finderRow`.  In each case, at last, the finder mode is deactivated.
  #
  # @param [Event] event  any event data
  # @see                  #_startFinderMode
  # @see                  #_stopFinderMode
  # @private
  #
  _onClickReferenceItem: (event) ->
    $tr = $(event.currentTarget)
    $finderRow = @$finderRow

    idx = @_getIndex $finderRow
    refIdx = @_getIndex $tr
    oldIdx = @_getFieldVal $finderRow, 'relative-to-pos'

    @_enableTypeOptions oldIdx unless oldIdx is -1
    @_setItemReference idx, refIdx
    @_disableTypeOptions $tr
    @_updateReferenceClasses()
    @_updateItems()
    @_stopFinderMode()

  # Called if the button to remove an item in pricing table in step 1 has been
  # clicked.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickRemoveItem: (event) ->
    $ = jq

    $target = $(event.currentTarget)
    $tr = $target.closest 'tr'
    fieldPrefix = @options.fieldNamePrefix
    index = @_getIndex $tr
    re = @_inputRegExp

    # fix row position labels and input names of all successing rows
    $tr
      .nextAll()
        .each((i, tr) =>
          $tr = $(tr)

          idx = index
          $tr.find('td:first-child > span')
            .text "#{idx + i + 1}."

          type = @_getFieldVal $tr, 'type'
          if type is 'relativeToPos'
            $tr.find('td.col-relative-to-pos')
              .find('input')
                .each( ->
                  $this = $(this)
                  val = $this.val()
                  $this.val val - 1 if val >= index
                )
              .end()
              .find('strong')
                .each( ->
                  $this = $(this)
                  val = parseInt($this.text(), 10)
                  $this.text val - 1 if val >= index + 1
                )

          prefix = fieldPrefix
          regexp = re
          $tr.find(':input').each( ->
            parts = @name.match regexp
            @name = "#{prefix}[#{idx + i}].#{parts[2]}" if parts
          )
        )
      .end()
      .remove()

    @_updateItems()

    return

  # Called when the button to remove pricing has been clicked.
  #
  # @private
  # @since  1.3
  #
  _onClickRemovePricing: ->
    if @_initialPricingEnabled
      ok = $.confirm $L('salesItem.pricing.removePricing.confirm')
      return false unless ok

    @$pricingEnabled.val ''
    @_toggleVisibility()

    $('#quantity').val $('#step3-quantity').val()
    $('#unit').val $('#step3-unit').val()
    val = $('#step3-unit-price').val().parseNumber()
    $('#unitPrice').val val.formatCurrencyValue() unless isNaN val

    return

  # Called when the button to start pricing has been clicked.
  #
  # @private
  # @since  1.3
  #
  _onClickStartPricing: ->
    @$pricingEnabled.val '1'
    @_toggleVisibility()
    @_addItem false

    return

  # Called if a key has been pressed.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onKeyDown: (event) ->
    # Esc
    @_stopFinderMode() if @$finderRow and (event.which is 27)

  # Registers click events for this widget.
  #
  # @return [jQuery]  the element representing this widget
  # @private
  #
  _registerClickEvents: ->
    @$element
      .on('click', '.up-btn', (event) => @_onClickMoveItem event, -1)
      .on('click', '.down-btn', (event) => @_onClickMoveItem event, 1)
      .on('click', 'tr:not(.row-non-removable) .remove-btn', (event) =>
        @_onClickRemoveItem event
      )
      .on('click', '.col-relative-to-pos i', (event) =>
        @_startFinderMode $(event.currentTarget)
        false
      )

  # Sets the value of the input control in the table cell with the given name
  # in the given item.  Numeric values are formatted before returned.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @param [String] name          the given name of the table cell
  # @param [String, Number] val   the value of the input control which should be set
  # @private
  #
  _setFieldVal: (item, name, val) ->
    $field = @_getField item, name
    $field.val switch name
      when 'quantity', 'unit-percent' then val.format()
      when 'unit-price', 'total-price' then val.formatCurrencyValue()

  # Sets the referred item for the given item in the associated table row.  The
  # method displays the index of the referred item and stores the index in the
  # hidden input field in the table row.
  #
  # @param [jQuery, Number] item    either the given zero-based index or the table row representing the referring item
  # @param [jQuery, Number] refItem either the given zero-based index or the table row representing the referred item
  # @private
  #
  _setItemReference: (item, refItem) ->
    idx = @_getIndex refItem
    @_getRow(item)
      .find('> .col-relative-to-pos')
        .find('strong')
          .text(String(idx + 1))
        .end()
        .find('> input')
          .val idx

  # Sets the given options in the stated selectize control as disabled.
  #
  # @param [jQuery] $select the given selectize control
  # @param [Array] options  the options to mark as disabled
  # @private
  # @since 2.0
  #
  _setOptionsToDisable: ($select, options) ->
    $select[0].selectize
      .plugins
      .settings
      .disable_options
      .disableOptions = options

    return

  # Starts the mode where the user should select a referred item.  The method
  # marks all rows in the table as selectable or non-selectable depending on
  # the following criteria.  The following list of criteria is checked in that
  # order.  The first matching criterion is used.
  #
  # 1.  the current row, that is, the referring row, is always non-selectable
  #     because a row cannot refer to itself
  # 2.  rows of type `relativeToPos` are always non-selectable because
  #     transient references are not (yet) implemented
  # 3.  rows of type `absolute` are always selectable because their unit price
  #     and total price can be computed definitely
  # 4.  rows before the current row are always selectable (if not limited by
  #     any criterion above) and rows after the current row are always
  #     non-selectable (if not permitted by any criterion above)
  #
  # @param [jQuery] $icon the symbol which was clicked to start finder mode
  # @see                  #_stopFinderMode
  # @see                  #_onClickReferenceItem
  # @private
  #
  _startFinderMode: ($icon) ->
    $tr = $icon.closest 'tr'

    @$finderRow = $tr
    $tr.addClass('row-non-selectable')
      .prevAll()
        .each((_, tr) =>
          $tr = $(tr)
          $tr.addClass(
            'row-' +
            (if @_getRowType($tr) is 'relativeToPos' then 'non-' else '') +
            'selectable'
          )
        )
      .end()
      .nextAll()
        .each((_, tr) =>
          $tr = $(tr)
          $tr.addClass(
            'row-' +
            (if @_getRowType($tr) is 'absolute' then '' else 'non-') +
            'selectable'
          )
        )
    @$element
      .off('click', '**')
      .on(
        'click', 'tr.row-selectable', (event) => @_onClickReferenceItem event
      )
    $(window.document).on 'keydown', (event) => @_onKeyDown event

  # Stops the mode where the user should select a referred item.
  #
  # @see  #_startFinderMode
  # @private
  #
  _stopFinderMode: ->
    @$finderRow = null
    @_getRows().removeClass 'row-selectable row-non-selectable'
    @$element.off 'click', '**'
    @_registerClickEvents()
    $(window.document).off 'keydown'

  # Swaps the indices of the input controls of both the given table rows.
  #
  # @param [jQuery] $tr     the given source table row
  # @param [jQuery] $destTr the given destination table row
  # @private
  #
  _swapInputItemPos: ($tr, $destTr) ->
    form = @_form

    index = @_getIndex $tr
    destIndex = @_getIndex $destTr

    fieldNames = SalesItemPricing.INPUT_FIELD_NAMES
    swap = (name, newName) ->
      elems = form.elements
      for fieldName in fieldNames
        el = elems[name + fieldName]
        el.name = newName + fieldName if el

      null

    name1 = @_getInputName index
    name2 = @_getInputName destIndex
    name3 = @_getInputName destIndex, '', '-dest'
    swap name1, name3
    swap name2, name1
    swap name3, name2

    return

  # Swaps the position numbers of both the given table rows.
  #
  # @param [jQuery] $tr     the given source table row
  # @param [jQuery] $destTr the given destination table row
  # @private
  #
  _swapItemPos: ($tr, $destTr) ->
    $td = $tr.find '.col-pos span'
    $destTd = $destTr.find '.col-pos span'

    s = $td.text()
    $td.text $destTd.text()
    $destTd.text s

    return

  # Swaps the references to both the given items.
  #
  # @param [jQuery, Number] item     either the given zero-based index or the table row representing the one item
  # @param [jQuery, Number] destItem either the given zero-based index or the table row representing the other item
  # @private
  #
  _swapItemReferences: (item, destItem) ->
    idx = @_getIndex item
    destIdx = @_getIndex destItem

    refs = @_getReferrers idx
    for ref in refs
      @_setItemReference (if ref is destIdx then idx else ref), destIdx
    destRefs = @_getReferrers destIdx
    for ref in destRefs
      if refs.indexOf(ref) is -1
        @_setItemReference (if ref is idx then destIdx else ref), idx

    return

  # Toggles the visibility of the pricing form section.
  #
  # @private
  #
  _toggleVisibility: ->
    $('.toggle-visibility')
      .filter('.hidden')
        .find(':input')
          .not('.disabled-always')
            .removeAttr('disabled')
          .end()
        .end()
      .end()
      .toggleClass('hidden')
      .filter('.hidden')
        .find(':input')
          .not(':button')
            .attr 'disabled', 'disabled'

  # Updates the computable fields in the given item.
  #
  # @param [jQuery, Number] item  either the given zero-based index or the table row representing the item
  # @private
  #
  _updateItem: (item) ->
    totalPrice = @_computeTotalPrice item
    unitPrice = @_computeUnitPrice item
    @_setFieldVal item, 'unit-price', unitPrice unless unitPrice is null
    @_setFieldVal item, 'total-price', totalPrice unless totalPrice is null

    return

  # Updates the computable fields of all items and updates the total sum of the
  # pricing table.  Furthermore, the function updates the computable fields in
  # the sales pricing section.
  #
  # @see  #_updateSalesPricing
  # @private
  #
  _updateItems: ->
    $ = jq

    sum = @_getCurrentSum()
    @_getRows().each (_, elem) => @_updateItem $(elem)

    $('#step1-total-price, #step2-total-price').val sum.formatCurrencyValue()
    quantity = $('#step1-pricing-quantity').val().parseNumber()
    quantityFmt = quantity.format()
    $('#step2-quantity, #step2-total-quantity').val quantityFmt
    unit = @_getSelLabels $('#step1-pricing-unit-select')
    $('#step2-unit, #step2-total-unit').val unit
    if quantity and unit.length
      $('#step1-total-price-quantity').text "(#{quantityFmt} #{unit})"
      $('#step1-unit-price-quantity').text "(1 #{unit})"
    else
      $('#step1-total-price-quantity').text ''
      $('#step1-unit-price-quantity').text ''
    unitPrice = sum / quantity
    $('#step1-unit-price, #step2-unit-price').val(
      if isNaN unitPrice then '---' else unitPrice.formatCurrencyValue()
    )

    @_updateSalesPricing()

    return

  # Updates the class names for each row in the pricing table.  If a row is
  # referenced by another one the class "row-non-removable" is added and the
  # title of the remove button is changed to explain the situation.
  #
  # @see  #_onClick
  # @private
  #
  _updateReferenceClasses: ->
    $ = jq

    @_getRows().each (i, tr) =>
      $tr = $(tr)
      referrers = @_getReferrers i
      if referrers.length
        $tr.addClass('row-non-removable')
          .find('.remove-btn')
            .attr 'title', $L('salesItem.pricing.error.notRemovable')
      else
        $tr.removeClass('row-non-removable')
          .find('.remove-btn')
            .attr 'title', $L('default.btn.remove')

    return

  # Updates the computed fields in the sales pricing section.
  #
  # @private
  #
  _updateSalesPricing: ->
    $ = jq

    discountPercent = $('#step2-discount-percent').val().parseNumber()
    step2TotalPrice = $('#step2-total-price').val().parseNumber()
    discountPercentAmount = discountPercent * step2TotalPrice / 100
    $('#step2-discount-percent-amount').val discountPercentAmount.formatCurrencyValue()

    adjustment = $('#step2-adjustment').val().parseNumber()
    step2TotalPrice += adjustment - discountPercentAmount
    step2TotalPriceFmt = step2TotalPrice.formatCurrencyValue()
    $('#step2-total').val step2TotalPriceFmt

    step1Qty = $('#step1-pricing-quantity').val().parseNumber()
    step1Unit = $('#step1-pricing-unit option:selected').val()
    step3Qty = $('#step3-quantity').val().parseNumber()
    step3Unit = $('#step3-unit option:selected').val()

    step2TotalUnitPrice = step2TotalPrice / step1Qty
    $('#step2-total-unit-price').val step2TotalUnitPrice.formatCurrencyValue()
    qty = if step1Unit is step3Unit then step1Qty else step3Qty
    step3UnitPrice = step2TotalPrice / qty
    $('#step3-unit-price').val step3UnitPrice.formatCurrencyValue()
    step3TotalPrice = step3UnitPrice * step3Qty
    $('#step3-total-price').val step3TotalPrice.formatCurrencyValue()

    return


#== Main ========================================

new SalesItemPricing $('.sales-item-pricing')

