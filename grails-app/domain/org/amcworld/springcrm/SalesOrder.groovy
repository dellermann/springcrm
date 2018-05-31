/*
 * SalesOrder.groovy
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
 * The class {@code SalesOrder} represents a sales order.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
class SalesOrder extends InvoicingTransaction {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'subject', 'billingAddr.street', 'billingAddr.poBox',
        'billingAddr.postalCode', 'billingAddr.location', 'billingAddr.state',
        'billingAddr.country', 'shippingAddr.street', 'shippingAddr.poBox',
        'shippingAddr.postalCode', 'shippingAddr.location',
        'shippingAddr.state', 'shippingAddr.country', 'headerText',
        'items.*name', 'items.*description', 'footerText', 'notes'
    ].asImmutable()
    public static final String TYPE = 'O'


    //-- Class fields ---------------------------

    static constraints = {
        orderDate nullable: true
        orderMethod nullable: true
        orderDocument nullable: true
        signature nullable: true
        dueDate nullable: true
        deliveryDate nullable: true
        quote nullable: true
    }
    static belongsTo = [quote: Quote]
    static hasMany = [invoices: Invoice]
    static mapping = {
        stage column: 'so_stage_id'
        signature type: 'text'
    }
    @SuppressWarnings("GroovyUnusedDeclaration")
    static nextNumberFilters = [Filters.eq('type', TYPE)]


    //-- Fields ---------------------------------

    /**
     * The actual date of delivery.
     */
    Date deliveryDate

    /**
     * The date when the delivery should take place.
     */
    Date dueDate

    /**
     * The date when the client placed the order.
     *
     * @since 2.2
     */
    Date orderDate

    /**
     * A document containing the order of the client.
     *
     * @since 2.2
     */
    DataFile orderDocument

    /**
     * The way the order has been placed.
     *
     * @since 2.2
     */
    OrderMethod orderMethod

    /**
     * The quote associated to the sales order.
     */
    Quote quote

    /**
     * The signature of the client in SVG format.
     *
     * @since 2.2
     */
    String signature

    /**
     * The stage of the sales order.
     */
    SalesOrderStage stage


    //-- Constructors ---------------------------

    /**
     * Creates an empty sales order.
     */
    SalesOrder() {
        super()
        type = TYPE
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
        type = TYPE
        quote = q
    }


    //-- Properties -----------------------------

    /*
     * XXX These properties need to be re-implemented from the super class
     * because otherwise the compile-time implementation of SalesOrderService
     * does not work.  It seems to be a bug in Grails for MongoDB.
     */

    @Override
    Organization getOrganization() {
        super.getOrganization()
    }

    @Override
    void setOrganization(Organization organization) {
        super.setOrganization organization
    }

    @Override
    Person getPerson() {
        super.getPerson()
    }

    @Override
    void setPerson(Person person) {
        super.setPerson person
    }

    @Override
    String getSubject() {
        super.getSubject()
    }

    @Override
    void setSubject(String subject) {
        super.setSubject subject
    }
}
