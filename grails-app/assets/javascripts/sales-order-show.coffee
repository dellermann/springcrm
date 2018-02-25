#
# sales-order-show.coffee
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
#= require _sales-order-signature


#== Classes =====================================

# Class `SalesOrderShow` represents a sales order show view.
#
# @author   Daniel Ellermann
# @version  3.0
#
class SalesOrderShow

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  SalesOrderSignature = window.modules.require 'SalesOrderSignature'


  #-- Constructor -------------------------------

  # Creates a new widget which handles the actions within a sales order form.
  #
  # @param [jQuery] $element  the element containing the form
  #
  constructor: ($element) ->
    new SalesOrderSignature(
      $element,
      onChangeSignature: ($input) => @_onChangeSignature $input
    )


  #-- Non-public methods ------------------------

  # Called when a new signature should be saved.  The method submits the form
  # containing the signature data.
  #
  # @param [jQuery] $input  the input where the signature data is stored
  # @private
  #
  _onChangeSignature: ($input) ->
    $input.closest('form').submit()

    return


new SalesOrderShow $('#sales-order-show')
