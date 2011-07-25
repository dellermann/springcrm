package org.amcworld.springcrm

class GoogleDataSyncStatus {

    static constraints = {
    }
	
	User user
	String type
	Long itemId
	String url
	Date lastSync = new Date()
}
