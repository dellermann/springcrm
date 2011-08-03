package org.amcworld.springcrm

class LdapSyncStatus {

    static constraints = {
    }

	Long itemId
	String dn
	Date lastSync = new Date()
}
