#
# person-form.coffee
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
#= require widgets/addr-fields
#= require _lightbox-widget


$ = jQuery


$("#organization").autocompleteex()

$(".document-delete").on "click", ->
  $ = jQuery
  $("#pictureRemove").val 1
  $(".document-preview").add(".document-preview-links").remove()

$('.addresses').addrfields
  menuItems:
    left: [
        action: 'clear'
        text: $L('person.mailingAddr.clear')
      ,
        action: 'copy'
        text: $L('person.mailingAddr.copy')
      ,
        action: 'loadFromOrganization'
        propPrefix: 'billingAddr'
        text: $L('person.addr.fromOrgBillingAddr')
      ,
        action: 'loadFromOrganization'
        propPrefix: 'shippingAddr'
        text: $L('person.addr.fromOrgShippingAddr')
    ]
    right: [
        action: 'clear'
        text: $L('person.otherAddr.clear')
      ,
        action: 'copy'
        text: $L('person.otherAddr.copy')
      ,
        action: 'loadFromOrganization'
        propPrefix: 'billingAddr'
        text: $L('person.addr.fromOrgBillingAddr')
      ,
        action: 'loadFromOrganization'
        propPrefix: 'shippingAddr'
        text: $L('person.addr.fromOrgShippingAddr')
    ]
  organizationId: '#organization.id'

# vim:set ts=2 sw=2 sts=2:

