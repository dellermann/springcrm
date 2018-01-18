#
# person-form.coffee
#
# Copyright (c) 2011-2018, Daniel Ellermann
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
#= require widgets/addr-fields
#= require _fileinput-builder
#= require _handlebars-ext
#= require templates/widgets/file-upload-image


$ = jQuery


# Class `PictureFileinput` represents a file input widget for setting the
# picture of a person.
#
# @author   Daniel Ellermann
# @version  3.0
#
class PictureFileinput

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new file input widget for setting the picture of a person.
  #
  # @param [Element] element  the file input control which is augmented as
  #                           widget
  #
  constructor: (element) ->
    $ = __jq

    @$element = $(element)
    @$pictureRemove = $('#pictureRemove')
    @url = $element.data 'picture'
    @newPicture = false

    @_initBuilder()


  #-- Non-public methods ------------------------

  # Initializes the builder to build a fileinput widget.  The method loads the
  # necessary templates, initializes the preview of the existing picture, and
  # registers the event listeners.
  #
  # @private
  #
  _initBuilder: ->
    tmpl = Handlebars.templates['widgets/file-upload-image']

    builder = new SPRINGCRM.FileinputBuilder
      browseIcon: '<i class="fa fa-picture-o"></i> '
      browseLabel: $L('person.picture.selectButton')
      layoutTemplates:
        preview: tmpl section: 'preview'
      maxFileSize: 1024
      previewFileType: 'image'
      previewTemplates:
        generic: tmpl section: 'preview-generic'
        image: tmpl section: 'preview-image'
        other: tmpl section: 'preview-other'
    builder.addOptions @_initPreviewOptions()

    builder.build @$element
    @_registerListeners()

    return

  # Initializes the initial preview if a picture already exists.
  #
  # @return [Object]  any options which should be set to display the initial
  #                   preview
  # @private
  #
  _initPreviewOptions: ->
    previewOptions = {}

    url = @url
    if url
      previewOptions.initialPreview = [
          """
<img src="#{url}" class="file-preview-image img-responsive" alt=""/>
"""
        ]

    @previewOptions = previewOptions

  # Called if the user removes the file from the input.  The method sets the
  # hidden picture remove field to inform the server that the picture should
  # be removed.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onFileCleared: (event) ->
    if @newPicture
      if @url
        $target = $(event.currentTarget)
        window.setTimeout( =>
            $target.fileinput 'refresh', @previewOptions
            @_registerListeners $target
            @newPicture = false

            return
          , 0
        )
    else
      @$pictureRemove.val '1'

    return

  # Called if a new picture has been selected and loading into the preview.
  #
  # @private
  #
  _onFileLoaded: ->
    @newPicture = true
    @$pictureRemove.val '0'

    return

  # Registers any event listeners for the file input control.
  #
  # @private
  #
  _registerListeners: ->
    @$element
      .on('filecleared', (event) => @_onFileCleared event)
      .on('fileloaded', => @_onFileLoaded())


# Class `PersonFormPage` represents the scripting for pages containing the
# person forms.
#
# @author   Daniel Ellermann
# @version  3.0
# @since    3.0
#
class PersonFormPage

  #-- Internal variables ------------------------

  # @nodoc
  $ = __jq = jQuery

  # @nodoc
  $LANG = $L


  #-- Constructor -------------------------------

  # Initializes the person form page.
  #
  constructor: ->
    $ = __jq
    $L = $LANG

    $('#picture').each -> new PictureFileinput(this)

    $('.addresses').addrfields
      menuItems:
        left: [
            action: 'clear'
            text: $L('person.mailingAddr.clear')
          ,
            action: 'copy'
            text: $L('person.mailingAddr.copy')
          ,
            action: 'loadFromOrganization'
            prefix: 'billingAddr'
            text: $L('person.addr.fromOrgBillingAddr')
          ,
            action: 'loadFromOrganization'
            prefix: 'shippingAddr'
            text: $L('person.addr.fromOrgShippingAddr')
        ]
        right: [
            action: 'clear'
            text: $L('person.otherAddr.clear')
          ,
            action: 'copy'
            text: $L('person.otherAddr.copy')
          ,
            action: 'loadFromOrganization'
            prefix: 'billingAddr'
            text: $L('person.addr.fromOrgBillingAddr')
          ,
            action: 'loadFromOrganization'
            prefix: 'shippingAddr'
            text: $L('person.addr.fromOrgShippingAddr')
        ]
      organizationId: '#organization-select'


new PersonFormPage()
