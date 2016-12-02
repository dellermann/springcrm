#
# projects.coffee
#
# Copyright (c) 2011-2016, Daniel Ellermann
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


# jQuery has been loaded by overview.coffee before
$ = jQuery


#== Classes =====================================

# The class `ProjectStatusButtons` handles the buttons in the panel "projects"
# on the overview page.
#
# @author   Daniel Ellermann
# @version  2.1
# @since    2.1
#
class ProjectStatusButtons

  #-- Internal variables ------------------------

  $ = jq = jQuery


  #-- Constructor -------------------------------

  # Creates a new project status button handling instance with the given panel.
  #
  # @param [jQuery] $element  the panel containing the project list
  #
  constructor: ($element) ->
    @$element = $element
      .on(
        'click', '.panel-body .project-status-buttons a',
        (event) => @_onClickStatusButton event
      )


  #-- Non-public methods ------------------------

  # Called when a status button has been clicked.  The method submits the
  # selected status is submitted and the panel is reloaded.
  #
  # @param [Event] event  any event data
  # @return [Boolean]     always `false` to prevent default behaviour
  # @private
  #
  _onClickStatusButton: (event) ->
    $button = $(event.currentTarget)
    url = $button.attr 'href'

    $.ajax(url)
      .done -> $('.overview-panels').overviewpanels 'reloadPanel', 'projects'

    false


#== Main ========================================

new ProjectStatusButtons $('#projects')
