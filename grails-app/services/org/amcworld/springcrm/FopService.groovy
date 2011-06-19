package org.amcworld.springcrm

import java.io.InputStream
import java.io.OutputStream
import java.io.Reader

import javax.servlet.ServletContext
import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.URIResolver
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource

import org.apache.avalon.framework.configuration.Configuration
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder
import org.apache.fop.apps.Fop
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.apache.fop.servlet.ServletContextURIResolver
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class FopService {

    static transactional = false
	
	ServletContext servletContext = SCH.servletContext
	protected FopFactory fopFactory
	protected URIResolver uriResolver
	
    void generatePdf(Reader data, String xslt, OutputStream out) {
		initialize()
		
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, out)
		
		TransformerFactory tfFactory = TransformerFactory.newInstance()
		tfFactory.URIResolver = uriResolver
		
		Source xsltSrc = uriResolver.resolve("servlet-context:${xslt}", null)
		Transformer transformer = tfFactory.newTransformer(xsltSrc)
		transformer.URIResolver = uriResolver
		transformer.setParameter('versionParam', '2.0')
		
		Source src = new StreamSource(data)
		Result res = new SAXResult(fop.getDefaultHandler())
		transformer.transform src, res
    }
	
	protected void initialize() {
		if (uriResolver == null) {
			uriResolver = new ServletContextURIResolver(servletContext)
		}
		if (fopFactory == null) {
			fopFactory = FopFactory.newInstance()
			fopFactory.URIResolver = uriResolver
			fopFactory.userConfig = fopConfiguration
		}
	}
	
	protected Configuration getFopConfiguration() {
		DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder()
		return builder.buildFromFile(
			servletContext.getRealPath('/WEB-INF/data/fo/fop-conf.xml')
		)
	}
}
