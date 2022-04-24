/*
 * InvoicingTransaction.groovy
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

import static java.math.BigDecimal.ZERO

import groovy.transform.CompileStatic
import org.grails.datastore.gorm.GormEntity
import org.grails.datastore.mapping.query.api.BuildableCriteria


/**
 * The class {@code InvoicingTransaction} acts as a base class of customer
 * accounts such as invoices, quotes etc.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class InvoicingTransaction
    implements GormEntity<InvoicingTransaction>, NumberedDomain
{

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
        total scale: 6, widget: 'currency'
        notes nullable: true, widget: 'textarea'
        createUser nullable: true
    }
    static belongsTo = [organization: Organization, person: Person]
    static embedded = ['billingAddr', 'shippingAddr']
    static hasMany = [
        items: InvoicingItem,
        termsAndConditions: TermsAndConditions
    ]
    static mapping = {
        docDate index: 'docDate'
        items cascade: 'all-delete-orphan'
        headerText type: 'text'
        footerText type: 'text'
        notes type: 'text'
        sort 'number'
        subject index: 'subject'
        termsAndConditions lazy: false
        order 'desc'
        type index: 'invoicing_transaction_type'
    }
    static transients = [
        'discountPercentAmount', 'shippingCostsGross', 'subtotalGross',
        'subtotalNet', 'taxRateSums'
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
     * The carrier used to transport the document generated from this customer
     * account.
     */
    Carrier carrier

    /**
     * The user has created this customer account.
     */
    User createUser

    /**
     * The timestamp when this customer account has been created.
     */
    Date dateCreated

    /**
     * A fixed discount amount of this customer account.  The value is
     * subtracted from the gross subtotal.
     */
    BigDecimal discountAmount = ZERO

    /**
     * A percentage discount amount of this customer account.  The value is
     * relative to the gross subtotal subtracted from it.
     */
    BigDecimal discountPercent = ZERO

    /**
     * The date of the document, that is, when this customer account has been
     * created.
     */
    Date docDate = new Date()

    /**
     * A text which appears in the footer of the document generated from this
     * customer account.
     */
    String footerText

    /**
     * A text which appears in the header of the document generated from this
     * customer account.
     */
    String headerText

    /**
     * The items of this customer account.
     */
    List<InvoicingItem> items

    /**
     * The timestamp when this customer account has been modified.
     */
    Date lastUpdated

    /**
     * Any notes which are not printed in the document generated from this
     * customer account.
     */
    String notes

    /**
     * The organization associated to this invoicing transaction.
     */
    Organization organization

    /**
     * The person associated to this invoicing transaction.
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
     * The date when the document generated from this customer account has been
     * shipped.
     */
    Date shippingDate

    /**
     * Any tax rate used to calculate the gross costs for shipping.
     */
    BigDecimal shippingTax = ZERO

    /**
     * The subject of this customer account.
     */
    String subject

    /**
     * The total of this customer account.  Normally, this method is called
     * by Hibernate only to set the total value from a database record.  You
     * should not call this method to set the total.  Use method
     * {@code computeTotal} instead.
     */
    BigDecimal total = ZERO

    /**
     * The type of customer account.  This value is intended to be set by
     * derived classes or by Hibernate during loading.  You should not set this
     * property by yourself.
     */
    String type


    //-- Constructors ---------------------------

    /**
     * Creates an empty customer account.
     */
    InvoicingTransaction() {}

    /**
     * Creates a new customer account using the data of the given customer
     * account.
     *
     * @param i the given customer account
     */
    InvoicingTransaction(InvoicingTransaction i) {
        subject = i.subject
        organization = i.organization
        person = i.person
        billingAddr = i.billingAddr ? new Address(i.billingAddr) : null
        shippingAddr = i.shippingAddr ? new Address(i.shippingAddr) : null
        headerText = i.headerText
        if (i.items != null) {
            items = new ArrayList(i.items.size())
            for (InvoicingItem item : i.items) {
                items << new InvoicingItem(item)
            }
        }
        footerText = i.footerText
        discountPercent = i.discountPercent
        discountAmount = i.discountAmount
        shippingCosts = i.shippingCosts
        shippingTax = i.shippingTax
        adjustment = i.adjustment
        total = i.total
        notes = i.notes
        termsAndConditions = i.termsAndConditions == null ? null
            : new HashSet(i.termsAndConditions)
    }


    //-- Properties -----------------------------

    /**
     * Sets the price adjustment of this customer account.
     *
     * @param adjustment    the adjustment that should be set; if {@code null}
     *                      it is converted to zero
     * @since 2.0
     */
    void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment == null ? ZERO : adjustment
    }

    /**
     * Sets a fixed discount amount of this customer account.
     *
     * @param discountAmount    the discount amount that should be set; if
     *                          {@code null} it is converted to zero
     * @since 2.0
     */
    void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount == null ? ZERO : discountAmount
    }

    /**
     * Sets a percentage discount amount of this customer account.
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
     * Sets the shipping costs of this customer account.
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
     * Sets the shipping tax of this customer account.
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
        (items*.total?.sum() ?: ZERO) + shippingCosts
    }

    /**
     * Computes a map of taxes used in this transaction.  The key represents the
     * tax rate (a percentage value), the value the sum of tax values of all
     * items which belong to this tax rate.
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
                addTaxRateSum res, item.tax, item.total
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
     * Sets the total of this customer account.  Normally, this method is
     * called by Hibernate only to set the total value from a database record.
     * You should not call this method to set the total.  Use method
     * {@code computeTotal} instead.
     *
     * @param total the total that should be set; if {@code null} it is
     *              converted to zero
     * @see         #computeTotal()
     * @since 2.0
     */
    void setTotal(BigDecimal total) {
        this.total = total == null ? ZERO : total
    }


    //-- Public methods -------------------------

    /**
     * Called before this customer account is updated in the underlying data
     * store.  The method computes the total value.
     */
    def beforeUpdate() {
        total = computeTotal()
    }

    /**
     * Called before this customer account is validated.  The method computes the
     * total value.
     */
    def beforeValidate() {
        total = computeTotal()
    }

    String computeFullName(SeqNumber seqNumber) {
        computeFullNumber(seqNumber) + ' ' + subject
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
     * Computes the total (gross) value. It is computed from the subtotal gross
     * value minus all discounts plus the adjustment.
     *
     * @return  the total (gross) value
     */
    BigDecimal computeTotal() {
        subtotalGross - discountPercentAmount - discountAmount + adjustment
    }

    /**
     * Copies the billing and shipping address from the given organization to
     * the corresponding addresses of this invoicing transaction.
     *
     * @param org   the given organization; if {@code null} the organization is
     *              taken from this invoicing transaction
     */
    void copyAddressesFromOrganization(Organization org = organization) {
        if (org) {
            billingAddr = new Address(org.billingAddr)
            shippingAddr = new Address(org.shippingAddr)
        }
    }

    @Override
    boolean equals(Object obj) {
        obj instanceof InvoicingTransaction && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    /**
     * Gets the largest number of any customer account within the limits of the
     * given sequence number.
     *
     * @param seq the given sequence number
     * @return    the largest number
     */
    int maxNumber(SeqNumber seq) {
        BuildableCriteria c = createCriteria()
        c.get {
            projections {
                max 'number'
            }
            and {
                eq 'type', type
                between 'number', seq.startValue, seq.endValue
            }
        } as int
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
     * Gets all items of this customer account which is associated to a sales
     * item of the given type.
     *
     * @param type  the given sales item type; {@code null} to select all items
     *              not associated to any sales item
     * @return      the items of this customer account associated to the given
     *              sales item type
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
