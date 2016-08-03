#
# report-sales-journal.coffee
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

# Class `TimeSpanReport` represents a widget that handles user actions in report
# which display entries of a particular time span.
#
# @author   Daniel Ellermann
# @version  2.0
#
class TimeSpanReport

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new instance to handle user actions in sales journals.
  #
  constructor: ->
    $('.month-year-selector')
      .on('click', '.btn-month', (event) => @_onClickMonth event)
      .on('click', '.year-selector a', (event) => @_onClickYear event)


  #-- Non-public methods ------------------------

  # Called if a month in the selector bar has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickMonth: (event) ->
    $ = jq

    $month = $(event.currentTarget)
      .siblings('.btn')
        .removeClass('active')
      .end()
      .addClass('active')
    @_submitFilter $month, $('.year-selector').data('current-year')

    false

  # Called if the year in the selector has been changed.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickYear: (event) ->
    $ = jq

    $target = $(event.currentTarget)
    $month = $('.month-year-selector .btn-month.active')
    $month = $('.month-year-selector .btn-whole-year') unless $month.length
    @_submitFilter $month, $target.data('year')

    false

  # Submits the currently selected month and year to the server and loads the
  # sales journal of that time.
  #
  # @param [jQuery] $aMonth the currently selected month link
  # @param [Number] year    the currently selected year
  # @private
  #
  _submitFilter: ($aMonth, year) ->
    url = new HttpUrl($aMonth.attr 'href')
    if year
      url.query.month = $aMonth.data 'month'
      url.query.year = year
    window.location.href = url.toString()



#== Main ========================================

new TimeSpanReport()
