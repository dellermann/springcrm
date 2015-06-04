#
# ticket-show.coffee
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
#= require _fileinput-builder
#= require _handlebars-ext
#= require templates/widgets/file-upload-document


#== Classes =====================================

# Class `TicketShowPage` represents the show view of the ticket section.
#
# @author   Daniel Ellermann
# @version  2.0
#
class TicketShowPage

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Initializes the ticket show view.
  #
  # @param [jQuery, Element, String] element  a jQuery object, an element or a jQuery selector representing the base element containing the whole show view
  #
  constructor: (element) ->
    @$element = $elem = $(element)
      .on(
        'click', '.take-on-link', (event) => @_onClickTakeOnLink event
      )
      .on(
        'click', '.close-ticket-link',
        (event) => @_onClickCloseTicketLink event
      )
      .on(
        'click', '.send-message-to-customer-link',
        (event) => @_onClickSendMessageToCustomerLink event
      )
      .on(
        'click', '.send-message-to-user-link',
        (event) => @_onClickSendMessageToUserLink event
      )
      .on(
        'click', '.assign-user-link',
        (event) => @_onClickAssignUserLink event
      )
      .on(
        'click', '.create-note-link',
        (event) => @_onClickCreateNoteLink event
      )

    tmpl = Handlebars.templates['widgets/file-upload-document']

    builder = new SPRINGCRM.FileinputBuilder
      browseIcon: '<i class="fa fa-file-o"></i> '
      browseLabel: $L('ticket.attachment.select')
      layoutTemplates:
        main1: tmpl section: 'main1'
      removeClass: 'btn btn-danger btn-sm'
      removeIcon: '<i class="fa fa-trash-o"></i> '
      removeLabel: $L('ticket.attachment.delete')
      showPreview: false
      showRemove: true

    builder.build $elem.find('#attachment')


  #-- Non-public methods ------------------------

  # Called when the link to assign to a new user has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickAssignUserLink: (event) ->
    $(event.currentTarget).confirmLink $L('ticket.changeStage.assign.confirm')

  # Called when the link to close the ticket has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickCloseTicketLink: (event) ->
    $(event.currentTarget).confirmLink $L('ticket.changeStage.closed.confirm')

  # Called when the button to create a note has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickCreateNoteLink: (event) ->
    $this = $(event.currentTarget)
    @_showMessageDialog $this.data('title'), $this.data('submit-url')

    false

  # Called when the button to send a message to the customer has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickSendMessageToCustomerLink: (event) ->
    $target = $(event.currentTarget)
    @_showMessageDialog $target.data('title'), $target.data('submit-url')

    false

  # Called when a menu item to send a message to a user has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickSendMessageToUserLink: (event) ->
    $target = $(event.currentTarget)
    @_showMessageDialog(
      $target.data('title'), $target.data('submit-url'),
      $target.data('user-id')
    )

    false

  # Called when the link to take on the ticket has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickTakeOnLink: (event) ->
    $(event.currentTarget).confirmLink $L('ticket.takeOn.confirm')

  # Shows the dialog to send messages or create notes.
  #
  # @param [String] title             the title which should be displayed in the dialog
  # @param [String] url               the URL which should be called when submitting the form
  # @param [String, Number] recipient the ID of the user which is the recipient of the message; if an empty string no recipient is set
  # @private
  #
  _showMessageDialog: (title, url, recipient = '') ->
    $('#send-message-dialog')
      .find('.modal-title')
        .text(title)
      .end()
      .find('form')
        .attr('action', url)
      .end()
      .find('#recipient')
        .val(recipient)
      .end()
      .on(
        'click', '.send-btn', (event) ->
          $(event.delegateTarget).find('form')
            .submit()
      )
      .modal('show')

    return


#== Main ========================================

new TicketShowPage '.inner-container'

# vim:set ts=2 sw=2 sts=2:
