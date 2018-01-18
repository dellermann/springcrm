/*
 * Quote.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import com.mongodb.client.model.Filters


/**
 * The class {@code Quote} represents a quote.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class Quote extends InvoicingTransaction {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'subject', 'billingAddr.street', 'billingAddr.poBox',
        'billingAddr.postalCode', 'billingAddr.location', 'billingAddr.state',
        'billingAddr.country', 'shippingAddr.street', 'shippingAddr.poBox',
        'shippingAddr.postalCode', 'shippingAddr.location',
        'shippingAddr.state', 'shippingAddr.country', 'headerText',
        'items.*name', 'items.*description', 'footerText', 'notes'
    ].asImmutable()
    public static final String TYPE = 'Q'


    //-- Class fields ---------------------------

    static constraints = {
        validUntil nullable: true
    }
    static hasMany = [salesOrders: SalesOrder, invoices: Invoice]
    static mapping = {
        stage column: 'quote_stage_id'
    }
    static nextNumberFilters = [Filters.eq('type', TYPE)]


    //-- Fields ---------------------------------

    /**
     * The stage of this quote.
     */
    QuoteStage stage

    /**
     * The date until this quote is valid.
     */
    Date validUntil


    //-- Constructors ---------------------------

    /**
     * Creates an empty quote.
     */
    Quote() {
        super()
        type = TYPE
    }

    /**
     * Create a quote using the data of the given one (copy constructor).
     *
     * @param q the given quote
     */
    Quote(Quote q) {
        super(q)
        type = q.type
    }
}
