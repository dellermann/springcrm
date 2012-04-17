/*
 * PurchaseInvoice.groovy
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

import static java.math.RoundingMode.HALF_UP


/**
 * The class {@code PurchaseInvoice} represents a purchase invoice.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class PurchaseInvoice {

    //-- Class variables ------------------------

    static constraints = {
		number(nullable: false, blank: false)
		subject(nullable: false, blank: false)
		vendor(nullable: true)
		vendorName(nullable: false, blank: false)
		docDate()
		dueDate()
		stage()
		paymentDate(nullable: true)
		paymentAmount(nullable: true, widget: 'currency')
		paymentMethod(nullable: true)
		items(minSize: 1)
		notes(nullable: true, widget: 'textarea')
		documentFile(nullable: true)
		discountPercent(nullable: true, scale: 2, min: 0.0, widget: 'percent')
		discountAmount(nullable: true, scale: 2, min: 0.0, widget: 'currency')
		shippingCosts(nullable: true, scale: 2, min: 0.0, widget: 'currency')
        shippingTax(nullable: true, scale: 1, min: 0.0, widget: 'percent')
		adjustment(nullable: true, scale: 2, widget: 'currency')
		total(scale: 2)
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [ vendor: Organization ]
	static hasMany = [ items: PurchaseInvoiceItem ]
	static mapping = {
		items cascade: 'all-delete-orphan'
		notes type: 'text'
        subject index: 'subject'
	}
	static searchable = true
	static transients = [
		'subtotalNet', 'subtotalGross', 'discountPercentAmount', 'taxRateSums'
	]


    //-- Instance variables ---------------------

	String number
	String subject
	String vendorName
	Date docDate = new Date()
	Date dueDate
	PurchaseInvoiceStage stage
	Date paymentDate
	BigDecimal paymentAmount
	PaymentMethod paymentMethod
	List<PurchaseInvoiceItem> items
	BigDecimal discountPercent
	BigDecimal discountAmount
	BigDecimal shippingCosts
    BigDecimal shippingTax = 19.0
	BigDecimal adjustment
	String notes
	String documentFile
	BigDecimal total
	Date dateCreated
	Date lastUpdated


    //-- Constructors ---------------------------

	PurchaseInvoice() {}

	PurchaseInvoice(PurchaseInvoice p) {
		number = p.number
		subject = p.subject
		vendor = p.vendor
		vendorName = p.vendorName
		items = new ArrayList(p.items.size())
		p.items.each { items << new PurchaseInvoiceItem(it) }
		discountPercent = p.discountPercent
		discountAmount = p.discountAmount
		shippingCosts = p.shippingCosts
		shippingTax = p.shippingTax
		adjustment = p.adjustment
		notes = p.notes
		total = p.total
	}


    //-- Public methods -------------------------

	BigDecimal getDiscountPercent() {
		return discountPercent ?: 0
	}

	BigDecimal getDiscountAmount() {
		return discountAmount ?: 0
	}

	BigDecimal getShippingCosts() {
		return shippingCosts ?: 0
	}

	BigDecimal getShippingTax() {
		return shippingTax ?: 0
	}

	BigDecimal getAdjustment() {
		return adjustment ?: 0
	}

	/**
	 * Gets the subtotal net value. It is computed by accumulating the total
	 * values of the items plus the shipping costs.
	 *
	 * @return	the subtotal net value
	 * @see		#getSubtotalGross()
	 */
	BigDecimal getSubtotalNet() {
		return items.total.sum() + getShippingCosts()
	}

	/**
	 * Gets the subtotal gross value. It is computed by adding the tax values
	 * to the subtotal net value.
	 *
	 * @return	the subtotal gross value
	 * @see		#getSubtotalNet()
	 */
	BigDecimal getSubtotalGross() {
		return subtotalNet + taxRateSums.values().sum()
	}

	/**
	 * Gets the discount amount which is granted when the user specifies a
	 * discount percentage value. The percentage value is related to the
	 * subtotal gross value.
	 *
	 * @return	the discount amount from the percentage value
	 * @see		#getSubtotalGross()
	 */
	BigDecimal getDiscountPercentAmount() {
		return (subtotalGross * getDiscountPercent()).divide(100.0, 2, HALF_UP)
	}

	/**
	 * Computes a map of taxes used in this transaction. The key represents the
	 * tax rate (a percentage value), the value the sum of tax values of all
	 * items which belong to this tax rate.
	 *
	 * @return	the tax rates and their associated tax value sums
	 */
	Map<Double, BigDecimal> getTaxRateSums() {
		Map<Double, BigDecimal> res = [: ]
		for (item in items) {
			double tax = item.tax.toDouble()
			res[tax] = (res[tax] ?: 0.0) + item.total * tax / 100.0
		}
		if (getShippingTax() != 0 && getShippingCosts() != 0) {
			double tax = getShippingTax().toDouble()
			res[tax] = (res[tax] ?: 0.0) + getShippingCosts() * tax / 100.0
		}
		return res.sort { e1, e2 -> e1.key <=> e2.key }
	}

	/**
	 * Computes the total (gross) value. It is computed from the subtotal gross
	 * value minus all discounts plus the adjustment.
	 *
	 * @return	the total (gross) value
	 */
	BigDecimal computeTotal() {
		return subtotalGross - discountPercentAmount - getDiscountAmount() +
			getAdjustment()
	}

	String toString() {
		return subject
	}

	def beforeValidate() {
		total = computeTotal()
	}

	def beforeInsert() {
		total = computeTotal()
	}

	def beforeUpdate() {
		total = computeTotal()
	}
}
