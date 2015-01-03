#
# lightbox.coffee
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
#= require _jquery
#= require bootstrap/transition
#= require bootstrap/modal
#= require _handlebars-ext
#= require templates/widgets/lightbox


$ = jQuery


#== Widgets =====================================

# Class `Lightbox` represents a widget that displays an image in a modal.
#
# @author   Daniel Ellermann
# @version  2.0
#
class Lightbox

  #-- Constructor -------------------------------

  # Creates a lightbox for the given element.
  #
  # @param [Element, jQuery] element  the given element
  # @param [Object] options           any options
  #
  constructor: (element, options) ->
    @$element = $el = $(element)
    @options = options

    $el.on 'click', (event) => @_onClick event


  #-- Non-public methods ------------------------

  # Called if the element has been clicked and the lightbox should be shown.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClick: (event) ->
    event.preventDefault()

    $modal = @$modal
    unless $modal
      options = @options
      html = Handlebars.templates['widgets/lightbox']
        title: options.title
        url: $(event.currentTarget).attr('href') ? options.url

      @$modal = $modal = $(html).appendTo 'body'

    $modal.modal()


Plugin = (option) ->
  @each ->
    $this = $(this)
    data = $this.data 'springcrm.lightbox'
    options = $.extend {}, $this.data(), typeof option is 'object' and option

    unless data
      $this.data 'springcrm.lightbox', (data = new Lightbox(this, options))
    data[option]() if typeof option is 'string'

old = $.fn.lightbox

$.fn.lightbox = Plugin
$.fn.lightbox.Constructor = Lightbox

$.fn.lightbox.noConflict = ->
  $.fn.lightbox = old
  this


#== Main ========================================

$('*[data-toggle="lightbox"]').each -> $(this).lightbox()

# vim:set ts=2 sw=2 sts=2:

