package org.amcworld.springcrm

class TaxClass extends SelValue {
	
	static constraints = {
		taxValue(scale:2, min:0.0d)
	}
	
	double taxValue
}
