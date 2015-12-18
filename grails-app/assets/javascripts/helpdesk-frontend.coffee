#
# ticket-frontend.coffee
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

# Class `HelpdeskFrontend` represents handling of the helpdesk frontend.
#
# @author   Daniel Ellermann
# @version  2.0
#
class HelpdeskFrontend

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery

  # @nodoc
  $LANG = $L


  #-- Constructor -------------------------------

  # Initializes the helpdesk frontend using the given container.
  #
  # @param [jQuery, Element, String] element  a jQuery object, an element or a jQuery selector representing the base element containing the whole show view
  #
  constructor: (element) ->
    $L = $LANG

    $elem = $(element)
      .on('click', '.send-message-link', => @_onClickSendMessageLink())
      .on(
        'click', '.close-ticket-link',
        (event) => @_onClickCloseTicketLink event
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

  # Called when the link to close the ticket has been clicked.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickCloseTicketLink: (event) ->
    $(event.currentTarget).confirmLink $L('ticket.changeStage.closed.confirm')

  # Called when a the link to send a message has been clicked.
  #
  # @return [Boolean] always `false` to prevent event bubbling
  # @private
  #
  _onClickSendMessageLink: ->
    @_showMessageDialog()

    false

  # Shows the dialog to send messages or create notes.
  #
  # @private
  #
  _showMessageDialog: ->
    $('#send-message-dialog')
      .on(
        'click', '.send-btn', (event) ->
          $(event.delegateTarget).find('form')
            .submit()
      )
      .modal('show')

    return


#== Main ========================================

new HelpdeskFrontend $('#frontend-container')
