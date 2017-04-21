#
# sales-order-form.coffee
#
# Copyright (c) 2011-2017, Daniel Ellermann
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
#= require _fileinput-builder
#= require _handlebars-ext
#= require templates/widgets/file-upload-document
#= require invoicing-transaction-form


#== Classes =====================================

# Class `DocumentFileinput` represents a file input widget for setting the
# document of a sales order.
#
# @author   Daniel Ellermann
# @version  2.2
#
class DocumentFileinput

  #-- Internal variables ------------------------

  # @nodoc
  $ = jQuery

  # @nodoc
  $LANG = $L


  #-- Constructor -------------------------------

  # Creates a new file input widget for setting the document of a purchase
  # invoice.
  #
  # @param [jQuery] $element  the file input control which is augmented as widget
  #
  constructor: ($element) ->
    $L = $LANG
    tmpl = Handlebars.templates['widgets/file-upload-document']

    builder = new SPRINGCRM.FileinputBuilder
      browseIcon: '<i class="fa fa-file-o"></i> '
      browseLabel: $L('purchaseInvoice.documentFile.select')
      layoutTemplates:
        main1: tmpl section: 'main1'
      removeClass: 'btn btn-danger btn-sm'
      removeIcon: '<i class="fa fa-trash-o"></i> '
      removeLabel: $L('purchaseInvoice.documentFile.delete')
      showPreview: false
      showRemove: true

    previewOptions = {}
    url = $element.data 'initial-file'
    if url
      previewOptions.initialPreview = [url]
    builder.addOptions previewOptions

    builder.build $element

    $fileRemove = $('#fileRemove')
    $element
      .on('filecleared', => $fileRemove.val '1')
      .on('fileloaded', => $fileRemove.val '0')


# Class `SalesOrder` represents a sales order form.
#
# @author   Daniel Ellermann
# @version  2.2
#
class SalesOrder

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new widget which handles the actions within a sales order form.
  #
  # @param [jQuery] $element  the element containing the form
  # @param [Object] [options] any options
  #
  constructor: ($element, options = {}) ->
    $ = jq

    @$element = $element

    $('#file').each -> new DocumentFileinput $(this)

    new SPRINGCRM.InvoicingTransaction $element, options


SPRINGCRM.SalesOrder = SalesOrder
