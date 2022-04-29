#
# invoicing-transaction-list.coffee
#
# Copyright (c) 2011-2022, Daniel Ellermann
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


$ = jQuery


$('.caption-with-year-selector')
    .on('click', '.open-selector-link', (event) ->
        $(event.delegateTarget)
            .find('.selector-container, .btn-show-all, .close-selector-link')
                .show()
        $(event.currentTarget).hide()

        false
    )
    .on('click', '.btn-show-all', (event) ->
        url = new HttpUrl(window.location.href)
        url.overwriteQuery year: ''

        window.location.href = url.toString()

        false
    )
    .on('click', '.close-selector-link', (event) ->
        $(event.delegateTarget)
            .find('.selector-container, .btn-show-all, .close-selector-link')
                .hide()
            .end()
            .find('.open-selector-link')
                .show()

        false
    )
    .on('change', '#year', (event) ->
        url = new HttpUrl(window.location.href)
        url.overwriteQuery year: $(event.currentTarget).val()

        window.location.href = url.toString()

        return
    )
