/*
 * PurchaseInvoiceNumberDiffSet.groovy
 *
 * Copyright (c) 2011-2022, Daniel Ellermann
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


package org.amcworld.springcrm.install.diffset

import org.amcworld.springcrm.PurchaseInvoice


class PurchaseInvoiceNumberDiffSet implements StartupDiffSet {

    //-- Public methods -------------------------

    @Override
    void execute() {
        int seqNumber = 0
        int lastYear = 0
        int number = PurchaseInvoice.count()
        for (int i = 0; i < number; i += 200) {
            PurchaseInvoice.list(offset: i, max: 200, sort: 'docDate')
                .each {
                    int year = it.docDate.getAt(Calendar.YEAR)
                    if (year != lastYear) {
                        lastYear = year
                        seqNumber = (year % 1000) * 1000
                    }
                    it.number = seqNumber++
                    it.save flush: true, failOnError: true
                }
        }
    }
}
