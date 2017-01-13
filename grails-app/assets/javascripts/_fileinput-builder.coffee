#
# _fileinput-builder.coffee
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
#= require _core
#= require bootstrap/fileinput


$LANG = $L


# Class `FileinputBuilder` builds `fileinput` widgets using default and builder
# specific options.
#
# @author   Daniel Ellermann
# @version  2.0
#
class FileinputBuilder

  #-- Internal variables ------------------------

  $ = jQuery
  $L = $LANG


  #-- Class variables ---------------------------

  @DEFAULTS =
    autoSizeImage: ->
    browseIcon: '<i class="fa fa-folder-open-o"></i>'
    browseLabel: $L('default.fileinput.browse.label')
    fileActionSettings:
      indicatorError: '<i class="fa fa-warning text-danger"></i>'
      indicatorErrorTitle: $L('default.fileinput.indicator.error')
      indicatorLoading:
        '<i class="fa fa-circle-o-notch fa-spin text-muted"></i>'
      indicatorLoadingTitle: $L('default.fileinput.indicator.loading')
      indicatorNew: '<i class="fa fa-upload text-warning"></i>'
      indicatorNewTitle: $L('default.fileinput.indicator.new')
      indicatorSuccess: '<i class="fa fa-check fa-lg text-success"></i>'
      indicatorSuccessTitle: $L('default.fileinput.indicator.success')
      removeIcon: '<i class="fa fa-trash"></i>'
      removeTitle: $L('default.fileinput.remove.label')
      uploadIcon: '<i class="fa fa-upload"></i>'
      uploadTitle: $L('default.fileinput.upload.label')
      zoomIcon: '<i class="fa fa-search-plus"></i>'
    layoutTemplates:
      icon: ''
    msgFileNotFound: $L('default.fileinput.msg.fileNotFound')
    msgFileNotReadable: $L('default.fileinput.msg.fileNotReadable')
    msgFilePreviewAborted: $L('default.fileinput.msg.filePreviewAborted')
    msgFilePreviewError: $L('default.fileinput.msg.filePreviewError')
    msgFilesTooMany: $L('default.fileinput.msg.filesTooMany')
    msgInvalidFileExtension: $L('default.fileinput.msg.invalidFileExtension')
    msgInvalidFileName: $L('default.fileinput.msg.invalidFileName')
    msgInvalidFileType: $L('default.fileinput.msg.invalidFileType')
    msgLoading: $L('default.fileinput.msg.loading')
    msgProgress: $L('default.fileinput.msg.progress')
    msgSelected: $L('default.fileinput.msg.selected')
    msgSizeTooLarge: $L('default.fileinput.msg.sizeTooLarge')
    msgSizeTooSmall: $L('default.fileinput.msg.sizeTooSmall')
    msgValidationError: '<span class="text-danger">' +
      '<i class="fa fa-warning"></i> ' +
      $L('default.fileinput.msg.validationError') + '</span>'
    previewZoomButtonIcons:
      prev: '<i class="fa fa-caret-left"></i>',
      next: '<i class="fa fa-caret-right"></i>',
      toggleheader: '<i class="fa fa-arrows-v"></i>',
      fullscreen: '<i class="fa fa-arrows-alt"></i>',
      borderless: '<i class="fa fa-expand"></i>',
      close: '<i class="fa fa-close"></i>'
    removeIcon: '<i class="fa fa-trash"></i>'
    removeLabel: $L('default.fileinput.remove.label')
    removeTitle: $L('default.fileinput.remove.title')
    showRemove: false
    showUpload: false
    uploadIcon: '<i class="fa fa-upload"></i>'
    uploadLabel: $L('default.fileinput.upload.label')
    uploadTitle: $L('default.fileinput.upload.title')


  #-- Constructor -------------------------------

  # Creates a new fileinput builder instance using the given builder specific
  # options.
  #
  # @param [Object] [options] the given options
  #
  constructor: (options = {}) ->
    @options = $.extend true, {}, FileinputBuilder.DEFAULTS, options


  #-- Public methods ----------------------------

  # Extends and overwrites the current options with the given options.
  #
  # @param [Object] [options]   the given options
  # @return [FileinputBuilder]  this object for method chaining
  #
  addOptions: (options) ->
    $.extend true, @options, options
    this

  # Builds a fileinput control for the given jQuery element.
  #
  # @param [jQuery] $element  the given jQuery element
  # @return [jQuery]          the given jQuery element for method chaining
  #
  build: ($element) -> $element.fileinput @options


SPRINGCRM.FileinputBuilder = FileinputBuilder

# vim:set ts=2 sw=2 sts=2:
