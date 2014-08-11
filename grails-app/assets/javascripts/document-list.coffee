#
# document-list.coffee
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
#= require jquery
#= require core
#= require filetype


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


  #-- Constructor -------------------------------

  # Creates a new document list within the given element and loads the files
  # and folders via AJAX.
  #
  # @param [Element] element                  the given container element
  # @event springcrm.documentlist.initialized after this widget has been initialized.
  #
  constructor: (element) ->
    @$element = $el = $(element)
    @loadDocumentList()
    $el.trigger $.Event('springcrm.documentlist.initialized')


  #-- Public methods ----------------------------

  # Loads the files and folders within the given absolute path.
  #
  # @param [String] path                      the given absolute path
  # @return [DocumentList]                    this object
  # @event springcrm.documentlist.pathchanged when the current path has been changed and after the files and folders have been rendered.
  #
  loadDocumentList: (path = '') ->
    $ = jQuery

    @currentPath = path
    url = @$element.data 'list-url'
    if url?
      $.getJSON(url, path: path)
        .done (data) =>
          @_renderBreadcrumbsPath path
          @_renderDocumentList path, data

          @$element.trigger $.Event('springcrm.documentlist.pathchanged'),
            path: path
          return
    $(this)

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

  # Obtains the absolute path to the parent of this path.
  #
  # @param [String] path  the given absolute path
  # @return [String]      the absolute path to the parent
  # @private
  #
  _parentPath: (path) ->
    pos = path.lastIndexOf '/'
    if pos < 0 then '' else path.substring 0, pos

  # Renders the breadcrumbs of the given path.
  #
  # @param [String] path    the given absolute path
  # @return [DocumentList]  this object
  # @private
  #
  _renderBreadcrumbsPath: (path) ->
    $ = jQuery

    $nav = $('<nav class="document-list-breadcrumbs"/>')
    $('<strong/>').text($L('document_path_label'))
      .appendTo $nav
    $ul = $('<ul/>').on('click', 'li', (event) =>
        @loadDocumentList $(event.currentTarget).data 'path'
        false
      )
      .appendTo $nav
    $('<li/>').text($L('document_path_root'))
      .data('path', '')
      .appendTo $ul
    if path
      currentPath = ''
      parts = path.split '/'
      for part in parts
        currentPath += '/' if currentPath
        currentPath += part

        $('<li/>').text(part)
          .data('path', currentPath)
          .appendTo $ul

    @$element.find('> nav')
        .remove()
      .end()
      .append $nav
    this

  # Renders the files and folders of the given path.
  #
  # @param [String] path    the given absolute path
  # @param [Object] data    the files and folders of the path received via AJAX
  # @return [DocumentList]  this object
  # @private
  #
  _renderDocumentList: (path, data) ->
    $ = jQuery

    $ul = $('<ul class="document-list-container"/>')
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
    if path
      $li = $('<li class="back-link"/>').appendTo $ul
      $('<i/>').appendTo $li
      $('<span class="name"/>').text($L('document_back_label'))
        .appendTo $li
      $('<span class="size"/>').appendTo $li
      $('<span class="permissions"/>').appendTo $li
    for folder in data.folders
      $li = $('<li class="folder"/>').appendTo $ul
      $('<i/>').appendTo $li
      @_renderName $li, folder
      $('<span class="size"/>').appendTo $li
      @_renderPermissions $li, folder
    for file in data.files
      $li = $('<li class="file"/>')
        .addClass("filetype-#{$.filetype(file.ext)}")
        .appendTo $ul
      $('<i/>').appendTo $li
      @_renderName $li, file
      $('<span class="size"/>').text(file.size.formatSize())
        .appendTo $li
      @_renderPermissions $li, file

    @$element.find('> ul')
        .remove()
      .end()
      .append $ul
    this

  # Renders the name of the given file or folder into the stated list item.
  #
  # @param [jQuery] $li the given list item
  # @param [Object] f   the given file or folder
  # @return [jQuery]    the generated `<span>` containing the name
  # @private
  #
  _renderName: ($li, f) ->
    $('<span class="name"/>').text(f.name)
      .appendTo $li

  # Renders the permissions of the given file or folder into the stated list
  # item.
  #
  # @param [jQuery] $li the given list item
  # @param [Object] f   the given file or folder
  # @return [jQuery]    the generated `<span>` containing the permissions
  # @private
  #
  _renderPermissions: ($li, f) ->
    $ = jQuery

    $perms = $('<span class="permissions"/>').appendTo $li

    b = f.readable
    $strong = $('<strong/>')
      .text(if b then $L('document_permissions_read') else '')
      .appendTo $perms
    $strong.attr 'title', $L('document_permissions_read_title') if b

    b = f.writeable
    $strong = $('<strong/>')
      .text(if b then $L('document_permissions_write') else '')
      .appendTo $perms
    $strong.attr('title', $L('document_permissions_write_title')) if b

    $perms


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

  @each ->
    $this = $(this)
    data = $this.data 'bs.documentlist'

    $this.data 'bs.documentlist', (data = new DocumentList(this)) unless data
    data[option].call $this if typeof option is 'string'


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
