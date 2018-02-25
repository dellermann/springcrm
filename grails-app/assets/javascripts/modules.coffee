#
# modules.coffee
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
#= require jquery/jquery


# Class `Modules` represents a simple module registry for JavaScript.  It
# allows exporting modules to the registry and importing it from other scripts.
#
# @author   Daniel Ellermann
# @version  3.0
# @since    3.0
#
class Modules

  #-- Internal variables ------------------------

  $ = jQuery


  #-- Constructor -------------------------------

  # Creates a new module registry.
  #
  constructor: ->
    @modules =
      $: $
      jQuery: $
      $L: $L


  #-- Public methods ----------------------------

  # Registers the given module under the stated name in the registry.
  #
  # @param [String] name    the name of the module; module names should be in
  #                         dashed lower case (kebap-case)
  # @param [Object] module  the module, normally a function or a class
  # @return [Modules]       this instance for method chaining
  # @throw                  if a module with the given name already exists
  #
  register: (name, module) ->
    if @modules[name]
      throw new Error("Module with name #{name} already registered.")

    @modules[name] = module

    this

  # Returns the module of the given name from the registry.
  #
  # @param [String] name      the given name of the module; module names should
  #                           be in dashed lower case (kebap-case)
  # @param [Object] defValue  any default value which should be used if no
  #                           module with the given name has been registered
  # @return [Object]          the module or `null` if no module with the given
  #                           name exists
  #
  require: (name, defValue = null) ->
    module = @modules[name]
    unless module
      if defValue
        module = defValue
      else
        throw new Error("Module with name #{name} not registered.")

    module


modules = window.modules
if modules and not modules instanceof Modules
  throw new Error('window.modules is already defined.')

window.modules = modules ? new Modules()
