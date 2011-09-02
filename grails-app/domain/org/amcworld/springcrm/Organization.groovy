package org.amcworld.springcrm

class Organization {

    static constraints = {
        number(unique:'recType')
		recType(range:1..3)
        name(blank:false)
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
        phone(maxSize:40, nullable:true)
        fax(maxSize:40, nullable:true)
        phoneOther(maxSize:40, nullable:true)
        email1(email:true, nullable:true)
        email2(email:true, nullable:true)
        website(url:/\s*/, nullable:true)
		legalForm(nullable:true)
        type(nullable:true)
        industry(nullable:true)
        owner(nullable:true)
        numEmployees(nullable:true)
        rating(nullable:true)
        notes(widget:'textarea', nullable:true)
		dateCreated()
		lastUpdated()
    }
    static hasMany = [
		persons:Person, calls:Call, noteEntries:Note, quotes:Quote,
		salesOrders:SalesOrder, invoices:Invoice,
		purchaseInvoices:PurchaseInvoice
	]
	static mapping = {
		calls column:'Organization'
		sort 'name'
		notes type:'text'
	}
	static searchable = true
	static transients = [
		'fullNumber', 'shortName', 'billingAddr', 'shippingAddr', 'customer',
		'vendor'
	]

	def seqNumberService

    int number
	byte recType
    String name
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
    String phone
    String fax
    String phoneOther
    String email1
    String email2
    String website
	String legalForm
    OrgType type
    Industry industry
    String owner
    String numEmployees
    Rating rating
    String notes
	Date dateCreated
	Date lastUpdated

	String getFullNumber() {
		return seqNumberService.format(getClass(), number)
	}

	String getShortName() {
		String res = name ?: ''
		if (res.length() > 40) {
			res = name.substring(0, 40) + '...'
		}
		return res
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

    void setWebsite(String website) {
		website = website ?: ''
        if ((website.size() > 0) && !(website =~ '^https?://')) {
			website = "http://${website}"
        }
        this.website = website
    }

	boolean isCustomer() {
		return (this.recType & 1) != 0;
	}
	
	boolean isVendor() {
		return (this.recType & 2) != 0;
	}

    String toString() {
        return name ?: ''
    }
}
