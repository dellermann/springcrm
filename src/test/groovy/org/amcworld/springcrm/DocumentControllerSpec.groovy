/*
 * DocumentControllerSpec.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.apache.commons.vfs2.FileObject
import org.apache.commons.vfs2.FileType
import org.apache.commons.vfs2.VFS
import org.grails.plugins.testing.GrailsMockMultipartFile
import org.grails.web.json.JSONObject
import spock.lang.Specification


@TestFor(DocumentController)
@Mock([Organization, Config])
class DocumentControllerSpec extends Specification {

    //-- Fields ---------------------------------

    FileObject root


    //-- Fixture methods ------------------------

    def setup() {
        createFixtureFileSystem()
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

    def 'Embedded list for an organization'() {
        given: 'some additional files'
        Thread.sleep 100		// sleeps are necessary to check lastModified
        Date d = new Date()
        FileObject f = root.resolveFile('foo/alpha.txt')
        f.createFile()
        OutputStream out = f.content.outputStream
        out << '''This is test file within the foo folder.

Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur mattis nibh
nulla, ut luctus tortor facilisis varius. Aenean vitae mauris feugiat nunc
facilisis mollis. Cras pellentesque ullamcorper dui, vitae efficitur mauris
dapibus nec. Quisque feugiat leo id quam aliquam, id porta lectus pretium. Nunc
ullamcorper ligula eget tortor finibus, sit amet egestas arcu porttitor. In
venenatis lacinia ipsum, et molestie quam sagittis vitae. Aliquam sit amet
ligula a dolor cursus eleifend at a mi. Nunc viverra arcu egestas massa
pellentesque ultrices. Mauris pellentesque nibh id augue dapibus volutpat.
Integer pulvinar sollicitudin erat, sit amet blandit neque eleifend sed. Ut
porta finibus turpis, non posuere nisi condimentum vitae. In hac habitasse
platea dictumst. In hac habitasse platea dictumst. Aenean rhoncus vitae mi et
pharetra. Ut fringilla felis egestas, dictum odio vitae, interdum quam.

Cras sollicitudin augue a mi eleifend venenatis. Fusce et finibus odio. Ut
fermentum rhoncus nulla, nec hendrerit lacus auctor vel. Phasellus congue velit
id commodo pellentesque. Pellentesque lectus leo, auctor a placerat sit amet,
vestibulum a mi. Vestibulum vitae scelerisque arcu. Sed vehicula scelerisque
maximus. Sed eu blandit purus, ac varius ipsum. Vivamus sit amet fermentum
augue. Pellentesque habitant morbi tristique senectus et netus et malesuada
fames ac turpis egestas. Morbi pulvinar massa dolor, vel vestibulum metus
dignissim vitae. Suspendisse fermentum pretium lectus, eget maximus magna
ultrices vel. Cras vel vulputate urna. Sed posuere, lorem sit amet scelerisque
vestibulum, tellus urna sodales diam, eu finibus mauris turpis sed diam. Sed
posuere felis id nisi ullamcorper condimentum.
'''
        out.close()
        Thread.sleep 100		// sleeps are necessary to check lastModified
        f = root.resolveFile('foo/quux')
        f.createFolder()
        f = root.resolveFile('foo/quux/beta.txt')
        f.createFile()
        out = f.content.outputStream
        out << 'This is yet another test file within the foo folder.'
        out.close()

        and: 'an organization'
        Organization org = mockOrganization()

        and: 'some auxiliary variables'
        Map alpha = [
            path: 'foo/alpha.txt',
            name: 'alpha.txt',
            size: 1764,
            lastModified: d
        ]
        Map beta = [
            path: 'foo/quux/beta.txt',
            name: 'beta.txt',
            size: 52,
            lastModified: d
        ]
        Map wheezy = [
            path: 'foo/wheezy.php',
            name: 'wheezy.php',
            size: 108
            // wheezy.php is created before d, so we cannot assert lastModified
            // here
        ]

        when: 'I obtain a list of files of this organization'
        def model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are three valid documents'
        checkEmbeddedList model, [alpha, beta, wheezy]

        when: 'I obtain a list of files of this organization in descending order'
        params.order = 'desc'
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are three valid documents'
        checkEmbeddedList model, [wheezy, beta, alpha]

        when: 'I obtain a list of files of this organization with invalid sort column and order'
        params.sort = 'foo'
        params.order = 'bar'
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are three valid documents'
        checkEmbeddedList model, [alpha, beta, wheezy]

        when: 'I obtain a list of files of this organization and sort by size'
        params.sort = 'size'
        params.order = null
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are three valid documents'
        checkEmbeddedList model, [beta, wheezy, alpha]

        when: 'I obtain a list of files of this organization and sort by size descending'
        params.sort = 'size'
        params.order = 'desc'
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are three valid documents'
        checkEmbeddedList model, [alpha, wheezy, beta]

        when: 'I obtain a list of files of this organization and sort by last modification time'
        params.sort = 'lastModified'
        params.order = 'asc'
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are three valid documents'
        checkEmbeddedList model, [wheezy, alpha, beta]

        when: 'I obtain a list of files of this organization and sort by last modification time descending'
        params.sort = 'lastModified'
        params.order = 'desc'
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are three valid documents'
        checkEmbeddedList model, [beta, alpha, wheezy]

        when: 'I obtain a list of files of this organization with max value'
        params.sort = 'lastModified'
        params.order = 'desc'
        params.max = 2
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are two valid documents'
        checkEmbeddedList model, [beta, alpha], 3

        when: 'I obtain a list of files of this organization with max and offset value'
        params.sort = 'lastModified'
        params.order = 'desc'
        params.max = 2
        params.offset = 1
        model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are two valid documents'
        checkEmbeddedList model, [alpha, wheezy], 3
    }

    def 'Embedded list for an organization with no documents'() {
        given: 'an organization'
        Organization org = mockOrganization()
        org.docPlaceholderValue = 'xyz'
        org.save flush: true

        when: 'I obtain a list of files of this organization'
        def model = controller.listEmbedded(org.id)

        then: 'I get a valid response'
        SC_OK == response.status

        and: 'there are no documents'
        checkEmbeddedList model, []
    }

    def 'Cannot display embedded list with non-existing organization'() {
        when: 'I obtain a list of files of a non-existing organization'
        controller.listEmbedded 1

        then: 'I get an error response'
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
        given: 'an empty file'
        def file = new GrailsMockMultipartFile(
            'file', 'foo.dat', 'application/octet-stream', new byte[0]
        )

        when: 'I upload this file'
        request.addFile file
        controller.upload ''

        then: 'I am redirected to the list and get a success message'
        '/document/index?path=' == response.redirectedUrl
        'document.upload.success' == flash.message

        and: 'the file has been created'
        root.resolveFile('foo.dat').exists()
    }

    def 'Upload non-empty file to base folder'() {
        given: 'a non-empty file'
        String data = 'Example file for upload'
        def file = new GrailsMockMultipartFile(
            'file', 'foo.txt', 'text/plain', data.bytes
        )

        when: 'I upload this file'
        request.addFile file
        controller.upload ''

        then: 'I am redirected to the list and get a success message'
        '/document/index?path=' == response.redirectedUrl
        'document.upload.success' == flash.message

        and: 'a file has been created'
        def f1 = root.resolveFile('foo.txt')
        f1.exists()
        data.length() == f1.content.size
        data == f1.content.inputStream.text

    }

    def 'Upload non-empty file to subdirectory'() {
        given: 'a non-empty file'
        String data = 'Example file for upload'
        def file = new GrailsMockMultipartFile(
            'file', 'foo.txt', 'text/plain', data.bytes
        )

        when: 'I upload this file to a subdirectory'
        request.addFile file
        controller.upload 'bar'

        then: 'I am redirected to the list and get a success message'
        '/document/index?path=bar' == response.redirectedUrl
        'document.upload.success' == flash.message

        and: 'a file has been created'
        def f2 = root.resolveFile('bar/foo.txt')
        f2.exists()
        data.length() == f2.content.size
        data == f2.content.inputStream.text
    }

    def 'Upload file to invalid folder'() {
        given: 'a non-empty file'
        String data = 'Example file for upload'
        def file = new GrailsMockMultipartFile(
            'file', 'foo.txt', 'text/plain', data.bytes
        )

        when: 'I upload this file to an invalid folder'
        request.addFile file
        controller.upload '..'

        then: 'I am redirected to the list and get an error message'
        '/document/index?path=..' == response.redirectedUrl
        'document.upload.error' == flash.message

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

    def 'Cannot delete whole root path with empty string'() {
        when: 'I delete the root folder'
        controller.delete ''

        then: 'I get a success code'
        SC_OK == response.status

        and: 'the root folder has not been deleted'
        root.exists()
        FileType.FOLDER == root.type
    }

    def 'Cannot delete whole root path with dot'() {
        when: 'I delete the root folder'
        controller.delete '.'

        then: 'I get a success code'
        SC_OK == response.status

        and: 'the root folder has not been deleted'
        root.exists()
        FileType.FOLDER == root.type
    }

    def 'Cannot delete whole root path with path'() {
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

    def 'Delete with return URL'() {
        when: 'I delete a file and specify a return URL'
        params.returnUrl = '/organization/show/1'
        controller.delete 'baz.txt'

        then: 'I am redirected'
        response.redirectedUrl == '/organization/show/1'

        and: 'the file has been deleted'
        !root.resolveFile('baz.txt').exists()
    }


    //-- Non-public methods ---------------------

    private void checkBarFolderContent(JSONObject json) {
        assert 0 == response.json.folders.size()
        assert 0 == response.json.files.size()
    }

    private void checkEmbeddedList(Map model, List expected,
                                   int totalSize = expected.size())
    {
        assert totalSize == model.documentInstanceTotal
        int size = expected.size()
        List documents = model.documentInstanceList
        assert size == documents.size()

        for (int i = 0; i < size; i++) {
            Map ed = expected[i]
            Map d = documents[i]
            assert ed.path == d.path
            assert ed.name == d.name
            assert ed.size == d.size
            if (ed.lastModified) {
                assert d.lastModified >= ed.lastModified
            }
        }
    }

    private void checkFooFolderContent(JSONObject json) {
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

    private void checkRootFolderContent(JSONObject json) {
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

    private void createFixtureFileSystem() {
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

        config.springcrm.dir.documents = 'ram:///'

        controller.documentService = new DocumentService()
        controller.documentService.grailsApplication = grailsApplication
    }

    private void destroyFixtureFileSystem() {
        VFS.manager.closeFileSystem root.fileSystem
    }

    private Organization mockOrganization() {
        mockDomain Organization, [
            [
                number: 40473,
                recType: 1,
                name: 'Your Organization',
                billingAddr: new Address(),
                shippingAddr: new Address(),
                phone: '3030303',
                fax: '703037494',
                phoneOther: '73903037',
                email1: 'info@yourorganization.de',
                email2: 'office@yourorganization.de',
                website: 'www.yourorganization.de',
                legalForm: 'Ltd.',
                type: new OrgType(name: 'foo'),
                industry: new Industry(name: 'bar'),
                owner: 'Mr. Smith',
                numEmployees: '5',
                rating: new Rating(name: 'active'),
                notes: 'whee',
                docPlaceholderValue: 'foo'
            ]
        ]

        Organization.first()
    }
}
