#
# _fileinput-builder.coffee
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
#= require bootstrap/fileinput
#= require bootstrap/fileinput-theme


# Class `FileinputBuilder` builds `fileinput` widgets using default and builder
# specific options.
#
# @author   Daniel Ellermann
# @version  3.0
#
class FileinputBuilder

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  $I = __$I = window.modules.require '$I'

  # @nodoc
  $L = __$L = window.modules.require '$L'

  # @nodoc
  $.fn.fileinputLocales = window.modules.require 'fileinputLocales'


  #-- Class variables ---------------------------

  @DEFAULTS =
    initialPreviewShowDelete: false
    language: $I.country
    previewFileType: 'any'
    showRemove: false
    showUpload: false
    theme: 'fa'


  #-- Constructor -------------------------------

  # Creates a new fileinput builder instance using the given builder specific
  # options.
  #
  # @param [Object] [options] the given options
  #
  constructor: (options = {}) ->
    @options = $.extend true, {}, FileinputBuilder.DEFAULTS, options


  #-- Public methods ----------------------------

  # Adds and possibly overwrites the option with the given key and value.
  #
  # @param [String] key         the given name of the option
  # @param [Object] value       the given value
  # @return [FileinputBuilder]  this object for method chaining
  #
  addOption: (key, value) ->
    @options[key] = value

    this

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


window.modules.register 'FileinputBuilder', FileinputBuilder
