#
# helpdesk-form.coffee
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


$ = jQuery
$accessCodeInput = $("#accessCode")
$generateAccessCodeBtn = $("#generate-access-code")


# Generates an access code of the given length.
#
# @param [Number] length  the length of the generated access code
# @return [String]        the generated access code
#
generateAccessCode = (length = 6) ->
  s = ""
  for i in [1..length]
    if Math.random() < 0.5
      n = Math.floor(Math.random() * 10)
      c = String n
    else
      n = Math.floor(Math.random() * 26 + 65)
      c = String.fromCharCode n
    s += c
  s


$('#organization-select').on 'change', ->
  organizationName = $(this).find(':selected').text()
  $name = $("#name")
  $name.val organizationName unless $name.val()

$generateAccessCodeBtn.on 'click', -> $accessCodeInput.val generateAccessCode()
$generateAccessCodeBtn.triggerHandler 'click' unless $accessCodeInput.val()

