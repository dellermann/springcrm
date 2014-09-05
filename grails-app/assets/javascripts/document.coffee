#
# document.coffee
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
#= require _document-list
#= require _document-file-upload


$ = jQuery
$documentList = $('#document-list')


$documentList.on 'springcrm.documentlist.pathchanged', (event, data) ->
  $('#current-path').val data.path
$('#document-list-upload').documentfileupload()

$('.create-folder-button').on 'click', ->
  $('#create-folder-dialog').dialog
    buttons: [
        click: ->
          $name = $('#create-folder-name')
          name = $name.val()
          if name
            data =
              path: $('#current-path').val(),
              name: name
            $.get $documentList.data('create-folder-url'), data, ->
              $documentList.documentlist 'addFolder',
                name: name
                readable: true
                writeable: true
          $name.val ''
          $(this).dialog 'close'
        text: $L('default.button.ok.label')
      ,
        class: 'red'
        click: ->
          $('#create-folder-name').val ''
          $(this).dialog 'close'
        text: $L('default.button.cancel.label')
    ]
    modal: true

