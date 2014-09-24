#
# ticket-frontend.coffee
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
#= require frontend
#= require ticket


class HelpdeskFrontend

  #-- Instance variables ------------------------

  $createTicketForm: $("#create-ticket-form")
  $sendMessageForm: $("#send-message-form")


  #-- Constructor -------------------------------

  constructor: ->
    @$sendMessageForm.on "click", ".cancel-btn", => @_onCloseSendMessageForm()
    $(".create-ticket-btn").on "click", => @_onClickCreateTicketBtn()
    $("#main-container")
      .on("click", ".send-btn", (event) => @_onClickSendBtn event)
      .on("click", ".close-btn", => @_onCloseTicket())
    $("#send-message-btn").on "click", => @_onOpenSendMessageDlg()


  #-- Non-public methods ------------------------

  _onClickCreateTicketBtn: ->
    $ = jQuery

    $form = @$createTicketForm
    $form.slideUp() unless $form.css("display") is "none"
    $form.slideDown()
    @$sendMessageForm.slideUp()
    $(".content-table tr").removeClass "active"
    $(".flash-message").remove()

  _onClickSendBtn: (event) ->
    $form = @$sendMessageForm
    $form.slideUp() unless $form.css("display") is "none"
    $tr = $(event.currentTarget).parents("tr")
      .addClass("active")
      .siblings()
        .removeClass("active")
      .end()
    @$createTicketForm.slideUp()
    $form.slideDown()
      .find("input[name=id]")
        .val $tr.data("ticket-id")
    false

  _onCloseSendMessageForm: ->
    $ = jQuery

    @$createTicketForm.slideDown()
    @$sendMessageForm.slideUp()
    $(".content-table tr").removeClass "active"
    $(".flash-message").remove()

  _onCloseTicket: ->
    $.confirm $L("ticket.changeStage.closed.confirm")

  _onOpenSendMessageDlg: ->
    $ = jQuery

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


new HelpdeskFrontend()

