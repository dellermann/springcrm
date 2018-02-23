#
# _document-file-input.coffee
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
#= require _fileinput-builder
#= require _handlebars-ext
#= require templates/widgets/file-upload-document


#== Classes =====================================

# Class `DocumentFileInput` represents a file input widget to set a document.
#
# @author   Daniel Ellermann
# @version  2.2
# @since    2.2
#
class DocumentFileInput

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  __$L = $L


  #-- Constructor -------------------------------

  # Creates a new file input widget for setting a document.
  #
  # @param [jQuery] $element  the file input control which is augmented as widget
  # @param [Object] [options] any options used to configure the widget
  #
  constructor: ($element, options = {}) ->
    $ = __jq
    $L = __$L
    tmpl = Handlebars.templates['widgets/file-upload-document']

    removeLabelKey = options.removeLabelKey ? 'default.document.delete'
    builderOptions = $.extend {},
        browseIcon: '<i class="fa fa-file-o"></i> '
        browseLabel: $L('default.document.select')
        layoutTemplates:
          main1: tmpl section: 'main1'
        removeClass: 'btn btn-danger btn-sm'
        removeIcon: '<i class="fa fa-trash-o"></i> '
        removeLabel: $L(removeLabelKey)
        showPreview: false
        showRemove: true
      ,
        options.builderOptions

    builder = new SPRINGCRM.FileinputBuilder builderOptions

    previewOptions = {}
    url = $element.data 'initial-file'
    previewOptions.initialPreview = [url] if url
    builder.addOptions previewOptions

    builder.build $element

    $fileRemove = $('#fileRemove')
    $element
      .on('filecleared', => $fileRemove.val '1')
      .on('fileloaded', => $fileRemove.val '0')


#== Main ========================================

SPRINGCRM.DocumentFileInput = DocumentFileInput
