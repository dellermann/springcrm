package org.amcworld.springcrm

import java.util.Date;

class User {

    static constraints = {
		userName(blank:false, nullable:false, unique:true)
		password(blank:false, password:true)
		firstName(blank:false)
		lastName(blank:false)
        phone(maxSize:40)
        phoneHome(maxSize:40)
        mobile(maxSize:40)
        fax(maxSize:40)
        email(email:true)
		dateCreated()
		lastUpdated()
    }
	static transients = ["fullName"]
	
	String userName
	String password
	String firstName
	String lastName
	String phone
	String phoneHome
	String mobile
	String fax
	String email
	Date dateCreated
	Date lastUpdated
	
	String getFullName() {
		return "${firstName} ${lastName}"
	}

	String toString() {
		return userName
	}
}
