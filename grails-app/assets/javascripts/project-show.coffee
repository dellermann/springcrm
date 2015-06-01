#
# project-show.coffee
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
#= require _jquery-ui-selectboxit
#= require _document-list


$ = jQuery


# The class `ProjectPhases` displays the list of project phases and handles
# all entries within.
#
# @author   Daniel Ellermann
# @version  2.0
#
class ProjectPhases
  
  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Instance variables ------------------------

  DEFAULT_OPTIONS =
    $output: $('#project-phase')
    changed: null
    create: null
    deleted: null
    select: null
    setPhaseUrl: null


  #-- Constructor -------------------------------

  # Creates a new project phases widget on the given element.
  #
  # @param [String, jQuery] elem  the given element
  # @param [Object] options       any options that should be set
  #
  constructor: (elem, options = {}) ->
    $ = jq
    @$element = $elem = $(elem)
    @options = options = $.extend {}, DEFAULT_OPTIONS, options

    @_initCallbacks()
    options.setPhaseUrl ?= $elem.data('set-phase-url')

    $elem
      .on('click', 'h4', (event) => @_onChange event)
      .on(
        'click', '.project-phase-actions-create', (event) => @_onCreate event
      )
      .on(
        'click', '.project-phase-actions-select', (event) => @_onSelect event
      )
      .on('click', '.item-delete-btn', (event) => @_onDelete event)


  #-- Non-public methods ------------------------

  # Gets the name of the phase the given element belongs to.
  #
  # @param [jQuery] $elem the given element
  # @return [String]      the phase name
  # @private
  #
  _getPhaseName: ($elem) ->
    $elem.closest('section')
      .data 'phase'

  # Initializes the callback function with the given name in the options.  The
  # method creates a jQuery `Callbacks` object and adds the callback function
  # to it if it is specified.
  #
  # @param [String] name  the name of the callback option
  # @return [Callbacks]   the generated `Callbacks` object
  # @private
  #
  _initCallback: (name) ->
    options = @options
    f = options[name]
    callbacks = $.Callbacks()
    callbacks.add f if f?
    options[name] = callbacks

  # Initializes all callback functions.
  #
  # @private
  #
  _initCallbacks: ->
    callbacks = ['changed', 'create', 'deleted', 'select']
    @_initCallback name for name in callbacks

    return

  # Called if the project phase has been changed.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onChange: (event) ->
    $ = jq
    options = @options
    $target = $(event.currentTarget)

    options.$output.text $target.text()

    $section = $target.parents('.project-phases > section')
    $section.addClass('active')
      .siblings()
        .removeClass 'active'
    phaseName = @_getPhaseName($section)

    $.ajax(
        data:
          phase: phaseName
        url: options.setPhaseUrl
      )
      .done => options.changed.fireWith this, [phaseName]

    false

  # Called if a project item should be created.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onCreate: (event) ->
    @options.create.fireWith this, [@_getPhaseName $(event.currentTarget)]

    false

  # Called if a project item should be deleted.  The function displays a
  # confirmation dialog and sends a deletion request to the server.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onDelete: (event) ->
    $ = jq
    $target = $(event.currentTarget)

    $.Deferred()
      .resolve()
      .then( -> $.confirm $L('project.removeItem.confirm.msg'))
      .done( -> $.ajax url: $target.attr('href'))
      .done( =>
        $target.closest('li')
          .remove()
        @options.deleted.fireWith this
      )

    false

  # Called if a project item should be selected from a list.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onSelect: (event) ->
    @options.select.fireWith this, [@_getPhaseName $(event.currentTarget)]
    false


# Class `ProjectCreateItemDlg` represents a dialog which is displayed to create
# a new project item.
#
# @author   Daniel Ellermann
# @version  2.0
#
class ProjectCreateItemDlg

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Creates and shows the dialog to create a new project item and sets the
  # current phase.
  #
  # @param [Element, String, jQuery] elem either an element, a jQuery selector or a jQuery object representing the dialog
  #
  constructor: (elem) ->
    @$element = $(elem)
      .on('click', '.modal-body .btn', (event) => @_onClickItemBtn event)


  #-- Public methods ----------------------------

  # Initializes the dialog with the given phase name and shows it.
  #
  # @param [String] phaseName the name of the currently active phase
  #
  show: (@phaseName) -> @$element.modal 'show'


  #-- Non-public methods ------------------------

  # Called if a button has been clicked to create a new project item.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `true` to prevent event bubbling
  # @private
  #
  _onClickItemBtn: (event) ->
    url = new HttpUrl $(event.currentTarget).attr('href')
    url.query.projectPhase = @phaseName
    window.location.href = url.toString()

    false


# The class `ProjectSelectItemDlg` represents a dialog that allows the user to
# select an existing content item.
#
# @author   Daniel Ellermann
# @version  2.0
#
class ProjectSelectItemDlg

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Creates and opens a new dialog on the given element using the stated phase.
  #
  # @param [Element, String, jQuery] elem either an element, a jQuery selector or a jQuery object representing the dialog
  #
  constructor: (elem) ->
    $ = jq
    @$element = $elem = $(elem)
    typeSelectorSel = '#select-project-item-type-selector'
    @$typeSelector = $(typeSelectorSel)
    @$header = $elem.find 'h2'
    itemListSel = '#select-project-item-list'
    @$itemList = $elem.find itemListSel
    @$searchField = $elem.find '#selector-search'

    @$searchArea = $elem.find '.search-field'
    @$documentList = $elem.find '#select-project-document-list'

    $elem
      .on('click', '.search-btn', => @_loadList())
      .on('click', '#select-project-item-add-btn', => @_onClickAddBtn())
      .on('change', typeSelectorSel, => @_onChangeType())
      .on('show.bs.modal', => @_onOpen())
      .on(
        'change', "#{itemListSel} .data-table th input:checkbox",
        (event) => @_onChangeWholeSelectionCheckbox event
      )
      .on(
        'click', "#{itemListSel} .data-table tbody a",
        (event) => @_onClickSelectItem event
      )
      .on('click', "#{itemListSel} a", (event) => @_onClickLink event)


  #-- Public methods ----------------------------

  # Initializes the dialog with the given phase name and shows it.
  #
  # @param [String] phaseName the name of the currently active phase
  #
  show: (@phaseName) -> @$element.modal 'show'


  #-- Non-public methods ------------------------

  # Changes the visibility of the given elements.
  #
  # @param [jQuery] $elem     the elements that should be affected
  # @param [Boolean] visible  `true` to show these elements; `false` otherwise
  # @private
  #
  _changeVisibility: ($elem, visible) ->
    $elem[if visible then 'show' else 'hide'].call $elem
    
    return

  # Gets the name of the controller of the item that are currently displayed.
  #
  # @return [String]  the controller name
  # @private
  #
  _getController: -> @_getSelectedType().controller

  # Gets the selected option type of items that are currently displayed.
  #
  # @return [Object]  information about the selected option or `null` if no option has been selected
  # @private
  #
  _getSelectedType: ->
    selectize = @_getTypeSelector()
    items = selectize.items

    if items.length then selectize.options[items[0]] else null

  # Gets the selectize widget of the type selector.
  #
  # @return [Selectize] the selectize widget
  # @private
  #
  _getTypeSelector: -> @$typeSelector[0].selectize

  # Loads the list of items with the given URL into the selector dialog.
  #
  # @param [String] url         the given URL; if `undefined` the URL of the currently selected data type is used
  # @param [Function] doneFunc  if specified this function is called if the list content has been loaded
  # @private
  #
  _loadList: (url, doneFunc) ->
    url ?= @_getSelectedType().value

    data = view: 'selector'
    search = @$searchField.val()
    data.search = search if search

    @$itemList.load url, data, doneFunc

    return

  # Called if the type selector has been changed.
  #
  # @private
  #
  _onChangeType: ->
    option = @_getSelectedType()

    changeVisibility = (itemListVisible) =>
      @_changeVisibility @$itemList, itemListVisible
      @_changeVisibility @$documentList, not itemListVisible

    @$header.text option.text

    url = option.value
    if option.controller is 'document'
      @_showDocumentSelector url, -> changeVisibility false
    else
      @$searchField.val ''
      @_loadList url, -> changeVisibility true


  # Called if the checkbox to select or unselect all entries in the item table
  # has been changed.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onChangeWholeSelectionCheckbox: (event) ->
    $target = $(event.currentTarget)

    $target.closest('.data-table')
      .find('tbody .col-type-row-selector input:checkbox')
        .prop 'checked', $target.is(':checked')

  # Called if the button to add an item has been clicked.
  #
  # @return [Boolean] always `false` to prevent event bubbling
  # @private
  #
  _onClickAddBtn: ->
    $ = jq

    if @_getController() is 'document'
      selection = @$documentList.documentlist 'selection'
      @_submitSelectedDocuments selection if selection.length
    else
      ids = []
      @$itemList.find('tbody :checked')
        .each -> ids.push $(this).closest('tr').data('item-id')
      @_submitSelectedItems ids if ids.length

    @$element.modal 'hide'
    false

  # Called if any link in the item selector dialog is clicked.  The method loads
  # the link into the same dialog.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickLink: (event) ->
    @_loadList $(event.currentTarget).attr 'href'

    false

  # Called if an item in the item selector dialog has been clicked and thus the
  # item is selected.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent event bubbling
  # @private
  #
  _onClickSelectItem: (event) ->
    event.stopImmediatePropagation()
    itemId = $(event.currentTarget).closest('tr')
      .data 'item-id'
    @_submitSelectedItems [itemId]
    @$element.dialog 'close'

    false

  # Called if the dialog should be displayed.
  #
  # @private
  #
  _onOpen: -> @$typeSelector.trigger 'change'

  # Called after the item selected from the item selector dialog has been
  # submitted to the server.
  #
  _onSubmittedSelectedItems: ->
    window.location.reload true

  # Shows the selector to select documents.
  #
  # @param [String] url         the URL used to obtain the documents from the server
  # @param [Function] doneFunc  if specified this function is called if the document list has been loaded
  # @private
  #
  _showDocumentSelector: (url, doneFunc) ->
    $dl = @$documentList
    if $dl.hasdocumentlist()
      doneFunc.call() if doneFunc?
    else
      $dl.documentlist
        hideActions: true
        init: doneFunc
        listUrl: url
        multiSelect: true
        scrollable: true

  # Submits the selected documents to the server to associate them to the
  # current project.
  #
  # @param [Array] documents  the paths of the selected documents
  #
  _submitSelectedDocuments: (documents) ->
    @_submitData
      projectPhase: @phaseName
      controllerName: 'document'
      documents: documents

  # Submits the selected items to the server to associate them to the current
  # project.
  #
  # @param [Array] ids  the IDs of the selected items
  #
  _submitSelectedItems: (ids) ->
    @_submitData
      projectPhase: @phaseName
      controllerName: @_getController()
      itemIds: ids.join()

    return

  # Submits the given data via AJAX.
  #
  # @param [Object] data  the data that should be submitted
  # @private
  #
  _submitData: (data) ->
    $.ajax(
        data: data
        type: 'POST'
        url: @$element.data 'submit-url'
      )
      .done => @_onSubmittedSelectedItems()

    return


# The class `ProjectShow` represents the whole show view of the project
# section.  It displays and handles the project phases list and the dialogs
# to create or select project items.
#
# @author   Daniel Ellermann
# @version  2.0
#
class ProjectShowPage

  #-- Internal variables ------------------------

  # @nodoc
  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new handler of the show view of the project section.
  #
  constructor: ->
    createDlg = new ProjectCreateItemDlg '#create-project-item-dialog'
    selectDlg = new ProjectSelectItemDlg '#select-project-item-dialog'

    new ProjectPhases '#project-phases',
      create: (phaseName) -> createDlg.show phaseName
      select: (phaseName) -> selectDlg.show phaseName

    $('#project-status').on 'change', (event) => @_onSelectProjectStatus event


  #-- Non-public methods ------------------------

  # Called if the project status has been changed.
  #
  # @param [Event] event  any event data
  # @private
  #
  _onSelectProjectStatus: (event) ->
    $ = jq
    $target = $(event.currentTarget)

    status = $target.val()
    $.get $target.data('submit-url'), status: status

    return


#== Main ========================================

new ProjectShowPage()

# vim:set ts=2 sw=2 sts=2:

