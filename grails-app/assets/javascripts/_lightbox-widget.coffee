#
# _lightbox-widget.coffee
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
#= require _lightbox


$ = jQuery


LightboxWidget =
  options:
    imgDir: "images/lightbox"
    imageBtnClose: "lightbox-btn-close.gif"
    imageBtnNext: "lightbox-btn-next.gif"
    imageBtnPrev: "lightbox-btn-prev.gif"
    imageLoading: "lightbox-ico-loading.gif"

  _create: ->
    opts = @options
    o = {}
    $.extend o, opts,
      imageBtnClose: opts.imgDir + "/" + opts.imageBtnClose
      imageBtnNext: opts.imgDir + "/" + opts.imageBtnNext
      imageBtnPrev: opts.imgDir + "/" + opts.imageBtnPrev
      imageLoading: opts.imgDir + "/" + opts.imageLoading

    @element.lightBox o

$.widget "springcrm.lightbox", LightboxWidget
