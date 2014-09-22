#
# config-seq-numbers.coffee
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
#= require application


$ = jQuery


#-- Classes -------------------------------------

ConfigSeqNumberWidget =
  _computeRndNumber: ($tr) ->
    start = parseInt @_getFieldVal($tr, "startValue"), 10
    end = parseInt @_getFieldVal($tr, "endValue"), 10
    if isNaN(start) or isNaN(end)
      ""
    else
      diff = Math.abs(end - start)
      String Math.round(Math.random() * diff) + start

  _create: ->
    $ = jQuery

    @element.on("change", (event) => @_onChange event)
      .find("tbody tr")
        .each (index, tr) => @_renderExample $(tr)

  _getFieldVal: ($tr, name) ->
    $tr.find("input[name$='.#{name}']").val()

  _needOrgNumber: ($tr) ->
    ctrl = $tr.data "controller-name"
    ctrl in ["quote", "salesOrder", "invoice", "dunning", "creditMemo"]

  _onChange: (event) ->
    $target = $(event.target)
    $tr = $target.parents("tr")
    @_renderExample $tr
    if $tr.data("controller-name") is "organization"
      $tr.siblings()
        .each (index, tr) =>
          $tr = $(tr)
          @_renderExample $tr if @_needOrgNumber $tr

  _renderExample: ($tr) ->
    s = @_getFieldVal $tr, "prefix"
    s += "-" unless s is ""
    s += @_computeRndNumber $tr

    if @_needOrgNumber $tr
      $orgTr = $tr.siblings "tr[data-controller-name=organization]"
      s += "-#{@_computeRndNumber $orgTr}"

    suffix = @_getFieldVal $tr, "suffix"
    s += "-#{suffix}" unless suffix is ""
    $tr.find("td:last-child").text s

$.widget "springcrm.configSeqNumber", ConfigSeqNumberWidget


#-- Main ----------------------------------------

$("#seq-numbers").configSeqNumber()

$serviceSelector = $(".service-selector")
loadServiceUrl = $serviceSelector.parents("fieldset")
  .data("load-service-url")
$serviceSelector.autocompleteex
  noSelectValue: ''
  valueInput: ->
    this.element
      .parents(".field")
        .find("input:hidden")
