package org.amcworld.springcrm

import static java.math.RoundingMode.HALF_UP
import java.util.Date

class InvoicingTransaction {

    static constraints = {
		number(unique:'type')
		type(blank:false, nullable:false, maxSize:1)
		subject(blank:false)
		organization()
		person(nullable:true)
		docDate()
		carrier(nullable:true)
		shippingDate(nullable:true)
        billingAddrStreet(widget:'textarea', nullable:true)
        billingAddrPoBox(nullable:true)
        billingAddrPostalCode(nullable:true)
        billingAddrLocation(nullable:true)
        billingAddrState(nullable:true)
        billingAddrCountry(nullable:true)
        shippingAddrStreet(widget:'textarea', nullable:true)
        shippingAddrPoBox(nullable:true)
        shippingAddrPostalCode(nullable:true)
        shippingAddrLocation(nullable:true)
        shippingAddrState(nullable:true)
        shippingAddrCountry(nullable:true)
		headerText(widget:'textarea', nullable:true)
		items(minSize:1)
		footerText(widget:'textarea', nullable:true)
		discountPercent(scale:2, min:0.0, nullable:true)
		discountAmount(scale:2, min:0.0, nullable:true)
		shippingCosts(scale:2, min:0.0, nullable:true)
        shippingTax(scale:1, min:0.0, nullable:true)
		adjustment(scale:2, nullable:true)
		total(scale:2)
		dateCreated()
		lastUpdated()
    }
	static hasMany = [
		items:InvoicingItem,
		termsAndConditions:TermsAndConditions
	]
	static mapping = {
		items cascade:'all-delete-orphan'
		headerText type:'text'
		footerText type:'text'
	}
	static searchable = true
	static transients = [
		'fullNumber', 'fullName', 'billingAddr', 'shippingAddr', 'subtotalNet',
		'subtotalGross', 'discountPercentAmount', 'taxRateSums'
	]
	
	def seqNumberService
	
	int number
	String type
	String subject
	Organization organization
	Person person
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
	Date dateCreated
	Date lastUpdated
	
	InvoicingTransaction() {}
	
	InvoicingTransaction(InvoicingTransaction i) {
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
		termsAndConditions = i.termsAndConditions
	}

	String getFullNumber() {
		String s = seqNumberService.formatWithPrefix(getClass(), number)
		if (organization) {
			s += '-' + organization.number
		}
		return s
	}
	
	String getFullName() {
		return "${fullNumber} ${subject}"
	}
	
	String getBillingAddr() {
		String s = billingAddrStreet ?: ''
		if (billingAddrLocation) {
			if (s) {
				s += ','
			}
			if (billingAddrPostalCode) {
				if (s) {
					s += ' '
				}
				s += billingAddrPostalCode ?: ''
			}
			if (s) {
				s += ' '
			}
			s += billingAddrLocation ?: ''
		}
		return s
	}
	
	String getShippingAddr() {
		String s = shippingAddrStreet ?: ''
		if (shippingAddrLocation) {
			if (s) {
				s += ','
			}
			if (shippingAddrPostalCode) {
				if (s) {
					s += ' '
				}
				s += shippingAddrPostalCode ?: ''
			}
			if (s) {
				s += ' '
			}
			s += shippingAddrLocation ?: ''
		}
		return s
	}

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
		Map<Double, BigDecimal> res = [:]
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

	def beforeInsert() {
		total = computeTotal()
	}

	def beforeUpdate() {
		total = computeTotal()
	}
}
