/*
 * FopServiceSpec.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.testing.services.ServiceUnitTest
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.sax.SAXSource
import javax.xml.transform.stream.StreamSource
import org.amcworld.springcrm.xml.TemplateURIResolver
import org.apache.avalon.framework.configuration.Configuration
import org.apache.fop.apps.FOUserAgent
import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.springframework.context.MessageSource
import org.xml.sax.XMLReader
import org.xml.sax.helpers.DefaultHandler
import spock.lang.Specification


class FopServiceSpec extends Specification
    implements ServiceUnitTest<FopService>
{

    //-- Feature methods ------------------------

    def 'Get non-existing user template directory'() {
        given: 'no print template configuration'
        config.springcrm.dir.print = null

        expect:
        null == service.userTemplateDir
    }

    def 'Get existing user template directory'() {
        given: 'a application setting'
        config.springcrm.dir.print = '/foo/bar'

        when: 'I obtain the user template directory'
        File f = service.userTemplateDir

        then: 'I get a valid path to the user template directory'
        '/foo/bar' == f.path
    }

    def 'Get template paths with non-existing user template directory'() {
        given: 'an application setting to an non-existing path'
        config.springcrm.dir.print = '/foo/bar'

        when: 'I obtain the template paths'
        Map<String, String> res = service.templatePaths

        then: 'I get a valid map of paths'
        1 == res.size()
        'classpath:public/print/default' == res['default']
    }

    def 'Get template paths with existing user template directory'() {
        given: 'an application setting to an existing path'
        File tempDir = File.createTempDir('springcrm-test-', '')
        File userDir1 = new File(tempDir, 'bar')
        userDir1.mkdir()
        File userDir2 = new File(tempDir, 'whee')
        userDir2.mkdir()
        config.springcrm.dir.print = tempDir.absolutePath

        when: 'I obtain the template paths'
        Map<String, String> res = service.templatePaths

        then: 'I get a valid map of paths'
        3 == res.size()
        'classpath:public/print/default' == res['default']
        'file://' + userDir1.absolutePath == res['bar']
        'file://' + userDir2.absolutePath == res['whee']

        cleanup:
        userDir1.delete()
        userDir2.delete()
        tempDir.delete()
    }

    def 'Get template paths with overwritten system directory'() {
        given: 'an application setting to an existing path'
        File tempDir = File.createTempDir('springcrm-test-', '')
        File userDir1 = new File(tempDir, 'bar')
        userDir1.mkdir()
        File userDir2 = new File(tempDir, 'default')
        userDir2.mkdir()
        config.springcrm.dir.print = tempDir.absolutePath

        when: 'I obtain the template paths'
        Map<String, String> res = service.templatePaths

        then: 'I get a valid map of paths'
        2 == res.size()
        'file://' + userDir1.absolutePath == res['bar']
        'file://' + userDir2.absolutePath == res['default']

        cleanup:
        userDir1.delete()
        userDir2.delete()
        tempDir.delete()
    }

    def 'Get template names with non-existing user template directory'() {
        given: 'an application setting to an non-existing path'
        config.springcrm.dir.print = '/foo/bar'

        and:
        MessageSource messageSource = Mock()
        service.messageSource = messageSource

        when: 'I obtain the template names'
        Map<String, String> res = service.templateNames

        then: 'I get a valid map of names'
        1 == res.size()
        'Default' == res['default']

        and: 'the template name had to be localized'
        1 * messageSource.getMessage('print.template.default', null, 'default', _) >> 'Default'
    }

    def 'Get template names with existing user template directory'() {
        given: 'some temporary directories'
        File tempDir = File.createTempDir('springcrm-test-', '')
        File userDir1 = new File(tempDir, 'bar')
        userDir1.mkdir()
        File userDir2 = new File(tempDir, 'whee')
        userDir2.mkdir()
        File userDir3 = new File(tempDir, 'foo')
        userDir3.mkdir()
        config.springcrm.dir.print = tempDir.absolutePath

        and: 'some meta files'
        File metaFile2 = new File(userDir2, 'meta.properties')
        metaFile2.text = 'name = Test template'
        File metaFile3 = new File(userDir3, 'meta.properties')
        metaFile3.text = 'foo = bar'

        and: 'a mocked message source'
        MessageSource messageSource = Mock()
        1 * messageSource.getMessage('print.template.default', null, 'default', _) >> 'Default'
        1 * messageSource.getMessage('print.template.bar', null, 'bar', _) >> 'Drafts'
        0 * messageSource.getMessage('print.template.whee', null, 'whee', _) >> 'Not used'
        1 * messageSource.getMessage('print.template.foo', null, 'foo', _) >> 'Branch A'
        service.messageSource = messageSource

        when: 'I obtain the template names'
        Map<String, String> res = service.templateNames

        then: 'I get a valid map of names'
        4 == res.size()
        'Default' == res['default']
        'Drafts' == res['bar']
        'Test template' == res['whee']
        'Branch A' == res['foo']

        cleanup:
        userDir1.delete()
        metaFile2.delete()
        userDir2.delete()
        metaFile3.delete()
        userDir3.delete()
        tempDir.delete()
    }

    def 'Get template names with overwritten system directory'() {
        given: 'some temporary directories'
        File tempDir = File.createTempDir('springcrm-test-', '')
        File userDir1 = new File(tempDir, 'bar')
        userDir1.mkdir()
        File userDir2 = new File(tempDir, 'default')
        userDir2.mkdir()
        config.springcrm.dir.print = tempDir.absolutePath

        and: 'some meta files'
        File metaFile2 = new File(userDir2, 'meta.properties')
        metaFile2.text = 'name = Test template'

        and: 'a mocked message source'
        MessageSource messageSource = Mock()
        0 * messageSource.getMessage('print.template.default', null, 'default', _) >> 'Default'
        1 * messageSource.getMessage('print.template.bar', null, 'bar', _) >> 'Drafts'
        service.messageSource = messageSource

        when: 'I obtain the template names'
        Map<String, String> res = service.templateNames

        then: 'I get a valid map of names'
        2 == res.size()
        'Test template' == res['default']
        'Drafts' == res['bar']

        cleanup:
        userDir1.delete()
        metaFile2.delete()
        userDir2.delete()
        tempDir.delete()
    }

    def 'Generate PDF of an invoice'() {
        given: 'some necessary values'
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        Date now = new Date()
        String xml = '<?xml test data?>'

        and: 'a template directory'
        File tempDir = File.createTempDir('springcrm-test-', '')
        File tplDir = new File(tempDir, 'bar')
        tplDir.mkdir()
        File configFile = new File(tplDir, 'fop-conf.xml')
        configFile.text = '''<?xml version="1.0" encoding="UTF-8"?>

<fop version="1.0">
  <default-page-settings width="210mm" height="297mm"/>
  <renderers>
    <renderer mime="application/pdf">
      <fonts>
        <auto-detect/>
      </fonts>
    </renderer>
  </renderers>
</fop>'''
        File xslFile = new File(tplDir, 'invoice-fo.xsl')
        xslFile.text = '<?xslt test file?>'
        config.springcrm.dir.print = tempDir.absolutePath

        and: 'a mocked XML reader'
        XMLReader xmlReader = Mock()
        service.xmlReader = xmlReader

        and: 'a mocked content handler'
        DefaultHandler handler = Mock()

        and: 'a mocked transformer'
        Transformer transformer = Mock()
        1 * transformer.setParameter('versionParam', '2.0')
        1 * transformer.transform(_, _) >> { SAXSource source, SAXResult result ->
            assert xmlReader.is(source.XMLReader)
            assert xml == source.inputSource.characterStream.text
            assert handler == result.handler
        }

        and: 'a mocked transformer factory'
        TransformerFactory transformerFactory = Mock()
        1 * transformerFactory.setURIResolver(_) >> { TemplateURIResolver resolver ->
            assert 'file://' + tplDir.absolutePath == resolver.userTemplatePath
        }
        1 * transformerFactory.newTransformer(_) >> { StreamSource source ->
            assert '<?xslt test file?>' == source.inputStream.text

            transformer
        }
        service.transformerFactory = transformerFactory

        and: 'a mocked FOP instance'
        Fop fop = Mock()
        1 * fop.defaultHandler >> handler

        and: 'a mocked FOP factory'
        FopFactory fopFactory = Mock()
        1 * fopFactory.setUserConfig(_) >> { Configuration config ->
            assert 'fop' == config.name
            assert '1.0' == config.getAttribute('version')
            Configuration c = config.getChild('default-page-settings', false)
            assert '210mm' == c.getAttribute('width')
            assert '297mm' == c.getAttribute('height')
            c = config.getChild('renderers', false).getChild('renderer', false)
            assert 'application/pdf' == c.getAttribute('mime')
            assert null != c.getChild('fonts', false).getChild('auto-detect', false)
        }
        1 * fopFactory.newFop(MimeConstants.MIME_PDF, _, _) >> { String mimeType, FOUserAgent ua, OutputStream out ->
            assert "springcrm v${grailsApplication.metadata.getApplicationVersion()}" == ua.producer
            assert now <= ua.creationDate
            assert baos.is(out)

            out.write 'PDF1.1/Test result'.bytes

            fop
        }
        1 * fopFactory.getBaseURL() >> ''
        1 * fopFactory.getTargetResolution() >> 72.0f
        1 * fopFactory.isAccessibilityEnabled() >> false
        1 * fopFactory.newFOUserAgent() >> new FOUserAgent(fopFactory)
        service.fopFactory = fopFactory

        when: 'I generate the PDF document'
        service.generatePdf(
            new StringReader(xml), 'invoice', 'bar', baos
        )

        then: 'the output stream contains the correct result'
        'PDF1.1/Test result' == baos.toString()

        cleanup:
        tempDir.deleteDir()
    }

    def 'Output PDF of an invoice'() {
        given: 'some necessary values'
        String xml = '<?xml test data?>'
        byte [] result = 'PDF1.1/Test result'.bytes

        and: 'a template directory'
        File tempDir = File.createTempDir('springcrm-test-', '')
        File tplDir = new File(tempDir, 'bar')
        tplDir.mkdir()
        File configFile = new File(tplDir, 'fop-conf.xml')
        configFile.text = '''<?xml version="1.0" encoding="UTF-8"?>

<fop version="1.0">
  <default-page-settings width="210mm" height="297mm"/>
  <renderers>
    <renderer mime="application/pdf">
      <fonts>
        <auto-detect/>
      </fonts>
    </renderer>
  </renderers>
</fop>'''
        File xslFile = new File(tplDir, 'invoice-fo.xsl')
        xslFile.text = '<?xslt test file?>'
        config.springcrm.dir.print = tempDir.absolutePath

        and: 'a mocked XML reader'
        XMLReader xmlReader = Mock()
        service.xmlReader = xmlReader

        and: 'a mocked content handler'
        DefaultHandler handler = Mock()

        and: 'a mocked transformer'
        Transformer transformer = Mock()
        1 * transformer.setParameter('versionParam', '2.0')
        1 * transformer.transform(_, _)

        and: 'a mocked transformer factory'
        TransformerFactory transformerFactory = Mock()
        1 * transformerFactory.setURIResolver(_)
        1 * transformerFactory.newTransformer(_) >> transformer
        service.transformerFactory = transformerFactory

        and: 'a mocked FOP instance'
        Fop fop = Mock()
        1 * fop.defaultHandler >> handler

        and: 'a mocked FOP factory'
        FopFactory fopFactory = Mock()
        1 * fopFactory.setUserConfig(_)
        1 * fopFactory.newFop(MimeConstants.MIME_PDF, _, _) >> { String mimeType, FOUserAgent ua, OutputStream out ->
            out.write result

            fop
        }
        1 * fopFactory.getBaseURL() >> ''
        1 * fopFactory.getTargetResolution() >> 72.0f
        1 * fopFactory.isAccessibilityEnabled() >> false
        1 * fopFactory.newFOUserAgent() >> new FOUserAgent(fopFactory)
        service.fopFactory = fopFactory

        when: 'I output the PDF document'
        byte [] pdf = service.outputPdf(xml, 'invoice', 'bar')

        then: 'the output stream contains the correct result'
        'PDF1.1/Test result' == pdf.toString()

        cleanup:
        tempDir.deleteDir()
    }
}
