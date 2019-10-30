/*
 * TicketSpec.groovy
 *
 * Copyright (c) 2011-2019, Daniel Ellermann
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

import spock.lang.Specification


/**
 *
 *
 * @author  Daniel Ellermann
 * @version 0.2
 * @since   0.2
 */
class TicketSpec extends Specification {

    //-- Feature methods ------------------------

    void 'can generate code'() {
        given: 'an organization'
        def org = new Organization()
        org.id = 48

        and: 'a helpdesk'
        def helpdesk = new Helpdesk(organization: org)
        helpdesk.id = 24

        and: 'a ticket'
        def ticket = new Ticket(helpdesk: helpdesk, subject: 'Test ticket')

        and: 'a set to examine code duplicates'
        HashSet<String> set = new HashSet<>(100)

        when: 'the codes are generated'
        for (int i = 0; i < 100; i++) set.add ticket.generateCode()

        then: 'the codes are valid'
        100 == set.size()
        !(null in set)

        and: 'all codes are 32 character long'
        for (String code in set) {
            32 == code.length()
        }
    }
}
