/*
 * CalendarEventViewPage.groovy
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


package org.amcworld.springcrm.page

import org.amcworld.springcrm.module.CalendarEventModule
import org.amcworld.springcrm.module.DialogModule


class CalendarEventViewPage extends DefaultContentPage {

    //-- Class variables ------------------------

    static at = { title == 'Kalendereinträge' }
    static content = {
		calHeader { $('table.fc-header') }
		currentDate { calHeader.find('.fc-header-title > h2').text() }
		events { moduleList CalendarEventModule, $('.fc-event') }
		gotoDateBtn { headerLeft.find('.fc-button-goto') }
		gotoDateDialog { module DialogModule, $('#goto-date-dialog') }
		headerLeft { calHeader.find('td.fc-header-left') }
		headerRight { calHeader.find('td.fc-header-right') }
		nextBtn { headerLeft.find('.fc-button-next') }
		prevBtn { headerLeft.find('.fc-button-prev') }
		todayBtn { headerLeft.find('.fc-button-today') }
		viewSelector { headerRight.find('.fc-button') }
    }


	//-- Public methods -------------------------

	/**
	 * Checks the labels of the calendar view selectors.
	 */
	void checkViewSelectors() {
		assert 'Tag' == viewSelector[0].text()
		assert 'Woche' == viewSelector[1].text()
		assert 'Monat' == viewSelector[2].text()
		assert 'Liste' == viewSelector[3].text()
	}

    /**
     * Opens the dialog to go to the given date.
     *
     * @param date	the date to go to in the format of the currently active
     * 				locale
     */
	void gotoDate(String date) {
		gotoDateBtn.click()

		waitFor { gotoDateDialog.displayed }
		gotoDateDialog.find('div.field > input').value date
		gotoDateDialog.buttons[0].click()

		waitFor { !gotoDateDialog.displayed }
	}
}
