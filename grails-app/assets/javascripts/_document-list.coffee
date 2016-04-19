#
# _document-list.coffee
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
#= require jquery/jquery
#= require _core
#= require _filetype
#= require _ui
#= require _handlebars-ext
#= require jquery/storage-api
#= require templates/document/document-list


# @nodoc
$ = jQuery


# Class `DocumentList` represents a Bootstrap compatible widget which renders
# a list of files and folders, allows traversing the tree, and downloading
# files.
#
# @author   Daniel Ellermann
# @version  2.1
# @since    1.4
#
class DocumentList

  #-- Private variables -------------------------

  $ = jq = jQuery


  #-- Class fields ------------------------------

  # The ID of the `<iframe>` which is internally generated to realize the file
  # download.
  #
  # @private
  #
  @IFRAME_ID: 'document-list-download-area'

  # The version of this widget.
  #
  @VERSION: '2.0.0'


  #-- Fields ------------------------------------

  DEFAULT_OPTIONS =
    hideActions: false
    init: null
    listUrl: null
    multiSelect: false
    pathChanged: null
    selected: null
    selectionMode: false
    removeFailed: null
    removeSuccess: null
    scrollable: false


  #-- Constructor -------------------------------

  # Creates a new document list within the given element and loads the files
  # and folders via AJAX.
  #
  # @param [Element] element  the given container element
  # @param [Object] options   any options that overwrite the default options
  #
  constructor: (element, options = {}) ->
    $ = jq

    @$element = $el = $(element)
    @options = options = $.extend {}, DEFAULT_OPTIONS, options
    options.listUrl = options.listUrl ? $el.data('list-url')
    options.selectionMode = true if options.selected or options.multiSelect

    @_initCallback 'init'
    @_initCallback 'pathChanged'
    @_initCallback 'removeFailed'
    @_initCallback 'removeSuccess'
    @_initCallback 'selected'

    @selectedPath = null
    @selectedPaths = []

    path = $el.data 'initial-path'
    unless path
      storage = $.localStorage
      if storage.isSet 'document-list.path'
        path = storage.get 'document-list.path'
    @loadDocumentList(path).done -> options.init.fireWith $el


  #-- Public methods ----------------------------

  # Adds a file to all documents lists in this widget.
  #
  # @param [Object] file  the file that should be added
  # @return [jQuery]      this object
  #
  addFile: (file) ->
    $ = jq

    $(this).each ->
      $(this).data('bs.documentlist')._addFile file

  # Adds a folder to all documents lists in this widget.
  #
  # @param [Object] folder  the folder that should be added
  # @return [jQuery]        this object
  #
  addFolder: (folder) ->
    $ = jq

    $(this).each ->
      $(this).data('bs.documentlist')._addFolder folder

  # Loads the files and folders within the given absolute path.
  #
  # @param [String] path  the given absolute path
  # @return [Promise]     the promise object after loading the document list
  #
  loadDocumentList: (path = '') ->
    $el = @$element
    options = @options

    @_getLoadDocumentListPromise(options.listUrl, path).done (data) ->
      @currentPath = path
      $.localStorage.set 'document-list.path', path
      @_renderDocumentList path, data
      options.pathChanged.fireWith $el, [path]

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

  # Gets or sets the selected files.
  #
  # @param [String, Array] selection  the pathes of files that should be selected; if omitted the current selection is returned
  # @return [String, Array, jQuery]   either a single path (in single selection mode) or an array of paths (in multiple selection mode); this object if the selection has been set
  #
  selection: (selection) ->
    $ = jq
    options = @options

    if selection?
      if options.selectionMode
        if options.multiSelect
          selection = [selection] unless $.isArray selection
          @selectedPaths = selection
          @_setSelection()
        else
          selection = selection.shift() if $.isArray selection
          @selectedPath = selection
      $(this)
    else
      res = null
      if options.selectionMode or options.multiSelect
        res = if options.multiSelect then @selectedPaths else @selectedPath
      res


  #-- Private methods ---------------------------

  # Adds the given file to the alphabetically sorted position in the document
  # list.
  #
  # @param [Object] file  the file that should be added
  # @private
  #
  _addFile: (file) ->
    $ = jq
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
    $ = jq
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
      $backLink = $ul.children '.back-link'
      if $backLink.length
        $backLink.after $li
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
    $ = jq

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
    $ = jq

    $.Deferred()
      .resolve()
      .then( -> $.confirm $L('default.delete.confirm.msg'))
      .then( =>
        path = @_getAbsolutePath $li
        $.ajax
          context: this
          data:
            confirmed: true
            path: path
          url: @$element.data('delete-url')
      )
      .done( =>
        $li.remove()
        @options.removeSuccess.fire @$element
      )
      .fail( => @options.removeFailed.fire @$element)

    return

  # Renders the files and folders of the given path.
  #
  # @param [String] path    the given absolute path
  # @param [Object] data    the files and folders of the path received via AJAX
  # @return [DocumentList]  this object
  # @private
  #
  _renderDocumentList: (path, data) ->
    $ = jq
    options = @options
    selectedPaths = @selectedPaths

    html = @_renderTemplate
      options: options
      path: path
      pathParts: @_splitPath path
      folders: data.folders
      files: $.map data.files, (file) ->
        p = path
        p += '/' if path
        p += file.name
        file.selected = $.inArray(p, selectedPaths) >= 0
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
        .on('click', '.file .name', (event) =>
          $li = $(event.currentTarget).parents 'li'
          if options.selectionMode
            @_selectFile $li
          else
            @_downloadFile @_getAbsolutePath $li
          false
        )
        .on('click', '.action-buttons .delete-btn', (event) =>
          @_removeDocument $(event.currentTarget).parents 'li'
          false
        )
        .on('change', '.selection input', (event) =>
          $target = $(event.currentTarget)
          @_selectFile $target.parents('li'), $target.is(':checked')
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
    $ = jq

    data.files = $.map data.files ? [], (file) ->
      file.fileType = $.filetype file.ext
      file.sizeFormatted = file.size.formatSize()
      file
    Handlebars.templates['document/document-list'] data

  # Stores the selection of the given file item in an internal list and calls
  # the `selection` callback.
  #
  # @param [jQuery] $li       the list item that has been clicked
  # @param [Boolean] selected `true` if the item has been selected; `false` otherwise
  # @private
  #
  _selectFile: ($li, selected = true) ->
    options = @options
    path = @_getAbsolutePath $li

    if options.multiSelect
      selectedPaths = @selectedPaths
      pos = $.inArray path, selectedPaths
      if selected
        if pos < 0 then selectedPaths.push path
      else if pos >= 0
        selectedPaths.splice pos, 1
      selection = selectedPaths.slice 0
    else
      selection = @selectedPath = path

    options.selected.fireWith @$element, [
        file: $li
        selected: selected
        selection: selection
      ]
    return

  # Sets the selection according the paths in `this.selectedPaths`.
  #
  # @private
  #
  _setSelection: ->
    $ = jq
    that = this
    paths = @selectedPaths

    @$element.find('.document-list-container .file')
      .each ->
        $this = $(this)
        path = that._getAbsolutePath $this
        checked = $.inArray(path, paths) >= 0
        $this.find('.selection input')
          .prop 'checked', checked
    return

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

  if option is 'path' or option is 'selection'
    $dl = $(this).data('bs.documentlist')
    return $dl[option].apply $dl, $.makeArray(arguments).slice(1)

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
$.fn.hasdocumentlist = -> @first().data('bs.documentlist')?

# @nodoc
$.fn.documentlist.noConflict = ->
  $.fn.documentlist = old
  this

$(window).on 'load', ->
  $('[data-list="document"]').each ->
    Plugin.call $(this)

# vim:set ts=2 sw=2 sts=2:
