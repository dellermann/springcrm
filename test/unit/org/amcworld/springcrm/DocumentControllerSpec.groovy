/*
 * DocumentControllerSpec.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm

import grails.test.mixin.TestFor
import javax.servlet.http.HttpServletResponse
import org.apache.commons.io.FileUtils
import org.apache.commons.vfs2.FileSystemManager
import org.apache.commons.vfs2.VFS
import org.codehaus.groovy.grails.web.json.JSONObject
import spock.lang.Specification


@TestFor(DocumentController)
class DocumentControllerSpec extends Specification {

    //-- Instance variables ---------------------

    File root


    //-- Fixture methods ------------------------

    def setup() {
        createFixtureFileSystem()

        def mock = mockFor(DocumentService)
        mock.demandExplicit.getRoot(1..3) { ->
            FileSystemManager fsm = VFS.manager
            fsm.resolveFile root.toString()
        }
        controller.documentService = mock.createMock()
    }

    def cleanup() {
        destroyFixtureFileSystem()
    }


    //-- Feature methods ------------------------

    def 'List root directory'() {
        when: 'I list the content of the root directory with empty path'
        params.path = ''
        controller.dir()

        then: 'I get the correct JSON data of these files'
        HttpServletResponse.SC_OK == response.status
        checkRootFolderContent response.json
    }

    def 'List non-empty subdirectory content'() {
        when: 'I list the content of the root directory with foo path'
        params.path = 'foo'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        HttpServletResponse.SC_OK == response.status
        checkFooFolderContent response.json

        when: 'I list the content of the root directory with foo/ path'
        params.path = 'foo/'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        HttpServletResponse.SC_OK == response.status
        checkFooFolderContent response.json

        when: 'I list the content of the root directory with a complex path'
        params.path = 'bar/./../foo/.'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        HttpServletResponse.SC_OK == response.status
        checkFooFolderContent response.json
    }

    def 'List empty subdirectory content'() {
        when: 'I list the content of the root directory with bar path'
        params.path = 'bar'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        HttpServletResponse.SC_OK == response.status
        checkBarFolderContent response.json

        when: 'I list the content of the root directory with /bar/ path'
        params.path = 'bar/'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        HttpServletResponse.SC_OK == response.status
        checkBarFolderContent response.json
    }

    def 'Cannot list a non-existing root directory'() {
        given: 'the root directory is deleted'
        destroyFixtureFileSystem()

        when: 'I list the content of the non-existing root directory'
        params.path = ''
        controller.dir()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Cannot list a forbidden file system root directory'() {
        when: 'I list the content of the root directory with / path'
        params.path = '/'
        controller.dir()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Cannot list a forbidden directory'() {
        when: 'I list a forbidden directory'
        params.path = '/foo'
        controller.dir()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status

        when: 'I list a forbidden directory'
        params.path = '/foo/'
        controller.dir()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Cannot list a forbidden parent directory'() {
        when: 'I list the content of the parent directory'
        params.path = '..'
        controller.dir()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Cannot list a forbidden parent directory with complex path'() {
        when: 'I list the content of the parent directory'
        params.path = 'foo/../bar/../..'
        controller.dir()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Download existing file in root directory'() {
        when: 'I download an existing file'
        params.path = 'baz.txt'
        controller.download()

        then: 'I get file'
        HttpServletResponse.SC_OK == response.status
        'This is a test file.' == response.text
        'attachment; filename="baz.txt"' == response.getHeader('Content-Disposition')
        20 == response.contentLength

        when: 'I download an existing file using a complex path'
        response.reset()
        params.path = 'bar/.././baz.txt'
        controller.download()

        then: 'I get file'
        HttpServletResponse.SC_OK == response.status
        'This is a test file.' == response.text
        'attachment; filename="baz.txt"' == response.getHeader('Content-Disposition')
        20 == response.contentLength
    }

    def 'Download existing file in subdirectory'() {
        when: 'I download an existing file'
        params.path = 'foo/wheezy.php'
        controller.download()

        then: 'I get file'
        HttpServletResponse.SC_OK == response.status
        '''<?php
function foobar() {
    echo 'This is a test.';
}

function helloWorld() {
    echo 'Hello World!';
}
''' == response.text
        'attachment; filename="wheezy.php"' == response.getHeader('Content-Disposition')
        108 == response.contentLength

        when: 'I download an existing file using a complex path'
        response.reset()
        params.path = 'bar/.././foo/../foo/wheezy.php'
        controller.download()

        then: 'I get file'
        HttpServletResponse.SC_OK == response.status
        '''<?php
function foobar() {
    echo 'This is a test.';
}

function helloWorld() {
    echo 'Hello World!';
}
''' == response.text
        'attachment; filename="wheezy.php"' == response.getHeader('Content-Disposition')
    }

    def 'Cannot download a non-existing file'() {
        when: 'I download a non-existing file'
        params.path = 'xyz/whee.doc'
        controller.download()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Cannot download a forbidden file'() {
        when: 'I download a forbidden file'
        params.path = '../parent-test.txt'
        controller.download()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }

    def 'Cannot download a directory'() {
        when: 'I download a directory'
        params.path = 'foo'
        controller.download()

        then: 'I get an error'
        HttpServletResponse.SC_NOT_FOUND == response.status
    }


    //-- Non-public methods ---------------------

    protected void checkBarFolderContent(JSONObject json) {
        assert 0 == response.json.folders.size()
        assert 0 == response.json.files.size()
    }

    protected void checkFooFolderContent(JSONObject json) {
        assert 0 == response.json.folders.size()

        def files = response.json.files
        assert 1 == files.size()
        def f = files[0]
        assert 'wheezy.php' == f.name
        assert 'php' == f.ext
        assert 108 == f.size
        assert f.readable
        assert f.writeable
    }

    protected void checkRootFolderContent(JSONObject json) {
        def folders = response.json.folders
        assert 2 == folders.size()
        def f = folders[0]
        assert 'bar' == f.name
        assert f.readable
        assert f.writeable
        f = folders[1]
        assert 'foo' == f.name
        assert f.readable
        assert f.writeable

        def files = response.json.files
        2 == files.size()
        f = files[0]
        assert 'baz.txt' == f.name
        assert 'txt' == f.ext
        assert 20 == f.size
        assert f.readable
        assert f.writeable
        f = files[1]
        assert 'yummy.csv' == f.name
        assert 'csv' == f.ext
        assert 32 == f.size
        assert f.readable
        assert f.writeable
    }

    protected void createFixtureFileSystem() {
        root = new File('/tmp/springcrm-test-4793732949')
        destroyFixtureFileSystem()

        root.mkdirs()
        def foo = new File(root, 'foo')
        foo.mkdir()
        def bar = new File(root, 'bar')
        bar.mkdir()
        new File(root, 'yummy.csv').text = '''foo;bar;baz
wheezy;groovy;yummy
'''
        new File(root, 'baz.txt').text = 'This is a test file.'
        new File(foo, 'wheezy.php').text = '''<?php
function foobar() {
    echo 'This is a test.';
}

function helloWorld() {
    echo 'Hello World!';
}
'''
        new File(root, '.directory').text = '''[directory]
hidden = true
'''
        new File(root.parent, 'parent-test.txt').text = 'Unreachable file in parent directory'
    }

    protected void destroyFixtureFileSystem() {
        FileUtils.deleteQuietly root
        new File(root.parent, 'parent-test.txt').delete()
    }
}
