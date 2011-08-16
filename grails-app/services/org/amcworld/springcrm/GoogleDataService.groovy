package org.amcworld.springcrm

import javax.servlet.http.HttpSession

import org.springframework.web.context.request.RequestContextHolder

import com.google.gdata.client.GoogleService
import com.google.gdata.data.BaseEntry
import com.google.gdata.data.contacts.ContactEntry;

/**
 * The class <code>GoogleDataService</code> represents a service which is able
 * to exchange data with Google Data.
 * 
 * @author		Daniel Ellermann
 * @version		0.9.0
 * @param <E>	the type of domain model classes which are handled by this
 * 				service
 * @param <G>	the type of Google Data entries which are handled by this
 * 				service
 */
abstract class GoogleDataService<E, G extends BaseEntry> {

	//-- Constants ------------------------------

	protected static final String APPLICATION_NAME = "amcworld-springcrm-0.9"


	//-- Class variables ------------------------

	static scope = 'session'
    static transactional = true


	//-- Instance variables ---------------------

	protected Class<G> entryClass
	protected URL feedUrl


	//-- Public methods -------------------------

	/**
	 * Converts the given item to a Google Data entry.
	 *
	 * @param item	the given item
	 * @param entry	a Google Data object which is to fill with the property
	 * 				values; if <code>null</code> a new entry is to create
	 * @return		the converted Google Data entry
	 */
	abstract G convertToGoogle(E item, G entry = null);

	/**
	 * Deletes the given Google Data entry.
	 *
	 * @param entry	the entry to delete
	 */
	void delete(G entry) {
		entry.delete()
	}
	
	/**
	 * Deletes all entries which are marked as deleted in the synchronization
	 * status table.
	 */
	void deleteMarkedEntries() {
		def srv = getService()
		List<GoogleDataSyncStatus> toDelete = findDeletedItems()
		toDelete.each {
			G entry = retrieve(it.url)
			if (entry) {
				delete(entry)
			}
			it.delete(flush:true)
		}
	}
	
	/**
	 * Inserts the given Google Data entry into the remote repository.
	 *
	 * @param entry	the entry to insert
	 * @return		the inserted entry with additional data such as ID
	 */
	G insert(G entry) {
		return service.insert(feedUrl, entry)
	}

	/**
	 * Marks the given item as deleted in the synchronization status table. It
	 * will be deleted during the next Google data synchronization.
	 * 
	 * @param item	the item to mark as deleted
	 */
	void markDeleted(E item) {
		def status = findSyncStatus(item)
		println "Status: " + status?.dump()
		if (status) {
			status.deleted = true
			status.save(flush:true)
		}
	}
	
	/**
	 * Retrieves the Google Data entry with the given URL and class.
	 *
	 * @param url	the given URL
	 * @return		the entry; <code>null</code> if no entry with the given
	 * 				URL is available
	 */
	G retrieve(String url) {
		G res = null
		try {
			res = service.getEntry(new URL(url), entryClass)
		} catch (ResourceNotFoundException) {
			/* already handled -> res = null */
		}
		return res
	}
	
	/**
	 * Synchronizes the given item with Google Data. If an associated Google
	 * Data item is set already it is updated. Otherwise, an new entry is
	 * created.
	 *
	 * @param item	the item to synchronize
	 * @return		the converted Google Data item
	 */
	G sync(E item) {
		GoogleDataSyncStatus status = findSyncStatus(item)
		if (status) {
			G contact = retrieve(status.url)
			if (contact) {
				contact = convertToGoogle(item, contact)
				update(contact)
			} else {
				contact = convertToGoogle(item)
				contact = insert(contact)
				status.url = contact.selfLink.href
			}
			afterUpdate(status)
			return contact
		} else {
			G contact = convertToGoogle(item)
			contact = insert(contact)
			afterInsert(item, contact)
			return contact
		}
	}

	/**
	 * Updates the given Google Data entry.
	 *
	 * @param entry	the entry to update
	 * @param url	an optional different URL used for update; if
	 * 				<code>null</code> the update URL is obtained from the given
	 * 				entry
	 */
	void update(G entry, String url = null) {
		if (url == null) {
			url = entry.editLink.href
		}
		service.update(new URL(url), entry)
	}


	//-- Non-public methods ---------------------

	/**
	 * Records the given item and the associated Google Data entry in the
	 * synchronization status table. This method should be called after
	 * inserting a Google Data entry.
	 *
	 * @param item		the item to record
	 * @param contact	the associated Google Data entry
	 */
	protected void afterInsert(E item, G entry) {
		def status = new GoogleDataSyncStatus(
			user:session.user, type:item.class.name, itemId:item.id,
			url:entry.selfLink.href
		)
		status.save(flush:true)
	}
	
	/**
	 * Updates the last synchronization time stamp and stores the given
	 * synchronization status. This method should be called after updating an
	 * existing Google Data entry.
	 * 
	 * @param status	the status to update
	 */
	protected void afterUpdate(GoogleDataSyncStatus status) {
		status.lastSync = new Date()
		status.save(flush:true)
	}
	
	/**
	 * Returns all entries from the synchronization status table where were
	 * marked as deleted.
	 * 
	 * @return	the synchronization status entries which are marked as deleted
	 */
	protected List<GoogleDataSyncStatus> findDeletedItems() {
		return GoogleDataSyncStatus.findAllByDeleted(true)
	}
	
	/**
	 * Returns the synchronization status entry for the given item.
	 * 
	 * @param item	the given item
	 * @return		the synchronization status entry; <code>null</code> if no
	 * 				such entry exists for the given item
	 */
	protected GoogleDataSyncStatus findSyncStatus(E item) {
		def c = GoogleDataSyncStatus.createCriteria()
		return c.get {
			eq('user', session.user)
			eq('type', item.class.name)
			eq('itemId', item.id)
		}
	}
	
	protected abstract GoogleService getService();

	/**
	 * Returns access to the user session.
	 *
	 * @return	the session instance
	 */
	protected HttpSession getSession() {
		return RequestContextHolder.currentRequestAttributes().session
	}
}
