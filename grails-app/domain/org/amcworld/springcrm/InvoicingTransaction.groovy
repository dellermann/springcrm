/*
 * InvoicingTransaction.groovy
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
import java.util.List;


/**
 * The class {@code InvoicingTransaction} acts as a base class of invoicing
 * transactions such as invoices, quotes etc.
 *
 * @author	Daniel Ellermann
 * @version 1.3
 */
class InvoicingTransaction {

    //-- Class variables ------------------------

    static constraints = {
		number(unique: 'type', widget: 'autonumber')
		type(nullable: false, blank: false, maxSize: 1)
		subject(blank: false)
		organization()
		person(nullable: true)
		docDate()
		carrier(nullable: true)
		shippingDate(nullable: true)
        billingAddrStreet(nullable: true, widget: 'textarea')
        billingAddrPoBox(nullable: true)
        billingAddrPostalCode(nullable: true)
        billingAddrLocation(nullable: true)
        billingAddrState(nullable: true)
        billingAddrCountry(nullable: true)
        shippingAddrStreet(nullable: true, widget: 'textarea')
        shippingAddrPoBox(nullable: true)
        shippingAddrPostalCode(nullable: true)
        shippingAddrLocation(nullable: true)
        shippingAddrState(nullable: true)
        shippingAddrCountry(nullable: true)
		headerText(nullable: true, widget: 'textarea')
		items(minSize: 1)
		footerText(nullable: true, widget: 'textarea')
		discountPercent(nullable: true, scale: 2, min: 0.0, widget: 'percent')
		discountAmount(nullable: true, scale: 10, min: 0.0, widget: 'currency')
		shippingCosts(nullable: true, scale: 10, min: 0.0, widget: 'currency')
        shippingTax(nullable: true, scale: 1, min: 0.0, widget: 'percent')
		adjustment(nullable: true, scale: 10, widget: 'currency')
		total(scale: 10)
        notes(nullable: true, widget: 'textarea')
		dateCreated()
		lastUpdated()
    }
    static belongsTo = [organization: Organization, person: Person]
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
		'fullNumber', 'fullName', 'billingAddr', 'shippingAddr', 'subtotalNet',
		'subtotalGross', 'discountPercentAmount', 'taxRateSums'
	]


    //-- Instance variables ---------------------

	def seqNumberService
    def viewService

	int number
	String type
	String subject
	Date docDate = new Date()
	Carrier carrier
	Date shippingDate
    String billingAddrStreet
    String billingAddrPoBox
    String billingAddrPostalCode
    String billingAddrLocation
    String billingAddrState
    String billingAddrCountry
    String shippingAddrStreet
    String shippingAddrPoBox
    String shippingAddrPostalCode
    String shippingAddrLocation
    String shippingAddrState
    String shippingAddrCountry
	String headerText
	List<InvoicingItem> items
	String footerText
	BigDecimal discountPercent
	BigDecimal discountAmount
	BigDecimal shippingCosts
    BigDecimal shippingTax = 19.0
	BigDecimal adjustment
	BigDecimal total
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
		carrier = i.carrier
		billingAddrStreet = i.billingAddrStreet
		billingAddrPoBox = i.billingAddrPoBox
		billingAddrPostalCode = i.billingAddrPostalCode
		billingAddrLocation = i.billingAddrLocation
		billingAddrState = i.billingAddrState
		billingAddrCountry = i.billingAddrCountry
		shippingAddrStreet = i.shippingAddrStreet
		shippingAddrPoBox = i.shippingAddrPoBox
		shippingAddrPostalCode = i.shippingAddrPostalCode
		shippingAddrLocation = i.shippingAddrLocation
		shippingAddrState = i.shippingAddrState
		shippingAddrCountry = i.shippingAddrCountry
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

	String getFullNumber() {
		String s = seqNumberService ? seqNumberService.formatWithPrefix(getClass(), number) : ''
		if (organization) {
			s += '-' + organization.number
		}
		return s
	}

	String getFullName() {
		return "${fullNumber} ${subject}"
	}

	String getBillingAddr() {
		StringBuilder s = new StringBuilder(billingAddrStreet ?: '')
		if (billingAddrLocation) {
			if (s) {
				s << ','
			}
			if (billingAddrPostalCode) {
				if (s) {
					s << ' '
				}
				s << billingAddrPostalCode ?: ''
			}
			if (s) {
				s << ' '
			}
			s << billingAddrLocation ?: ''
		}
		return s.toString()
	}

	String getShippingAddr() {
		StringBuilder s = new StringBuilder(shippingAddrStreet ?: '')
		if (shippingAddrLocation) {
			if (s) {
				s << ','
			}
			if (shippingAddrPostalCode) {
				if (s) {
					s << ' '
				}
				s << shippingAddrPostalCode ?: ''
			}
			if (s) {
				s << ' '
			}
			s << shippingAddrLocation ?: ''
		}
		return s.toString()
	}

	BigDecimal getDiscountPercent() {
		return discountPercent ?: 0
	}

	BigDecimal getDiscountAmount() {
		return discountAmount ?: 0
	}

    /**
     * Renders a list of error messages in the embedded items.
     *
     * @return  a list of error messages
     * @since   1.2
     */
    List<String> getItemErrors() {
        return viewService.getItemErrorMessages(this)
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
		return items ? (items.total.sum() + getShippingCosts()) : 0
	}

	/**
	 * Gets the subtotal gross value. It is computed by adding the tax values
	 * to the subtotal net value.
	 *
	 * @return	the subtotal gross value
	 * @see		#getSubtotalNet()
	 */
	BigDecimal getSubtotalGross() {
		return taxRateSums ? (subtotalNet + taxRateSums.values().sum()) : 0
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
		Map<Double, BigDecimal> res = [:]
        if (items) {
    		for (item in items) {
    			double tax = (item.tax != null) ? item.tax.toDouble() : 0.0
    			res[tax] = (res[tax] ?: 0.0) + item.total * tax / 100.0
    		}
    		if (getShippingTax() != 0 && getShippingCosts() != 0) {
    			double tax = getShippingTax().toDouble()
    			res[tax] = (res[tax] ?: 0.0) + getShippingCosts() * tax / 100.0
    		}
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

	int maxNumber(SeqNumber seq) {
		def c = InvoicingTransaction.createCriteria()
		return c.get {
			projections {
				max('number')
			}
			and {
				eq('type', type)
				between('number', seq.startValue, seq.endValue)
			}
		}
	}

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
        println "Saving ${type}-${number} in " + Thread.currentThread().stackTrace
	}
}
