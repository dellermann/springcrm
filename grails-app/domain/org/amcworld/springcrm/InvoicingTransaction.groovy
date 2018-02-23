/*
 * InvoicingTransaction.groovy
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

import static java.math.BigDecimal.ZERO

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import org.bson.types.ObjectId


/**
 * The class {@code InvoicingTransaction} acts as a base class of customer
 * accounts such as invoices, quotes etc.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@EqualsAndHashCode(includes = ['id'])
class InvoicingTransaction implements NumberedDomain {

    //-- Constants ------------------------------

    private static final BigDecimal HUNDRED = new BigDecimal(100i)


    //-- Class fields ---------------------------

    static constraints = {
        number unique: 'type', widget: 'autonumber'
        type blank: false, maxSize: 1
        subject blank: false, widget: 'textarea', attributes: [nl2br: true]
        person nullable: true
        carrier nullable: true
        shippingDate nullable: true
        headerText nullable: true, widget: 'textarea'
        items nullable: false, minSize: 1
        footerText nullable: true, widget: 'textarea'
        discountPercent min: ZERO, scale: 2, widget: 'percent'
        discountAmount min: ZERO, scale: 6, widget: 'currency'
        shippingCosts min: ZERO, scale: 6, widget: 'currency'
        shippingTax min: ZERO, scale: 2, widget: 'percent'
        adjustment scale: 6, widget: 'currency'
        notes nullable: true, widget: 'textarea'
        createUser nullable: true
    }
    static belongsTo = [organization: Organization, person: Person]
    static embedded = ['billingAddr', 'shippingAddr', 'items']
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
        subject index: true
        termsAndConditions lazy: false
        order 'desc'
    }
    static transients = [
        'discountPercentAmount', 'fullName', 'shippingCostsGross',
        'subtotalGross', 'subtotalNet', 'taxRateSums', 'total'
    ]


    //-- Fields ---------------------------------

    /**
     * A positive or negative value used to adjust the gross total.
     */
    BigDecimal adjustment = ZERO

    /**
     * The address of the organization where to send the invoices to.
     */
    Address billingAddr

    /**
     * The carrier used to transport the document generated from the customer
     * account.
     */
    Carrier carrier

    /**
     * The user has created the invoicing transaction.
     */
    User createUser

    /**
     * The timestamp when the invoicing transaction has been created.
     */
    Date dateCreated

    /**
     * A fixed discount amount of the invoicing transaction.  The value is
     * subtracted from the gross subtotal.
     */
    BigDecimal discountAmount = ZERO

    /**
     * A percentage discount amount of the invoicing transaction.  The value is
     * relative to the gross subtotal subtracted from it.
     */
    BigDecimal discountPercent = ZERO

    /**
     * The date of the document, that is, when the invoicing transaction has
     * been created.
     */
    Date docDate = new Date()

    /**
     * A text which appears in the footer of the document generated from the
     * invoicing transaction.
     */
    String footerText

    /**
     * A text which appears in the header of the document generated from the
     * invoicing transaction.
     */
    String headerText

    /**
     * The ID of the invoicing transaction.
     */
    ObjectId id

    /**
     * The items of the invoicing transaction.
     */
    List<InvoicingItem> items

    /**
     * The timestamp when the invoicing transaction has been modified.
     */
    Date lastUpdated

    /**
     * Any notes which are not printed in the document generated from the
     * invoicing transaction.
     */
    String notes

    /**
     * The organization associated to the invoicing transaction.
     */
    Organization organization

    /**
     * The person associated to the invoicing transaction.
     */
    Person person

    /**
     * The address of the organization where to deliver to.
     */
    Address shippingAddr

    /**
     * The net costs for shipping.
     */
    BigDecimal shippingCosts = ZERO

    /**
     * The date when the document generated from the invoicing transaction has
     * been shipped.
     */
    Date shippingDate

    /**
     * Any tax rate used to calculate the gross costs for shipping.
     */
    BigDecimal shippingTax = ZERO

    /**
     * The subject of the invoicing transaction.
     */
    String subject

    /**
     * The type of invoicing transaction.  This value is intended to be set by
     * derived classes or by Hibernate during loading.  You should not set the
     * property by yourself.
     */
    String type


    //-- Constructors ---------------------------

    /**
     * Creates an empty invoicing transaction.
     */
    InvoicingTransaction() {}

    /**
     * Creates a new invoicing transaction using the data of the given customer
     * account.
     *
     * @param transaction   the given invoicing transaction
     */
    InvoicingTransaction(InvoicingTransaction transaction) {
        subject = transaction.subject
        organization = transaction.organization
        person = transaction.person
        billingAddr = transaction.billingAddr \
            ? new Address(transaction.billingAddr)
            : null
        shippingAddr = transaction.shippingAddr \
            ? new Address(transaction.shippingAddr)
            : null
        headerText = transaction.headerText
        if (transaction.items != null) {
            items = new ArrayList(transaction.items.size())
            for (InvoicingItem item : transaction.items) {
                items << new InvoicingItem(item)
            }
        }
        footerText = transaction.footerText
        discountPercent = transaction.discountPercent
        discountAmount = transaction.discountAmount
        shippingCosts = transaction.shippingCosts
        shippingTax = transaction.shippingTax
        adjustment = transaction.adjustment
        notes = transaction.notes
        termsAndConditions = transaction.termsAndConditions == null ? null
            : new HashSet(transaction.termsAndConditions)
    }


    //-- Properties -----------------------------

    /**
     * Sets the price adjustment of the invoicing transaction.
     *
     * @param adjustment    the adjustment that should be set; if {@code null}
     *                      it is converted to zero
     * @since 2.0
     */
    void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment == null ? ZERO : adjustment
    }

    /**
     * Sets a fixed discount amount of the invoicing transaction.
     *
     * @param discountAmount    the discount amount that should be set; if
     *                          {@code null} it is converted to zero
     * @since 2.0
     */
    void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount == null ? ZERO : discountAmount
    }

    /**
     * Sets a percentage discount amount of the invoicing transaction.
     *
     * @param discountPercent   the percentage discount amount that should be
     *                          set; if {@code null} it is converted to zero
     * @since 2.0
     */
    void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent == null ? ZERO : discountPercent
    }

    /**
     * Gets the discount amount which is granted when the user specifies a
     * discount percentage value.  The percentage value is related to the
     * subtotal gross value.
     *
     * @return  the discount amount from the percentage value
     * @see     #getSubtotalGross()
     */
    BigDecimal getDiscountPercentAmount() {
        subtotalGross * discountPercent / HUNDRED
    }

    /**
     * Gets the full name of the invoicing transaction, that is, the full
     * number from property {@code fullNumber} and the subject.
     *
     * @return  the full name
     */
    String getFullName() {
        "${computeFullNumber()} ${subject}"
    }

    /**
     * Sets the shipping costs of the invoicing transaction.
     *
     * @param shippingCosts the shipping costs that should be set; if
     *                      {@code null} it is converted to zero
     * @since 2.0
     */
    void setShippingCosts(BigDecimal shippingCosts) {
        this.shippingCosts = shippingCosts == null ? ZERO : shippingCosts
    }

    /**
     * Gets the gross shipping costs.
     *
     * @return  the gross shipping costs
     * @since   2.0
     */
    BigDecimal getShippingCostsGross() {
        shippingCosts * (HUNDRED + shippingTax) / HUNDRED
    }

    /**
     * Sets the shipping tax of the invoicing transaction.
     *
     * @param shippingTax the shipping tax that should be set; if {@code null} it
     *                    is converted to zero
     * @since 2.0
     */
    void setShippingTax(BigDecimal shippingTax) {
        this.shippingTax = shippingTax == null ? ZERO : shippingTax
    }

    /**
     * Gets the subtotal gross value.  It is computed by adding the tax values
     * to the subtotal net value.
     *
     * @return  the subtotal gross value
     * @see     #getSubtotalNet()
     */
    BigDecimal getSubtotalGross() {
        (items*.totalGross?.sum() ?: ZERO) + shippingCostsGross
    }

    /**
     * Gets the subtotal net value.  It is computed by accumulating the total
     * values of the items plus the shipping costs.
     *
     * @return  the subtotal net value
     * @see     #getSubtotalGross()
     */
    BigDecimal getSubtotalNet() {
        (items*.totalNet?.sum() ?: ZERO) + shippingCosts
    }

    /**
     * Computes a map of taxes used in the transaction.  The key represents the
     * tax rate (a percentage value), the value the sum of tax values of all
     * items which belong to the tax rate.
     * <p>
     * The keys which represent the tax rates in the returned map are stored as
     * {@code Double} values because the more precise {@code BigDecimal} values
     * are less suitable as map keys.  This is due to
     * {@code BigDecimal.hashCode()} returns different hash codes for numbers
     * which are numerically equal but differ in scale (like 2.0 and 2.00). You
     * should not use the keys of the returned map in computations due to the
     * floating point number issues.
     *
     * @return  the tax rates and their associated tax value sums
     * @see     BigDecimal#hashCode()
     */
    @CompileStatic
    Map<Double, BigDecimal> getTaxRateSums() {
        Map<Double, BigDecimal> res = new HashMap<Double, BigDecimal>(5)
        if (items) {
            for (InvoicingItem item in items) {
                addTaxRateSum res, item.tax, item.totalNet
            }
        }
        if (shippingTax && shippingCosts) {
            addTaxRateSum res, shippingTax, shippingCosts
        }

        res.sort {
            Map.Entry<Double, BigDecimal> e1,
            Map.Entry<Double, BigDecimal> e2 ->
            e1.key <=> e2.key
        }
    }

    /**
     * The total of the invoicing transaction.
     */
    BigDecimal getTotal() {
        subtotalGross - discountPercentAmount - discountAmount + adjustment
    }


    //-- Public methods -------------------------

    /**
     * Called before the invoicing transaction is created in the underlying data
     * store.  The method computes the total value.
     */
    def beforeInsert() {
        computeDynamicValues()
    }

    /**
     * Called before the invoicing transaction is updated in the underlying data
     * store.  The method computes the total value.
     */
    def beforeUpdate() {
        computeDynamicValues()
    }

    /**
     * Computes the sequence number in the instance.  The method works like the
     * method in {@code NumberedDomain} but adds the number of the organization
     * to the result.
     *
     * @param seqNumber the given sequence number which specifies prefix and
     *                  suffix; may be {@code null}
     * @return          the formatted sequence number
     * @since 3.0
     */
    @Override
    String computeFullNumber(SeqNumber seqNumber) {
        StringBuilder buf = new StringBuilder(
            NumberedDomain.super.computeFullNumber(seqNumber, withSuffix: false)
        )
        if (organization != null) {
            buf << '-' << organization.number
        }
        if (seqNumber.suffix) {
            buf << '-' << seqNumber.suffix
        }

        buf.toString()
    }

    /**
     * Copies the billing and shipping address from the given organization to
     * the corresponding addresses of the invoicing transaction.
     *
     * @param org   the given organization; if {@code null} the organization is
     *              taken from the invoicing transaction
     */
    void copyAddressesFromOrganization(Organization org = organization) {
        if (org) {
            billingAddr = new Address(org.billingAddr)
            shippingAddr = new Address(org.shippingAddr)
        }
    }

    @Override
    String toString() {
        subject ?: ''
    }


    //-- Non-public methods ---------------------

    /**
     * Adds the given tax rate to the given map.
     *
     * @param map       the given map
     * @param taxRate   the given tax rate
     * @param value     the price value for the given tax rate
     */
    private static void addTaxRateSum(Map<Double, BigDecimal> map,
                                      BigDecimal taxRate, BigDecimal value)
    {
        Double tr = taxRate.doubleValue()
        map[tr] = (map[tr] ?: ZERO) + value * taxRate / HUNDRED
    }

    /**
     * Computes dynamic values such as the net and gross subtotal, gross
     * shipping costs and total.
     *
     * @since 3.0
     */
    private void computeDynamicValues() {
        this['discountPercentAmount'] = discountPercentAmount
        this['shippingCostsGross'] = shippingCostsGross
        this['subtotalGross'] = subtotalGross
        this['subtotalNet'] = subtotalNet
        this['total'] = total

        items*.computeDynamicValues()
    }

    /**
     * Gets all items of the invoicing transaction which is associated to a
     * sales item of the given type.
     *
     * @param type  the given sales item type; {@code null} to select all items
     *              not associated to any sales item
     * @return      the items of the invoicing transaction associated to the
     *              given sales item type
     * @since       2.0
     */
    @CompileStatic
    protected List<InvoicingItem> itemsOfType(String type) {
        List<InvoicingItem> res = new ArrayList<InvoicingItem>()
        if (items != null) {
            for (InvoicingItem item : items) {
                SalesItem si = item.salesItem
                if (type == null && si == null || si?.type == type) {
                    res.add item
                }
            }
        }

        res
    }
}
