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
#= require document-list
#= require mustache
#= require load-image
#= require load-image-ios
#= require load-image-orientation
#= require load-image-meta
#= require load-image-exif
#= require load-image-exif-map
#= require jquery-fileupload
#= require jquery-fileupload-process
#= require jquery-fileupload-image
#= require jquery-fileupload-audio
#= require jquery-fileupload-video
#= require jquery-fileupload-validate


$ = jQuery


# Represents an implementation of the fileupload plugin which displays a upload
# request list and allows the individual upload of files.
#
# @author   Daniel Ellermann
# @version  1.4
# @since    1.4
#
class DocumentFileUpload

  #-- Instance variables ------------------------

  DEFAULT_OPTIONS =
    $fileList: $('#upload-files')
    $uploadRequestTemplate: $('#add-upload-request-template')


  #-- Constructor -------------------------------

  # Creates a new document file upload area at the given element.
  #
  # @param [jQuery] $fileUpload the element where the file upload area should be initialized
  # @param [Object] options     any options that configure the file upload area
  #
  constructor: ($fileUpload, options = {}) ->
    @$fileUpload = $($fileUpload)
    @options = $.extend {}, DEFAULT_OPTIONS, options
    @_initialize()


  #-- Non-public methods ------------------------

  # Hides the file list if there are no items in the list.
  #
  # @private
  #
  _hideFileList: ->
    $fileList = @options.$fileList
    $fileList.hide() unless $fileList.find('tbody > tr').length

  # Initializes this widget.
  #
  # @private
  #
  _initialize: ->
    @$fileUpload.fileupload
      add: (event, data) => @_onAdd event, data
    @options.$fileList
      .on('click', '.cancel', (event) => @_onCancelItemUpload event)
      .on('click', '.start', (event) => @_onStartItemUpload event)
    return

  # Called if the user adds files to the file upload.  The method is either
  # called for many or only one file.
  #
  # @param [Event] event          any event data
  # @param [Object] data          information about the added files
  # @option data [FileList] files the added files
  # @private
  #
  _onAdd: (event, data) ->
    html = @_renderTemplate data
    data.context = ctx = $(html).data('data', data)

    data.process( =>
        @$fileUpload.fileupload 'process', data
      )
      .always( ->
        $ = jQuery
        files = data.files

        ctx.each (index) ->
          file = files[index]
          $(this)
            .find('.size')
              .text(file.size.formatSize())
            .end()
            .find('.preview')
              .append file.preview
      )
      .done( ->
        ctx.find('.start')
          .prop 'disabled', false
      ).done( =>
        @options.$fileList
          .find('tbody')
            .append(ctx)
          .end()
          .show()
      )
    return

  # Called if the user cancels the upload of one item.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onCancelItemUpload: (event) ->
    $template = $(event.currentTarget).closest '.upload-request-template'
    data = $template.data 'data'
    data.abort() if data.abort

    $template.remove()
    @_hideFileList()
    false

  # Called if the user starts the upload of one item.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onStartItemUpload: (event) ->
    $button = $(event.currentTarget)
    $template = $button.closest '.upload-request-template'
    $button.prop 'disabled', true

    data = $template.data 'data'
    data.submit() if data?.submit

    $template.remove()
    @_hideFileList()
    false

  # Renders the template that produces a row in the table of upload requests.
  #
  # @param [Object] data  any data used for rendering
  # @return [String]      the rendered HTML code
  # @private
  #
  _renderTemplate: (data) ->
    Mustache.render @options.$uploadRequestTemplate.html(),
      files: data.files


$('#document-list').on 'springcrm.documentlist.pathchanged', (event, data) ->
  $('#current-path').val data.path

new DocumentFileUpload $('#document-list-upload')
