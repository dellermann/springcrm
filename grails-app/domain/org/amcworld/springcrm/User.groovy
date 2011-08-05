package org.amcworld.springcrm

import java.util.Date;

class User {

    static constraints = {
		userName(blank:false, nullable:false, unique:true)
		password(blank:false, password:true)
		firstName(blank:false)
		lastName(blank:false)
        phone(maxSize:40, nullable:true)
        phoneHome(maxSize:40, nullable:true)
        mobile(maxSize:40, nullable:true)
        fax(maxSize:40, nullable:true)
        email(blank:false, nullable:false, email:true)
		settings()
		dateCreated()
		lastUpdated()
    }
	static transients = ['fullName']
	
	String userName
	String password
	String firstName
	String lastName
	String phone
	String phoneHome
	String mobile
	String fax
	String email
	Map settings
	Date dateCreated
	Date lastUpdated
	
	String getFullName() {
		return "${firstName ?: ''} ${lastName ?: ''}"
	}

	String toString() {
		return fullName
	}
}
