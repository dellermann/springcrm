#
# core-test.coffee
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


$ = jQuery


#-- Fixtures ------------------------------------

$L = (key) -> $L._messages[key.replace /\./g, '_']
$L._messages =
  default_format_date: 'dd.MM.yyyy'
  default_format_datetime: 'dd.MM.yyyy HH:mm'
  default_format_time: 'HH:mm'
window.$L = $L

SPRINGCRM = SPRINGCRM ? {}


#-- Feature tests -------------------------------

QUnit.module 'Numbers'
QUnit.test 'Number formatting without precision', (assert) ->
  assert.equal (45).format(), '45'
  assert.equal (-45).format(), '-45'
  assert.equal (45.123).format(), '45,123'
  assert.equal (-45.123).format(), '-45,123'
  assert.equal (0.123456).format(), '0,123456'
  assert.equal (-0.123456).format(), '-0,123456'
  assert.equal (1234567.123).format(), '1.234.567,123'
  assert.equal (-1234567.123).format(), '-1.234.567,123'
  assert.equal (123456.123).format(), '123.456,123'
  assert.equal (-123456.123).format(), '-123.456,123'
  assert.equal (0 / 0).format(), '---'

QUnit.test 'Number formatting with null precision', (assert) ->
  n = null
  assert.equal (45).format(n), '45'
  assert.equal (-45).format(n), '-45'
  assert.equal (45.123).format(n), '45,123'
  assert.equal (-45.123).format(n), '-45,123'
  assert.equal (0.123456).format(n), '0,123456'
  assert.equal (-0.123456).format(n), '-0,123456'
  assert.equal (1234567.123).format(n), '1.234.567,123'
  assert.equal (-1234567.123).format(n), '-1.234.567,123'
  assert.equal (123456.123).format(n), '123.456,123'
  assert.equal (-123456.123).format(n), '-123.456,123'
  assert.equal (0 / 0).format(n), '---'

QUnit.test 'Number formatting with precision', (assert) ->
  n = 2
  assert.equal (45).format(n), '45,00'
  assert.equal (-45).format(n), '-45,00'
  assert.equal (45.123).format(n), '45,12'
  assert.equal (45.128).format(n), '45,13'
  assert.equal (-45.123).format(n), '-45,12'
  assert.equal (-45.128).format(n), '-45,13'
  assert.equal (0.123456).format(n), '0,12'
  assert.equal (-0.123456).format(n), '-0,12'
  assert.equal (1234567.123).format(n), '1.234.567,12'
  assert.equal (-1234567.123).format(n), '-1.234.567,12'
  assert.equal (123456.123).format(n), '123.456,12'
  assert.equal (-123456.123).format(n), '-123.456,12'
  assert.equal (0 / 0).format(n), '---'
  assert.equal Number.MIN_VALUE.format(n), '0,00'
  assert.equal Number.POSITIVE_INFINITY.format(n), '---'
  assert.equal Number.NEGATIVE_INFINITY.format(n), '---'

QUnit.test 'Number formatting with negative precision', (assert) ->
  n = -2
  assert.equal (45).format(n), '45,00'
  assert.equal (-45).format(n), '-45,00'
  assert.equal (45.123).format(n), '45,12'
  assert.equal (45.128).format(n), '45,13'
  assert.equal (-45.123).format(n), '-45,12'
  assert.equal (-45.128).format(n), '-45,13'
  assert.equal (0.123456).format(n), '0,12'
  assert.equal (-0.123456).format(n), '-0,12'
  assert.equal (1234567.123).format(n), '1.234.567,12'
  assert.equal (-1234567.123).format(n), '-1.234.567,12'
  assert.equal (123456.123).format(n), '123.456,12'
  assert.equal (-123456.123).format(n), '-123.456,12'
  assert.equal (0 / 0).format(n), '---'
  assert.equal Number.MIN_VALUE.format(n), '0,00'
  assert.equal Number.POSITIVE_INFINITY.format(n), '---'
  assert.equal Number.NEGATIVE_INFINITY.format(n), '---'

QUnit.test 'Number formatting with string precision', (assert) ->
  n = '2'
  assert.equal (45).format(n), '45,00'
  assert.equal (-45).format(n), '-45,00'
  assert.equal (45.123).format(n), '45,12'
  assert.equal (45.128).format(n), '45,13'
  assert.equal (-45.123).format(n), '-45,12'
  assert.equal (-45.128).format(n), '-45,13'
  assert.equal (0.123456).format(n), '0,12'
  assert.equal (-0.123456).format(n), '-0,12'
  assert.equal (1234567.123).format(n), '1.234.567,12'
  assert.equal (-1234567.123).format(n), '-1.234.567,12'
  assert.equal (123456.123).format(n), '123.456,12'
  assert.equal (-123456.123).format(n), '-123.456,12'
  assert.equal (0 / 0).format(n), '---'
  assert.equal Number.MIN_VALUE.format(n), '0,00'
  assert.equal Number.POSITIVE_INFINITY.format(n), '---'
  assert.equal Number.NEGATIVE_INFINITY.format(n), '---'

QUnit.test 'Currency value formatting', (assert) ->
  assert.equal (45).formatCurrencyValue(), '45,00'
  assert.equal (-45).formatCurrencyValue(), '-45,00'
  assert.equal (45.123).formatCurrencyValue(), '45,12'
  assert.equal (45.128).formatCurrencyValue(), '45,13'
  assert.equal (-45.123).formatCurrencyValue(), '-45,12'
  assert.equal (-45.128).formatCurrencyValue(), '-45,13'
  assert.equal (0.123456).formatCurrencyValue(), '0,12'
  assert.equal (-0.123456).formatCurrencyValue(), '-0,12'
  assert.equal (1234567.123).formatCurrencyValue(), '1.234.567,12'
  assert.equal (-1234567.123).formatCurrencyValue(), '-1.234.567,12'
  assert.equal (123456.123).formatCurrencyValue(), '123.456,12'
  assert.equal (-123456.123).formatCurrencyValue(), '-123.456,12'
  assert.equal (0 / 0).formatCurrencyValue(), '---'
  assert.equal Number.MIN_VALUE.formatCurrencyValue(), '0,00'
  assert.equal Number.POSITIVE_INFINITY.formatCurrencyValue(), '---'
  assert.equal Number.NEGATIVE_INFINITY.formatCurrencyValue(), '---'

QUnit.test 'Number parsing', (assert) ->
  assert.equal '45'.parseNumber(), 45
  assert.equal '45,'.parseNumber(), 45
  assert.equal '45,00'.parseNumber(), 45
  assert.equal '45,123'.parseNumber(), 45.123
  assert.equal '1.234.567,123'.parseNumber(), 1234567.123
  assert.equal '-45'.parseNumber(), -45
  assert.equal '-45,00'.parseNumber(), -45
  assert.equal '-45,123'.parseNumber(), -45.123
  assert.equal '-1.234.567,123'.parseNumber(), -1234567.123
  assert.equal '0,123456'.parseNumber(), 0.123456
  assert.equal '-0,123456'.parseNumber(), -0.123456
  assert.equal ',123456'.parseNumber(), 0.123456
  assert.equal '-,123456'.parseNumber(), -0.123456

QUnit.test 'Number rounding', (assert) ->
  assert.equal (45).round(2), 45
  assert.equal (45.1).round(2), 45.1
  assert.equal (45.12).round(2), 45.12
  assert.equal (45.123).round(2), 45.12
  assert.equal (45.128).round(2), 45.13
  assert.equal (45.128).round(3), 45.128
  assert.equal (45.12345678901).round(3), 45.123
  assert.equal (45.12398765432).round(3), 45.124
  assert.equal (0).round(2), 0
  assert.equal Number.MIN_VALUE.round(2), 0
  assert.ok isNaN((0 / 0).round(2))

QUnit.test 'Size formatting', (assert) ->
  assert.equal (0).formatSize(), '0 B'
  assert.equal (10).formatSize(), '10 B'
  assert.equal (142).formatSize(), '142 B'
  assert.equal (980).formatSize(), '980 B'
  assert.equal (1000).formatSize(), '1000 B'
  assert.equal (1023).formatSize(), '1023 B'
  assert.equal (1024).formatSize(), '1,0 KB'
  assert.equal (1025).formatSize(), '1,0 KB'
  assert.equal (1178).formatSize(), '1,2 KB'
  assert.equal (17389).formatSize(), '17,0 KB'
  assert.equal (949303).formatSize(), '927,1 KB'
  assert.equal (1023896).formatSize(), '999,9 KB'
  assert.equal (1048576).formatSize(), '1,0 MB'
  assert.equal (4740903).formatSize(), '4,5 MB'
  assert.equal (83740903).formatSize(), '79,9 MB'
  assert.equal (201049321).formatSize(), '191,7 MB'
  assert.equal (7840347242).formatSize(), '7,3 GB'
  assert.equal (48240347242).formatSize(), '44,9 GB'
  assert.equal (470537492143).formatSize(), '438,2 GB'
  assert.equal (7361316616641).formatSize(), '6,7 TB'
  assert.equal (73561316616641).formatSize(), '66,9 TB'
  assert.equal (331346123564354).formatSize(), '301,4 TB'
  assert.equal (3831346123564354).formatSize(), '3.484,6 TB'

QUnit.module 'Regular expressions'
QUnit.test 'Regular expression escaping', (assert) ->
  assert.equal RegExp.escape('.'), '\\.'
  assert.equal RegExp.escape('[abc]'), '\\[abc\\]'
  assert.equal RegExp.escape('{abc}'), '\\{abc\\}'
  assert.equal RegExp.escape('1+4'), '1\\+4'
  assert.equal RegExp.escape('What?'), 'What\\?'
  assert.equal RegExp.escape('Hat^'), 'Hat\\^'
  assert.equal RegExp.escape('1.000.000 $'), '1\\.000\\.000 \\$'
  assert.equal RegExp.escape('\\'), '\\\\'
  assert.equal RegExp.escape('/'), '\\/'

QUnit.module 'Date and time'
QUnit.test 'Date formatting', (assert) ->
  d = new Date(2012, 11, 29, 22, 28, 0, 0)
  assert.equal d.format('yy'), '12'
  assert.equal d.format('yyy'), '2012'
  assert.equal d.format('yyyy'), '2012'
  assert.equal d.format('MM.yyyy'), '12.2012'
  assert.equal d.format('yyyy-MM'), '2012-12'
  assert.equal d.format('dd.MM.yyyy'), '29.12.2012'
  assert.equal d.format('dd.MM.yyyy HH:mm'), '29.12.2012 22:28'
  assert.equal d.format('date'), '29.12.2012'
  assert.equal d.format('datetime'), '29.12.2012 22:28'
  assert.equal d.format('time'), '22:28'
  assert.equal d.format(), '29.12.2012 22:28'
  d = new Date(1980, 2, 7, 9, 5, 0, 0)
  assert.equal d.format('d.M.yy H:m'), '7.3.80 9:5'

QUnit.test 'Date parsing', (assert) ->
  assert.equal '2012'.parseDate('yyyy').getTime(), new Date(2012, 0, 1, 0, 0, 0, 0).getTime()
  assert.equal '12.2012'.parseDate('MM.yyyy').getTime(), new Date(2012, 11, 1, 0, 0, 0, 0).getTime()
  assert.equal '2012-12'.parseDate('yyyy-MM').getTime(), new Date(2012, 11, 1, 0, 0, 0, 0).getTime()
  assert.equal '29.12.2012'.parseDate('dd.MM.yyyy').getTime(), new Date(2012, 11, 29, 0, 0, 0, 0).getTime()
  assert.equal '29.12.2012 22:28'.parseDate('dd.MM.yyyy HH:mm').getTime(), new Date(2012, 11, 29, 22, 28, 0, 0).getTime()
  assert.equal '29.12.2012'.parseDate('date').getTime(), new Date(2012, 11, 29, 0, 0, 0, 0).getTime()
  assert.equal '29.12.2012 22:28'.parseDate('datetime').getTime(), new Date(2012, 11, 29, 22, 28, 0, 0).getTime()
  assert.equal '22:28'.parseDate('time').getTime(), new Date(1970, 0, 1, 22, 28, 0, 0).getTime()
  assert.equal '29.12.2012 22:28'.parseDate().getTime(), new Date(2012, 11, 29, 22, 28, 0, 0).getTime()
  assert.equal '29.12.2012 22:28'.parseDate('d.M.yyy H:m').getTime(), new Date(2012, 11, 29, 22, 28, 0, 0).getTime()
  assert.equal '29.12.2012 22:28'.parseDate('d.M.yy H:m').getTime(), new Date(2012, 11, 29, 22, 28, 0, 0).getTime()
  assert.equal '29.12.2012 22:28'.parseDate('d.M.y H:m').getTime(), new Date(2012, 11, 29, 22, 28, 0, 0).getTime()

QUnit.module 'URLs'
QUnit.test 'Empty URL construction', (assert) ->
  url = new HttpUrl()
  assert.equal url.scheme, 'http'
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, `undefined`
  assert.equal url.port, `undefined`
  assert.equal url.path, `undefined`
  assert.propEqual url.query, {}
  assert.equal url.fragmentIdentifier, `undefined`

QUnit.test 'URL parsing', (assert) ->
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, 'https'
  assert.equal url.userName, 'jsmith'
  assert.equal url.password, 'secret'
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, 8080
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('https://jsmith:secret@www.example.com/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, 'https'
  assert.equal url.userName, 'jsmith'
  assert.equal url.password, 'secret'
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, 443
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('http://jsmith:secret@www.example.com/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, 'http'
  assert.equal url.userName, 'jsmith'
  assert.equal url.password, 'secret'
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, 80
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('http://jsmith@www.example.com/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, 'http'
  assert.equal url.userName, 'jsmith'
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, 80
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('http://www.example.com/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, 'http'
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, 80
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('jsmith:secret@www.example.com/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, 'jsmith'
  assert.equal url.password, 'secret'
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, `undefined`
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('www.example.com:8070/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, 8070
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('www.example.com/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, `undefined`
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('www.example.com/foo/bar#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, `undefined`
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query, {}
  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('www.example.com/foo/bar')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, `undefined`
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query, {}
  assert.equal url.fragmentIdentifier, `undefined`
  url = new HttpUrl('www.example.com?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, `undefined`
  assert.equal url.path, ''
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('www.example.com?attr1=wheezy%3dgib&attr2=jolly+funky')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, `undefined`
  assert.equal url.path, ''
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, `undefined`
  url = new HttpUrl('www.example.com')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'www.example.com'
  assert.equal url.port, `undefined`
  assert.equal url.path, ''
  assert.propEqual url.query, {}
  assert.equal url.fragmentIdentifier, `undefined`

QUnit.test 'URL parsing without host', (assert) ->
  url = new HttpUrl('/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, ''
  assert.equal url.port, `undefined`
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('/foo/bar#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, ''
  assert.equal url.port, `undefined`
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query, {}
  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('/foo/bar')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, ''
  assert.equal url.port, `undefined`
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query, {}
  assert.equal url.fragmentIdentifier, `undefined`
  url = new HttpUrl('foo/bar')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, 'foo'
  assert.equal url.port, `undefined`
  assert.equal url.path, '/bar'
  assert.propEqual url.query, {}
  assert.equal url.fragmentIdentifier, `undefined`
  url = new HttpUrl('?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, ''
  assert.equal url.port, `undefined`
  assert.equal url.path, ''
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'
  url = new HttpUrl('?attr1=wheezy%3dgib&attr2=jolly+funky')
  assert.equal url.scheme, `undefined`
  assert.equal url.userName, `undefined`
  assert.equal url.password, `undefined`
  assert.equal url.host, ''
  assert.equal url.port, `undefined`
  assert.equal url.path, ''
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, `undefined`

QUnit.test 'IDN URL parsing', (assert) ->
  url = new HttpUrl('https://jsmith:secret@www.gäßler.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  assert.equal url.scheme, 'https'
  assert.equal url.userName, 'jsmith'
  assert.equal url.password, 'secret'
  assert.equal url.host, 'www.gäßler.com'
  assert.equal url.port, 8080
  assert.equal url.path, '/foo/bar'
  assert.propEqual url.query,
    attr1: 'wheezy=gib'
    attr2: 'jolly funky'

  assert.equal url.fragmentIdentifier, 'part-1'

QUnit.test 'URL building', (assert) ->
  url = new HttpUrl()
  url.host = 'www.example.com'
  assert.equal 'http://www.example.com', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  assert.equal 'https://www.example.com', url.toString()
  url = new HttpUrl()
  url.host = 'www.example.com'
  url.port = 8080
  assert.equal 'http://www.example.com:8080', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.port = 8080
  assert.equal 'https://www.example.com:8080', url.toString()
  url = new HttpUrl()
  url.userName = 'jsmith'
  url.host = 'www.example.com'
  assert.equal 'http://jsmith@www.example.com', url.toString()
  url = new HttpUrl()
  url.userName = 'jsmith'
  url.password = 'secret'
  url.host = 'www.example.com'
  assert.equal 'http://jsmith:secret@www.example.com', url.toString()
  url = new HttpUrl()
  url.userName = 'jsmith'
  url.password = 'secret'
  url.host = 'www.example.com'
  url.port = 8070
  assert.equal 'http://jsmith:secret@www.example.com:8070', url.toString()
  url = new HttpUrl()
  url.host = 'www.example.com'
  url.path = '/foo/bar'
  assert.equal 'http://www.example.com/foo/bar', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.path = '/foo/bar'
  assert.equal 'https://www.example.com/foo/bar', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.port = 8080
  url.path = '/foo/bar'
  assert.equal 'https://www.example.com:8080/foo/bar', url.toString()
  url = new HttpUrl()
  url.host = 'www.example.com'
  url.query =
    attr1: 'wheezy=gib/thee'
    attr2: 'jolly funky'

  assert.equal 'http://www.example.com?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky', url.toString()
  url = new HttpUrl()
  url.host = 'www.example.com'
  url.path = '/foo/bar'
  url.query =
    attr1: 'wheezy=gib/thee'
    attr2: 'jolly funky'

  assert.equal 'http://www.example.com/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.path = '/foo/bar'
  url.query =
    attr1: 'wheezy=gib/thee'
    attr2: 'jolly funky'

  assert.equal 'https://www.example.com/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.port = 8080
  url.path = '/foo/bar'
  url.query =
    attr1: 'wheezy=gib/thee'
    attr2: 'jolly funky'

  assert.equal 'https://www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky', url.toString()
  url = new HttpUrl()
  url.host = 'www.example.com'
  url.fragmentIdentifier = 'part-1'
  assert.equal 'http://www.example.com#part-1', url.toString()
  url = new HttpUrl()
  url.host = 'www.example.com'
  url.path = '/foo/bar'
  url.fragmentIdentifier = 'part-1'
  assert.equal 'http://www.example.com/foo/bar#part-1', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.path = '/foo/bar'
  url.fragmentIdentifier = 'part-1'
  assert.equal 'https://www.example.com/foo/bar#part-1', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.port = 8080
  url.path = '/foo/bar'
  url.fragmentIdentifier = 'part-1'
  assert.equal 'https://www.example.com:8080/foo/bar#part-1', url.toString()
  url = new HttpUrl()
  url.scheme = 'https'
  url.host = 'www.example.com'
  url.port = 8080
  url.path = '/foo/bar'
  url.query =
    attr1: 'wheezy=gib/thee'
    attr2: 'jolly funky'

  url.fragmentIdentifier = 'part-1'
  assert.equal 'https://www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1', url.toString()

QUnit.test 'Parsing and building', (assert) ->
  url = 'https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://jsmith@www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://jsmith@www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://www.example.com:8080/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://www.example.com/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://www.example.com/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://www.example.com?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://www.example.com?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://www.example.com/foo/bar#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://www.example.com/foo/bar#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://www.example.com?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://www.example.com?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://www.example.com/foo/bar'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://www.example.com/foo/bar'
  assert.equal url, new HttpUrl(url).toString()
  url = 'https://www.example.com'
  assert.equal url, new HttpUrl(url).toString()
  url = 'http://www.example.com'
  assert.equal url, new HttpUrl(url).toString()
  url = 'www.example.com'
  assert.equal url, new HttpUrl(url).toString()
  url = '/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = '/foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky'
  assert.equal url, new HttpUrl(url).toString()
  url = '/foo/bar#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = 'foo/bar?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = '?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky#part-1'
  assert.equal url, new HttpUrl(url).toString()
  url = '?attr1=wheezy%3Dgib%2Fthee&attr2=jolly%20funky'
  assert.equal url, new HttpUrl(url).toString()
  url = '#part-1'
  assert.equal url, new HttpUrl(url).toString()

QUnit.test 'Overwrite query parameters', (assert) ->
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  url.query.attr3 = 'buzz'
  assert.equal 'https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3Dgib&attr2=jolly%20funky&attr3=buzz#part-1', url.toString()
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  $.extend url.query,
    attr1: 'buzzy'
    attr3: 'gobbish'

  assert.equal 'https://jsmith:secret@www.example.com:8080/foo/bar?attr1=buzzy&attr2=jolly%20funky&attr3=gobbish#part-1', url.toString()
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  delete url.query.attr1

  assert.equal 'https://jsmith:secret@www.example.com:8080/foo/bar?attr2=jolly%20funky#part-1', url.toString()
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  url.overwriteQuery attr3: 'buzz'
  assert.equal 'https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3Dgib&attr2=jolly%20funky&attr3=buzz#part-1', url.toString()
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  url.overwriteQuery
    attr1: 'buzzy'
    attr3: 'gobbish'

  assert.equal 'https://jsmith:secret@www.example.com:8080/foo/bar?attr1=buzzy&attr2=jolly%20funky&attr3=gobbish#part-1', url.toString()
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  url.overwriteQuery 'attr3=buzz'
  assert.equal 'https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3Dgib&attr2=jolly%20funky&attr3=buzz#part-1', url.toString()
  url = new HttpUrl('https://jsmith:secret@www.example.com:8080/foo/bar?attr1=wheezy%3dgib&attr2=jolly+funky#part-1')
  url.overwriteQuery 'attr1=buzzy&attr3=gobbish'
  assert.equal 'https://jsmith:secret@www.example.com:8080/foo/bar?attr1=buzzy&attr2=jolly%20funky&attr3=gobbish#part-1', url.toString()
