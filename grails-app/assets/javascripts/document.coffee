#
# document.coffee
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
#= require _document-file-input
#= require _document-list


$ = jQuery


#== Classes =====================================

# Class `DocumentList` handles the document list.
#
# @author   Daniel Ellermann
# @version  3.0
# @since    2.2
#
class DocumentList

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery

  # @nodoc
  DocumentFileInput = window.modules.require 'DocumentFileInput'


  #-- Constructor -------------------------------

  # Creates a new handler for the document list.
  #
  constructor: ->
    $ = jq

    @$currentPath = $('#current-path')
    @$documentList = $documentList = $('#document-list').documentlist
      pathChanged: (path) => @_onPathChanged path
    @createFolderUrl = $documentList.data 'create-folder-url'

    new DocumentFileInput(
      $('#upload-file'),
      builderOptions:
        showUpload: true
    )

    $('#create-folder-dialog').on(
        'click', '.create-btn', (event) => @_onClickCreateFolderDialog event
      )


  #-- Non-public methods ------------------------

  # Called when the button to create a new folder has been clicked.  The method
  # displays a dialog which allows entering the folder name.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onClickCreateFolderDialog: (event) ->
    $dialog = $(event.delegateTarget)
    $input = $dialog.find '.modal-body .form-control'

    name = $input.val()
    if name
      data =
        name: name
        path: @$currentPath.val()
      $.get(@createFolderUrl, data).done => @_onFolderCreated()

    $input.val ''
    $dialog.modal 'hide'

    return

  # Called when the folder has been created on the server.
  #
  # @private
  #
  _onFolderCreated: ->
    @$documentList.documentlist 'addFolder',
      name: name
      readable: true
      writeable: true

    return

  # Called when the path of the document list has been changed.
  #
  # @param [String] path  the current path
  # @private
  #
  _onPathChanged: (path) ->
    @$currentPath.val path

    return


new DocumentList()
