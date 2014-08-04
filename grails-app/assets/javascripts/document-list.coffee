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


$ = jQuery


class DocumentList

  #-- Private variables -------------------------

  $ = jQuery


  #-- Class variables ---------------------------

  @VERSION: '1.4.10'


  #-- Constructor -------------------------------

  constructor: (element) ->
    @$element = $(element)
    @loadDocumentList()


  #-- Public methods ----------------------------

  loadDocumentList: (path = '') ->
    @currentPath = path
    url = @$element.data 'list-url'
    if url?
      $.getJSON url, path: path, (data) =>
        @_renderBreadcrumbsPath path
        @_renderDocumentList path, data
        true


  #-- Private methods ---------------------------

  _parentPath = (path) ->
    pos = path.lastIndexOf '/'
    if pos < 0 then '' else path.substring 0, pos

  _renderBreadcrumbsPath: (path) ->
    $ = jQuery

    $fragment = $(document.createDocumentFragment())
    $nav = $('<nav class="document-list-breadcrumbs"/>').appendTo $fragment
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
      .append $fragment

  _renderDocumentList: (path, data) ->
    $ = jQuery

    $fragment = $(document.createDocumentFragment())
    $ul = $('<ul class="document-list-container"/>')
      .on('click', 'li.back-link', =>
        @loadDocumentList _parentPath @currentPath
        false
      )
      .on('click', 'li.folder', (event) =>
        path = @currentPath
        path += '/' if path
        path += $(event.currentTarget).find('.name').text()
        @loadDocumentList path
        false
      )
      .appendTo $fragment
    if path
      $li = $('<li class="back-link"/>').appendTo $ul
      $('<i/>').appendTo $li
      $('<span class="name"/>').text($L('document_back_label'))
        .appendTo $li
      $('<span class="size"/>').appendTo $li
    for folder in data.folders
      $li = $('<li class="folder"/>').appendTo $ul
      $('<i/>').appendTo $li
      $('<span class="name"/>').text(folder.name)
        .appendTo $li
      $('<span class="size"/>').appendTo $li
    for file in data.files
      $li = $('<li class="file"/>')
        .addClass("filetype-#{$.filetype(file.ext)}")
        .appendTo $ul
      $('<i/>').appendTo $li
      $('<span class="name"/>').text(file.name)
        .appendTo $li
      $('<span class="size"/>').text(_sizeToString file.size)
        .appendTo $li

    @$element.find('> ul')
        .remove()
      .end()
      .append $fragment

  _sizeToString = (size) ->
    if size >= 1024 ** 3
      "#{(size / 1024 ** 3).format(1)} GB"
    else if size >= 1024 ** 2
      "#{(size / 1024 ** 2).format(1)} MB"
    else if size >= 1024
      "#{(size / 1024).format(1)} KB"
    else
      "#{size} B"


Plugin = (option) ->
  $ = jQuery

  @each ->
    $this = $(this)
    data = $this.data 'bs.documentlist'

    $this.data 'bs.documentlist', (data = new DocumentList(this)) unless data
    data[option].call $this if typeof option is 'string'


old = $.fn.documentlist

$.fn.documentlist = Plugin
$.fn.documentlist.Constructor = DocumentList

$.fn.documentlist.noConflict = ->
  $.fn.documentlist = old
  this

$(window).on 'load', ->
  $('[data-list="document"]').each ->
    Plugin.call $(this)
