#
# organization-form.coffee
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


$ = jQuery
$recType = $('#recType')


#== Functions ===================================

initRecTypes = ->
  $this = $(this)
  $this.attr 'checked', true if $recType.val() & $this.val()

onClickRecType = ->
  $rt = $recType
  val = $(this).val()

  if @checked
    $rt.val $rt.val() | val
  else
    $rt.val $rt.val() & val

  true


#== Main ========================================

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

$('.rec-type').on('click', onClickRecType)
  .each initRecTypes

# vim:set ts=2 sw=2 sts=2:

