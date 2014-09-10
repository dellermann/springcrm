/*
 * DocumentServiceSpec.groovy
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

import org.apache.commons.vfs2.FileType;

import grails.test.mixin.TestFor
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.FileSystemException
import spock.lang.Specification


@TestFor(DocumentService)
class DocumentServiceSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Get default root path'() {
        expect: 'I get a non-blank root path'
        null != service.rootPath
        '' != service.rootPath
    }

    def 'Get root path'() {
        when: 'I set various configuration values'
        service.grailsApplication.config.springcrm.dir.documents = path

        then: 'I get these values as root path'
        rootPath == service.rootPath

        where:
        path                        | rootPath
        '/tmp/springcrm'            | '/tmp/springcrm'
        '/home/jsmith/.springcrm'   | '/home/jsmith/.springcrm'
    }

    def 'Get root'() {
        when: 'I set various configuration values'
        service.grailsApplication.config.springcrm.dir.documents = path

        then: 'I get valid root objects'
        def root = service.root
        null != root
        rootUrl == root.URL.toString()

        where:
        path                            | rootUrl
        '/tmp/springcrm'                | 'file:///tmp/springcrm'
        '/home/jsmith/.springcrm'       | 'file:///home/jsmith/.springcrm'
    }

	def 'Get valid file'() {
		given: 'a root path'
		service.grailsApplication.config.springcrm.dir.documents =
			'/tmp/springcrm-test/47bc3f71ad90b3'

		when: 'I obtain a valid, but non-existing file'
		FileObject f = service.getFile('foo.txt')

		then: 'I get a file object'
		null != f
		!f.exists()
		'/tmp/springcrm-test/47bc3f71ad90b3/foo.txt' == f.name.path
		'foo.txt' == f.name.baseName

		when: 'I obtain a valid, but non-existing file'
		f = service.getFile('./foo.txt')

		then: 'I get a file object'
		null != f
		!f.exists()
		'/tmp/springcrm-test/47bc3f71ad90b3/foo.txt' == f.name.path
		'foo.txt' == f.name.baseName
	}

	def 'Get valid file in subdirectory'() {
		given: 'a root path'
		service.grailsApplication.config.springcrm.dir.documents =
			'/tmp/springcrm-test/47bc3f71ad90b3'

		when: 'I obtain a valid, but non-existing file'
		FileObject f = service.getFile('bar/foo.txt')

		then: 'I get a file object'
		null != f
		!f.exists()
		'/tmp/springcrm-test/47bc3f71ad90b3/bar/foo.txt' == f.name.path
		'foo.txt' == f.name.baseName

		when: 'I obtain a valid, but non-existing file'
		f = service.getFile('./bar/.././bar/foo.txt')

		then: 'I get a file object'
		null != f
		!f.exists()
		'/tmp/springcrm-test/47bc3f71ad90b3/bar/foo.txt' == f.name.path
		'foo.txt' == f.name.baseName
	}

	def 'Get invalid files'() {
		given: 'a root path'
		service.grailsApplication.config.springcrm.dir.documents =
			'/tmp/springcrm-test/47bc3f71ad90b3'

		when: 'I obtain an invalid file in the parent directory'
		service.getFile('../foo.txt')

		then: 'I get an exception'
		thrown FileSystemException

		when: 'I obtain an invalid file in the parent directory'
		service.getFile('./bar/../../foo.txt')

		then: 'I get an exception'
		thrown FileSystemException

		when: 'I obtain an invalid file in the root directory'
		service.getFile('/foo.txt')

		then: 'I get an exception'
		thrown FileSystemException
	}

	def 'Upload file'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()

		and: 'an example file'
		String data = 'Example file for upload'
		InputStream input = new ByteArrayInputStream(data.bytes)

		when: 'I upload this file'
		FileObject f = service.uploadFile('.', 'foo.txt', input)

		then: 'the file was uploaded successfully'
		new File(root, 'foo.txt').exists()

		and: 'I get valid file information'
		null != f
		f.exists()
		'/tmp/springcrm-test/47bc3f71ad90b3/foo.txt' == f.name.path
		'foo.txt' == f.name.baseName
		data.length() == f.content.size
		data == f.content.inputStream.text

		cleanup:
		root.deleteDir()
	}

	def 'Upload file to subdirectory'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()
		File bar = new File(root, 'bar')
		bar.mkdir()

		and: 'an example file'
		String data = 'Example file for upload into subdirectory "bar"'
		InputStream input = new ByteArrayInputStream(data.bytes)

		when: 'I upload this file'
		FileObject f = service.uploadFile('bar', 'foo.txt', input)

		then: 'the file was uploaded successfully'
		new File(bar, 'foo.txt').exists()

		and: 'I get valid file information'
		null != f
		f.exists()
		'/tmp/springcrm-test/47bc3f71ad90b3/bar/foo.txt' == f.name.path
		'foo.txt' == f.name.baseName
		data.length() == f.content.size
		data == f.content.inputStream.text

		cleanup:
		root.deleteDir()
	}

	def 'Upload file to invalid path'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()

		and: 'an example file'
		String data = 'Example file for upload'
		InputStream input = new ByteArrayInputStream(data.bytes)

		when: 'I upload this file'
		service.uploadFile('..', 'foo.txt', input)

		then: 'an exception is thrown'
		thrown FileSystemException

		and: 'the file was not uploaded'
		!new File(root.parentFile, 'foo.txt').exists()

		cleanup:
		root.deleteDir()
	}

	def 'Upload and overwrite existing file'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()

		and: 'two example files'
		String data1 = 'Example file for upload'
		InputStream input1 = new ByteArrayInputStream(data1.bytes)
		String data2 = 'Another example file for upload and overwrite'
		InputStream input2 = new ByteArrayInputStream(data2.bytes)

		when: 'I upload this file'
		service.uploadFile('.', 'foo.txt', input1)

		then: 'the file was uploaded successfully'
		def f1 = new File(root, 'foo.txt')
		f1.exists()
		data1 == f1.text

		when: 'I upload and overwrite this file'
		FileObject f = service.uploadFile('.', 'foo.txt', input2)

		then: 'the file was uploaded successfully'
		def f2 = new File(root, 'foo.txt')
		f2.exists()
		data2 == f2.text

		and: 'I get valid file information'
		null != f
		f.exists()
		'/tmp/springcrm-test/47bc3f71ad90b3/foo.txt' == f.name.path
		'foo.txt' == f.name.baseName
		data2.length() == f.content.size
		data2 == f.content.inputStream.text

		cleanup:
		root.deleteDir()
	}

	def 'Create new folder'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()

		when: 'I create a folder'
		FileObject f = service.createFolder('.', 'my-new-folder')

		then: 'I get a valid FileObject'
		null != f
		f.exists()
		f.type == FileType.FOLDER
		'/tmp/springcrm-test/47bc3f71ad90b3/my-new-folder' == f.name.path
		'my-new-folder' == f.name.baseName

		and: 'the folder was created successfully'
		File file = new File(root, 'my-new-folder')
		file.exists()
		file.directory

		cleanup:
		root.deleteDir()
	}

	def 'Create new folder in subdirectory'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()
		File bar = new File(root, 'bar')
		bar.mkdir()

		when: 'I create a folder'
		FileObject f = service.createFolder('bar', 'my-new-folder')

		then: 'I get a valid FileObject'
		null != f
		f.exists()
		f.type == FileType.FOLDER
		'/tmp/springcrm-test/47bc3f71ad90b3/bar/my-new-folder' == f.name.path
		'my-new-folder' == f.name.baseName

		and: 'the folder was created successfully'
		File file = new File(bar, 'my-new-folder')
		file.exists()
		file.directory

		cleanup:
		root.deleteDir()
	}

	def 'Create new folder in invalid path'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()

		when: 'I create a folder'
		service.createFolder('..', 'my-new-folder')

		then: 'I get a valid FileObject'
		thrown FileSystemException

		and: 'the folder was not created'
		!new File(root, 'my-new-folder').exists()

		cleanup:
		root.deleteDir()
	}

	def 'Create new folder with invalid name'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()

		when: 'I create a folder'
		service.createFolder('.', '..')

		then: 'I get a valid FileObject'
		thrown FileSystemException

		cleanup:
		root.deleteDir()
	}

	def 'Create new folder and overwrite existing one'() {
		given: 'a root path'
		String rootPath = '/tmp/springcrm-test/47bc3f71ad90b3'
		service.grailsApplication.config.springcrm.dir.documents = rootPath
		File root = new File(rootPath)
		root.mkdirs()

		when: 'I create a folder'
		FileObject f = service.createFolder('.', 'my-new-folder')

		then: 'I get a valid FileObject'
		null != f
		f.exists()
		f.type == FileType.FOLDER
		'/tmp/springcrm-test/47bc3f71ad90b3/my-new-folder' == f.name.path
		'my-new-folder' == f.name.baseName

		and: 'the folder was created successfully'
		File file1 = new File(root, 'my-new-folder')
		file1.exists()
		file1.directory

		when: 'I create the folder anew'
		f = service.createFolder('.', 'my-new-folder')

		then: 'I get a valid FileObject'
		null != f
		f.exists()
		f.type == FileType.FOLDER
		'/tmp/springcrm-test/47bc3f71ad90b3/my-new-folder' == f.name.path
		'my-new-folder' == f.name.baseName

		and: 'the folder still exists'
		File file2 = new File(root, 'my-new-folder')
		file2.exists()
		file2.directory

		cleanup:
		root.deleteDir()
	}
}