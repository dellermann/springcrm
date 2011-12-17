package org.amcworld.springcrm

class NotificationController {

	/**
	 * Sends an error report to AMC World Technologies.
	 */
    def reportError() {
		sendMail {
			multipart true
			to 'error@amc-world.ath.cx'
			from 'noreply@amc-world.ath.cx'
			subject 'SpringCRM Fehlerbericht'
			body '''Liebes AMC World Technologies Team,

bitte überprüft den folgenden Fehlerbericht.

Mit freundlichen Grüßen

AMC World Technologies Systemdienst'''
			attachBytes 'error-report.xml', 'text/xml', params.xml.getBytes('UTF-8')
		}
	}
}
