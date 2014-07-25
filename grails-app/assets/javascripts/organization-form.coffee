#
# organization-form.js
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


$ = jQuery
$recType = $("#recType")

initRecTypes = ->
  $this = $(this)
  $this.attr "checked", true if $recType.val() & $this.val()

onClickRecType = ->
  $rt = $recType
  $this = $(this)
  if @checked
    $rt.val $rt.val() | $this.val()
  else
    $rt.val $rt.val() & ~$this.val()
  true

$("#addresses").addrfields
  leftPrefix: "billingAddr"
  menuItems: [
    action: "clear"
    side: "left"
    text: $L("organization.billingAddr.clear")
  ,
    action: "copy"
    side: "left"
    text: $L("organization.billingAddr.copy")
  ,
    action: "clear"
    side: "right"
    text: $L("organization.shippingAddr.clear")
  ,
    action: "copy"
    side: "right"
    text: $L("organization.shippingAddr.copy")
  ]
  rightPrefix: "shippingAddr"

$(".rec-type").on("click", onClickRecType)
  .each initRecTypes

