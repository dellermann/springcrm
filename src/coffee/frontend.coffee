#
# frontend.coffee
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
$createTicketForm = $("#create-ticket-form")
$sendMessageForm = $("#send-message-form")
storage = $.localStorage


#-- Functions -----------------------------------

onCloseSendMessageForm = ->
  $createTicketForm.slideDown()
  $sendMessageForm.slideUp()
  $(".content-table tr").removeClass "active"

onCloseTicket = ->
  $.confirm $L("ticket.changeStage.closed.confirm")

onOpenSendMessageDlg = ->
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
  false

onSendMessage = ->
  $ = jQuery

  $sendMsgForm = $sendMessageForm
  $sendMsgForm.slideUp() unless $sendMsgForm.css("display") is "none"
  $tr = $(this).parents("tr")
    .addClass("active")
    .siblings()
      .removeClass("active")
    .end()
  $createTicketForm.slideUp()
  $sendMsgForm.slideDown()
    .find("input[name=id]")
      .val $tr.data("ticket-id")
  false


#-- Main ----------------------------------------

$("#font-size-sel").fontsize
  change: (event, fontSize) ->
    storage.set 'fontSize', fontSize
  currentSize: storage.get('fontSize')
$sendMessageForm.on "click", ".cancel-btn", onCloseSendMessageForm
$(".content-table").on("click", ".send-btn", onSendMessage)
  .on("click", ".close-btn", onCloseTicket)
$("#send-message-btn").on "click", onOpenSendMessageDlg

