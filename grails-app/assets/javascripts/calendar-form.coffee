#
# calendar-form.coffee
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
#= require _handlebars-ext
#= require templates/calendar/add-reminder


REMINDER_SEL_OPTIONS = [
  '1m', '5m', '10m', '15m', '20m', '30m', '45m', '1h', '2h', '3h', '4h', '5h',
  '6h', '8h', '12h', '1d', '2d', '3d', '1w', '2w', '4w'
]
$ = jQuery
$tabs = $('#tabs-recurrence-type')
addReminderTemplate = null


#-- Functions ------------------------------

enableRecurrenceEndCtrls = (value) ->
  switch value
    when 'until'
      $input = $('#recurrence\\.until-date').enable()
      $('#recurrence\\.cnt').disable()
    when 'count'
      $input = $('#recurrence\\.cnt').enable()
      $('#recurrence\\.until-date').disable()
    when 'none'
      $('#recurrence\\.until-date').disable()
      $('#recurrence\\.cnt').disable()
  $input

getReminderOptions = (value) ->
  res = []
  for o in REMINDER_SEL_OPTIONS
    v = parseInt o, 10
    switch o.charAt(o.length - 1)
      when 'm'
        u = 'minute'
      when 'h'
        u = 'hour'
      when 'd'
        u = 'day'
      when 'w'
        u = 'week'
    u += 's' if v > 1
    res.push
      optionValue: o
      selected: (if value is o then 'selected="selected"' else '')
      unit: $L("calendarEvent.reminder.unit.#{u}")
      value: v
  res

initRecurrenceTypes = ->
  $ = jQuery

  recurType = Number $('#tabs-recurrence-type input:radio:checked').val()
  if recurType is 0
    $('#recurrence-end').hide()
  else
    $("#recurrence-interval-#{recurType}").val $('#recurrence-interval').val()
    $("#recurrence-monthDay-#{recurType}").val $('#recurrence-monthDay').val()
    $("#recurrence-weekdayOrd-#{recurType}").val $('#recurrence-weekdayOrd').val()
    $("#recurrence-month-#{recurType}").val $('#recurrence-month').val()
    if recurType in [30, 50, 70]
      wds = $('#recurrence-weekdays').val().split /,/
      if recurType is 30
        $('#tabs-recurrence-type-30 input:checkbox').attr 'checked', false
        $("#recurrence-weekdays-30-#{wd}").attr 'checked', true for wd in wds
      else
        $("#recurrence-weekdays-#{recurType}").val wds[0] if wds.length
    $('#recurrence-end').show()
  $tabs.find("a[href='#tabs-recurrence-type-#{recurType}']").click()

initReminders = ->
  $ = jQuery

  reminders = []
  val = $('#reminders').val()
  reminders.push options: getReminderOptions r for r in val.split /\s/ if val

  s = renderAddReminderTemplate reminders: reminders
  $('#reminder-selectors').html(s)
    .on 'click', '.button', onClickReminderRemoveBtn
  $('#reminder-add-btn').on 'click', onClickReminderAddBtn

onChangeAllDay = ->
  $('#start-time').add('#end-time')
    .toggleEnable not @checked

onChangeTabContent = ->
  $ = jQuery

  if $(this).attr('id').match /^tabs-recurrence-type-(\d+)$/
    $("#recurrence-type-#{RegExp.$1}").trigger 'click'

onClickReminderAddBtn = ->
  $ = jQuery

  s = renderAddReminderTemplate reminders: [options: getReminderOptions()]
  $s = $(s)

  $sel = $('#reminder-selectors')
  $ul = $sel.find('ul')
  if $ul.length
    $ul.append $s.find 'li'
  else
    $sel.append $s
  $('html').scrollTop $(this).position().top
  false

onClickReminderRemoveBtn = ->
  $ = jQuery

  $(this).parent().remove()
  $('html').scrollTop $('#reminder-add-btn').position().top
  false

onLoadedOrganization = (data) ->
  s = ''
  s += data.shippingAddrStreet  if data.shippingAddrStreet
  if data.shippingAddrPostalCode or data.shippingAddrLocation
    s += ', '  if s isnt ''
    s += data.shippingAddrPostalCode + ' '  if data.shippingAddrPostalCode
    s += data.shippingAddrLocation  if data.shippingAddrLocation
  $('#location').val s

onSelectOrganization = (value) ->
  $ = jQuery

  $.getJSON $('#organization').data('get-url'),
      id: value
    , onLoadedOrganization

onSelectTab = ->
  $ = jQuery

  val = Number $(this).val()
  $tabs.find("a[href='#tabs-recurrence-type-#{val}']").click()
  $('#recurrence-end').toggle val isnt 0

onSubmitForm = ->
  $ = jQuery

  recurType = Number $('#tabs-recurrence-type input:radio:checked').val()
  if recurType > 0
    val = $("#recurrence-interval-#{recurType}").val()
    $('#recurrence-interval').val val or 1
    $('#recurrence-monthDay').val $("#recurrence-monthDay-#{recurType}").val()
    $('#recurrence-weekdayOrd').val $("#recurrence-weekdayOrd-#{recurType}").val()
    $('#recurrence-month').val $("#recurrence-month-#{recurType}").val()
    if recurType is 30
      wds = []
      $('#tabs-recurrence-type-30 input:checkbox:checked').each ->
        wds.push Number $(this).val()
      wds.sort()
      $('#recurrence-weekdays').val wds.join ','
    else if recurType is 50
      $('#recurrence-weekdays').val $('#recurrence-weekdays-50').val()
    else if recurType is 70
      $('#recurrence-weekdays').val $('#recurrence-weekdays-70').val()

  reminders = []
  $('#reminder-selectors select').each ->
    reminders.push $(this).val()
  $('#reminders').val reminders.join ' '
  true

renderAddReminderTemplate = (data) ->
  Handlebars.templates['calendar/add-reminder'] data


#-- Main -----------------------------------

# initialize autocompletion fields
$('#organization').autocompleteex select: onSelectOrganization

# initialize all-day checkbox
$('#allDay').on('change', onChangeAllDay)
  .triggerHandler 'change'

# initialize tabs
$tabs.tabs()
  .on('change', 'input:radio', onSelectTab)
  .on('change', '.ui-tabs-panel', onChangeTabContent)

# initialize recurrence controls
$('#recurrence-end').on 'change', 'dt input:radio', ->
  $input = enableRecurrenceEndCtrls $(this).val()
  $input.focus() if $input
enableRecurrenceEndCtrls $('#recurrence-end dt input:radio:checked').val()
initRecurrenceTypes()

# initialize submit hook
$('#calendarEvent-form').on 'submit', onSubmitForm

# initialize reminders
initReminders()

