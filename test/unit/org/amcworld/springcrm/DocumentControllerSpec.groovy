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

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND
import static javax.servlet.http.HttpServletResponse.SC_OK

import grails.test.mixin.TestFor
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.FileType
import org.apache.commons.vfs2.VFS
import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import org.codehaus.groovy.grails.web.json.JSONObject
import spock.lang.Specification


@TestFor(DocumentController)
class DocumentControllerSpec extends Specification {

    //-- Instance variables ---------------------

    FileObject root


    //-- Fixture methods ------------------------

    def setup() {
        createFixtureFileSystem()
		mockDocumentService()
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
        SC_OK == response.status
        checkRootFolderContent response.json
    }

    def 'List non-empty subdirectory content'() {
        when: 'I list the content of the root directory with foo path'
        params.path = 'foo'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        SC_OK == response.status
        checkFooFolderContent response.json

        when: 'I list the content of the root directory with foo/ path'
        params.path = 'foo/'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        SC_OK == response.status
        checkFooFolderContent response.json

        when: 'I list the content of the root directory with a complex path'
        params.path = 'bar/./../foo/.'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        SC_OK == response.status
        checkFooFolderContent response.json
    }

    def 'List empty subdirectory content'() {
        when: 'I list the content of the root directory with bar path'
        params.path = 'bar'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        SC_OK == response.status
        checkBarFolderContent response.json

        when: 'I list the content of the root directory with /bar/ path'
        params.path = 'bar/'
        controller.dir()

        then: 'I get the correct JSON data of these files'
        SC_OK == response.status
        checkBarFolderContent response.json
    }

    def 'Cannot list a forbidden parent directory'() {
        when: 'I list the content of the parent directory'
        params.path = '..'
        controller.dir()

        then: 'I get an error'
        SC_NOT_FOUND == response.status
    }

    def 'Cannot list a forbidden parent directory with complex path'() {
        when: 'I list the content of the parent directory'
        params.path = 'foo/../bar/../..'
        controller.dir()

        then: 'I get an error'
        SC_NOT_FOUND == response.status
    }

    def 'Download existing file in root directory'() {
        when: 'I download an existing file'
        params.path = 'baz.txt'
        controller.download()

        then: 'I get file'
        SC_OK == response.status
        'This is a test file.' == response.text
        'attachment; filename="baz.txt"' == response.getHeader('Content-Disposition')
        20 == response.contentLength

        when: 'I download an existing file using a complex path'
        response.reset()
        params.path = 'bar/.././baz.txt'
        controller.download()

        then: 'I get file'
        SC_OK == response.status
        'This is a test file.' == response.text
        'attachment; filename="baz.txt"' == response.getHeader('Content-Disposition')
        20 == response.contentLength
    }

    def 'Download existing file in subdirectory'() {
        when: 'I download an existing file'
        params.path = 'foo/wheezy.php'
        controller.download()

        then: 'I get file'
        SC_OK == response.status
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
        SC_OK == response.status
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
        SC_NOT_FOUND == response.status
    }

    def 'Cannot download a forbidden file'() {
        when: 'I download a forbidden file'
        params.path = '../parent-test.txt'
        controller.download()

        then: 'I get an error'
        SC_NOT_FOUND == response.status
    }

    def 'Cannot download a directory'() {
        when: 'I download a directory'
        params.path = 'foo'
        controller.download()

        then: 'I get an error'
        SC_NOT_FOUND == response.status
    }

	def 'Upload empty file'() {
		when: 'I upload an empty file'
		def file = new GrailsMockMultipartFile(
			'file', 'foo.dat', 'application/octet-stream', new byte[0]
		)
		request.addFile file
		controller.upload ''

		then: 'I get a success code'
        SC_OK == response.status

		and: 'no file has been created'
		!root.resolveFile('foo.dat').exists()
	}

	def 'Upload non-empty file'() {
		given: 'an example file content'
		String data = 'Example file for upload'

		when: 'I upload a file'
		def file = new GrailsMockMultipartFile(
			'file', 'foo.txt', 'text/plain', data.bytes
		)
		request.addFile file
		controller.upload ''

		then: 'I get information about the uploaded file'
		'foo.txt' == response.json.name
		'txt' == response.json.ext
		data.length() == response.json.size
		response.json.readable
		response.json.writeable

		and: 'a file has been created'
		def f1 = root.resolveFile('foo.txt')
		f1.exists()
		data.length() == f1.content.size
		data == f1.content.inputStream.text

		when: 'I upload a file to a subdirectory'
		request.addFile file
		controller.upload 'bar'

		then: 'I get information about the uploaded file'
		'foo.txt' == response.json.name
		'txt' == response.json.ext
		data.length() == response.json.size
		response.json.readable
		response.json.writeable

		and: 'a file has been created'
		def f2 = root.resolveFile('bar/foo.txt')
		f2.exists()
		data.length() == f2.content.size
		data == f2.content.inputStream.text
	}

	def 'Upload file to invalid folder'() {
		given: 'an example file content'
		String data = 'Example file for upload'

		when: 'I upload a file'
		def file = new GrailsMockMultipartFile(
			'file', 'foo.txt', 'text/plain', data.bytes
		)
		request.addFile file
		controller.upload '..'

		then: 'I get an error'
        SC_NOT_FOUND == response.status

		and: 'no file has been created'
		!root.resolveFile('foo.dat').exists()
	}

	def 'Create a folder'() {
		when: 'I create a folder'
		controller.createFolder '', 'my-new-folder'

		then: 'I get a success code'
		SC_OK == response.status

		and: 'the folder has been created'
		FileObject f = root.resolveFile('my-new-folder')
		f.exists()
		FileType.FOLDER == f.type
	}

	def 'Create a folder in invalid path'() {
		when: 'I create a folder'
		controller.createFolder '..', 'my-new-folder'

		then: 'I get an error code'
		SC_NOT_FOUND == response.status

		and: 'the folder has not been created'
		!root.resolveFile('my-new-folder').exists()
	}

	def 'Create a folder with invalid name'() {
		when: 'I create a folder'
		controller.createFolder '', '..'

		then: 'I get an error code'
		SC_NOT_FOUND == response.status
	}

	def 'Delete a file'() {
		when: 'I delete a file'
		controller.delete 'baz.txt'

		then: 'I get a success code'
		SC_OK == response.status

		and: 'the file has been deleted'
		!root.resolveFile('baz.txt').exists()
	}

	def 'Delete a file in subdirectory'() {
		when: 'I delete a file'
		controller.delete 'baz.txt'

		then: 'I get a success code'
		SC_OK == response.status

		and: 'the file has been deleted'
		!root.resolveFile('baz.txt').exists()
	}

	def 'Delete a folder'() {
		when: 'I delete a folder'
		controller.delete 'foo'

		then: 'I get a success code'
		SC_OK == response.status

		and: 'the folder has been deleted'
		!root.resolveFile('foo').exists()
	}

	def 'Cannot delete whole root path'() {
		when: 'I delete the root folder'
		controller.delete ''

		then: 'I get a success code'
		SC_OK == response.status

		and: 'the root folder has not been deleted'
		root.exists()
		FileType.FOLDER == root.type

		when: 'I delete the root folder'
		controller.delete '.'

		then: 'I get a success code'
		SC_OK == response.status

		and: 'the root folder has not been deleted'
		root.exists()
		FileType.FOLDER == root.type

		when: 'I delete the root folder'
		controller.delete './foo/.././.'

		then: 'I get a success code'
		SC_OK == response.status

		and: 'the root folder has not been deleted'
		root.exists()
		FileType.FOLDER == root.type
	}

	def 'Cannot delete a file with an invalid path'() {
		when: 'I delete the parent folder'
		controller.delete '..'

		then: 'I get an error code'
		SC_NOT_FOUND == response.status

		and: 'the root folder has not been deleted'
		root.exists()
		FileType.FOLDER == root.type
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
		root = VFS.manager.resolveFile('ram:///')
		FileObject foo = root.resolveFile('foo')
		foo.createFolder()
		FileObject bar = root.resolveFile('bar')
		bar.createFolder()
		FileObject f = root.resolveFile('yummy.csv')
		f.createFile()
		def out = f.content.outputStream
		out << '''foo;bar;baz
wheezy;groovy;yummy
'''
		out.close()
		f = root.resolveFile('baz.txt')
		f.createFile()
		out = f.content.outputStream
		out << 'This is a test file.'
		out.close()
		f = foo.resolveFile('wheezy.php')
		f.createFile()
		out = f.content.outputStream
		out << '''<?php
function foobar() {
    echo 'This is a test.';
}

function helloWorld() {
    echo 'Hello World!';
}
'''
		out.close()
    }

    protected void destroyFixtureFileSystem() {
		VFS.manager.closeFileSystem root.fileSystem
    }

	protected void mockDocumentService() {
		DocumentService.metaClass.getRootPath { -> 'ram:///' }
		controller.documentService = new DocumentService()
	}
}
