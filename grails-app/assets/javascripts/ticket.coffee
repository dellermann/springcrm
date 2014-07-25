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


#-- Functions -----------------------------------

# Called if the user clicks the button to create a note.
#
# @return {Boolean} always `false` to prevent event bubbling
#
onClickCreateNoteBtn = ->
  $this = $(this)
  showMessageDialog $this.data("title"), $this.data("submit-url")

# Called if the user clicks the button to send a message to the customer.
#
# @return {Boolean} always `false` to prevent event bubbling
#
onClickSendMessageToCustomerBtn = ->
  $this = $(this)
  showMessageDialog $this.data("title"), $this.data("submit-url")

# Called if the user clicks a menu item to send a message to a user.
#
# @return {Boolean} always `false` to prevent event bubbling
#
onClickSendMessageToUserMenu = ->
  $ = jQuery
  $this = $(this)
  $span = $("#send-message-to-user-menu .button:first-child")
  showMessageDialog $span.data("title"), $span.data("submit-url"), $this.data("user-id")

# Shows the dialog to send messages or create notes.
#
# @param {String} title             the title which should be displayed in the dialog
# @param {String} url               the URL which should be called when submitting the form
# @param {String|Number} recipient  the ID of the user which is the recipient of the message; if an empty string no recipient is set
# @return {Boolean}                 always `false` to prevent event bubbling
#
showMessageDialog = (title, url, recipient = "") ->
  $("#send-message-dialog")
    .find("form")
      .attr("action", url)
    .end()
    .find("#recipient")
      .val(recipient)
    .end()
    .dialog
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
      title: title
      width: "40em"
  false


#-- Main ----------------------------------------

$("#send-message-to-customer-btn").on "click", onClickSendMessageToCustomerBtn
$("#send-message-to-user-menu").on "click", "ul a", onClickSendMessageToUserMenu
$("#create-note-btn").on "click", onClickCreateNoteBtn
$("#take-on-btn").on "click", ->
  $.confirm $L("ticket.takeOn.confirm")
$("#assign-user-menu").on "click", "a", ->
  $.confirm $L("ticket.changeStage.assign.confirm")
$("#close-ticket-btn").on "click", ->
  $.confirm $L("ticket.changeStage.closed.confirm")

