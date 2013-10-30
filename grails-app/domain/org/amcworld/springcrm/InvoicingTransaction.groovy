/*
 * InvoicingTransaction.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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
 * The class {@code InvoicingTransaction} acts as a base class of invoicing
 * transactions such as invoices, quotes etc.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 */
class InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
        number unique: 'type', widget: 'autonumber'
        type blank: false, maxSize: 1
        subject blank: false
        organization()
        person nullable: true
        docDate()
        carrier nullable: true
        shippingDate nullable: true
        headerText nullable: true, widget: 'textarea'
        items minSize: 1
        footerText nullable: true, widget: 'textarea'
        discountPercent scale: 2, min: 0.0d, widget: 'percent'
        discountAmount min: 0.0d, widget: 'currency'
        shippingCosts min: 0.0d, widget: 'currency'
        shippingTax scale: 1, min: 0.0d, widget: 'percent'
        adjustment widget: 'currency'
        total widget: 'currency'
        notes nullable: true, widget: 'textarea'
        dateCreated()
        lastUpdated()
    }
    static belongsTo = [organization: Organization, person: Person]
    static embedded = ['billingAddr', 'shippingAddr']
    static hasMany = [
        items: InvoicingItem,
        termsAndConditions: TermsAndConditions
    ]
    static mapping = {
        items cascade: 'all-delete-orphan'
        headerText type: 'text'
        footerText type: 'text'
        notes type: 'text'
        sort 'number'
        subject index: 'subject'
        order 'desc'
    }
    static searchable = true
    static transients = [
        'discountPercentAmount', 'fullName', 'fullNumber', 'subtotalGross',
        'subtotalNet', 'taxRateSums'
    ]


    //-- Instance variables ---------------------

    def seqNumberService

    int number
    String type
    String subject
    Date docDate = new Date()
    Carrier carrier
    Date shippingDate
    Address billingAddr
    Address shippingAddr
    String headerText
    List<InvoicingItem> items
    String footerText
    double discountPercent
    double discountAmount
    double shippingCosts
    double shippingTax = 19.0d
    double adjustment
    double total
    String notes
    Date dateCreated
    Date lastUpdated


    //-- Constructors ---------------------------

    InvoicingTransaction() {}

    InvoicingTransaction(InvoicingTransaction i) {
        this()
        subject = i.subject
        organization = i.organization
        person = i.person
        billingAddr = new Address(i.billingAddr)
        shippingAddr = new Address(i.shippingAddr)
        headerText = i.headerText
        items = new ArrayList(i.items.size())
        i.items.each { items << new InvoicingItem(it) }
        footerText = i.footerText
        discountPercent = i.discountPercent
        discountAmount = i.discountAmount
        shippingCosts = i.shippingCosts
        shippingTax = i.shippingTax
        adjustment = i.adjustment
        total = i.total
        notes = i.notes
        termsAndConditions = i.termsAndConditions
    }


    //-- Public methods -------------------------

    def beforeValidate() {
        total = computeTotal()
    }

    def beforeInsert() {
        if (number == 0) {
            number = seqNumberService.nextNumber(getClass())
        }
        total = computeTotal()
    }

    def beforeUpdate() {
        total = computeTotal()
    }

    /**
     * Computes the total (gross) value. It is computed from the subtotal gross
     * value minus all discounts plus the adjustment.
     *
     * @return  the total (gross) value
     */
    double computeTotal() {
        subtotalGross - discountPercentAmount - discountAmount + adjustment
    }

    /**
     * Copies the billing and shipping address from the given organization to
     * the corresponding addresses of this invoicing transaction.
     *
     * @param org   the given organization; if {@code null} the organization is
     *              taken from this invoicing transaction
     */
    void copyAddressesFromOrganization(Organization org = null) {
        if (!org) {
            org = organization
        }
        if (org) {
            billingAddr = new Address(org.billingAddr)
            shippingAddr = new Address(org.shippingAddr)
        }
    }

    @Override
    boolean equals(Object obj) {
        (obj instanceof InvoicingTransaction) ? obj.id == id : false
    }

    /**
     * Gets the discount amount which is granted when the user specifies a
     * discount percentage value. The percentage value is related to the
     * subtotal gross value.
     *
     * @return  the discount amount from the percentage value
     * @see     #getSubtotalGross()
     */
    double getDiscountPercentAmount() {
        subtotalGross * discountPercent / 100.0d
    }

    String getFullName() {
        "${fullNumber} ${subject}"
    }

    String getFullNumber() {
        StringBuilder buf = new StringBuilder()
        if (seqNumberService) {
            buf << seqNumberService.formatWithPrefix(getClass(), number)
        }
        if (organization) {
            buf << '-' << organization.number
        }
        buf.toString()
    }

    /**
     * Gets the subtotal gross value. It is computed by adding the tax values
     * to the subtotal net value.
     *
     * @return  the subtotal gross value
     * @see     #getSubtotalNet()
     */
    double getSubtotalGross() {
        subtotalNet + (taxRateSums.values().sum() ?: 0.0d)
    }

    /**
     * Gets the subtotal net value. It is computed by accumulating the total
     * values of the items plus the shipping costs.
     *
     * @return  the subtotal net value
     * @see		  #getSubtotalGross()
     */
    double getSubtotalNet() {
        items ? (items.total.sum() + shippingCosts) : 0.0d
    }

    /**
     * Computes a map of taxes used in this transaction. The key represents the
     * tax rate (a percentage value), the value the sum of tax values of all
     * items which belong to this tax rate.
     *
     * @return	the tax rates and their associated tax value sums
     */
    Map<Double, Double> getTaxRateSums() {
        Map<Double, Double> res = [: ]
        if (items) {
            for (item in items) {
                double tax = item.tax
                res[tax] = (res[tax] ?: 0.0d) + item.total * tax / 100.0d
            }
        }
        if (shippingTax != 0.0d && shippingCosts != 0.0d) {
            double tax = shippingTax
            res[tax] = (res[tax] ?: 0.0d) + shippingCosts * tax / 100.0d
        }
        res.sort { e1, e2 -> e1.key <=> e2.key }
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    int maxNumber(SeqNumber seq) {
        def c = InvoicingTransaction.createCriteria()
        c.get {
            projections {
                max('number')
            }
            and {
                eq('type', type)
                between('number', seq.startValue, seq.endValue)
            }
        }
    }

    @Override
    String toString() {
        subject
    }
}
