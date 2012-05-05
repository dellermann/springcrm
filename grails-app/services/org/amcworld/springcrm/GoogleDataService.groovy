/*
 * GoogleDataService.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.amcworld.springcrm

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.services.calendar.CalendarScopes
import com.google.gdata.client.GoogleService
import com.google.gdata.data.BaseEntry
import javax.servlet.http.HttpSession
import org.springframework.web.context.request.RequestContextHolder


/**
 * The class {@code GoogleDataService} represents a service which is able to
 * exchange data with Google API (former: Google Data).
 *
 * @author      Daniel Ellermann
 * @version     1.0
 * @param <E>   the type of domain model classes which are handled by this
 *              service
 * @param <G>   the type of Google API entries which are handled by this
 *              service
 */
abstract class GoogleDataService<E, G extends BaseEntry> {

	//-- Constants ------------------------------

    /**
     * The identifier for this application used in connections to Google.
     */
	protected static final String APPLICATION_NAME = 'amcworld-springcrm-1.0'


	//-- Class variables ------------------------

	static scope = 'session'
    static transactional = false


	//-- Instance variables ---------------------

	protected Class<G> entryClass
    def googleOAuthService
	protected URL feedUrl
    protected GoogleService svc


	//-- Public methods -------------------------

	/**
	 * Converts the given item to a Google API entry.
	 *
	 * @param item     the given item
	 * @param entry    a Google API object which is to fill with the property
	 *                 values; if {@code null} a new entry is to create
	 * @return         the converted Google API entry
	 */
	abstract G convertToGoogle(E item, G entry = null)

	/**
	 * Deletes the given Google Data entry.
	 *
	 * @param entry    the entry to delete
	 */
	void delete(G entry) {
		entry.delete()
	}

	/**
	 * Deletes all entries which are marked as deleted in the synchronization
	 * status table.
	 */
	void deleteMarkedEntries() {
		def srv = service
		List<GoogleDataSyncStatus> toDelete = findDeletedItems()
		toDelete.each {
			G entry = retrieve(it.url)
			if (entry) {
				delete(entry)
			}
			it.delete(flush: true)
		}
	}

	/**
	 * Inserts the given Google API entry into the remote repository.
	 *
	 * @param entry    the entry to insert
	 * @return         the inserted entry with additional data such as ID
	 */
	G insert(G entry) {
		return service.insert(feedUrl, entry)
	}

	/**
	 * Marks the given item as deleted in the synchronization status table. It
	 * will be deleted during the next Google API synchronization.
	 *
	 * @param item the item to mark as deleted
	 */
	void markDeleted(E item) {
		def status = findSyncStatus(item)
		if (status) {
			status.deleted = true
			status.save(flush: true)
		}
	}

	/**
	 * Retrieves the Google API entry with the given URL and class.
	 *
	 * @param url  the given URL
	 * @return     the entry; {@code null} if no entry with the given URL is
	 *             available
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
	 * Synchronizes the given item with Google API. If an associated Google API
	 * item is set already it is updated. Otherwise, an new entry is created.
	 *
	 * @param item the item to synchronize
	 * @return     the converted Google API item
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
	 * Updates the given Google API entry.
	 *
	 * @param entry    the entry to update
	 * @param url      an optional different URL used for update; if
	 *                 {@code null} the update URL is obtained from the given
	 *                 entry
	 */
	void update(G entry, String url = null) {
		if (url == null) {
			url = entry.editLink.href
		}
		service.update(new URL(url), entry)
	}


	//-- Non-public methods ---------------------

	/**
	 * Records the given item and the associated Google API entry in the
	 * synchronization status table. This method should be called after
	 * inserting a Google API entry.
	 *
	 * @param item     the item to record
	 * @param contact  the associated Google Data entry
	 */
	protected void afterInsert(E item, G entry) {
		def status = new GoogleDataSyncStatus(
			user: session.user, type: item.class.name, itemId: item.id,
			url: entry.selfLink.href
		)
		status.save(flush: true)
	}

	/**
	 * Updates the last synchronization time stamp and stores the given
	 * synchronization status. This method should be called after updating an
	 * existing Google API entry.
	 *
	 * @param status   the status to update
	 */
	protected void afterUpdate(GoogleDataSyncStatus status) {
		status.lastSync = new Date()
		status.save(flush: true)
	}

	/**
	 * Returns all entries from the synchronization status table where were
	 * marked as deleted.
	 *
	 * @return the synchronization status entries which are marked as deleted
	 */
	protected List<GoogleDataSyncStatus> findDeletedItems() {
		return GoogleDataSyncStatus.findAllByDeleted(true)
	}

	/**
	 * Returns the synchronization status entry for the given item.
	 *
	 * @param item the given item
	 * @return     the synchronization status entry; {@code null} if no such
	 *             entry exists for the given item
	 */
	protected GoogleDataSyncStatus findSyncStatus(E item) {
		def c = GoogleDataSyncStatus.createCriteria()
		return c.get {
			eq('user', session.user)
			eq('type', item.class.name)
			eq('itemId', item.id)
		}
	}

    /**
     * Gets access to the underlying Google API service.  The service is fully
     * authenticated.  The actual Google API service implementation is obtained
     * from the subclass by method {@code getServiceInstance()}.
     *
     * @return  the Google API service instance
     * @see     #getServiceInstance()
     * @since   1.0
     */
    protected synchronized GoogleService getService() {
        if (!svc) {
            svc = serviceInstance
        }

        def credential = googleOAuthService.loadCredential(session.user.userName)
        if (!credential) {
            throw new GoogleAuthException('error.googleAuthException.message.noCredentials')
        }

        svc.OAuth2Credentials = credential
        return svc
    }

    /**
     * Gets the underlying Google API service.  Derived classes must implement
     * the instantiation of such a service class.
     *
     * @return  the Google API service instance
     * @since   1.0
     */
	protected abstract GoogleService getServiceInstance()

	/**
	 * Returns access to the user session.
	 *
	 * @return the session instance
	 */
	protected HttpSession getSession() {
		return RequestContextHolder.currentRequestAttributes().session
	}
}
