#
# sales-order-form.coffee
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
#= require _sales-order-signature
#= require invoicing-transaction-form


#== Classes =====================================

# Class `SalesOrder` represents a sales order form.
#
# @author   Daniel Ellermann
# @version  3.0
#
class SalesOrder

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  DocumentFileInput = window.modules.require 'DocumentFileInput'

  # @nodoc
  InvoicingTransaction = window.modules.require 'InvoicingTransaction'

  # @nodoc
  SalesOrderSignature = window.modules.require 'SalesOrderSignature'


  #-- Constructor -------------------------------

  # Creates a new widget which handles the actions within a sales order form.
  #
  # @param [jQuery] $element  the element containing the form
  # @param [Object] [options] any options
  #
  constructor: ($element, options = {}) ->
    $element.find('#file')
      .each -> new DocumentFileInput $(this)

    new SalesOrderSignature($element)
    new InvoicingTransaction($element, options)


window.modules.register 'SalesOrder', SalesOrder
