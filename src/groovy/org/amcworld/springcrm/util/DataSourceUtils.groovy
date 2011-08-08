package org.amcworld.springcrm.util

import static org.codehaus.groovy.grails.commons.ApplicationAttributes.APPLICATION_CONTEXT

import javax.servlet.ServletContext
import org.apache.commons.logging.LogFactory

class DataSourceUtils {

	/*
	 * Fix for the database killing idle connections
	 * http://sacharya.com/grails-dbcp-stale-connections/
	 */


	//-- Constants ------------------------------

    private static final MS = 1000 * 15 * 60


	//-- Class variables ------------------------

	private static final log = LogFactory.getLog(this)


	//-- Constructors ---------------------------

	private DataSourceUtils() {}


	//-- Public methods -------------------------

    public static void tune(ServletContext servletContext) {
 
        def ctx = servletContext.getAttribute(APPLICATION_CONTEXT)
        ctx.dataSource.targetDataSource.with {d ->
            d.setMinEvictableIdleTimeMillis(MS)
            d.setTimeBetweenEvictionRunsMillis(MS)
            d.setNumTestsPerEvictionRun(3)
            d.setTestOnBorrow(true)
            d.setTestWhileIdle(true)
            d.setTestOnReturn(true)
            d.setValidationQuery('select 1')
            d.setMinIdle(1)
            d.setMaxActive(16)
            d.setInitialSize(8)
        }
 
        if (log.infoEnabled) {
            log.info 'Configured Datasource properties:'
            ctx.dataSource.targetDataSource.properties
				.findAll {k, v -> !k.contains('password') }
				.each {p -> log.info " ${p}" }
        }
    }
}
