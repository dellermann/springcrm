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
		dateCreated()
		lastUpdated()
    }
	static hasMany = [
		items:InvoicingItem,
		termsAndConditions:TermsAndConditions
	]
	static mapping = {
		items cascade:'all-delete-orphan'
	}
	static transients = [
		'fullNumber', 'fullName', 'billingAddr', 'shippingAddr', 'subtotalNet',
		'subtotalGross', 'discountPercentAmount', 'total', 'taxRateSums'
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
	List items
	String footerText
	BigDecimal discountPercent
	BigDecimal discountAmount
	BigDecimal shippingCosts
    BigDecimal shippingTax = 19.0
	BigDecimal adjustment
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

	BigDecimal getSubtotalNet() {
		return items.total.sum() + shippingCosts
	}
	
	BigDecimal getSubtotalGross() {
		return subtotalNet + taxRateSums.values().sum()
	}
	
	BigDecimal getDiscountPercentAmount() {
		return (subtotalGross * discountPercent).divide(100.0, 2, HALF_UP)
	}
	
	BigDecimal getTotal() {
		return subtotalGross - discountPercentAmount - discountAmount +
			adjustment
	}
	
	Map getTaxRateSums() {
		Map res = [:]
		for (item in items) {
			double tax = item.tax.toDouble()
			res[tax] = (res[tax] ?: 0.0) + item.total * tax / 100.0
		}
		if (shippingTax != 0 && shippingCosts != 0) {
			double tax = shippingTax.toDouble()
			res[tax] = (res[tax] ?: 0.0) + shippingCosts * tax / 100.0
		}
		return res.sort { e1, e2 -> e1.key <=> e2.key }
	}
	
	String toString() {
		return subject
	}
	
	def beforeUpdate() {
		items = items.findAll { it != null }
	}
}
