#
# calendar-view.coffee
#
# Copyright (c) 2011-2013, Daniel Ellermann
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


$ = jQuery
$calendar = $("#calendar")


# Called if the user clicks a day in the calendar view.  The method loads the
# form to create a new calendar event.
#
# @param {Date} date      the currently clicked date, optionally with time
# @param {Boolean} allDay whether or not to load the events for all day
#
onClickDay = (date, allDay) ->
  fc = $.fullCalendar
  d = fc.formatDate date, $L("default.format.date")
  t = fc.formatDate date, $L("default.format.time")
  start = encodeURIComponent "#{d} #{t}"
  allDay = encodeURIComponent String(allDay)
  window.location.href = $calendar.data("create-event-url") + "?start=#{start}&allDay=#{allDay}"

# Called if the dialog to select a date to go to should be opened.
#
onClickGotoDateBtn = ->
  $dlg = $("#goto-date-dialog")
  onOk = ->
    s = $dlg.find("input").val()
    if s
      date = s.parseDate "date"
      $calendar.fullCalendar "gotoDate", date
    $dlg.dialog("close")

  $dlg.find("input").on "keydown", (event) ->
    onOk() if event.which == 13
    true
  $dlg.dialog
    buttons: [
        click: onOk
        text: $L("default.button.ok.label")
      ,
        click: ->
          $dlg.dialog("close")
        text: $L("default.button.cancel.label")
    ]
    modal: true
    width: 200

# Called if the user clicks the list view button.  The method loads the list
# view of the calendar.
#
onClickListViewBtn = ->
  window.location.href = $calendar.data("list-view-url")

# Called if a calendar event is moved by drag & drop, that is, the start and
# end time is changed.
#
# @param {Object} event         data about the calendar event
# @param {Number} dayDelta      the difference of the start and end time in days
# @param {Number} minuteDelta   the difference of the start and end time in minutes
# @param {Boolean} allDay       whether or not the calendar event has been moved to the all-day section
# @param {Function} revertFunc  a function to be called if the moved calendar event is to revert, for example, in case of an AJAX error
#
onDropEvent = (event, dayDelta, minuteDelta, allDay, revertFunc) ->
  if allDay
    revertFunc.call()
    return

  $.ajax
    data:
      id: event.id
      start: event.start.getTime()
      end: event.end.getTime()
    error: ->
      revertFunc.call()
    url: $calendar.data("update-event-url")

# Called if the user hovers the buttons in the calendar view header.  The
# method simply adds a CSS class to visualize the hover state.
#
onHoverBtn = ->
  $(this).toggleClass "ui-state-hover"

# Called if a calendar event is resized, that is, the end time is changed.
#
# @param {Object} event         data about the calendar event
# @param {Number} dayDelta      the difference of the end time in days
# @param {Number} minuteDelta   the difference of the end time in minutes
# @param {Function} revertFunc  a function to be called if the resized calendar event is to revert, for example, in case of an AJAX error
#
onResizeEvent = (event, dayDelta, minuteDelta, revertFunc) ->
  $.ajax
    data:
      id: event.id
      start: event.start.getTime()
      end: event.end.getTime()
    error: ->
      revertFunc.call()
    url: $calendar.data("update-event-url")


$calendar.fullCalendar
  allDayText: $L("calendarEvent.allDay.label")
  axisFormat: $L("calendarEvent.axis.format")
  buttonText: $L("calendarEvent.button.text")
  columnFormat: $L("calendarEvent.column.format")
  dayClick: onClickDay
  dayNames: $L("weekdaysLong")
  dayNamesShort: $L("weekdaysShort")
  defaultView: $calendar.data("current-view")
  editable: true
  eventDrop: onDropEvent
  eventResize: onResizeEvent
  eventSources: [ url: $calendar.data("load-events-url") ]
  firstDay: $L("calendarFirstDay")
  isRTL: $L("calendarRTL")
  header:
    center: "title"
    left: "prev,next today"
    right: "agendaDay,agendaWeek,month"
  monthNames: $L("monthNamesLong")
  monthNamesShort: $L("monthNamesShort")
  theme: true
  timeFormat: $L("calendarEvent.time.format")
  titleFormat: $L("calendarEvent.title.format")

$(".fc-header-left").append """
  <span class="fc-button fc-button-goto ui-state-default ui-corner-right">
    <span class="fc-button-inner">
      <span class="fc-button-content">#{$L('calendarEvent.button.text.gotoDate')}</span>
      <span class="fc-button-effect"></span>
    </span>
  </span>
"""
$(".fc-button-today").removeClass "ui-corner-right"
$(".fc-button-goto").on("click", onClickGotoDateBtn)
  .on("hover", onHoverBtn)
$(".fc-header-right").append """
  <span class="fc-button fc-button-list ui-state-default ui-corner-right">
    <span class="fc-button-inner">
      <span class="fc-button-content">#{$L('calendarEvent.button.text.list')}</span>
      <span class="fc-button-effect"></span>
    </span>
  </span>
"""
$(".fc-button-month").removeClass "ui-corner-right"
$(".fc-button-list").on("click", onClickListViewBtn)
  .on("hover", onHoverBtn)
