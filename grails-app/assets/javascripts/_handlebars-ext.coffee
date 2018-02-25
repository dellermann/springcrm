#
# handlebars-ext.coffee
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
#= require handlebars


$L = window.modules.require '$L'


Handlebars.registerHelper 'section', (name, options) ->
  if arguments.length < 2
    throw new Error 'Handlebars helper "section" needs 1 parameter.'

  if @section is name
    options.fn this
  else
    options.inverse this

Handlebars.registerHelper 'message', (key) -> $L(key)
