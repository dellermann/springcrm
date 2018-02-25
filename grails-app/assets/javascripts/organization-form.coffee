#
# organization-form.coffee
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


# Class `OrganizationFormPage` handles the scripting for the organization form
# page.
#
# @author   Daniel Ellermann
# @version  3.0
# @since    3.0
#
class OrganizationFormPage

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  $L = __$L = window.modules.require '$L'


  #-- Constructor -------------------------------

  # Creates a new scripting instance for the organization form page.
  #
  constructor: ->
    $ = __jq
    $L = __$L
    @$recType = $('#recType')

    $('.addresses').addrfields
      menuItems:
        left: [
          action: 'clear'
          text: $L('organization.billingAddr.clear')
        ,
          action: 'copy'
          text: $L('organization.billingAddr.copy')
        ]
        right: [
          action: 'clear'
          text: $L('organization.shippingAddr.clear')
        ,
          action: 'copy'
          text: $L('organization.shippingAddr.copy')
        ]

    $('.rec-type')
      .on('click', (event) => @_onClickRecType event)
      .each (_, elem) => @_initRecTypes elem


  #-- Public methods ----------------------------

  # Initializes the given checkbox for the type of organization record.
  #
  # @param [Element] elem the given checkbox which should be initialized
  # @private
  #
  _initRecTypes: (elem) ->
    $elem = $(elem)
    $elem.attr 'checked', true if @$recType.val() & $elem.val()

  # Called when the user clicks a checkbox to set the type of organization
  # record.  The method computes a numeric value by combining the checkbox
  # value bitwise.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `true` to allow default behaviour
  # @private
  #
  _onClickRecType: (event) ->
    target = event.currentTarget
    val = target.value

    $rt = @$recType
    if target.checked
      $rt.val $rt.val() | val
    else
      $rt.val $rt.val() & val

    true


new OrganizationFormPage()
