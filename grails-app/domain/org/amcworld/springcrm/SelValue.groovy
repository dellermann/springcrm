package org.amcworld.springcrm

class SelValue {

    static constraints = {
        name(blank: false)
        orderId()
    }
	static mapping = {
		sort 'orderId'
        id(generator: 'org.hibernate.id.enhanced.SequenceStyleGenerator', params: [initial_value: 50000])
    }

    String name
    int orderId = 0

    String toString() {
        return name ?: ''
    }
}
