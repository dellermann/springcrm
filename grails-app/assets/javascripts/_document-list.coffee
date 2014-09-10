#
# _document-list.coffee
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
#= require _core
#= require _filetype
#= require _ui
#= require _handlebars-ext
#= require templates/document/document-list


# @nodoc
$ = jQuery


# Class `DocumentList` represents a Bootstrap compatible widget which renders
# a list of files and folders, allows traversing the tree, and downloading
# files.
#
# @author   Daniel Ellermann
# @version  1.4
# @since    1.4
#
class DocumentList

  #-- Private variables -------------------------

  $ = jQuery


  #-- Class variables ---------------------------

  # The ID of the `<iframe>` which is internally generated to realize the file
  # download.
  #
  # @private
  #
  @IFRAME_ID: 'document-list-download-area'

  # The version of this widget.
  #
  @VERSION: '1.4.10'


  #-- Instance variables ------------------------

  DEFAULT_OPTIONS =
    init: null
    pathChanged: null
    removeFailed: null
    removeSuccess: null


  #-- Constructor -------------------------------

  # Creates a new document list within the given element and loads the files
  # and folders via AJAX.
  #
  # @param [Element] element  the given container element
  # @param [Object] options   any options that overwrite the default options
  #
  constructor: (element, options = {}) ->
    $ = jQuery

    @$element = $el = $(element)
    @options = options = $.extend {}, DEFAULT_OPTIONS, options
    @_initCallback 'init'
    @_initCallback 'pathChanged'
    @_initCallback 'removeFailed'
    @_initCallback 'removeSuccess'
    @loadDocumentList().done -> options.init.fireWith $el


  #-- Public methods ----------------------------

  # Adds a file to all documents lists in this widget.
  #
  # @param [Object] file  the file that should be added
  # @return [jQuery]      this object
  #
  addFile: (file) ->
    $ = jQuery

    $(this).each ->
      $(this).data('bs.documentlist')._addFile file

  # Adds a folder to all documents lists in this widget.
  #
  # @param [Object] folder  the folder that should be added
  # @return [jQuery]        this object
  #
  addFolder: (folder) ->
    $ = jQuery

    $(this).each ->
      $(this).data('bs.documentlist')._addFolder folder

  # Loads the files and folders within the given absolute path.
  #
  # @param [String] path  the given absolute path
  # @return [Promise]     the promise object after loading the document list
  #
  loadDocumentList: (path = '') ->
    $el = @$element

    @_getLoadDocumentListPromise($el.data('list-url'), path).done (data) ->
      @currentPath = path
      @_renderDocumentList path, data
      @options.pathChanged.fireWith $el, [path]

  # Gets or sets the current path which is displayed.
  #
  # @param [String] path      the given path; if omitted the current path is returned
  # @return [String, jQuery]  either the current path or this object if the path has been set
  #
  path: (path) ->
    if path?
      @loadDocumentList path
    else
      @currentPath


  #-- Private methods ---------------------------

  # Adds the given file to the alphabetically sorted position in the document
  # list.
  #
  # @param [Object] file  the file that should be added
  # @private
  #
  _addFile: (file) ->
    $ = jQuery
    name = file.name
    $ul = @$element.find('.document-list-container')

    $item = null
    $ul.children('.file').each ->
      $this = $(this)
      if $this.find('.name').text() > name
        $item = $this
        false

    html = @_renderTemplate files: [file]
    $li = $(html).find 'li.file'
    if $item
      $item.before $li
    else
      $li.appendTo $ul
    return

  # Adds the given folder to the alphabetically sorted position in the document
  # list.
  #
  # @param [Object] folder  the folder that should be added
  # @private
  #
  _addFolder: (folder) ->
    $ = jQuery
    name = folder.name
    $ul = @$element.find('.document-list-container')

    $item = null
    insertBefore = false
    $ul.children('.folder').each ->
      $this = $(this)
      $item = $this
      if $this.find('.name').text() > name
        insertBefore = true
        false

    html = @_renderTemplate folders: [folder]
    $li = $(html).find 'li.folder'
    if insertBefore
      $item.before $li
    else if $item
      $item.after $li
    else
      $li.prependTo $ul
    return

  # Downloads the file with the given absolute path in an internal `<iframe>`.
  #
  # @param [String] absPath the given absolute path
  # @return [DocumentList]  this object
  # @private
  #
  _downloadFile: (absPath) ->
    $ = jQuery

    $iframe = $("##{@IFRAME_ID}")
    unless $iframe.length
      $iframe = $('<iframe/>')
        .attr('id', @IFRAME_ID)
        .css('display', 'none')
        .appendTo $(window.document.body)

    url = @$element.data 'download-url'
    url += if url.indexOf('?') < 0 then '?' else '&'
    url += "path=#{encodeURIComponent(absPath)}"
    $iframe.attr 'src', url
    this

  # Gets the absolute path stored in the given tree element.
  #
  # @param [jQuery] $elem the given tree element
  # @return [String]      the absolute path
  # @private
  #
  _getAbsolutePath: ($elem) ->
    relPath = $elem.find('.name').text()
    path = @currentPath
    path += '/' if path
    path + relPath

  # Gets the `Promise` object after loading the document list of the given
  # path.
  #
  # @param [String] url   the URL that is to request for the document list
  # @param [String] path  the given path
  # @return [Promise]     the promise object
  # @private
  #
  _getLoadDocumentListPromise: (url, path) ->
    deferred = $.Deferred()

    if url?
      deferred.resolveWith this, [url, path]
    else
      deferred.rejectWith this, [path]

    deferred.then (url, path) ->
      $.ajax
        context: this
        data:
          path: path
        dataType: 'json',
        url: url

  # Initializes the callback function with the given name in the options.  The
  # method creates a jQuery `Callbacks` object and adds the callback function
  # to it if it is specified.
  #
  # @param [String] name  the name of the callback option
  # @return [Callbacks]   the generated `Callbacks` object
  # @private
  #
  _initCallback: (name) ->
    options = @options
    f = options[name]
    callbacks = $.Callbacks()
    callbacks.add f if f?
    options[name] = callbacks

  # Obtains the absolute path to the parent of this path.
  #
  # @param [String] path  the given absolute path
  # @return [String]      the absolute path to the parent
  # @private
  #
  _parentPath: (path) ->
    pos = path.lastIndexOf '/'
    if pos < 0 then '' else path.substring 0, pos

  # Removes the file or folder represented by the given list item.
  #
  # @param [jQuery] $li the list item representing the file or folder
  # @private
  #
  _removeDocument: ($li) ->
    $ = jQuery

    deferred = $.Deferred()
    promise = deferred.then( (li) ->
        path = @_getAbsolutePath $(li)
        $.ajax
          context: this
          data:
            path: path
          url: @$element.data('delete-url')
      )
      .done( ->
        $li.remove()
        @options.removeSuccess.fire @$element
      )
      .fail( ->
        @options.removeFailed.fire @$element
      )

    if $.confirm $L('default.delete.confirm.msg')
      deferred.resolveWith this, $li
    else
      deferred.rejectWith this

    return

  # Renders the files and folders of the given path.
  #
  # @param [String] path    the given absolute path
  # @param [Object] data    the files and folders of the path received via AJAX
  # @return [DocumentList]  this object
  # @private
  #
  _renderDocumentList: (path, data) ->
    $ = jQuery

    html = @_renderTemplate
      path: path
      pathParts: @_splitPath path
      folders: data.folders
      files: $.map data.files, (file) ->
        file.fileType = $.filetype file.ext
        file.sizeFormatted = file.size.formatSize()
        file

    @$element.empty()
      .html(html)
      .find('.document-list-breadcrumbs')
        .on('click', 'li', (event) =>
          @loadDocumentList $(event.currentTarget).data 'path'
          false
        )
      .end()
      .find('.document-list-container')
        .on('click', '.back-link', =>
          @loadDocumentList @_parentPath @currentPath
          false
        )
        .on('click', '.folder', (event) =>
          @loadDocumentList @_getAbsolutePath $(event.currentTarget)
          false
        )
        .on('click', '.file', (event) =>
          @_downloadFile @_getAbsolutePath $(event.currentTarget)
          false
        )
        .on('click', '.action-buttons .delete-btn', (event) =>
          @_removeDocument $(event.currentTarget).parents 'li'
          false
        )
    this

  # Renders the Handlebars template using the given data.
  #
  # @param [Object] data  the given data
  # @return [String]      the generated HTML
  # @private
  #
  _renderTemplate: (data) ->
    data.files = $.map data.files ? [], (file) ->
      file.fileType = $.filetype file.ext
      file.sizeFormatted = file.size.formatSize()
      file
    Handlebars.templates['document/document-list'] data

  # Splits the given path into parts.
  #
  # @param [String] path  the given absolute path
  # @return [Array]       an array containing the parts; each part is an object containing the keys `path` and `label`
  # @private
  #
  _splitPath: (path) ->
    res = []
    if path
      currentPath = ''
      parts = path.split '/'
      for part in parts
        currentPath += '/' if currentPath
        currentPath += part
        res.push
          path: currentPath
          label: part
    res


# Renders a document list for the elements in this jQuery object.
#
# @param [String] option  a widget operation that should be called
# @return [jQuery]        this jQuery object
#
Plugin = (option) ->
  $ = jQuery

  if option is 'path'
    $dl = $(this).data('bs.documentlist')
    return $dl.path.apply $dl, $.makeArray(arguments).slice(1)

  args = arguments
  @each ->
    $this = $(this)
    data = $this.data 'bs.documentlist'

    unless data
      $this.data 'bs.documentlist', (data = new DocumentList(this, args[0]))
    if typeof option is 'string'
      data[option].apply $this, $.makeArray(args).slice 1


# @nodoc
old = $.fn.documentlist

# @nodoc
$.fn.documentlist = Plugin
# @nodoc
$.fn.documentlist.Constructor = DocumentList

# @nodoc
$.fn.documentlist.noConflict = ->
  $.fn.documentlist = old
  this

$(window).on 'load', ->
  $('[data-list="document"]').each ->
    Plugin.call $(this)