#
# config-seq-numbers.coffee
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
#= require application


$ = jQuery


#== Classes =====================================

# `ConfigSeqNumberWidget` handles value changes in the sequence number table.
# For example, it generates sample values when input values are changed in
# that table.
#
# @author   Daniel Ellermann
# @version  2.0
#
class ConfigSeqNumberWidget

  #-- Constructor -------------------------------

  # Creates a new widget to handle sequence numbers using the given container.
  #
  # @param [jQuery] $elem the given container
  #
  constructor: ($elem) ->
    @$element = $elem
      .on('change', 'input', (event) => @_onChange event)
      .on('click', '.seq-number-example', (event) => @_onChange event)
      .find('tbody tr')
        .each (index, tr) => @_renderExample $(tr)


  #-- Non-public methods ------------------------

  # Computes a random number between the start and the end value specified in
  # the given table row.
  #
  # @param [jQuery] $tr the given table row
  # @return [String]    the random number as string
  # @private
  #
  _computeRndNumber: ($tr) ->
    start = parseInt @_getFieldVal($tr, 'startValue'), 10
    end = parseInt @_getFieldVal($tr, 'endValue'), 10
    return '' if isNaN(start) or isNaN(end)

    diff = Math.abs(end - start)
    String Math.round(Math.random() * diff) + start

  # Gets the value of the field with the given name in the specified table
  # row.
  #
  # @param [jQuery] $tr   the specified table row
  # @param [String] name  the name of the field
  # @return [String]      the value
  # @private
  #
  _getFieldVal: ($tr, name) -> $tr.find("input[name$='.#{name}']").val()

  # Checks whether or not the example sequence number in the given table row
  # needs an organization number.
  #
  # @param [jQuery] $tr the table row
  # @return [Boolean]   `true` if an organization number must be included in
  #                     the generated sequence number; `false` otherwise
  # @private
  #
  _needOrgNumber: ($tr) ->
    ctrl = $tr.data 'controller-name'
    ctrl in ['quote', 'salesOrder', 'invoice', 'dunning', 'creditMemo']

  # Called if a input control in the sequence number table has been changed.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChange: (event) ->
    $target = $(event.target)
    $tr = $target.parents 'tr'

    @_renderExample $tr

    if $tr.data('controller-name') is 'organization'
      $tr.siblings()
        .each (index, tr) =>
          $tr = $(tr)
          @_renderExample $tr if @_needOrgNumber $tr

    return

  # Renders an example value for the given table row.
  #
  # @param [jQuery] $tr the given table row
  # @private
  #
  _renderExample: ($tr) ->
    s = @_getFieldVal $tr, 'prefix'
    s += "-" unless s is ''
    s += @_computeRndNumber $tr

    if @_needOrgNumber $tr
      $orgTr = $tr.siblings 'tr[data-controller-name=organization]'
      s += "-#{@_computeRndNumber $orgTr}"

    suffix = @_getFieldVal $tr, 'suffix'
    s += "-#{suffix}" unless suffix is ''
    $tr.find('.seq-number-example output').text s

    return


#== Main ========================================

new ConfigSeqNumberWidget $('#seq-numbers')
