#
# document-list-test.coffee
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
  document_back_label: 'back'
  document_path_label: 'Path'
  document_path_root: 'Root folder'
  document_permissions_read: 'R'
  document_permissions_read_title: 'Read'
  document_permissions_write: 'W'
  document_permissions_write_title: 'Write'
window.$L = $L

SPRINGCRM = SPRINGCRM ? {}

BASE_URL = '/springcrm/document/list'

$.mockjax
  data:
    path: ''
  responseText:
    folders: [
        name: 'bar'
        readable: true
        writeable: false
      ,
        name: 'foo'
        readable: true
        writeable: true
    ]
    files: [
        name: 'baz.txt'
        ext: 'txt'
        size: 20405
        readable: true
        writeable: true
      ,
        name: 'yummy.csv'
        ext: 'csv'
        size: 40307430
        readable: false
        writeable: true
    ]
  url: BASE_URL
$.mockjax
  data:
    path: 'foo'
  responseText:
    folders: [
      name: 'wheezy'
      readable: true
      writeable: true
    ]
    files: [
        name: 'hello-world.md'
        ext: 'md'
        size: 303122
        readable: false
        writeable: true
      ,
        name: 'hello-world.c'
        ext: 'c'
        size: 749393
        readable: true
        writeable: true
    ]
  url: BASE_URL
$.mockjax
  data:
    path: 'foo/wheezy'
  responseText:
    folders: []
    files: [
        name: 'README.md'
        ext: 'md'
        size: 403720
        readable: true
        writeable: true
      ,
        name: 'Documentation.odt'
        ext: 'odt'
        size: 1023034
        readable: false
        writeable: false
    ]
  url: BASE_URL


#-- QUnit extensions ----------------------------

QUnit.assert.textEqual = ($element, text, message) ->
  actual = $element.text()
  QUnit.push actual is text, actual, text, message

QUnit.assert.is = ($element, selector, message) ->
  ok = $element.is(selector)
  QUnit.push ok, ok, true, message

QUnit.assert.itemSize = ($element, size, message) ->
  actual = $element.length
  QUnit.push actual is size, actual, size, message


#-- Feature tests -------------------------------

QUnit.module 'Instantiation'
QUnit.test 'Instantiate widget without loading', (assert) ->
  fixtureDocumentLists()

  $dl = $('.document-list')
  $dl.documentlist()

  assert.equal $dl.html(), '', 'both download lists are empty'
  assert.notEqual $dl.data('bs.documentlist'), null, 'both elements are marked as download-list'

QUnit.asyncTest 'Instantiate widget with loading', (assert) ->
  expect 43

  fixtureDocumentLists()

  $('.document-list').documentlist()
    .on 'springcrm.documentlist.pathchanged', ->
      checkRootFolder $('#dl1'), $('#dl2'), assert
      QUnit.start()


QUnit.module 'API calls'
QUnit.asyncTest 'Get initial path', (assert) ->
  expect 2

  fixtureDocumentLists()

  $('#dl2').documentlist()
    .one 'springcrm.documentlist.pathchanged', (event, data) ->
      assert.equal data.path, ''
      assert.equal $(this).documentlist('path'), ''
      QUnit.start()

QUnit.asyncTest 'Get path of subfolders', (assert) ->
  expect 4

  fixtureDocumentLists()

  f1 = (event, data) ->
    assert.equal data.path, ''
    $('#dl2').one('springcrm.documentlist.pathchanged', f2)
      .find('> ul > li:nth-child(2)')
        .click()
  f2 = (event, data) ->
    assert.equal data.path, 'foo'
    $('#dl2').one('springcrm.documentlist.pathchanged', f3)
      .find('> ul > li:nth-child(2)')
        .click()
  f3 = (event, data) ->
    assert.equal data.path, 'foo/wheezy'
    assert.equal $(this).documentlist('path'), 'foo/wheezy'
    QUnit.start()

  $('#dl2').documentlist()
    .one 'springcrm.documentlist.pathchanged', f1

QUnit.asyncTest 'Set path', (assert) ->
  expect 31

  fixtureDocumentLists()

  f1 = (event, data) ->
    assert.equal data.path, ''
    $(this).one('springcrm.documentlist.pathchanged', f2)
      .documentlist 'path', 'foo/wheezy'
  f2 = (event, data) ->
    assert.equal data.path, 'foo/wheezy'
    assert.equal $(this).documentlist('path'), 'foo/wheezy'
    checkWheezyFolder $('#dl1'), $('#dl2'), assert
    QUnit.start()

  $('#dl2').documentlist()
    .one 'springcrm.documentlist.pathchanged', f1


QUnit.module 'User interaction'
QUnit.asyncTest 'Click on folder', (assert) ->
  expect 40

  fixtureDocumentLists()

  f1 = (event, data) ->
    assert.equal data.path, ''
    $('#dl2').one('springcrm.documentlist.pathchanged', f2)
      .find('> ul > li:nth-child(2)')
        .click()
  f2 = (event, data) ->
    assert.equal data.path, 'foo'
    checkFooFolder $('#dl1'), $('#dl2'), assert
    QUnit.start()

  $('.document-list').documentlist()
    .one 'springcrm.documentlist.pathchanged', f1

QUnit.asyncTest 'Click on two folders', (assert) ->
  expect 31

  fixtureDocumentLists()

  f1 = (event, data) ->
    assert.equal data.path, ''
    $('#dl2').one('springcrm.documentlist.pathchanged', f2)
      .find('> ul > li:nth-child(2)')
        .click()
  f2 = (event, data) ->
    assert.equal data.path, 'foo'
    $('#dl2').one('springcrm.documentlist.pathchanged', f3)
      .find('> ul > li:nth-child(2)')
        .click()
  f3 = (event, data) ->
    assert.equal data.path, 'foo/wheezy'
    checkWheezyFolder $('#dl1'), $('#dl2'), assert
    QUnit.start()

  $('.document-list').documentlist()
    .one 'springcrm.documentlist.pathchanged', f1

QUnit.asyncTest 'Click on back link', (assert) ->
  expect 46

  fixtureDocumentLists()

  f1 = (event, data) ->
    assert.equal data.path, ''
    $('#dl2').one('springcrm.documentlist.pathchanged', f2)
      .find('> ul > li:nth-child(2)')
        .click()
  f2 = (event, data) ->
    assert.equal data.path, 'foo'
    $('#dl2').one('springcrm.documentlist.pathchanged', f3)
      .find('> ul > .back-link')
        .click()
  f3 = (event, data) ->
    assert.equal data.path, ''
    checkRootFolder $('#dl1'), $('#dl2'), assert
    QUnit.start()

  $('.document-list').documentlist()
    .one 'springcrm.documentlist.pathchanged', f1

QUnit.asyncTest 'Click on two back links', (assert) ->
  expect 86

  fixtureDocumentLists()

  f1 = (event, data) ->
    assert.equal data.path, ''
    $('#dl2').one('springcrm.documentlist.pathchanged', f2)
      .find('> ul > li:nth-child(2)')
        .click()
  f2 = (event, data) ->
    assert.equal data.path, 'foo'
    $('#dl2').one('springcrm.documentlist.pathchanged', f3)
      .find('> ul > li:nth-child(2)')
        .click()
  f3 = (event, data) ->
    assert.equal data.path, 'foo/wheezy'
    $('#dl2').one('springcrm.documentlist.pathchanged', f4)
      .find('> ul > .back-link')
        .click()
  f4 = (event, data) ->
    assert.equal data.path, 'foo'
    checkFooFolder $('#dl1'), $('#dl2'), assert
    $('#dl2').one('springcrm.documentlist.pathchanged', f5)
      .find('> ul > .back-link')
        .click()
  f5 = (event, data) ->
    assert.equal data.path, ''
    checkRootFolder $('#dl1'), $('#dl2'), assert
    QUnit.start()

  $('.document-list').documentlist()
    .one 'springcrm.documentlist.pathchanged', f1


#-- Auxiliary functions -------------------------

checkFooFolder = ($emptyList, $nonEmptyList, assert) ->
  assert.itemSize $emptyList.children('ul'), 0, 'one download list is empty'

  $ul = $nonEmptyList.children('ul')
  $lis = $ul.find('> li')
  assert.is $ul, '.document-list-container', 'has class "document-list-container"'
  assert.itemSize $ul, 1, 'exactly 1 <ul> element'
  assert.itemSize $lis, 4, 'exactly 4 <li> elements inside'

  $li = $lis.eq(0)
  assert.is $li, '.back-link', 'item 1 has class "back-link"'
  assert.itemSize $li.find('> i'), 1, 'item 1 has an icon'
  assert.textEqual $li.find('> .name'), 'back', 'item 1 is called "back"'
  assert.equal $li.find('> .permissions').html(), '', 'item 1 has no permissions'
  $li = $lis.eq(1)
  assert.is $li, '.folder', 'item 2 has class "folder"'
  assert.itemSize $li.find('> i'), 1, 'item 2 has an icon'
  assert.textEqual $li.find('> .name'), 'wheezy', 'item 1 is called "wheezy"'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 2 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 2, 'item 2 has two permissions with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), 'R', 'item 2 is readable'
  assert.equal $li.find('> .permissions > strong:first').attr('title'), 'Read', 'item 2 has readable title'
  assert.textEqual $li.find('> .permissions > strong:last'), 'W', 'item 2 is writeable'
  assert.equal $li.find('> .permissions > strong:last').attr('title'), 'Write', 'item 2 has writeable title'
  $li = $lis.eq(2)
  assert.is $li, '.file', 'item 3 has class "file"'
  assert.is $li, '.filetype-text', 'item 3 is a text file'
  assert.itemSize $li.find('> i'), 1, 'item 3 has an icon'
  assert.textEqual $li.find('> .name'), 'hello-world.md', 'item 3 is called "hello-world.md"'
  assert.textEqual $li.find('> .size'), '296,0 KB', 'item 3 has a size of 296,0 KB'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 3 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 1, 'item 3 has one permission with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), '', 'item 3 is not readable'
  assert.textEqual $li.find('> .permissions > strong:last'), 'W', 'item 3 is writeable'
  assert.equal $li.find('> .permissions > strong:last').attr('title'), 'Write', 'item 3 has writeable title'
  $li = $lis.eq(3)
  assert.is $li, '.file', 'item 4 has class "file"'
  assert.is $li, '.filetype-code', 'item 4 is a code file'
  assert.itemSize $li.find('> i'), 1, 'item 4 has an icon'
  assert.textEqual $li.find('> .name'), 'hello-world.c', 'item 4 is called "hello-world.c"'
  assert.textEqual $li.find('> .size'), '731,8 KB', 'item 4 has a size of 731,8 KB'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 4 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 2, 'item 4 has two permissions with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), 'R', 'item 4 is readable'
  assert.equal $li.find('> .permissions > strong:first').attr('title'), 'Read', 'item 4 has readable title'
  assert.textEqual $li.find('> .permissions > strong:last'), 'W', 'item 4 is writeable'
  assert.equal $li.find('> .permissions > strong:last').attr('title'), 'Write', 'item 4 has writeable title'

checkRootFolder = ($emptyList, $nonEmptyList, assert) ->
  assert.equal $emptyList.children('ul').length, 0, 'one download list is empty'

  $ul = $nonEmptyList.children('ul')
  $lis = $ul.find('> li')
  assert.ok $ul.is('.document-list-container'), 'has class "document-list-container"'
  assert.itemSize $ul, 1, 'exactly 1 <ul> element'
  assert.itemSize $lis, 4, 'exactly 4 <li> elements inside'

  $li = $lis.eq(0)
  assert.is $li, '.folder', 'item 1 has class "folder"'
  assert.itemSize $li.find('> i'), 1, 'item 1 has an icon'
  assert.textEqual $li.find('> .name'), 'bar', 'item 1 is called "bar"'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 1 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 1, 'item 1 has one permission with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), 'R', 'item 1 is readable'
  assert.equal $li.find('> .permissions > strong:first').attr('title'), 'Read', 'item 1 has readable title'
  assert.textEqual $li.find('> .permissions > strong:last'), '', 'item 1 is not writeable'
  $li = $lis.eq(1)
  assert.is $li, '.folder', 'item 2 has class "folder"'
  assert.itemSize $li.find('> i'), 1, 'item 2 has an icon'
  assert.textEqual $li.find('> .name'), 'foo', 'item 2 is called "foo"'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 2 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 2, 'item 2 has two permissions with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), 'R', 'item 2 is readable'
  assert.equal $li.find('> .permissions > strong:first').attr('title'), 'Read', 'item 2 has readable title'
  assert.textEqual $li.find('> .permissions > strong:last'), 'W', 'item 2 is writeable'
  assert.equal $li.find('> .permissions > strong:last').attr('title'), 'Write', 'item 2 has writeable title'
  $li = $lis.eq(2)
  assert.is $li, '.file', 'item 3 has class "file"'
  assert.is $li, '.filetype-text', 'item 3 is a text file'
  assert.itemSize $li.find('> i'), 1, 'item 3 has an icon'
  assert.textEqual $li.find('> .name'), 'baz.txt', 'item 3 is called "baz.txt"'
  assert.textEqual $li.find('> .size'), '19,9 KB', 'item 3 has a size of 19,9 KB'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 3 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 2, 'item 3 has two permissions with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), 'R', 'item 3 is readable'
  assert.equal $li.find('> .permissions > strong:first').attr('title'), 'Read', 'item 3 has readable title'
  assert.textEqual $li.find('> .permissions > strong:last'), 'W', 'item 3 is writeable'
  assert.equal $li.find('> .permissions > strong:last').attr('title'), 'Write', 'item 3 has writeable title'
  $li = $lis.eq(3)
  assert.is $li, '.file', 'item 4 has class "file"'
  assert.is $li, '.filetype-spreadsheet', 'item 4 is a spreadsheet file'
  assert.itemSize $li.find('> i'), 1, 'item 4 has an icon'
  assert.textEqual $li.find('> .name'), 'yummy.csv', 'item 4 is called "yummy.csv"'
  assert.textEqual $li.find('> .size'), '38,4 MB', 'item 4 has a size of 38,4 MB'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 4 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 1, 'item 4 has one permission with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), '', 'item 4 is not readable'
  assert.textEqual $li.find('> .permissions > strong:last'), 'W', 'item 4 is writeable'
  assert.equal $li.find('> .permissions > strong:last').attr('title'), 'Write', 'item 4 has writeable title'
  assert.notEqual $emptyList.add($nonEmptyList).data('bs.documentlist'), null, 'both document lists are marked as document-list'

checkWheezyFolder = ($emptyList, $nonEmptyList, assert) ->
  assert.itemSize $emptyList.children('ul'), 0, 'one download list is empty'

  $ul = $nonEmptyList.children('ul')
  $lis = $ul.find('> li')
  assert.is $ul, '.document-list-container', 'has class "document-list-container"'
  assert.itemSize $ul, 1, 'exactly 1 <ul> element'
  assert.itemSize $lis, 3, 'exactly 3 <li> elements inside'

  $li = $lis.eq(0)
  assert.is $li, '.back-link', 'item 1 has class "back-link"'
  assert.itemSize $li.find('> i'), 1, 'item 1 has an icon'
  assert.textEqual $li.find('> .name'), 'back', 'item 1 is called "back"'
  assert.equal $li.find('> .permissions').html(), '', 'item 1 has no permissions'
  $li = $lis.eq(1)
  assert.is $li, '.file', 'item 2 has class "file"'
  assert.is $li, '.filetype-text', 'item 2 is a text file'
  assert.itemSize $li.find('> i'), 1, 'item 2 has an icon'
  assert.textEqual $li.find('> .name'), 'README.md', 'item 2 is called "README.md"'
  assert.textEqual $li.find('> .size'), '394,3 KB', 'item 2 has a size of 394,3 KB'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 2 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 2, 'item 2 has two permissions with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), 'R', 'item 2 is readable'
  assert.equal $li.find('> .permissions > strong:first').attr('title'), 'Read', 'item 2 has readable title'
  assert.textEqual $li.find('> .permissions > strong:last'), 'W', 'item 2 is writeable'
  assert.equal $li.find('> .permissions > strong:last').attr('title'), 'Write', 'item 2 has writeable title'
  $li = $lis.eq(2)
  assert.is $li, '.file', 'item 3 has class "file"'
  assert.is $li, '.filetype-document', 'item 3 is a text document file'
  assert.itemSize $li.find('> i'), 1, 'item 3 has an icon'
  assert.textEqual $li.find('> .name'), 'Documentation.odt', 'item 3 is called "Documentation.odt"'
  assert.textEqual $li.find('> .size'), '999,1 KB', 'item 3 has a size of 999,1 KB'
  assert.itemSize $li.find('> .permissions > strong'), 2, 'item 3 has two permissions'
  assert.itemSize $li.find('> .permissions > strong[title]'), 0, 'item 3 has no permissions with a title'
  assert.textEqual $li.find('> .permissions > strong:first'), '', 'item 3 is not readable'
  assert.textEqual $li.find('> .permissions > strong:last'), '', 'item 3 is not writeable'

fixtureDocumentLists = ->
  $('#qunit-fixture').append '''
    <div id="dl1" class="document-list"></div>
    <div id="dl2" class="document-list" data-list-url="/springcrm/document/list"></div>
  '''
