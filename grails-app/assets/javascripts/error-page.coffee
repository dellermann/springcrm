#
# error-page.coffee
#
# Copyright (c) 2011-2016, Daniel Ellermann
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
#= require bootstrap/transition
#= require bootstrap/collapse


#== Classes =====================================

# Class `ErrorPage` represents the necessary scripting of the error page
# including submission of an error report.
#
# @author   Daniel Ellermann
# @version  2.0
#
class ErrorPage

  #-- Internal variables ------------------------

  # @nodoc
  $ = jQuery


  #-- Class variables ---------------------------

  # The names of the placeholders in the error report XML and input controls
  # in the error report form.
  #
  @FIELDS = [
    'name'
    'email'
    'description'
  ]


  #-- Constructor -------------------------------

  # Creates a new instance of the error page.
  #
  constructor: ->
    $ = jQuery

    $form = $('#error-submit-form form')
      .on('change', ':input', (event) => @_onChangedInput event)
      .on('submit', (event) => @_onSubmitForm event)

    @$reportData = $reportData = $('#report-data')
    @origXml = $reportData.text()

    @_rewriteXml $form


  #-- Non-public methods ------------------------

  # Called if an input control in the error report form has been changed.  The
  # method re-computes the error report XML.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangedInput: (event) ->
    @_rewriteXml $(event.currentTarget).closest('form')
    return

  # Called if the error report form should be submitted.  The method rewrites
  # the error report XML and submits it via AJAX.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent the default action
  # @private
  #
  _onSubmitForm: (event) ->
    $form = $(event.currentTarget)
    xml = @_rewriteXml $form

    $.ajax(
        data:
          xml: xml
        dataType: 'html'
        url: $form.data('report-error-url')
      )
      .done (html) -> $form.replaceWith html

    false

  # Re-computes the error report XML with the values from the input controls in
  # the given error report form.
  #
  # @param [jQuery] $form the given error report form
  # @return [String]      the re-computed XML
  # @private
  #
  _rewriteXml: ($form) ->
    fields = ErrorPage.FIELDS
    form = $form[0]

    xml = @origXml
    for f in fields
      value = $(form.elements[f]).val()
        .replace('&', '&amp;')
        .replace('<', '&lt;')
      xml = xml.replace "%#{f}%", value

    @$reportData.text xml
    xml


#== Main ========================================

new ErrorPage()

# vim:set ts=2 sw=2 sts=2:

