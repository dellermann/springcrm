#
# report-sales-journal.coffee
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
#= require jquery-ui-selectboxit


$ = jQuery


# Called if the year selector has been changed.
#
onChangeYearSelector = ->
  submitFilter $("#month-selector").find(".current")

# Called if the month selector has been clicked.
#
# @param {Object} event the event data
# @return {Boolean}     always `false` to prevent event bubbling
#
onClickMonthSelector = (event) ->
  $li = $(event.target).parent("li")
  $li
    .siblings()
      .removeClass("current")
    .end()
    .addClass "current"
  submitFilter $li
  false

# Submits the currently selected month and year to the server.
#
# @param {jQuery} $monthLink  the currently clicked month link
#
submitFilter = ($monthLink) ->
  params =
    month: $monthLink.data("month")
    year: $("#year-selector").val()

  url = $monthLink.find("a").attr("href")
  url += (if /\?/.test(url) then "&" else "?") + $.param(params)
  window.location.href = url

$("#month-selector").click onClickMonthSelector
$("#year-selector").selectBoxIt(
    theme: "jqueryui"
  ).change onChangeYearSelector
