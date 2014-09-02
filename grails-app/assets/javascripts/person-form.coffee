#
# person-form.coffee
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
#= require _lightbox-widget


$ = jQuery


$("#organization").autocompleteex()

$(".document-delete").on "click", ->
  $ = jQuery
  $("#pictureRemove").val 1
  $(".document-preview").add(".document-preview-links").remove()

$("#addresses").addrfields
  leftPrefix: "mailingAddr"
  menuItems: [
    action: "clear"
    side: "left"
    text: $L("person.mailingAddr.clear")
  ,
    action: "copy"
    side: "left"
    text: $L("person.mailingAddr.copy")
  ,
    action: "loadFromOrganization"
    propPrefix: "billingAddr"
    side: "left"
    text: $L("person.addr.fromOrgBillingAddr")
  ,
    action: "loadFromOrganization"
    propPrefix: "shippingAddr"
    side: "left"
    text: $L("person.addr.fromOrgShippingAddr")
  ,
    action: "clear"
    side: "right"
    text: $L("person.otherAddr.clear")
  ,
    action: "copy"
    side: "right"
    text: $L("person.otherAddr.copy")
  ,
    action: "loadFromOrganization"
    propPrefix: "billingAddr"
    side: "right"
    text: $L("person.addr.fromOrgBillingAddr")
  ,
    action: "loadFromOrganization"
    propPrefix: "shippingAddr"
    side: "right"
    text: $L("person.addr.fromOrgShippingAddr")
  ]
  organizationId: "#organization\\.id"
  rightPrefix: "otherAddr"
