#
# phone-call-form.coffee
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
#= require _typeahead


$ = jQuery


#== Classes =====================================

class CallFormPage

  #-- Internal variables ------------------------

  # @nodoc
  $ = jQuery


  #-- Constructor -------------------------------

  constructor: ($element) ->
    @$element = $element

    @$organization = $element.find '#organization-select'
    @$person = $element.find '#person-select'
    @$phone = $element.find '#phone'

    $element
      .on(
        'change', '#organization-select, #person-select',
        => @phoneNumbers.clear()
      )

    @_initPhoneTypeahead()


  #-- Non-public methods ------------------------

  # Gets the ID and an URL to obtain phone numbers of either an organization
  # or person.
  #
  # @return [Object]  an object containing the ID in element `id` and the URL in element `url`; returns `null` if neither an organization nor a person has been selected
  # @private
  #
  _getParentIdAndUrl: ->
    $phone = @$phone

    id = @$person.val()
    if id
      return id: id, url: $phone.data 'load-person-phone-numbers-url'

    id = @$organization.val()
    if id
      return id: id, url: $phone.data 'load-organization-phone-numbers-url'

    null

  # Initializes the typeahead feature of the phone input control.
  #
  # @private
  #
  _initPhoneTypeahead: ->
    @phoneNumbers = phoneNumbers = new Bloodhound
      datumTokenizer: (datum) -> datum
      identify: (datum) -> datum.replace /[^0-9+]/, ''
      queryTokenizer: (str) -> str
      remote:
        prepare: (query, settings) =>
          data = @_getParentIdAndUrl()
          return settings unless data?

          url = new HttpUrl(data.url)
          url.query.id = data.id
          settings.url = url.toString()

          settings
        url: 'http://'
    phoneNumbers.initialize()

    @$phone.typeahead
        highlight: true
        hint: true
        minLength: 1
      ,
        source: phoneNumbers.ttAdapter()

    return


#== Main ========================================

new CallFormPage $('#phoneCall-form')
