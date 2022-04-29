/*
 * PurchaseInvoice.groovy
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


package org.amcworld.springcrm

import static java.math.BigDecimal.ZERO

import groovy.transform.CompileStatic
import java.math.RoundingMode
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code PurchaseInvoice} represents a purchase invoice.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 */
class PurchaseInvoice implements GormEntity<PurchaseInvoice>, NumberedDomain {

    //-- Constants ------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'number', 'invoiceNumber', 'subject', 'vendorName', 'notes',
        'items.*name', 'items.*description'
    ].asImmutable()

    private static final BigDecimal HUNDRED = new BigDecimal(100i)


    //-- Class fields ---------------------------

    static constraints = {
        number unique: true, widget: 'autonumber'
        invoiceNumber blank: false
        subject blank: false
        vendor nullable: true
        vendorName blank: false
        paymentDate nullable: true
        paymentAmount scale: 6, widget: 'currency'
        paymentMethod nullable: true
        items nullable: false, minSize: 1
        notes nullable: true, widget: 'textarea'
        documentFile nullable: true
        discountPercent min: ZERO, scale: 2, widget: 'percent'
        discountAmount min: ZERO, scale: 6, widget: 'currency'
        shippingCosts min: ZERO, scale: 6, widget: 'currency'
        shippingTax min: ZERO, scale: 2, widget: 'percent'
        adjustment scale: 6, widget: 'currency'
        total scale: 6
    }
    static belongsTo = [vendor: Organization]
    static hasMany = [items: PurchaseInvoiceItem]
    static mapping = {
        items cascade: 'all-delete-orphan'
        notes type: 'text'
        subject index: 'subject'
    }
    static transients = [
        'balance', 'balanceColor', 'discountPercentAmount',
        'paymentStateColor', 'subtotalNet', 'subtotalGross', 'taxRateSums'
    ]


    //-- Fields ---------------------------------

    def userService

    /**
     * The number of this purchase invoice.
     */
    String invoiceNumber

    /**
     * The subject of this purchase invoice.
     */
    String subject

    /**
     * The name of the vendor if property {@code vendor} is not used.
     */
    String vendorName

    /**
     * The date of this purchase invoice.
     */
    Date docDate = new Date()

    /**
     * The due date of payment.
     */
    Date dueDate

    /**
     * The stage of this purchase invoice.
     */
    PurchaseInvoiceStage stage

    /**
     * The items of this purchase invoice.
     */
    List<PurchaseInvoiceItem> items

    /**
     * A percentage discount amount of this purchase invoice.  The value is
     * relative to the gross subtotal subtracted from it.
     */
    BigDecimal discountPercent = ZERO

    /**
     * A fixed discount amount of this purchase invoice.  The value is
     * subtracted from the gross subtotal.
     */
    BigDecimal discountAmount = ZERO

    /**
     * The net costs for shipping.
     */
    BigDecimal shippingCosts = ZERO

    /**
     * Any tax rate used to calculate the gross costs for shipping.
     */
    BigDecimal shippingTax = ZERO

    /**
     * A positive or negative value used to adjust the gross total.
     */
    BigDecimal adjustment = ZERO

    /**
     * The total of this customer account.  Normally, this method is called
     * by Hibernate only to set the total value from a database record.  You
     * should not call this method to set the total.  Use method
     * {@code computeTotal} instead.
     */
    BigDecimal total = ZERO

    /**
     * Any notes which are not printed in the document generated from this
     * customer account.
     */
    String notes

    /**
     * The associated document of this purchase invoice.
     */
    DataFile documentFile

    /**
     * The date of payment.
     */
    Date paymentDate

    /**
     * The amount of payment.
     */
    BigDecimal paymentAmount = ZERO

    /**
     * The payment method.
     */
    PaymentMethod paymentMethod

    /**
     * The timestamp when this customer account has been created.
     */
    Date dateCreated

    /**
     * The timestamp when this customer account has been modified.
     */
    Date lastUpdated


    //-- Constructors ---------------------------

    /**
     * Creates an empty purchase invoice.
     */
    PurchaseInvoice() {}

    /**
     * Creates a purchase invoice using the data of the given one (copy
     * constructor).
     *
     * @param p the given purchase invoice
     */
    PurchaseInvoice(PurchaseInvoice p) {
        invoiceNumber = p.invoiceNumber
        subject = p.subject
        vendor = p.vendor
        vendorName = p.vendorName
        if (p.items != null) {
            items = new ArrayList(p.items.size())
            for (PurchaseInvoiceItem item : p.items) {
                items << new PurchaseInvoiceItem(item)
            }
        }
        discountPercent = p.discountPercent
        discountAmount = p.discountAmount
        shippingCosts = p.shippingCosts
        shippingTax = p.shippingTax
        adjustment = p.adjustment
        notes = p.notes
        total = p.total
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
     * Gets the balance of this purchase invoice, that is the difference
     * between the payment amount and the invoice total sum.
     *
     * @return  the purchase invoice balance
     * @since   1.0
     */
    BigDecimal getBalance() {
        int n = userService.numFractionDigitsExt
        RoundingMode rm = RoundingMode.HALF_UP

        paymentAmount.setScale(n, rm) - total.setScale(n, rm)
    }

    /**
     * Gets the name of a color indicating the status of the balance of this
     * purchase invoice.  This property is usually used to compute CSS classes
     * in the views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getBalanceColor() {
        String color = 'default'
        if (balance < ZERO) {
            color = 'red'
        } else if (balance > ZERO) {
            color = 'green'
        }

        color
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
     * Gets the name of a color indicating the payment state of this purchase
     * invoice.  This property is usually used to compute CSS classes in the
     * views.
     *
     * @return  the indicator color
     * @since   1.0
     */
    String getPaymentStateColor() {
        String color = 'default'
        switch (stage?.id) {
        case 2103:                       // rejected
            color = 'purple'
            break
        case 2102:                       // paid
            color = (balance >= ZERO) ? 'green' : colorIndicatorByDate()
            break
        }

        color
    }

    /**
     * Sets the payment amount of this purchase invoice.
     *
     * @param paymentAmount the payment amount that should be set; if
     *                      {@code null} it is converted to zero
     * @since 2.0
     */
    void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount == null ? ZERO : paymentAmount
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
     * Sets the shipping tax of this purchase invoice.
     *
     * @param shippingTax   the shipping tax that should be set; if
     *                      {@code null} it is converted to zero
     * @since 2.0
     */
    void setShippingTax(BigDecimal shippingTax) {
        this.shippingTax = shippingTax == null ? ZERO : shippingTax
    }

    /**
     * Gets the subtotal gross value. It is computed by adding the tax values
     * to the subtotal net value.
     *
     * @return  the subtotal gross value
     * @see     #getSubtotalNet()
     */
    BigDecimal getSubtotalGross() {
        (items*.totalGross?.sum() ?: ZERO) + shippingCostsGross
    }

    /**
     * Gets the subtotal net value. It is computed by accumulating the total
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
            for (PurchaseInvoiceItem item in items) {
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
     * Sets the total of this customer account.  Normally, this method is called
     * by Hibernate only to set the total value from a database record.  You
     * should not call this method to set the total.  Use method
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
     * Called before this purchase invoice is created in the underlying data
     * store.  The method computes the total value.
     */
    def beforeInsert() {
        total = computeTotal()
    }

    /**
     * Called before this purchase invoice is updated in the underlying data
     * store.  The method computes the total value.
     */
    def beforeUpdate() {
        total = computeTotal()
    }

    /**
     * Called before this purchase invoice is validated.  The method computes
     * the total value.
     */
    def beforeValidate() {
        total = computeTotal()
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

    @Override
    boolean equals(Object obj) {
        obj instanceof PurchaseInvoice && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        subject ?: ''
    }


    //-- Non-public methods ---------------------

    /**
     * Adds the given tax rate to the specified map.
     *
     * @param map       the map to add the tax rate to
     * @param taxRate   the given tax rate
     * @param value     the given value
     */
    private static void addTaxRateSum(Map<Double, BigDecimal> map,
                                      BigDecimal taxRate, BigDecimal value)
    {
        Double tr = taxRate.doubleValue()
        map[tr] = (map[tr] ?: ZERO) + value * taxRate / HUNDRED
    }

    /**
     * Returns the color indicator for the payment state depending on the
     * current date and its relation to the due date of payment.
     *
     * @return  the indicator color
     * @since   1.0
     */
    private String colorIndicatorByDate() {
        String color = 'default'
        if (dueDate != null) {
            Date d = new Date()
            if (d >= dueDate - 3) {
                if (d <= dueDate) {
                    color = 'yellow'
                } else if (d <= dueDate + 3) {
                    color = 'orange'
                } else {
                    color = 'red'
                }
            }
        }

        color
    }
}
