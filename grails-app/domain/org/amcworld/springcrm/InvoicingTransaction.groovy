package org.amcworld.springcrm

import java.util.Date;

class InvoicingTransaction {

    static constraints = {
		number(unique:true)
		subject(blank:false)
		organization()
		person(nullable:true)
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
		footerText(widget:'textarea', nullable:true)
		discountPercent(scale:2, min:0.0d)
		discountAmount(scale:2, min:0.0d)
		shippingCosts(scale:2, min:0.0d)
        shippingTax(scale:1, min:0.0d)
		adjustment(scale:2)
		dateCreated()
		lastUpdated()
    }
	static hasMany = [items:InvoicingItem]
	static transients = [
		'fullNumber', 'billingAddr', 'shippingAddr', 'subTotal', 
		'discountPercentAmount', 'total', 'taxRateSums'
	]
	
	int number
	String subject
	Organization organization
	Person person
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
	String footerText
	double discountPercent
	double discountAmount
	double shippingCosts
    double shippingTax
	double adjustment
	Date dateCreated
	Date lastUpdated

	String getFullNumber() {
		String s = number.toString()
		if (organization) {
			s += '-' + organization.number
		}
		return s
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

	double getSubTotal() {
		return items.total.sum()
	}
	
	double getDiscountPercentAmount() {
		return (subTotal * discountPercent / 100.0).round(2)
	}
	
	double getTotal() {
		return subTotal - discountPercentAmount - discountAmount +
			shippingCosts + adjustment
	}
	
	Map getTaxRateSums() {
		Map res = [:]
		for (item in items) {
			double tax = item.tax
			res[tax] = (res[tax] ?: 0.0d) + (item.total * tax / 100).round(2)
		}
		return res
	}
	
	String toString() {
		return subject
	}
}
