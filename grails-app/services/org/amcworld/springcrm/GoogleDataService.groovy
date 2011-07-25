package org.amcworld.springcrm

import javax.servlet.http.HttpSession

import org.springframework.web.context.request.RequestContextHolder

import com.google.gdata.data.contacts.ContactEntry;

abstract class GoogleDataService<E, G> {

	static scope = 'session'
    static transactional = true
	
	protected static final String APPLICATION_NAME = "amcworld-springcrm-0.9"
	
	/**
	 * Inserts the given Google Data entry into the remote repository.
	 *
	 * @param contact	the entry to insert
	 * @return			the inserted contact with additional data such as ID
	 */
	protected void afterInsert(E item, G entry) {
		def status = new GoogleDataSyncStatus(
			user:session.user, type:item.class.name, itemId:item.id,
			url:entry.selfLink.href
		)
		status.save(flush:true)
	}
	
	protected void afterUpdate(GoogleDataSyncStatus status) {
		status.lastSync = new Date()
		status.save(flush:true)
	}
	
	protected GoogleDataSyncStatus findSyncStatus(E item) {
		def c = GoogleDataSyncStatus.createCriteria()
		return c.get {
			eq('user', session.user)
			eq('type', item.class.name)
			eq('itemId', item.id)
		}
	}

	protected HttpSession getSession() {
		return RequestContextHolder.currentRequestAttributes().session
	}
}
