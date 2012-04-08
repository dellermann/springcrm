/*
 * Dunning.groovy
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
 * The class {@code Dunning} represents a dunning which belongs to an invoice.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class Dunning extends InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
		level()
		stage()
		dueDatePayment()
		paymentDate(nullable: true)
		paymentAmount(nullable: true, min: 0.0, scale: 2, widget: 'currency')
		paymentMethod(nullable: true)
		invoice()
    }
	static belongsTo = [invoice: Invoice]
	static mapping = {
		stage column: 'dunning_stage_id'
	}
	static searchable = true
    static transients = ['paymentStateColor']


    //-- Instance variables ---------------------

	DunningLevel level
	DunningStage stage
	Date dueDatePayment
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod


    //-- Instance initializer -------------------

	{
		type = 'D'
	}


    //-- Constructors ---------------------------

	Dunning() {}

	Dunning(Invoice i) {
		super(i)
		invoice = i
        subject = ''
        headerText = ''
        footerText = ''
	}

	Dunning(Dunning d) {
		super(d)
		level = d.level
		stage = d.stage
		dueDatePayment = d.dueDatePayment
		paymentDate = d.paymentDate
		paymentAmount = d.paymentAmount
		paymentMethod = d.paymentMethod
		invoice = d.invoice
	}


    //-- Public methods -------------------------

    /**
     * Gets the name of a color indicating the payment state of this dunning.
     *
     * @return  the indicator color
     */
    String getPaymentStateColor() {
        String color = 'white'
        switch (stage?.id) {
        case 2206:                      // cancelled
            color = 'green'
            break
        case 2205:                      // booked out
            color = 'black'
            break
        case 2204:                      // cashing
            color = 'blue'
            break
        case 2203:                      // paid
            color = 'green'
            if ((paymentAmount ?: 0) - (total ?: 0) >= 0) {
                break
            }
            // else fall through
        case 2202:                      // delivered
            Date d = new Date()
            if (d >= dueDatePayment - 3) {
                if (d <= dueDatePayment) {
                    color = 'yellow'
                } else if (d <= dueDatePayment + 3) {
                    color = 'orange'
                } else {
                    color = 'red'
                }
            }
            break
        }
        return color
    }
}
