#
# document.coffee
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
#= require _document-list
#= require _fileinput-builder
#= require templates/widgets/file-upload-document


$ = jQuery


#== Classes =====================================

# Class `DocumentFileinput` represents a file input widget for setting the
# document of a purchase invoice.
#
# @author   Daniel Ellermann
# @version  2.0
#
class DocumentFileinput

  #-- Internal variables ------------------------

  # @nodoc
  $ = jQuery

  # @nodoc
  $LANG = $L


  #-- Constructor -------------------------------

  # Creates a new file input widget for setting the document of a purchase
  # invoice.
  #
  # @param [jQuery] $element  the file input control which is augmented as widget
  #
  constructor: ($element) ->
    $L = $LANG
    tmpl = Handlebars.templates['widgets/file-upload-document']

    builder = new SPRINGCRM.FileinputBuilder
      browseIcon: '<i class="fa fa-file-o"></i> '
      browseLabel: $L('purchaseInvoice.documentFile.select')
      layoutTemplates:
        main1: tmpl section: 'main1'
      removeClass: 'btn btn-danger btn-sm'
      removeIcon: '<i class="fa fa-trash-o"></i> '
      removeLabel: $L('purchaseInvoice.documentFile.delete')
      showPreview: false
      showRemove: true
      showUpload: true

    previewOptions = {}
    url = $element.data 'initial-file'
    if url
      previewOptions.initialPreview = [url]
    builder.addOptions previewOptions

    builder.build $element


#== Main ========================================

$documentList = $('#document-list').documentlist
  pathChanged: (path) -> $('#current-path').val path

new DocumentFileinput $('#upload-file')

$('#create-folder-dialog').on 'click', '.create-btn', (event) ->
  $dialog = $(event.delegateTarget)
  $input = $dialog.find '.modal-body .form-control'

  name = $input.val()
  if name
    data =
      path: $('#current-path').val(),
      name: name
    $.get($documentList.data('create-folder-url'), data).done ->
      $documentList.documentlist 'addFolder',
        name: name
        readable: true
        writeable: true

  $input.val ''
  $dialog.modal 'hide'

# vim:set ts=2 sw=2 sts=2:

