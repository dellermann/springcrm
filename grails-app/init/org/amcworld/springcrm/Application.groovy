/*
 * Application.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration


/**
 * The class {@code Application} represents the default starter of the whole
 * application.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   2.1
 */
class Application extends GrailsAutoConfiguration {

    //-- Public methods -------------------------

    static void main(String [] args) {
        GrailsApp.run(Application, args)
    }
}
