/*
 * DataFileTest.groovy
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

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification


@TestFor(DataFile)
@Mock([DataFile])
class DataFileSpec extends Specification {

    //-- Feature methods ------------------------

    def 'Create an instance from a non-existing File object'() {
        given: 'a File object'
        def f = new File('foo', 'bar')

        when: 'I create a DataFile instance from that file'
        def df = new DataFile(f)

        then: 'I get a IllegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'Create an instance from an existing HTML file'() {
        given: 'an HTML string'
        String s = createHtmlFileContent()

        and: 'a File object'
        def f = File.createTempFile('springcrm-unit-test', '.html')
        f.text = s

        when: 'I create a DataFile instance from that file'
        def df = new DataFile(f)

        then: 'I get a valid DataFile instance'
        f.name == df.fileName
        'text/html' == df.mimeType
        s.length() == df.fileSize

        cleanup:
        f.delete()
    }

    def 'Create an instance from an existing GIF file'() {
        given: 'a GIF image'
        def img = createGifFileContent()

        and: 'a File object'
        def f = File.createTempFile('springcrm-unit-test', '.gif')
        f.bytes = img

        when: 'I create a DataFile instance from that file'
        def df = new DataFile(f)

        then: 'I get a valid DataFile instance'
        f.name == df.fileName
        'image/gif' == df.mimeType
        img.length == df.fileSize

        cleanup:
        f.delete()
    }

    def 'Create an instance from an existing GIF file with diffent file extension'() {
        given: 'a GIF image'
        def img = createGifFileContent()

        and: 'a File object'
        def f = File.createTempFile('springcrm-unit-test', '.dat')
        f.bytes = img

        when: 'I create a DataFile instance from that file'
        def df = new DataFile(f)

        then: 'I get a valid DataFile instance'
        f.name == df.fileName
        'image/gif' == df.mimeType
        img.length == df.fileSize

        cleanup:
        f.delete()
    }

    def 'Create an instance from a non-existing MultipartFile object'() {
        given: 'an empty MultipartFile object'
        def mp = new GrailsMockMultipartFile(
            'b037c7320af7303', 'test.html', 'text/html', [] as byte[]
        )

        when: 'I create a DataFile instance from that file'
        def df = new DataFile(mp)

        then: 'I get a IllegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'Create an instance from an existing HTML MultipartFile'() {
        given: 'an HTML string'
        String s = createHtmlFileContent()

        and: 'a MultipartFile object'
        def mp = new GrailsMockMultipartFile(
            'b037c7320af7303', 'test.html', 'text/html', s.bytes
        )

        when: 'I create a DataFile instance from that MultipartFile instance'
        def df = new DataFile(mp)

        then: 'I get a valid DataFile instance'
        'test.html' == df.fileName
        'text/html' == df.mimeType
        s.length() == df.fileSize
    }

    def 'Create an instance from an existing GIF MultipartFile'() {
        given: 'an GIF image'
        def img = createGifFileContent()

        and: 'a MultipartFile object'
        def mp = new GrailsMockMultipartFile(
            'b037c7320af7303', 'image.gif', 'text/html', img
        )

        when: 'I create a DataFile instance from that MultipartFile instance'
        def df = new DataFile(mp)

        then: 'I get a valid DataFile instance'
        'image.gif' == df.fileName
        'image/gif' == df.mimeType
        img.length == df.fileSize
    }

    def 'Create the storage name with given ID'() {
        given: 'a DataFile instance'
        def df = new DataFile(fileName: 'foo.dat', fileSize: 1024)

        when: 'I set the ID to the discrete values'
        df.id = id

        then: 'I get a valid storage name'
        s == df.storageName

        where:
                     id | s
                      1 | '0000000000000001'
                      2 | '0000000000000002'
                     10 | '000000000000000A'
                     15 | '000000000000000F'
                     16 | '0000000000000010'
                    256 | '0000000000000100'
        611_864_233_482 | '0000008E75F3460A'
    }

    def 'Create the storage name without ID'() {
        given: 'a DataFile instance'
        def df = new DataFile(fileName: 'foo.dat', fileSize: 1024)

        when: 'I set the ID to zero and obtain the storage name'
        df.id = 0
        df.storageName

        then: 'I get an IllegalStateException'
        thrown(IllegalStateException)

        when: 'I unset the ID and obtain the storage name'
        df.id = null
        df.storageName

        then: 'I get an IllegalStateException'
        thrown(IllegalStateException)
    }

    def 'Check for equality'() {
        given: 'two DataFile objects with different content'
        def df1 = new DataFile(fileName: 'foo.dat', fileSize: 1024)
        def df2 = new DataFile(fileName: 'bar.txt', fileSize: 4759)

        and: 'the same IDs'
        df1.id = 30324
        df2.id = 30324

        expect: 'both these DataFile objects are equal'
        df2 == df1
        df1 == df2
    }

    def 'Check for inequality'() {
        given: 'two DataFile objects with the same content'
        def df1 = new DataFile(fileName: 'foo.dat', fileSize: 1024)
        def df2 = new DataFile(fileName: 'foo.dat', fileSize: 1024)

        and: 'both the IDs set to different values'
        df1.id = 30324
        df2.id = 30325

        when: 'I compare both these DataFile objects'
        boolean b1 = (df2 != df1)
        boolean b2 = (df1 != df2)

        then: 'they are not equal'
        b1
        b2

        when: 'I compare to null'
        df2 = null

        then: 'they are not equal'
        df2 != df1
        df1 != df2

        when: 'I compare to another type'
        String s = 'foo'

        then: 'they are not equal'
        df1 != s
    }

    def 'Populate with a non-existing File object'() {
        given: 'a File object'
        def f = new File('foo', 'bar')

        and: 'an empty DataFile instance'
        def df = new DataFile()

        when: 'I populate the DataFile instance with that file'
        df.populate f

        then: 'I get a IllegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'Populate with an existing File object'() {
        given: 'a GIF image'
        def img = createGifFileContent()

        and: 'a File object'
        def f = File.createTempFile('springcrm-unit-test', '.gif')
        f.bytes = img

        and: 'an empty DataFile instance'
        def df = new DataFile()

        when: 'I populate the DataFile instance with that file'
        df.populate f

        then: 'I get a valid DataFile instance'
        f.name == df.fileName
        'image/gif' == df.mimeType
        img.length == df.fileSize

        cleanup:
        f.delete()
    }

    def 'Populate with an non-existing MultipartFile object'() {
        given: 'an empty MultipartFile object'
        def mp = new GrailsMockMultipartFile(
            'b037c7320af7303', 'test.html', 'text/html', [] as byte[]
        )

        and: 'an empty DataFile instance'
        def df = new DataFile()

        when: 'I populate the DataFile instance with that MultipartFile object'
        df.populate mp

        then: 'I get a IllegalArgumentException'
        thrown(IllegalArgumentException)
    }

    def 'Populate with an existing MultipartFile object'() {
        given: 'a GIF image'
        def img = createGifFileContent()

        and: 'a MultipartFile object'
        def mp = new GrailsMockMultipartFile(
            'b037c7320af7303', 'image.gif', 'text/html', img
        )

        and: 'an empty DataFile instance'
        def df = new DataFile()

        when: 'I populate the DataFile instance with that MultipartFile object'
        df.populate mp

        then: 'I get a valid DataFile instance'
        'image.gif' == df.fileName
        'image/gif' == df.mimeType
        img.length == df.fileSize
    }

    def 'Obtain string representation'() {
        given: 'a DataFile instance'
        def df = new DataFile(fileSize: 1024)

        when: 'I set the file name'
        df.fileName = f

        then: 'I get a valid string representation'
        s == df.toString()

        where:
        f               | s
        null            | ''
        ''              | ''
        '  '            | '  '
        'foo'           | 'foo'
        'bar.html'      | 'bar.html'
    }

    def 'FileName constraints'() {
        setup:
        mockForConstraintsTests(DataFile)

        when:
        def df = new DataFile(fileName: f, fileSize: 1024)
        df.validate()

        then:
        !valid == df.hasErrors()

        where:
        f               | valid
        null            | false
        ''              | false
        ' '             | false
        '      '        | false
        '  \t \n '      | false
        'foo'           | true
        'any name'      | true
        'foo.html'      | true
    }

    def 'FileSize constraints'() {
        setup:
        mockForConstraintsTests(DataFile)

        when:
        def df = new DataFile(fileName: 'foo.odt', fileSize: fs)
        df.validate()

        then:
        !valid == df.hasErrors()

        where:
                 fs | valid
                  0 | true
                  1 | true
                 20 | true
              3_373 | true
            430_404 | true
        104_857_600 | true
        104_857_601 | false
                 -1 | false
               -100 | false
    }


    //-- Non-public methods ---------------------

    protected byte [] createGifFileContent() {
        [
            0x47, 0x49, 0x46, 0x38, 0x37, 0x61, 0x01, 0x00,
            0x01, 0x00, 0x80, 0x00, 0x00, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0x2c, 0x00, 0x00, 0x00, 0x00,
            0x01, 0x00, 0x01, 0x00, 0x00, 0x02, 0x02, 0x44,
            0x01, 0x00, 0x3b
        ] as byte[]
    }

    protected String createHtmlFileContent() {
        '<html><head><title>Test</title></head><body>Text</body></html>'
    }
}
