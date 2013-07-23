#
# ticket.coffee
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


onClickSendMessageBtn = ->
  $("#send-message-dialog").dialog
    buttons: [
        click: ->
          $(this).find("form").submit()
        text: $L("default.button.send.label")
      ,
        class: "red"
        click: ->
          $(this).dialog("close")
        text: $L("default.button.cancel.label")
    ]
    minHeight: "15em"
    modal: true
    width: "40em"

$("#send-message-btn").on "click", onClickSendMessageBtn
$("#close-ticket-btn").on "click", ->
  $.confirm $L("ticket.changeStage.closed.confirm")
