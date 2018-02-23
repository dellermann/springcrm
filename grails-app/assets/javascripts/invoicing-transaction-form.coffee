#
# invoicing-transaction-form.coffee
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
#= require widgets/addr-fields
#= require selectize/selectize
#= require _invoicing-items


#== Classes =====================================

# Class `InvoicingTransaction` contains the scripting needed for invoicing
# transaction forms.
#
# @author   Daniel Ellermann
# @version  3.0
#
class InvoicingTransaction

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  $LANG = $L


  #-- Class variables ---------------------------

  @DEFAULTS =
    checkStageTransition: true
    handleInvoiceDunningChange: false
    organizationId: '#organization-select'
    reducedView: false
    stageValues: null


  #-- Constructor -------------------------------

  # Creates a new widget which handles the actions within an invoicing
  # transaction form.
  #
  # @param [jQuery] $element  the element containing the form
  # @param [Object] [options] any options
  #
  constructor: ($element, options = {}) ->
    $ = __jq

    @$element = $element
    @$paymentAmount = $('#paymentAmount')
    @$stillUnpaid = $('#still-unpaid')
    @options = opts = $.extend {}, InvoicingTransaction.DEFAULTS, options

    unless options.reducedView
      new SPRINGCRM.InvoicingItems($element.find '.price-table')

    $element
      .on('click', '#still-unpaid', (event) => @_onClickStillUnpaid event)
      .on('change', '#paymentAmount', => @_onChangePaymentAmount())
      .find('#paymentAmount')
        .trigger('change')
      .end()

    @_initStageValues()
    if opts.handleInvoiceDunningChange
      $element.on 'change', '#invoice-select, #dunning-select', (event) =>
        @_onChangeInvoiceDunning event

    organizationId = $(opts.organizationId)
      .on('change', (event) => @_onSelectOrganization event)
      .val()
    @_updateDueDate organizationId
    $('.invoicing-transaction-selector.selectized').each (_, elem) =>
      @_initInvoicingTransactionSelector elem
    @_initAddrFields()


  #-- Non-public methods ------------------------

  # Changes the stage to the given value if the current value is below.
  #
  # @param [Number] value the value that should be set
  # @private
  # @since 2.0
  #
  _changeStage: (value) ->
    $stage = @$element.find '#stage-select'
    $stage[0].selectize.addItem value if parseInt($stage.val(), 10) < value

  # Gets the modified closing balance of this invoicing transaction used as
  # base for dynamic balance computation.
  #
  # @param [jQuery] $btn  the link which contains the modified closing balance as data attribute
  # @return [Number]      the modified closing balance
  # @private
  #
  _getModifiedClosingBalance: ($btn = @$stillUnpaid) ->
    parseFloat $btn.data 'modified-closing-balance'

  # Gets the total value of this invoicing transaction.
  #
  # @return [Number]  the total value
  # @private
  #
  _getTotal: ->
    $t = $('#total-price')
    (if $t.is 'output' then $t.text() else $t.val()).parseNumber()

  # Gets the unpaid amount of this invoicing transaction considering the
  # already paid amount and any credit memos.
  #
  # @return [Number]  the unpaid amount
  # @private
  #
  _getUnpaid: ->
    paymentAmount = @$paymentAmount.val().parseNumber()
    unpaid = @_getTotal() - paymentAmount - @_getModifiedClosingBalance()
    unpaid.round $I.numFractionsExt

  # Initializes the address fields of this form.
  #
  # @private
  #
  _initAddrFields: ->
    $L = $LANG

    @$addresses = $('.addresses').addrfields
      menuItems:
        left: [
            action: 'clear'
            text: $L('invoicingTransaction.billingAddr.clear')
          ,
            action: 'copy'
            text: $L('invoicingTransaction.billingAddr.copy')
          ,
            action: 'loadFromOrganization'
            prefix: 'billingAddr'
            text: $L('invoicingTransaction.addr.fromOrgBillingAddr')
        ]
        right: [
            action: 'clear'
            text: $L('invoicingTransaction.shippingAddr.clear')
          ,
            action: 'copy'
            text: $L('invoicingTransaction.shippingAddr.copy')
          ,
            action: 'loadFromOrganization'
            prefix: 'shippingAddr'
            text: $L('invoicingTransaction.addr.fromOrgShippingAddr')
        ]
      organizationId: @options.organizationId

    return

  # Initializes the selectors for dependent invoicing transactions which
  # display the transaction number in a special way.
  #
  # @param [Element] selector the selector that should be initialized
  # @private
  #
  _initInvoicingTransactionSelector: (selector) ->
    $ = __jq

    selectize = selector.selectize
    $.extend true, selectize.settings,
      labelField: 'fullName'
      searchField: ['name', 'number']
      sortField:
        direction: 'desc'
        field: 'number'
      render:
        item: (data, escape) ->
          """
<div class="item">
  <span class="number">#{escape(data.number)}</span>
  #{escape(data.name)}
</div>
"""
        option: (data, escape) ->
          """
<div class="option">
  <span class="number">#{escape(data.number)}</span>
  #{escape(data.name)}
</div>
"""

    item = $(selector).data 'value'
    if item
      selectize.addOption item
      selectize.addItem item.id

    return

  # Initializes the input fields according to the stage values defined in the
  # options.
  #
  # @private
  #
  _initStageValues: ->
    $el = @$element
    opts = @options
    stageValues = opts.stageValues

    if stageValues
      $el.on 'change', '#stage-select', (event) => @_onChangeStage event
      if stageValues.shipping
        $el.on 'change', '#shippingDate-date', (event) =>
          @_onChangeShippingDate event
        if opts.checkStageTransition
          $el.on 'submit', (event) => @_onSubmitForm event
      if stageValues.payment
        $el.on 'change', '#paymentDate-date', (event) =>
          @_onChangePaymentDate event

    return

  # Called if the invoice or dunning has been changed.  The method recomputes
  # the closing balance of a credit memo.
  #
  # @param [Event] event  any event data
  # @return [Promise]     the status of obtaining the closing balance
  # @private
  # @since                2.0
  #
  _onChangeInvoiceDunning: (event) ->
    $ = __jq
    $target = $(event.currentTarget)

    $.ajax(
        context: this
        data:
          id: $target.val()
        dataType: 'json'
        url: $target.data 'get-closing-balance-url'
      )
      .done (data) ->
        $ = __jq

        @$element
          .find('#still-unpaid')
            .each(->
              $this = $(this)
              initialBalance = parseFloat $this.data 'initial-balance'
              val = initialBalance - data.closingBalance
              $this.data 'modified-closing-balance', val
            )
          .end()
          .find('#paymentAmount')
            .trigger('change')

  # Called if the payment amount has been changed.  The method sets a CSS class
  # to the unpaid amount link in order to indicate the payment status (unpaid,
  # paid too much, paid).
  #
  # @private
  #
  _onChangePaymentAmount: ->
    val = @_getUnpaid()
    cls = switch
      when val > 0 then 'still-unpaid-unpaid'
      when val < 0 then 'still-unpaid-too-much'
      else 'still-unpaid-paid'

    @$stillUnpaid
      .removeClass(
        'still-unpaid-unpaid still-unpaid-paid still-unpaid-too-much'
      )
      .addClass(cls)
      .find('output')
        .text(val.formatCurrencyValueExt())

    return

  # Called if the payment date has been changed.  The method also changes the
  # stage of the invoicing transaction to "payment" if it is below "payment".
  #
  # @param [Event] event  the event data
  # @private
  #
  _onChangePaymentDate: (event) ->
    if $(event.currentTarget).val() isnt ''
      @_changeStage @options.stageValues.payment

  # Called if the shipping date has been changed.  The method also changes the
  # stage of the invoicing transaction to "shipping" if it is below "shipping".
  #
  # @param [Event] event  the event data
  # @private
  #
  _onChangeShippingDate: (event) ->
    if $(event.currentTarget).val() isnt ''
      @_changeStage @options.stageValues.shipping

  # Called if the user changes the stage of the invoicing transaction.  The
  # method populates the shipping date or payment date field according to the
  # selection.
  #
  # @param [Event] event  the event data
  # @return [Boolean]     `true` to allow event bubbling
  # @private
  #
  _onChangeStage: (event) ->
    $ = __jq

    stageValues = @options.stageValues
    $input = switch parseInt $(event.currentTarget).val(), 10
      when stageValues.shipping then $('#shippingDate-date')
      when stageValues.payment then $('#paymentDate-date')
      else null
    $input.datepicker 'update', new Date() if $input and not $input.val()

    true

  # Called if the link "still unpaid" has been clicked.  The method sets the
  # payment amount to the balanced value indicating that the invoicing
  # transaction has been paid.  The method considers credit memos.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickStillUnpaid: (event) ->
    if @_getUnpaid() > 0
      d = $I.numFractionsExt
      total = @_getTotal().round d
      bal = @_getModifiedClosingBalance($(event.currentTarget)).round d
      val = total - bal
      @$paymentAmount.val(val.formatCurrencyValueExt()).trigger 'change' if val

    return

  # Called if an organization has been selected.  The method fills in the
  # address fields with the data of the selected organization.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onSelectOrganization: (event) ->
    @$addresses.addrfields 'loadFromOrganization'
    organization = $(event.currentTarget).val()
    @_updateDueDate organization if organization

    return

  # Called if the invoicing transaction form is submitted.  The method checks
  # whether there is a stage transition and asks the user for confirmation if
  # the stages changes to a value equal to or above "shipping".  This is
  # necessary because the application prevents access to shipped invoicing
  # transactions for non-admin users.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     whether or not the form should be submitted
  # @private
  #
  _onSubmitForm: (event) ->
    $form = $(event.currentTarget)

    $oldStage = $('#stage-old')
    if $oldStage.length
      oldVal = parseInt $oldStage.val(), 10
      if oldVal > 0
        newVal = $form.find('#stage-select').val()
        shippingStageValue = @options.stageValues.shipping
        if (oldVal < shippingStageValue) and (newVal >= shippingStageValue)
          $.confirm($LANG('invoicingTransaction.changeState.label'))
            .done ->
              $form.off('submit')
                .submit()
          return false

    true

  # Updates the due date if the organization changes.  The method obtains the
  # term of payment and computes the due date.
  #
  # @param [Number] organizationId  the ID of the organization
  # @private
  # @since 2.0
  #
  _updateDueDate: (organizationId) ->
    $ = __jq

    $dueDate = $('#dueDatePayment-date')
    return unless $dueDate.length

    url = $dueDate.parent().data 'get-organization-url'
    $.getJSON(url, id: organizationId)
      .done((data) ->
        termOfPayment = data.termOfPayment
        unless termOfPayment is null
          d = new Date(Date.now() + termOfPayment * 86400000)
          $dueDate.val d.format 'date' if $dueDate.val() is ''

        return
      )

    return

SPRINGCRM.InvoicingTransaction = InvoicingTransaction
