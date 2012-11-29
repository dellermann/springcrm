/*
 * NotificationController.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


/**
 * The class {@code NotificationController} contains actions which notify
 * the developer team about errors.
 *
 * @author	Daniel Ellermann
 * @version 1.2
 */
class NotificationController {

    //-- Public methods -------------------------

	/**
	 * Sends an error report to AMC World Technologies.
	 */
    def reportError() {
		sendMail {
			multipart true
			to 'error@amc-world.de'
			from 'noreply@amc-world.de'
			subject 'SpringCRM Fehlerbericht'
			body '''Liebes SpringCRM-Entwicklerteam,

bitte überprüft den folgenden Fehlerbericht.

Mit freundlichen Grüßen

AMC World Technologies Systemdienst'''
			attachBytes 'error-report.xml', 'text/xml', params.xml.getBytes('UTF-8')
		}
	}
}
