#
# remote-list.coffee
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
#= require jquery/jquery
#= require _core


$ = jQuery


#== Widgets =====================================

# Class `RemoteList` loads a list of items from a remote server and rewrites
# all links within this list.
#
# @author   Daniel Ellermann
# @version  2.0
#
class RemoteList

  #-- Class variables ---------------------------

  @DEFAULTS =
    container: '> div'


  #-- Constructor -------------------------------

  # Creates a remote list within the given element.
  #
  # @param [Element, jQuery] element  the given element
  # @param [Object] options           any options
  #
  constructor: (element, options) ->
    @$element = $el = $(element)
    @options = options

    url = options.loadUrl
    @_loadContent @_computeUrl url if url


  #-- Non-public methods ------------------------

  # Computes the URL to load the remote data from either the given URL or the
  # options.  If option `loadParams` is specified the parameters given there
  # are added to the URL.
  #
  # @param [String] url the given URL to use; if not specified the URL is obtained from option `loadUrl`
  # @return [String]    the computed URL
  # @private
  #
  _computeUrl: (url) ->
    $el = @$element
    options = @options

    url = new HttpUrl(url or options.loadUrl)
    params = options.loadParams
    url.overwriteQuery params if params
    url.toString()

  # Loads the content of the given URL.
  #
  # @param [String] url   the given URL
  # @return [RemoteList]  this object
  # @private
  #
  _loadContent: (url) ->
    $el = @$element
    opts = @options

    $container = $el.find(opts.container)
    $container.load url, =>
      $ = jQuery
      $element = $el

      $element.on 'click', 'thead a, .pagination a', (event) =>
        @_onClickLink event

      returnUrl = opts.returnUrl
      if returnUrl
        $element.find('tbody .btn')
          .each ->
            $this = $(this)
            url = $this.attr('href')
            url += (if url.indexOf('?') < 0 then '?' else '&')
            url += "returnUrl=#{returnUrl}"
            $this.attr 'href', url

      $container.removeAttr 'aria-busy'

    this

  # Called if a link has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickLink: (event) ->
    @_loadContent @_computeUrl $(event.currentTarget).attr('href')
    false


Plugin = (option) ->
  @each ->
    $this = $(this)
    data = $this.data 'springcrm.remotelist'
    options = $.extend {}, RemoteList.DEFAULTS, $this.data(),
      typeof option is 'object' and option

    unless data
      $this.data 'springcrm.remotelist', (data = new RemoteList(this, options))
    data[option]() if typeof option is 'string'

old = $.fn.remotelist

$.fn.remotelist = Plugin
$.fn.remotelist.Constructor = RemoteList

$.fn.remotelist.noConflict = ->
  $.fn.remotelist = old
  this


#== Main ========================================

$('.remote-list').remotelist()

# vim:set ts=2 sw=2 sts=2:
