#
# purchase-invoice-form.coffee
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
#= require _document-file-input
#= require _typeahead
#= require invoicing-transaction-form


#== Classes =====================================

# Class `PurchaseInvoice` represents a purchase invoice form.
#
# @author   Daniel Ellermann
# @version  3.0
#
class PurchaseInvoice

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new widget which handles the actions within a purchase invoice
  # form.
  #
  # @param [jQuery] $element  the element containing the form
  # @param [Object] [options] any options
  #
  constructor: ($element, options = {}) ->
    $ = __jq

    @$element = $element

    @_initVendorTypeahead()
    $('#file').each -> new SPRINGCRM.DocumentFileInput $(this),
      removeLabelKey: 'purchaseInvoice.documentFile.delete'

    new SPRINGCRM.InvoicingTransaction $element, options


  #-- Non-public methods ------------------------

  # Initialize the typeahead field for vendors.
  #
  # @private
  #
  _initVendorTypeahead: ->
    $vendor = $('#vendor')
    $vendorName = $('#vendorName')

    vendors = new Bloodhound
      datumTokenizer: (datum) -> datum.name
      queryTokenizer: Bloodhound.tokenizers.whitespace
      remote:
        prepare: (query, settings) ->
          url = new HttpUrl(settings.url)
          url.query.name = query
          settings.url = url.toString()

          settings
        url: $vendorName.data 'load-url'
    vendors.initialize()

    $vendorName.typeahead(
          highlight: true
          hint: true
          minLength: 1
        ,
          displayKey: 'name'
          name: 'vendor'
          source: vendors.ttAdapter()
      )
      .on('typeahead:selected', (event, suggestion) ->
        $vendor.val suggestion.id
      )

    return


SPRINGCRM.PurchaseInvoice = PurchaseInvoice
