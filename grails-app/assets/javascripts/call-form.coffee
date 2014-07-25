#
# call-form.coffee
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


phoneNumbers = null

onLoadedPhoneNumbers = (data, term) ->
  l = []
  respData = []
  for d in data
    if d
      val = d.toLowerCase()
      if val
        l.push val
        respData.push val if val.indexOf(term) >= 0
  phoneNumbers = l
  respData

onLoadPhoneNumbers = (request, response) ->
  $orgId = $("#organization\\.id")
  $personId = $("#person\\.id")
  term = request.term.toLowerCase()

  data = {}
  if $personId.val()
    url = $("#phone").data("load-person-phone-numbers-url")
    data.id = $personId.val()
  else if $orgId.val()
    url = $("#phone").data("load-organization-phone-numbers-url")
    data.id = $orgId.val()

  if phoneNumbers
    response $.grep(phoneNumbers, (val) -> val.indexOf(term) >= 0)
  else if url
    $.getJSON url, data, (data) ->
      response onLoadedPhoneNumbers(data, term)
      return
  return

$("#organization").autocompleteex select: ->
  phoneNumbers = undefined
  return

$("#person").autocompleteex
  loadParameters: ->
    organization: $("#organization\\.id").val()

  select: ->
    phoneNumbers = undefined
    return

$("#phone").autocomplete source: onLoadPhoneNumbers
