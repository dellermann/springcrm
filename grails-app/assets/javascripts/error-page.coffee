#
# error-page.coffee
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

FIELDS = [
  'name'
  'email'
  'description'
]
$form = $('#bugreport-form')
$reportData = $('#report-data')

rewriteXml = ->
  fields = FIELDS
  form = $form[0]

  text = origText
  for f in fields
    value = $(form.elements[f]).val()
      .replace('&', '&amp;')
      .replace('<', '&lt;')
    text = text.replace "%#{f}%", value

  $reportData.text text
  text

submitForm = ->
  $f = $form
  xml = rewriteXml()
  $.ajax
    data:
      xml: xml
    dataType: 'html'
    success: (html) ->
      $f.replaceWith html
      return
    url: $f.data('report-error-url')
  return

origText = $reportData.text()
$('#accordion').accordion active: 2
$form.on('change', ':input', ->
    rewriteXml()
  )
  .on 'click', 'button', ->
    submitForm()
rewriteXml()
