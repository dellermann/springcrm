/*
 * SalesOrder.groovy
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


package org.amcworld.springcrm


/**
 * The class {@code SalesOrder} represents a sales order.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class SalesOrder extends InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
        stage()
        dueDate nullable: true
        deliveryDate nullable: true
        quote nullable: true
    }
    static belongsTo = [quote: Quote]
    static hasMany = [invoices: Invoice]
    static mapping = {
        stage column: 'so_stage_id'
    }


    //-- Instance variables ---------------------

    /**
     * The stage of this sales order.
     */
    SalesOrderStage stage

    /**
     * The date when the delivery should take place.
     */
    Date dueDate

    /**
     * The actual date of delivery.
     */
    Date deliveryDate


    //-- Constructors ---------------------------

    /**
     * Creates an empty sales order.
     */
    SalesOrder() {
        super()
        type = 'O'
    }

    /**
     * Creates a sales order using the data of the given one (copy
     * constructor).
     *
     * @param so    the given sales order
     */
    SalesOrder(SalesOrder so) {
        super(so)
        type = so.type
        quote = so.quote
    }

    /**
     * Creates a sales order using which is associated to the given quote.
     *
     * @param q the given quote
     */
    SalesOrder(Quote q) {
        super(q)
        type = 'O'
        quote = q
    }
}

