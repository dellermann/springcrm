#
# invoicing-transaction-form.coffee
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
#= require invoicing-items


$ = jQuery


# Defines a jQuery widget which handles invoicing transactions such as quotes,
# invoices, dunnings etc.
#
# @mixin
# @author   Daniel Ellermann
# @version  1.4
#
InvoicingTransactionWidget =

  options:
    checkStageTransition: true
    organizationId: "#organization\\.id"
    stageValues: null

  # Gets the ID of the selected organization.
  #
  # @return {Number}  the ID of the selected organization
  #
  getOrganizationId: ->
    organization: $(@options.organizationId).val()

  # Refreshes the modified closing balance after changing the invoice or
  # dunning associated with a credit memo.  The method obtains the closing
  # balance of the invoice or dunning and re-computes the "still unpaid" value.
  #
  # @param {Object} data      any data of the changed invoice or dunning
  # @option {Number} data id  the ID of the selected invoice or dunning
  # @option {String} data url the URL used to obtain the closing balance from the invoice or dunning
  #
  refreshModifiedClosingBalance: (data) ->
    $.getJSON data.url, { id: data.id }, (d) =>
      @element
        .find("#still-unpaid")
          .data("modified-closing-balance", -d.closingBalance)
        .end()
        .find("#paymentAmount")
          .trigger("change")

  # Initializes this widget.
  #
  _create: ->
    @element
      .on("click", "#still-unpaid", (event) => @_onClickStillUnpaid(event))
      .on("change", "#paymentAmount", => @_onChangePaymentAmount())
      .find(".price-table")
        .invoicingitems()
      .end()
      .find("#organization")
        .autocompleteex(
          select: => @_onSelectOrganization()
        )
      .end()
      .find("#person")
        .autocompleteex(
          loadParameters: => @getOrganizationId()
        )
      .end()
      .find("#paymentAmount")
        .trigger("change")
      .end()
      .find("#addresses")
        .addrfields(
          leftPrefix: "billingAddr"
          menuItems: [
            action: "clear"
            side: "left"
            text: $L("invoicingTransaction.billingAddr.clear")
          ,
            action: "copy"
            side: "left"
            text: $L("invoicingTransaction.billingAddr.copy")
          ,
            action: "loadFromOrganization"
            propPrefix: "billingAddr"
            side: "left"
            text: $L("invoicingTransaction.addr.fromOrgBillingAddr")
          ,
            action: "clear"
            side: "right"
            text: $L("invoicingTransaction.shippingAddr.clear")
          ,
            action: "copy"
            side: "right"
            text: $L("invoicingTransaction.shippingAddr.copy")
          ,
            action: "loadFromOrganization"
            propPrefix: "shippingAddr"
            side: "right"
            text: $L("invoicingTransaction.addr.fromOrgShippingAddr")
          ]
          organizationId: @options.organizationId
          rightPrefix: "shippingAddr"
        )
    @_initStageValues()

  # Gets the modified closing balance of this invoicing transaction used as
  # base for dynamic balance computation.
  #
  # @param {jQuery} $a  the link which contains the modified closing balance as data attribute
  # @return {Number}    the modified closing balance
  #
  _getModifiedClosingBalance: ($a = @element.find("#still-unpaid")) ->
    parseFloat $a.data("modified-closing-balance")

  # Gets the total value of this invoicing transaction.
  #
  # @return {Number}  the total value
  #
  _getTotal: ->
    $("#total-price").text().parseNumber()

  # Gets the unpaid amount of this invoicing transaction considering the
  # already paid amount and any credit memos.
  #
  # @return {Number}  the unpaid amount
  #
  _getUnpaid: ->
    paymentAmount = @element.find("#paymentAmount").val().parseNumber()
    unpaid = @_getTotal() - paymentAmount - @_getModifiedClosingBalance()
    unpaid.round $I.numFractionsExt

  # Initializes the input fields according to the stage values defined in the
  # options.
  #
  # @return {jQuery}  this widget object
  #
  _initStageValues: ->
    $ = jQuery
    el = @element

    opts = @options
    stageValues = opts.stageValues
    if stageValues
      el.on("change", "#stage", (event) => @_onChangeStage event)
      if stageValues.shipping
        el.on(
            "change", "#shippingDate-date",
            (event) => @_onChangeShippingDate event
          )
        el.on("submit", => @_onSubmitForm()) if opts.checkStageTransition
      if stageValues.payment
        el.on(
            "change", "#paymentDate-date",
            (event) => @_onChangePaymentDate event
          )
    this

  # Called if the payment amount is changed.  The method sets a CSS class to
  # the unpaid amount link in order to indicate the payment status (unpaid,
  # paid too much, paid).
  #
  _onChangePaymentAmount: ->
    val = @_getUnpaid()
    cls = switch
      when val > 0 then "still-unpaid-unpaid"
      when val < 0 then "still-unpaid-too-much"
      else "still-unpaid-paid"

    @element.find("#still-unpaid")
      .removeClass("still-unpaid-unpaid still-unpaid-paid still-unpaid-too-much")
      .addClass(cls)
      .find("output")
        .text(val.formatCurrencyValueExt())

  # Called if the payment date is changed.  The method also changes the stage
  # of the invoicing transaction to "payment" if it is below "payment".
  #
  # @param {Object} event the event data
  #
  _onChangePaymentDate: (event) ->
    $stage = @element.find("#stage")
    v = @options.stageValues.payment
    $stage.val v if $(event.currentTarget).val() isnt "" and $stage.val() < v

  # Called if the shipping date is changed.  The method also changes the stage
  # of the invoicing transaction to "shipping" if it is below "shipping".
  #
  # @param {Object} event the event data
  #
  _onChangeShippingDate: (event) ->
    $stage = @element.find("#stage")
    v = @options.stageValues.shipping
    $stage.val v if $(event.currentTarget).val() isnt "" and $stage.val() < v

  # Called if the user changes the stage of the invoicing transaction.  The
  # method populates the shipping date or payment date field according to the
  # selection.
  #
  # @param {Object} event the event data
  # @return {Boolean}     `true` to allow event bubbling
  #
  _onChangeStage: (event) ->
    $ = jQuery

    stageValues = @options.stageValues
    $input = null
    switch parseInt $(event.currentTarget).val(), 10
      when stageValues.shipping then $input = $("#shippingDate-date")
      when stageValues.payment then $input = $("#paymentDate-date")
    $input.datepicker("setDate", new Date()).trigger("change") if $input
    true

  # Called if the user clicks the link "still unpaid".  The method sets the
  # payment amount to the balanced value indicating that the invoicing
  # transaction has been paid.  The method considers credit memos.
  #
  # @param {Object} event the event data
  # @return {Boolean}     always `false` to prevent event bubbling
  #
  _onClickStillUnpaid: (event) ->
    if @_getUnpaid() > 0
      d = $I.numFractionsExt
      total = @_getTotal().round(d)
      bal = @_getModifiedClosingBalance($(event.currentTarget)).round(d)
      val = total - bal
      if val
        @element.find("#paymentAmount")
          .val(val.formatCurrencyValueExt())
          .trigger("change")
    false

  # Called if the user selects an organization.  The method fills in the
  # address fields with the data of the selected organization.
  #
  _onSelectOrganization: ->
    @element.find("#addresses")
      .addrfields("loadFromOrganizationToLeft", "billingAddr")
      .addrfields("loadFromOrganizationToRight", "shippingAddr")

  # Called if the invoicing transaction form is submitted.  The method checks
  # whether there is a stage transition and asks the user for confirmation if
  # the stages changes to a value equal to or above "shipping".  This is
  # necessary because the application prevents access to shipped invoicing
  # transactions for non-admin users.
  #
  # @return {Boolean} `true` if the form should be submitted; `false` otherwise
  #
  _onSubmitForm: ->
    res = true
    $oldStage = $("#old-stage")
    if $oldStage.length
      oldVal = $oldStage.val()
      if oldVal > 0
        newVal = @element.find("#stage").val()
        shippingStageValue = @options.stageValues.shipping
        if (oldVal < shippingStageValue) and (newVal >= shippingStageValue)
          res = $.confirm $L("invoicingTransaction.changeState.label")
    res

$.widget "springcrm.invoicingtransaction", InvoicingTransactionWidget
