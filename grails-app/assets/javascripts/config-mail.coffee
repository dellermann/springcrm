#
# config-mail.coffee
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


$ = jQuery


$('#config-form').on('change', 'input[name="config.mailUseConfig"]', ->
    $ = jQuery

    enabled = $(this).val() is 'true'
    $(
      '#host, #port, #mail-auth-false, #mail-auth-true, ' +
      '#mail-encryption-none, #mail-encryption-ssl, #mail-encryption-starttls'
    ).enable enabled

    enabled = enabled and $('#mail-auth-true:checked').length > 0
    $('#user-name, #password').enable enabled
  )
  .on('change', 'input[name="config.mailAuth"]', ->
    $ = jQuery

    $useConfig = $('#mail-use-config-user:checked')
    enabled = $(this).val() is 'true' and $useConfig.length > 0
    $('#user-name, #password').enable enabled
  )

$('input[name="config.mailUseConfig"]:checked').trigger 'change'
$('input[name="config.mailAuth"]:checked').trigger 'change'
