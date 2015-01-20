/*
 * Quote.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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
 * The class {@code Quote} represents a quote.
 *
 * @author  Daniel Ellermann
 * @version 1.5
 */
class Quote extends InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
        stage()
        validUntil nullable: true
    }
    static hasMany = [salesOrders: SalesOrder, invoices: Invoice]
    static mapping = {
        stage column: 'quote_stage_id'
    }
    static searchable = true


    //-- Instance variables ---------------------

    QuoteStage stage
    Date validUntil


    //-- Constructors ---------------------------

    Quote() {
        super()
        type = 'Q'
    }

    Quote(Quote q) {
        super(q)
        type = 'Q'
    }
}

