/*
 * Application.groovy
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


package springcrm

import static grails.util.Metadata.current as metaInfo

import grails.boot.config.GrailsAutoConfiguration
import groovy.transform.CompileStatic
import javax.naming.Context
import javax.naming.InitialContext
import javax.naming.NameNotFoundException
import javax.naming.NoInitialContextException
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.config.PropertiesFactoryBean
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.context.EnvironmentAware
import org.springframework.core.env.*
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource


/**
 * The class {@code Application} represents the default starter of the whole
 * application.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
class Application extends GrailsAutoConfiguration implements EnvironmentAware {

    //-- Class fields ---------------------------

    private static final Log log = LogFactory.getLog(this)


    //-- Public methods -------------------------

    static void main(String [] args) {
        LauncherFrame frame = new LauncherFrame()

        SpringCrmGrailsApp app = new SpringCrmGrailsApp(Application)
        app.headless = false
        app.addListeners frame
        app.run args
    }

    @Override
    void setEnvironment(Environment environment) {
        if (!(environment instanceof AbstractEnvironment)) {
            return
        }

        AbstractEnvironment env = (AbstractEnvironment) environment
        log.debug "Environment: ${env}"

        parseConfigFile(
            env, 'system.properties', getUserConfigFile('config.properties')
        )
        parseConfigFile(
            env, 'system.groovy', getUserConfigFile('config.groovy')
        )
        parseConfigFile(
            env, 'system.yaml', getUserConfigFile('config.yml')
        )

        String path = configSystemPropertyValue
        if (path) {
            parseConfigFile env, 'system.system', path
        }

        try {
            Context jndi =
                (Context) new InitialContext().lookup('java:comp/env')
            String springcrmConfig = (String) jndi.lookup('springcrmConfig')
            parseConfigFile env, 'instance', springcrmConfig
        } catch (NoInitialContextException ignore) {
            /* ignored */
        } catch (NameNotFoundException ignore) {
            /* ignored */
        } catch (Exception e) {
            log.error "Instance configuration lookup failed: ${e}"
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Gets the value of the system property where the configuration file
     * can be specified.  The system property is named
     * {@code springcrm.config.location}.
     *
     * @return  the value of the property; {@code null} if that property is not
     *          set
     */
    private static String getConfigSystemPropertyValue() {
        String propName = metaInfo.getApplicationName() + '.config.location'

        System.getProperty propName
    }

    /**
     * Gets the path to a configuration file in the application-specific folder
     * in the user's home directory.
     *
     * @param fileName  the given file name
     * @return          the absolute path
     */
    private static String getUserConfigFile(String fileName) {
        StringBuilder buf = new StringBuilder(System.getProperty('user.home'))
        buf << System.getProperty('file.separator')
        buf << '.' << metaInfo.getApplicationName()
        buf << System.getProperty('file.separator')
        buf << fileName

        buf.toString()
    }

    /**
     * Parses a configuration file with the given path and sets it as
     * properties with the highest priority in the given environment.  The
     * method supports the following file types:
     * <ul>
     *   <li>YAML (extensions ".yml" or ".yaml")</li>
     *   <li>Groovy (extension ".groovy")</li>
     *   <li>Java Properties (extension ".properties")</li>
     * </ul>
     *
     * @param environment   the given environment
     * @param name          the name of the generated property source
     * @param path          the path in the file system to the configuration
     *                      file
     */
    private static void parseConfigFile(AbstractEnvironment environment,
                                        String name, String path) {
        Resource res = new FileSystemResource(path)
        log.debug "Trying loading configuration file ${res.path}"
        if (res.exists()) {
            log.debug "Configuration file ${res.path} found -> load and parse it"
            PropertySource src = null
            if (path.endsWith('.yml') || path.endsWith('.yaml')) {
                YamlPropertiesFactoryBean ypfb = new YamlPropertiesFactoryBean()
                ypfb.resources = [res] as Resource[]
                ypfb.afterPropertiesSet()
                src = new PropertiesPropertySource(name, ypfb.getObject())
            } else if (path.endsWith('.properties')) {
                PropertiesFactoryBean pfb = new PropertiesFactoryBean()
                pfb.locations = [res] as Resource[]
                src = new PropertiesPropertySource(name, pfb.getObject())
            } else if (path.endsWith('.groovy')) {
                ConfigObject config = new ConfigSlurper().parse(res.URL)
                src = new MapPropertySource(name, config)
            }

            if (src != null) {
                environment.propertySources.addFirst src
            }
        }
    }
}
