#
# filetype-test.coffee
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


#-- Feature tests -------------------------------

QUnit.module 'Coding style'
QUnit.test 'Auxiliary object ExtDb is not visible', (assert) ->
  assert.equal window.ExtDb, `undefined`

QUnit.module 'Filetypes'
QUnit.test 'Obtain existing filetypes in lower case', (assert) ->
  assert.equal $.filetype('bmp'), 'image'
  assert.equal $.filetype('gif'), 'image'
  assert.equal $.filetype('jpeg'), 'image'
  assert.equal $.filetype('jpg'), 'image'
  assert.equal $.filetype('png'), 'image'
  assert.equal $.filetype('ods'), 'spreadsheet'
  assert.equal $.filetype('xls'), 'spreadsheet'
  assert.equal $.filetype('c'), 'code'
  assert.equal $.filetype('cpp'), 'code'
  assert.equal $.filetype('php'), 'code'
  assert.equal $.filetype('java'), 'code'
  assert.equal $.filetype('groovy'), 'code'
  assert.equal $.filetype('gz'), 'archive'
  assert.equal $.filetype('zip'), 'archive'
  assert.equal $.filetype('targz'), 'archive'
  assert.equal $.filetype('7z'), 'archive'
  assert.equal $.filetype('tar'), 'archive'
  assert.equal $.filetype('mp3'), 'audio'
  assert.equal $.filetype('pdf'), 'pdf'
  assert.equal $.filetype('odt'), 'document'
  assert.equal $.filetype('ott'), 'document'
  assert.equal $.filetype('doc'), 'document'
  assert.equal $.filetype('dot'), 'document'
  assert.equal $.filetype('odp'), 'presentation'
  assert.equal $.filetype('ppt'), 'presentation'
  assert.equal $.filetype('txt'), 'text'
  assert.equal $.filetype('md'), 'text'
  assert.equal $.filetype('ascii'), 'text'
  assert.equal $.filetype('xml'), 'text'
  assert.equal $.filetype('xsl'), 'text'
  assert.equal $.filetype('xslt'), 'text'
  assert.equal $.filetype('xsd'), 'text'
  assert.equal $.filetype('avi'), 'video'
  assert.equal $.filetype('mpeg'), 'video'
  assert.equal $.filetype('3gp'), 'video'

QUnit.test 'Obtain filetypes in upper case', (assert) ->
  assert.equal $.filetype('BMP'), 'image'
  assert.equal $.filetype('GIF'), 'image'
  assert.equal $.filetype('JPEG'), 'image'
  assert.equal $.filetype('JPG'), 'image'
  assert.equal $.filetype('PNG'), 'image'
  assert.equal $.filetype('ODS'), 'spreadsheet'
  assert.equal $.filetype('XLS'), 'spreadsheet'
  assert.equal $.filetype('C'), 'code'
  assert.equal $.filetype('CPP'), 'code'
  assert.equal $.filetype('PHP'), 'code'
  assert.equal $.filetype('JAVA'), 'code'

QUnit.test 'Obtain existing filetypes in mixed case', (assert) ->
  assert.equal $.filetype('Bmp'), 'image'
  assert.equal $.filetype('gIf'), 'image'
  assert.equal $.filetype('jpEG'), 'image'
  assert.equal $.filetype('jpG'), 'image'
  assert.equal $.filetype('PnG'), 'image'
  assert.equal $.filetype('oDS'), 'spreadsheet'
  assert.equal $.filetype('XLs'), 'spreadsheet'

QUnit.test 'Obtain non-existing filetypes', (assert) ->
  assert.equal $.filetype('xxx'), 'file'
  assert.equal $.filetype('unkown'), 'file'
  assert.equal $.filetype('foo'), 'file'
  assert.equal $.filetype('gossip'), 'file'
