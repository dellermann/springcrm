/*
 * SpringCrmGrailsApp.groovy
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

import grails.boot.GrailsApp
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import org.springframework.core.env.Environment


/**
 * The class {@SpringCrmGrailsApp} extends the base {@code GrailsApp} class and
 * outputs a banner at startup.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   2.1
 */
@CompileStatic
@InheritConstructors
class SpringCrmGrailsApp extends GrailsApp {

    //-- Constants ------------------------------

    /**
     * The banner graphic.  See
     * <a href="http://patorjk.com/software/taag/#p=display&f=Standard&t=SpringCRM" target="_blank">AsciiArt</a>
     * for more information.*/
    private static String BANNER = '''
  ____             _              ____ ____  __  __
 / ___| _ __  _ __(_)_ __   __ _ / ___|  _ \\|  \\/  |
 \\___ \\| '_ \\| '__| | '_ \\ / _` | |   | |_) | |\\/| |
  ___) | |_) | |  | | | | | (_| | |___|  _ <| |  | |
 |____/| .__/|_|  |_|_| |_|\\__, |\\____|_| \\_\\_|  |_|
       |_|                 |___/'''


    //-- Non-public methods ---------------------

    @Override
    protected void printBanner(Environment environment) {
        PrintStream out = System.out
        out.println BANNER
        out.println()

        row 'App version', metaInfo.getApplicationVersion(), out
        row 'Build number', metaInfo['info.app.buildNumber'], out
        row 'Environment', metaInfo.getEnvironment(), out
        row 'Grails version', metaInfo.getGrailsVersion(), out
        row 'Groovy version', GroovySystem.version, out
        row 'JVM version', System.getProperty('java.version'), out
        row 'Servlet version', metaInfo.getServletVersion(), out

        out.println()
        out.println()
    }

    /**
     * Prints a row containing a label and a value.
     *
     * @param label the given label
     * @param value the given value
     * @param out   the output stream to write to
     */
    private static void row(String label, Object value, PrintStream out) {
        out.print ':: '
        out.print label.padRight(16)
        out.print ' :: '
        out.println value
    }
}
