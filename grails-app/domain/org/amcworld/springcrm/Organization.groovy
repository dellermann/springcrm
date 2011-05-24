package org.amcworld.springcrm

class Organization {

    static constraints = {
        number(blank:false, unique:true)
        name(blank:false)
        billingAddrStreet(widget:"textarea")
        billingAddrPoBox()
        billingAddrPostalCode()
        billingAddrLocation()
        billingAddrState()
        billingAddrCountry()
        shippingAddrStreet(widget:"textarea")
        shippingAddrPoBox()
        shippingAddrPostalCode()
        shippingAddrLocation()
        shippingAddrState()
        shippingAddrCountry()
        phone(maxSize:40)
        fax(maxSize:40)
        phoneOther(maxSize:40)
        email1(email:true)
        email2(email:true)
        website(url:/\s*/)
		legalForm()
        type(nullable:true)
        industry(nullable:true)
        owner()
        numEmployees()
        rating(nullable:true)
        notes(widget:"textarea")
		dateCreated()
		lastUpdated()
    }
    static hasMany = [persons:Person, calls:Call]
	static mapping = {
		calls column:"Organization"
	}
	static transients = ["fullNumber", "billingAddr", "shippingAddr"]

    int number
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
		return "ORG-" + number
	}
	
	String getBillingAddr() {
		String s = billingAddrStreet ?: ""
		if (billingAddrLocation) {
			if (s) {
				s += ","
			}
			if (billingAddrPostalCode) {
				if (s) {
					s += " "
				}
				s += billingAddrPostalCode ?: ""
			}
			if (s) {
				s += " "
			}
			s += billingAddrLocation ?: ""
		}
		return s
	}
	
	String getShippingAddr() {
		String s = shippingAddrStreet ?: ""
		if (shippingAddrLocation) {
			if (s) {
				s += ","
			}
			if (shippingAddrPostalCode) {
				if (s) {
					s += " "
				}
				s += shippingAddrPostalCode ?: ""
			}
			if (s) {
				s += " "
			}
			s += shippingAddrLocation ?: ""
		}
		return s
	}

    void setWebsite(String website) {
        if ((website.size() > 0) && !(website =~ "^https?://")) {
			website = "http://" + website
        }
        this.website = website
    }

    String toString() {
        return name ?: ""
    }
}
