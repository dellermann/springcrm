#
# settings-google-auth.coffee
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
#=require application


$ = jQuery


$('#google-auth-revoke').on 'click', ->
  $('#dialog-confirm').dialog
    buttons: [
        class: 'red'
        click: ->
          $this = $(this)
          $this.dialog 'close'
          window.location.href = $this.data 'submit-url'
        text: $L('user.settings.googleAuth.revoke.confirm.disconnect')
      ,
        click: -> $(this).dialog 'close'
        text: $L('default.button.cancel.label')
    ]
    modal: true
    resizable: false

