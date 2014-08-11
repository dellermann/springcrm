#
# core-different-locale-test.coffee
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


#-- Feature tests -------------------------------

QUnit.module 'Numbers'
QUnit.test 'Number formatting without precision', (assert) ->
  assert.equal (45).format(), '45'
  assert.equal (-45).format(), '-45'
  assert.equal (45.123).format(), '45.123'
  assert.equal (-45.123).format(), '-45.123'
  assert.equal (0.123456).format(), '0.123456'
  assert.equal (-0.123456).format(), '-0.123456'
  assert.equal (1234567.123).format(), '1,234,567.123'
  assert.equal (-1234567.123).format(), '-1,234,567.123'
  assert.equal (123456.123).format(), '123,456.123'
  assert.equal (-123456.123).format(), '-123,456.123'
  assert.equal (0 / 0).format(), '---'

QUnit.test 'Number formatting with null precision', (assert) ->
  n = null
  assert.equal (45).format(n), '45'
  assert.equal (-45).format(n), '-45'
  assert.equal (45.123).format(n), '45.123'
  assert.equal (-45.123).format(n), '-45.123'
  assert.equal (0.123456).format(n), '0.123456'
  assert.equal (-0.123456).format(n), '-0.123456'
  assert.equal (1234567.123).format(n), '1,234,567.123'
  assert.equal (-1234567.123).format(n), '-1,234,567.123'
  assert.equal (123456.123).format(n), '123,456.123'
  assert.equal (-123456.123).format(n), '-123,456.123'
  assert.equal (0 / 0).format(n), '---'

QUnit.test 'Number formatting with precision', (assert) ->
  n = 2
  assert.equal (45).format(n), '45.00'
  assert.equal (-45).format(n), '-45.00'
  assert.equal (45.123).format(n), '45.12'
  assert.equal (45.128).format(n), '45.13'
  assert.equal (-45.123).format(n), '-45.12'
  assert.equal (-45.128).format(n), '-45.13'
  assert.equal (0.123456).format(n), '0.12'
  assert.equal (-0.123456).format(n), '-0.12'
  assert.equal (1234567.123).format(n), '1,234,567.12'
  assert.equal (-1234567.123).format(n), '-1,234,567.12'
  assert.equal (123456.123).format(n), '123,456.12'
  assert.equal (-123456.123).format(n), '-123,456.12'
  assert.equal (0 / 0).format(n), '---'
  assert.equal Number.MIN_VALUE.format(n), '0.00'
  assert.equal Number.POSITIVE_INFINITY.format(n), '---'
  assert.equal Number.NEGATIVE_INFINITY.format(n), '---'

QUnit.test 'Number formatting with negative precision', (assert) ->
  n = -2
  assert.equal (45).format(n), '45.00'
  assert.equal (-45).format(n), '-45.00'
  assert.equal (45.123).format(n), '45.12'
  assert.equal (45.128).format(n), '45.13'
  assert.equal (-45.123).format(n), '-45.12'
  assert.equal (-45.128).format(n), '-45.13'
  assert.equal (0.123456).format(n), '0.12'
  assert.equal (-0.123456).format(n), '-0.12'
  assert.equal (1234567.123).format(n), '1,234,567.12'
  assert.equal (-1234567.123).format(n), '-1,234,567.12'
  assert.equal (123456.123).format(n), '123,456.12'
  assert.equal (-123456.123).format(n), '-123,456.12'
  assert.equal (0 / 0).format(n), '---'
  assert.equal Number.MIN_VALUE.format(n), '0.00'
  assert.equal Number.POSITIVE_INFINITY.format(n), '---'
  assert.equal Number.NEGATIVE_INFINITY.format(n), '---'

QUnit.test 'Number formatting with string precision', (assert) ->
  n = '2'
  assert.equal (45).format(n), '45.00'
  assert.equal (-45).format(n), '-45.00'
  assert.equal (45.123).format(n), '45.12'
  assert.equal (45.128).format(n), '45.13'
  assert.equal (-45.123).format(n), '-45.12'
  assert.equal (-45.128).format(n), '-45.13'
  assert.equal (0.123456).format(n), '0.12'
  assert.equal (-0.123456).format(n), '-0.12'
  assert.equal (1234567.123).format(n), '1,234,567.12'
  assert.equal (-1234567.123).format(n), '-1,234,567.12'
  assert.equal (123456.123).format(n), '123,456.12'
  assert.equal (-123456.123).format(n), '-123,456.12'
  assert.equal (0 / 0).format(n), '---'
  assert.equal Number.MIN_VALUE.format(n), '0.00'
  assert.equal Number.POSITIVE_INFINITY.format(n), '---'
  assert.equal Number.NEGATIVE_INFINITY.format(n), '---'

QUnit.test 'Currency value formatting', (assert) ->
  assert.equal (45).formatCurrencyValue(), '45.000'
  assert.equal (-45).formatCurrencyValue(), '-45.000'
  assert.equal (45.123).formatCurrencyValue(), '45.123'
  assert.equal (45.1238).formatCurrencyValue(), '45.124'
  assert.equal (-45.123).formatCurrencyValue(), '-45.123'
  assert.equal (-45.1238).formatCurrencyValue(), '-45.124'
  assert.equal (0.123456).formatCurrencyValue(), '0.123'
  assert.equal (-0.123456).formatCurrencyValue(), '-0.123'
  assert.equal (1234567.123).formatCurrencyValue(), '1,234,567.123'
  assert.equal (-1234567.123).formatCurrencyValue(), '-1,234,567.123'
  assert.equal (123456.123).formatCurrencyValue(), '123,456.123'
  assert.equal (-123456.123).formatCurrencyValue(), '-123,456.123'
  assert.equal (0 / 0).formatCurrencyValue(), '---'
  assert.equal Number.MIN_VALUE.formatCurrencyValue(), '0.000'
  assert.equal Number.POSITIVE_INFINITY.formatCurrencyValue(), '---'
  assert.equal Number.NEGATIVE_INFINITY.formatCurrencyValue(), '---'

QUnit.test 'Number parsing', (assert) ->
  assert.equal '45'.parseNumber(), 45
  assert.equal '45.'.parseNumber(), 45
  assert.equal '45.00'.parseNumber(), 45
  assert.equal '45.123'.parseNumber(), 45.123
  assert.equal '1,234,567.123'.parseNumber(), 1234567.123
  assert.equal '-45'.parseNumber(), -45
  assert.equal '-45.00'.parseNumber(), -45
  assert.equal '-45.123'.parseNumber(), -45.123
  assert.equal '-1,234,567.123'.parseNumber(), -1234567.123
  assert.equal '0.123456'.parseNumber(), 0.123456
  assert.equal '-0.123456'.parseNumber(), -0.123456
  assert.equal '.123456'.parseNumber(), 0.123456
  assert.equal '-.123456'.parseNumber(), -0.123456
