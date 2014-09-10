#
# _document-file-upload.coffee
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
#= require _jquery
#= require _document-list
#= require _document-file-upload
#= require _load-image
#= require _load-image-ios
#= require _load-image-orientation
#= require _load-image-meta
#= require _load-image-exif
#= require _load-image-exif-map
#= require _jquery-fileupload
#= require _jquery-fileupload-process
#= require _jquery-fileupload-image
#= require _jquery-fileupload-audio
#= require _jquery-fileupload-video
#= require _jquery-fileupload-validate
#= require _handlebars-ext
#= require templates/document/document-file-upload


# @nodoc
$ = jQuery


# Represents an implementation of the fileupload plugin which displays a upload
# request list and allows the individual upload of files.
#
# @author   Daniel Ellermann
# @version  1.4
# @since    1.4
#
class DocumentFileUpload

  #-- Private variables -------------------------

  $ = jQuery


  #-- Class variables ---------------------------

  # The version of this widget.
  #
  @VERSION: '1.4.10'


  #-- Instance variables ------------------------

  DEFAULT_OPTIONS =
    $documentList: $('.document-list')
    $fileList: $('.document-upload-filelist')
    $uploadRequestTemplate: $('#add-upload-request-template')


  #-- Constructor -------------------------------

  # Creates a new document file upload area at the given element.
  #
  # @param [Element] element    the given container element
  # @param [Object] options     any options that configure the file upload area
  #
  constructor: (element, options = {}) ->
    @$element = $(element)
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
    $el = @$element

    $el.fileupload
      add: (event, data) => @_onAdd event, data
      done: (event, data) => @_onDone event, data
    @options.$fileList
      .on('click', '.document-upload-cancel', (event) =>
        @_onCancelItemUpload event
        false
      )
      .on('click', '.document-upload-start', (event) =>
        @_onStartItemUpload event
        false
      )
    $('.document-upload-cancel').on 'click', (event) =>
      @_onCancelAllItemUpload()
    $('.document-upload-start').on 'click', (event) =>
      @_onStartAllItemUpload()
    $el.trigger $.Event('springcrm.documentfileupload.initialized')
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
        @$element.fileupload 'process', data
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
        ctx.find('.document-upload-start')
          .prop 'disabled', false
      ).done( =>
        @options.$fileList
          .find('tbody')
            .append(ctx)
          .end()
          .show()
      )
    return

  # Called if all the uploads in the queue should be removed.
  #
  # @private
  #
  _onCancelAllItemUpload: ->
    @options.$fileList
      .find('.document-upload-cancel')
        .click()

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

  # Called if a file has been uploaded.
  #
  # @param [Event] event  any event data
  # @param [Object] data  information about the upload
  # @private
  #
  _onDone: (event, data) ->
    if data.textStatus is 'success'
      file = data.result
      @options.$documentList.documentlist 'addFile', file
    null

  # Called if all items in the upload queue should be processed.
  #
  # @private
  #
  _onStartAllItemUpload: ->
    @options.$fileList
      .find('.document-upload-start')
        .click()

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
    Handlebars.templates['document/document-file-upload']
      files: data.files


# Renders a document list for the elements in this jQuery object.
#
# @param [String] option  a widget operation that should be called
# @return [jQuery]        this jQuery object
#
Plugin = (option) ->
  $ = jQuery

  args = arguments
  @each ->
    $this = $(this)
    data = $this.data 'bs.documentfileupload'

    unless data
      $this.data 'bs.documentfileupload',
        (data = new DocumentFileUpload(this, $.makeArray(args).slice 1))
    if typeof option is 'string'
      data[option].apply $this, $.makeArray(args).slice 1


# @nodoc
old = $.fn.documentfileupload

# @nodoc
$.fn.documentfileupload = Plugin
# @nodoc
$.fn.documentfileupload.Constructor = DocumentFileUpload

# @nodoc
$.fn.documentfileupload.noConflict = ->
  $.fn.documentfileupload = old
  this

