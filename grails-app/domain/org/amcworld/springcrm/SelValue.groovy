package org.amcworld.springcrm

class SelValue {

    static constraints = {
        name(blank:false)
        orderId()
    }

    String name
    int orderId = 0

    String toString() {
        return name ?: ""
    }
}
