#
# purchase-invoice-form.coffee
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
#= require invoicing-transaction-form
#


$ = jQuery


PurchaseInvoiceWidget =
  options:
    loadVendorsUrl: null

  _create: ->
    $ = jQuery
    $.springcrm.invoicingtransaction::_create.call this
    @element.find(".price-table")
      .invoicingitems "option",
        productListUrl: null
        serviceListUrl: null

    $("#vendorName").autocomplete
      focus: @_onFocusVendor
      select: @_onSelectVendor
      source: (request, response) => @_onLoadVendors request, response

    $(".document-delete").on "click", ->
      $ = jQuery
      $("#fileRemove").val 1
      $(".document-preview").add(".document-preview-links").remove()

  _onFocusVendor: (event, ui) ->
    $(event.target).val ui.item.label
    false

  _onLoadVendors: (request, response) ->
    $ = jQuery
    url = @options.loadVendorsUrl
    if url
      $.getJSON url,
        name: request.term
      , (data) ->
        response $.map(data, (item) ->
          label: item.name
          value: item.id
        )

  _onSelectVendor: (event, ui) ->
    $ = jQuery
    item = ui.item
    $("#vendor").val item.label
    $("#vendor\\.id").val item.value
    false

$.widget "springcrm.purchaseinvoice", $.springcrm.invoicingtransaction,
  PurchaseInvoiceWidget
