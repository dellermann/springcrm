#
# ticket-form.coffee
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

# Class `TicketFormPage` represents the form view of the ticket section.
#
# @author   Daniel Ellermann
# @version  2.0
#
class TicketFormPage

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Initializes the ticket form view.
  #
  # @param [jQuery, Element, String] element  a jQuery object, an element or a jQuery selector representing the base element containing the whole form view
  #
  constructor: (element) ->
    @$element = $elem = $(element)

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


#== Main ========================================

new TicketFormPage '.inner-container'

# vim:set ts=2 sw=2 sts=2:
