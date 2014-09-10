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


#-- Initialization functions --------------------

mockAjax = ->
  $ = jQuery

  baseData =
    url: LIST_URL

  $.mockjaxClear()
  $.mockjax $.extend {}, baseData,
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
  $.mockjax $.extend {}, baseData,
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
  $.mockjax $.extend {}, baseData,
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

  $.mockjax
    status: 200
    url: DELETE_URL

  null

testDocumentList = (elem, chainFunc) ->
  chain = new TriggerChain()

  $(elem).documentlist
    init: -> chainFunc.call this, chain
    pathChanged: (path) -> chain.trigger path


#-- Fixtures ------------------------------------

$L = (key) -> $L._messages[key.replace /\./g, '_']
$L._messages =
  default_btn_remove: 'Remove entry'
  default_delete_confirm_msg: 'Are you sure?'
  document_back_label: 'back'
  document_path_label: 'Path'
  document_path_root: 'Root folder'
  document_permissions_read: 'R'
  document_permissions_read_title: 'Read'
  document_permissions_write: 'W'
  document_permissions_write_title: 'Write'
window.$L = $L

BASE_URL = '/springcrm/document'
DELETE_URL = BASE_URL + '/delete'
LIST_URL = BASE_URL + '/list'

mockAjax()


#-- QUnit extensions ----------------------------

((assert) ->
  assert.backLinkEqual = ($lis) ->
    $li = $lis.first()
    assert.is $li, '.back-link', 'item 1 has class "back-link"'
    assert.itemSize $li.find('> i'), 1, 'item 1 has an icon'
    assert.textEqual $li.find('> .name'), 'back', 'item 1 is called "back"'
    assert.equal $li.find('> .permissions').html(), '',
      'item 1 has no permissions'

  assert.fileEqual = ($lis, index, name, type, size, readable, writeable) ->
    $li = $lis.eq index
    pos = index + 1

    this.is $li, '.file', "item #{pos} has class 'file'"
    this.is $li, ".filetype-#{type}", "item #{pos} is of type #{type}"
    this.itemSize $li.find('> i'), 1, "item #{pos} has an icon"
    this.textEqual $li.find('> .name'), name, "item #{pos} is called '#{name}'"
    this.textEqual $li.find('> .size'), size,
      "item #{pos} has a size of #{size}"

    numTitles = 0
    numTitles++ if readable
    numTitles++ if writeable
    $strongs = $li.find('> .permissions > strong')
    this.itemSize $strongs, 2, "item #{pos} has two permissions"
    this.itemSize $strongs.filter('[title]'), numTitles,
      "item #{pos} has #{numTitles} permissions with a title"
    $strong = $strongs.first()
    this.textEqual $strong, (if readable then 'R' else ''),
      "item #{pos} is #{if readable then '' else 'not '}readable"
    if readable
      this.equal $strong.attr('title'), 'Read', "item #{pos} has readable title"
    $strong = $strongs.last()
    this.textEqual $strong, (if writeable then 'W' else ''),
      "item #{pos} is #{if writeable then '' else 'not '}writeable"
    if writeable
      this.equal $strong.attr('title'), 'Write',
        "item #{pos} has writeable title"
    this.itemSize $li.find('.action-buttons .delete-btn'), 1,
      "item #{pos} has a delete button"

  assert.folderEqual = ($lis, index, name, readable, writeable) ->
    $li = $lis.eq index
    pos = index + 1

    assert.is $li, '.folder', "item #{pos} has class 'folder'"
    assert.itemSize $li.find('> i'), 1, "item #{pos} has an icon"
    assert.textEqual $li.find('> .name'), name,
      "item #{pos} is called '#{name}'"

    numTitles = 0
    numTitles++ if readable
    numTitles++ if writeable
    $strongs = $li.find('> .permissions > strong')
    this.itemSize $strongs, 2, "item #{pos} has two permissions"
    this.itemSize $strongs.filter('[title]'), numTitles,
      "item #{pos} has #{numTitles} permissions with a title"
    $strong = $strongs.first()
    this.textEqual $strong, (if readable then 'R' else ''),
      "item #{pos} is #{if readable then '' else 'not '}readable"
    if readable
      this.equal $strong.attr('title'), 'Read', "item #{pos} has readable title"
    $strong = $strongs.last()
    this.textEqual $strong, (if writeable then 'W' else ''),
      "item #{pos} is #{if writeable then '' else 'not '}writeable"
    if writeable
      this.equal $strong.attr('title'), 'Write',
        "item #{pos} has writeable title"
    $deleteBtn = $li.find('.action-buttons .delete-btn')
    this.itemSize $deleteBtn, 1, "item #{pos} has a delete button"
    this.equal $deleteBtn.attr('title'), 'Remove entry',
      "item #{pos} has a delete button with title"

  assert.documentListEqual = ($ul, data) ->
    $lis = $ul.find('> li')

    backLink = data.backLink
    folders = data.folders ? []
    files = data.files ? []

    size = folders.length + files.length
    size++ if backLink

    assert.is $ul, '.document-list-container',
      'has class "document-list-container"'
    assert.itemSize $ul, 1, 'exactly 1 <ul> element'
    assert.itemSize $lis, size, "exactly #{size} <li> elements inside"

    offset = 0
    if backLink
      offset++
      this.backLinkEqual $lis
    for f, i in folders
      this.folderEqual $lis, offset + i, f[0], f[1], f[2]
    for f, j in files
      this.fileEqual $lis, offset + i + j, f[0], f[1], f[2], f[3], f[4]

  assert.is = ($element, selector, message) ->
    ok = $element.is(selector)
    QUnit.push ok, ok, true, message

  assert.itemSize = ($element, size, message) ->
    actual = $element.length
    QUnit.push actual is size, actual, size, message

  assert.textEqual = ($element, text, message) ->
    actual = $element.text()
    QUnit.push actual is text, actual, text, message

) QUnit.assert


#-- Feature tests -------------------------------

QUnit.module 'Instantiation'
QUnit.test 'Instantiate widget without loading', (assert) ->
  fixtureDocumentLists()

  $dl = $('.document-list')
  $dl.documentlist()

  assert.equal $dl.html(), '', 'both download lists are empty'
  assert.notEqual $dl.data('bs.documentlist'), null, 'both elements are marked as download-list'

QUnit.asyncTest 'Instantiate widget with loading', (assert) ->
  expect 49

  fixtureDocumentLists()

  $('.document-list').documentlist
    init: ->
      checkRootFolder $('#dl1'), $('#dl2'), assert
      assert.notEqual $('#dl1').add('#dl2').data('bs.documentlist'),
        null, 'both document lists are marked as document-list'
      QUnit.start()


QUnit.module 'API calls'
QUnit.asyncTest 'Get initial path', (assert) ->
  expect 1

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      assert.equal $(this).documentlist('path'), '',
        '"path" returned correct path'
      QUnit.start()

QUnit.asyncTest 'Get path of subfolders', (assert) ->
  expect 3

  fixtureDocumentLists()

  testDocumentList '#dl2', (chain) ->
    $this = $(this)

    chain.newPromise( -> $('#dl2 > ul > li:nth-child(2)').click())
      .then( (path) ->
        assert.equal path, 'foo', 'path is set correctly'
        @newPromise -> $('#dl2 > ul > li:nth-child(2)').click()
      )
      .done (path) ->
        assert.equal path, 'foo/wheezy', 'path is set correctly'
        assert.equal $this.documentlist('path'), 'foo/wheezy',
          '"path" returned correct path'
        QUnit.start()

QUnit.asyncTest 'Set path', (assert) ->
  expect 32

  fixtureDocumentLists()

  testDocumentList '#dl2', (chain) ->
    $this = $(this)

    chain.newPromise( -> $this.documentlist 'path', 'foo/wheezy')
      .done (path) ->
        assert.equal path, 'foo/wheezy', 'path is set correctly'
        assert.equal $this.documentlist('path'), 'foo/wheezy',
          '"path" returned correct path'
        checkWheezyFolder $('#dl1'), $('#dl2'), assert
        QUnit.start()

QUnit.asyncTest 'Add file before item', (assert) ->
  expect 60

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFile',
        name: 'foo.txt'
        ext: 'txt'
        size: 4503
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.file'), 3, 'now it has 3 file elements'
      assert.documentListEqual $ul,
        folders: [
          ['bar', true, false]
          ['foo', true, true]
        ]
        files: [
          ['baz.txt', 'text', '19,9 KB', true, true]
          ['foo.txt', 'text', '4,4 KB', true, true]
          ['yummy.csv', 'spreadsheet', '38,4 MB', false, true]
        ]

      QUnit.start()

QUnit.asyncTest 'Add file after item', (assert) ->
  expect 60

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFile',
        name: 'zzz.txt'
        ext: 'txt'
        size: 4503
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.file'), 3, 'now it has 3 file elements'
      assert.documentListEqual $ul,
        folders: [
          ['bar', true, false]
          ['foo', true, true]
        ]
        files: [
          ['baz.txt', 'text', '19,9 KB', true, true]
          ['yummy.csv', 'spreadsheet', '38,4 MB', false, true]
          ['zzz.txt', 'text', '4,4 KB', true, true]
        ]

      QUnit.start()

QUnit.asyncTest 'Add file to list with folders only', (assert) ->
  expect 37

  $ = jQuery

  $.mockjaxClear()
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
      files: []
    url: LIST_URL

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFile',
        name: 'foo.txt'
        ext: 'txt'
        size: 4503
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.file'), 1, 'now it has 1 file element'
      assert.documentListEqual $ul,
        folders: [
          ['bar', true, false]
          ['foo', true, true]
        ]
        files: [
          ['foo.txt', 'text', '4,4 KB', true, true]
        ]

      mockAjax()
      QUnit.start()

QUnit.asyncTest 'Add file to empty list', (assert) ->
  expect 16

  $ = jQuery

  $.mockjaxClear()
  $.mockjax
    data:
      path: ''
    responseText:
      folders: []
      files: []
    url: LIST_URL

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFile',
        name: 'foo.txt'
        ext: 'txt'
        size: 4503
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.file'), 1, 'now it has 1 file element'
      assert.documentListEqual $ul,
        files: [
          ['foo.txt', 'text', '4,4 KB', true, true]
        ]

      mockAjax()
      QUnit.start()

QUnit.asyncTest 'Add folder before item', (assert) ->
  expect 59

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFolder',
        name: 'breeze'
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.folder'), 3,
        'now it has 3 folder elements'
      assert.documentListEqual $ul,
        folders: [
          ['bar', true, false]
          ['breeze', true, true]
          ['foo', true, true]
        ]
        files: [
          ['baz.txt', 'text', '19,9 KB', true, true]
          ['yummy.csv', 'spreadsheet', '38,4 MB', false, true]
        ]

      QUnit.start()

QUnit.asyncTest 'Add folder after item', (assert) ->
  expect 59

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFolder',
        name: 'mood'
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.folder'), 3,
        'now it has 3 folder elements'
      assert.documentListEqual $ul,
        folders: [
          ['bar', true, false]
          ['foo', true, true]
          ['mood', true, true]
        ]
        files: [
          ['baz.txt', 'text', '19,9 KB', true, true]
          ['yummy.csv', 'spreadsheet', '38,4 MB', false, true]
        ]

      QUnit.start()

QUnit.asyncTest 'Add folder to list with files only', (assert) ->
  expect 38

  $ = jQuery

  $.mockjaxClear()
  $.mockjax
    data:
      path: ''
    responseText:
      folders: []
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
    url: LIST_URL

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFolder',
        name: 'breeze'
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.folder'), 1,
        'now it has 1 folder element'
      assert.documentListEqual $ul,
        folders: [
          ['breeze', true, true]
        ]
        files: [
          ['baz.txt', 'text', '19,9 KB', true, true]
          ['yummy.csv', 'spreadsheet', '38,4 MB', false, true]
        ]

      mockAjax()
      QUnit.start()

QUnit.asyncTest 'Add folder to empty list', (assert) ->
  expect 15

  $ = jQuery

  $.mockjaxClear()
  $.mockjax
    data:
      path: ''
    responseText:
      folders: []
      files: []
    url: LIST_URL

  fixtureDocumentLists()

  $('#dl2').documentlist
    init: ->
      $dl = $(this)
      $dl.documentlist 'addFolder',
        name: 'breeze'
        readable: true
        writeable: true

      $ul = $dl.children 'ul'
      assert.itemSize $ul.children('li.folder'), 1,
        'now it has 1 folder element'
      assert.documentListEqual $ul,
        folders: [
          ['breeze', true, true]
        ]

      mockAjax()
      QUnit.start()


QUnit.module 'User interaction'
QUnit.asyncTest 'Click on folder', (assert) ->
  expect 43

  fixtureDocumentLists()

  testDocumentList '#dl2', (chain) ->
    chain.newPromise( -> $('#dl2 > ul > li:nth-child(2)').click())
      .done (path) ->
        assert.equal path, 'foo', 'path is set correctly'
        checkFooFolder $('#dl1'), $('#dl2'), assert
        QUnit.start()

QUnit.asyncTest 'Click on two folders', (assert) ->
  expect 32

  fixtureDocumentLists()

  testDocumentList '#dl2', (chain) ->
    chain.newPromise( -> $('#dl2 > ul > li:nth-child(2)').click())
      .then( (path) ->
        assert.equal path, 'foo', 'path is set correctly'
        @newPromise -> $('#dl2 > ul > li:nth-child(2)').click()
      )
      .done (path) ->
        assert.equal path, 'foo/wheezy', 'path is set correctly'
        checkWheezyFolder $('#dl1'), $('#dl2'), assert
        QUnit.start()

QUnit.asyncTest 'Click on back link', (assert) ->
  expect 50

  fixtureDocumentLists()

  testDocumentList '#dl2', (chain) ->
    chain.newPromise( -> $('#dl2 > ul > li:nth-child(2)').click())
      .then( (path) ->
        assert.equal path, 'foo'
        @newPromise -> $('#dl2 > ul > .back-link').click()
      )
      .done (path) ->
        assert.equal path, ''
        checkRootFolder $('#dl1'), $('#dl2'), assert
        QUnit.start()

QUnit.asyncTest 'Click on two back links', (assert) ->
  expect 94

  fixtureDocumentLists()

  testDocumentList '#dl2', (chain) ->
    chain.newPromise( -> $('#dl2 > ul > li:nth-child(2)').click())
      .then( (path) ->
        assert.equal path, 'foo', 'path is set correctly'
        @newPromise -> $('#dl2 > ul > li:nth-child(2)').click()
      )
      .then( (path) ->
        assert.equal path, 'foo/wheezy', 'path is set correctly'
        @newPromise -> $('#dl2 > ul > .back-link').click()
      )
      .then( (path) ->
        assert.equal path, 'foo', 'path is set correctly'
        checkFooFolder $('#dl1'), $('#dl2'), assert
        @newPromise -> $('#dl2 > ul > .back-link').click()
      )
      .done (path) ->
        assert.equal path, '', 'path is set correctly'
        checkRootFolder $('#dl1'), $('#dl2'), assert
        QUnit.start()

QUnit.asyncTest 'Click on delete button and confirm', (assert) ->
  $ = jQuery

  expect 36

  oldConfirm = $.confirm
  $.confirm = (msg) ->
    assert.equal msg, 'Are you sure?', 'the confirm message is correct'
    true

  fixtureDocumentLists()

  $('.document-list').documentlist
    init: ->
      $('#dl2 > ul > li:nth-child(3) .delete-btn').click()
    removeSuccess: ->
      assert.documentListEqual $('#dl2 > ul'),
        folders: [
          ['bar', true, false]
          ['foo', true, true]
        ]
        files: [
          ['yummy.csv', 'spreadsheet', '38,4 MB', false, true]
        ]
      $.confirm = oldConfirm
      QUnit.start()

QUnit.asyncTest 'Click on delete button but cancel', (assert) ->
  $ = jQuery

  expect 49

  oldConfirm = $.confirm
  $.confirm = (msg) ->
    assert.equal msg, 'Are you sure?', 'the confirm message is correct'
    false

  fixtureDocumentLists()

  $('.document-list').documentlist
    init: ->
      $('#dl2 > ul > li:nth-child(3) .delete-btn').click()
    removeFailed: ->
      checkRootFolder $('#dl1'), $('#dl2'), assert
      $.confirm = oldConfirm
      QUnit.start()

QUnit.asyncTest 'Click on delete button but AJAX failed', (assert) ->
  $ = jQuery

  expect 49

  oldConfirm = $.confirm
  $.confirm = (msg) ->
    assert.equal msg, 'Are you sure?', 'the confirm message is correct'
    true

  $.mockjaxClear()
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
    url: LIST_URL
  $.mockjax
    status: 404
    url: DELETE_URL

  fixtureDocumentLists()

  $('.document-list').documentlist
    init: ->
      $('#dl2 > ul > li:nth-child(3) .delete-btn').click()
    removeFailed: ->
      checkRootFolder $('#dl1'), $('#dl2'), assert
      mockAjax()
      $.confirm = oldConfirm
      QUnit.start()


#-- Auxiliary functions -------------------------

checkFooFolder = ($emptyList, $nonEmptyList, assert) ->
  assert.itemSize $emptyList.children('ul'), 0, 'one download list is empty'
  assert.documentListEqual $nonEmptyList.children('ul'),
    backLink: true
    folders: [
      ['wheezy', true, true]
    ]
    files: [
      ['hello-world.md', 'text', '296,0 KB', false, true]
      ['hello-world.c', 'code', '731,8 KB', true, true]
    ]

checkRootFolder = ($emptyList, $nonEmptyList, assert) ->
  assert.equal $emptyList.children('ul').length, 0,
    'one download list is empty'
  assert.documentListEqual $nonEmptyList.children('ul'),
    folders: [
      ['bar', true, false]
      ['foo', true, true]
    ]
    files: [
      ['baz.txt', 'text', '19,9 KB', true, true]
      ['yummy.csv', 'spreadsheet', '38,4 MB', false, true]
    ]

checkWheezyFolder = ($emptyList, $nonEmptyList, assert) ->
  assert.itemSize $emptyList.children('ul'), 0, 'one download list is empty'
  assert.documentListEqual $nonEmptyList.children('ul'),
    backLink: true
    files: [
      ['README.md', 'text', '394,3 KB', true, true]
      ['Documentation.odt', 'document', '999,1 KB', false, false]
    ]

fixtureDocumentLists = ->
  $('#qunit-fixture').append '''
    <div id="dl1" class="document-list"></div>
    <div id="dl2" class="document-list"
      data-list-url="/springcrm/document/list"
      data-delete-url="/springcrm/document/delete"
      ></div>
  '''

