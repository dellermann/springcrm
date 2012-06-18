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

import static org.amcworld.springcrm.google.SyncSource.LOCAL

import com.google.api.client.auth.oauth2.Credential
import org.amcworld.springcrm.google.GoogleAuthException;
import org.amcworld.springcrm.google.RecoverableGoogleSyncException
import org.amcworld.springcrm.google.SyncSource


/**
 * The class {@code GoogleDataService} represents a service which is able to
 * exchange data with Google API (former: Google Data).
 *
 * @author      Daniel Ellermann
 * @version     1.0
 * @param <E>   the type of local domain models which are handled by this
 *              service
 * @param <G>   the type of Google entries which are handled by this service
 */
abstract class GoogleDataService<E, G>
    extends org.amcworld.springcrm.GoogleService
{

	//-- Constants ------------------------------

    /**
     * The identifier for this application used in connections to Google.
     */
	protected static final String APPLICATION_NAME = 'amcworld-springcrm-1.0'


	//-- Class variables ------------------------

	static scope = 'session'
    static transactional = false


	//-- Instance variables ---------------------

    def googleOAuthService
    protected Class<E> localEntryClass


	//-- Public methods -------------------------

	/**
	 * Marks the given local entry as deleted in the synchronization status
	 * table.  It will be deleted during the next Google synchronization.
	 *
	 * @param localEntry   the local entry to mark as deleted
	 */
	void markDeleted(E localEntry) {
		def status = findSyncStatus(localEntry)
		if (status) {
			status.deleted = true
			status.save(flush: true)
		}
	}

    /**
     * Synchronizes local entries with Google ones.
     *
     * @since 1.0
     */
    void sync() {
        Map<Long, E> localEntries = [: ]
        log.debug('Class E: ' + localEntryClass?.dump())
        localEntryClass.'list'().each { localEntries[it.ident()] = it }
        Map<String, G> googleEntries = loadGoogleEntries()
        List<GoogleDataSyncStatus> syncEntries =
            GoogleDataSyncStatus.findAllByType(localEntryClass.name)

        /* update and delete entries */
        log.debug('Start syncing with Google, part I (L = local entry, G = Google entry, S = sync source)')
        for (GoogleDataSyncStatus status : syncEntries) {
            try {
                Long id = status.itemId
                String url = status.url
                G googleEntry = googleEntries.get(url)
                if (status.deleted) {
                    if (googleEntry && !hasChanged(googleEntry, status.etag)
                        || (primarySyncSource == LOCAL))
                    {
                        log.debug "L deleted, G unmodified or S = local → delete G: G = ${googleEntry}"
                        syncDeleteGoogle(googleEntry)
                        googleEntries.remove(url)
                    } else if (log.debugEnabled) {
                        if (!googleEntry) {
                            log.debug 'L deleted, G deleted → nothing to do'
                        } else if (hasChanged(googleEntry, status.etag)) {
                            log.debug "L deleted, G modified, S = remote → leave in L2 for later local recreation: G = ${googleEntry}"
                        }
                    }
                    status.delete(flush: true)
                } else {
                    E localEntry = localEntries[id]
                    if (localEntry.lastUpdated > status.lastSync) {
                        if (!googleEntry) {
                            if (primarySyncSource != LOCAL) {
                                log.debug "L modified, G deleted, S = remote → delete L: L = ${localEntry}"
                                syncDeleteLocal(localEntry)
                                localEntries.remove(id)
                            } else if (log.debugEnabled) {
                                log.debug "L modified, G deleted, S = local → leave in L1 for later recreation at Google: L = ${localEntry}"
                            }
                            status.delete(flush: true)
                        } else if (hasChanged(googleEntry, status.etag)) {
                            if (primarySyncSource == LOCAL) {
                                log.debug "L modified, G modified, S = local → update G: L = ${localEntry}, G = ${googleEntry}"
                                syncUpdateGoogle(localEntry, googleEntry)
                            } else {
                                log.debug "L modified, G modified, S = remote → update L: L = ${localEntry}, G = ${googleEntry}"
                                syncUpdateLocal(localEntry, googleEntry)
                            }
                            status.updateToCurrent(getEtag(googleEntry))
                            status.save(flush: true)
                            localEntries.remove(id)
                            googleEntries.remove(url)
                        } else {
                            log.debug "L modified, G unmodified → update G: L = ${localEntry}, G = ${googleEntry}"
                            syncUpdateGoogle(localEntry, googleEntry)
                            status.updateToCurrent(getEtag(googleEntry))
                            status.save(flush: true)
                            localEntries.remove(id)
                            googleEntries.remove(url)
                        }
                    } else {
                        if (!googleEntry) {
                            log.debug "L unmodified, G deleted → delete L: L = ${localEntry}"
                            syncDeleteLocal(localEntry)
                            localEntries.remove(id)
                            status.delete(flush: true)
                        } else if (hasChanged(googleEntry, status.etag)) {
                            log.debug "L unmodified, G modified → update L: L = ${localEntry}, G = ${googleEntry}"
                            syncUpdateLocal(localEntry, googleEntry)
                            status.updateToCurrent(getEtag(googleEntry))
                            status.save(flush: true)
                            localEntries.remove(id)
                            googleEntries.remove(url)
                        } else {
                            log.debug "L unmodified, G unmodified → nothing to do: L = ${localEntry}, G = ${googleEntry}"
                            localEntries.remove(id)
                            googleEntries.remove(url)
                        }
                    }
                }
            } catch (RecoverableGoogleSyncException e) { /* ignore */ }
        }

        /* create new entries */
        log.debug('Start syncing with Google, part II (L = local entry, G = Google entry)')
        for (E localEntry : localEntries.values()) {
            try {
                log.debug "L exists, G doesn't exist → create G: L = ${localEntry}"
                G googleEntry = syncInsertGoogle(localEntry)
                insertStatus(localEntry, googleEntry)
            } catch (RecoverableGoogleSyncException e) { /* ignore */ }
        }
        log.debug('Start syncing with Google, part III (L = local entry, G = Google entry)')
        for (G googleEntry : googleEntries.values()) {
            try {
                log.debug "L doesn't exist, G exists → create L: G = ${googleEntry}"
                E localEntry = syncInsertLocal(googleEntry)
                insertStatus(localEntry, googleEntry)
            } catch (RecoverableGoogleSyncException e) { /* ignore */ }
        }
    }


	//-- Non-public methods ---------------------

    /**
     * Converts the given local entry to a Google entry.
     *
     * @param localEntry                        the given local entry
     * @param googleEntry                       a Google entry which is to fill
     *                                          with the property values; if
     *                                          {@code null} a new Google entry
     *                                          is to create
     * @return                                  the converted Google entry
     * @throws RecoverableGoogleSyncException   if a mandatory value in the
     *                                          local entry is missing or
     *                                          malformed
     */
    protected abstract G convertToGoogle(E localEntry, G googleEntry = null)
        throws RecoverableGoogleSyncException

    /**
     * Converts the given Google entry to a local entry.
     *
     * @param localEntry                        the local entry to be filled
     *                                          with the converted properties
     * @param googleEntry                       the given Google entry
     * @return                                  a reference to the given local
     *                                          entry in parameter
     *                                          {@code localEntry}
     * @throws RecoverableGoogleSyncException   if a mandatory value in the
     *                                          Google entry is missing or
     *                                          malformed
     */
    protected abstract E convertToLocal(E localEntry, G googleEntry)
        throws RecoverableGoogleSyncException

    /**
     * Deletes the given Google entry.
     *
     * @param entry the entry to delete
     * @since       1.0
     */
    protected abstract void deleteGoogleEntry(G entry)

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
			eq('user', user)
			eq('type', item.class.name)
			eq('itemId', item.id)
		}
	}

    /**
     * Gets the ETag of the given Google entry.
     *
     * @param entry the given entry
     * @return      the ETag
     * @since       1.0
     */
    protected abstract String getEtag(G entry)

    /**
     * Gets the URL of the given Google entry.
     *
     * @param entry the given entry
     * @return      the URL
     * @since       1.0
     */
    protected abstract String getUrl(G entry)

    /**
     * Gets the primary synchronization source as defined by the user.
     *
     * @return  the primary synchronization source
     * @since   1.0
     */
    protected SyncSource getPrimarySyncSource() {
        String s = user.settings['primarySyncSource' + localEntryClass.name]
        return (s == null) ? LOCAL : SyncSource.valueOf(SyncSource, s)
    }

    /**
     * Checks whether or not the given Google entry has been changed since the
     * last synchronization.
     *
     * @param entry the given entry
     * @param etag  the ETag of the entry from the last synchronization
     * @return      {@code true} if the entry has changed; {@code false}
     *              otherwise
     * @since       1.0
     */
    protected boolean hasChanged(G entry, String etag) {
        return getEtag(entry) != etag
    }

    /**
     * Inserts the given Google entry.
     *
     * @param entry the entry to insert
     * @return      the inserted entry
     * @since       1.0
     */
    protected abstract G insertGoogleEntry(G entry)

    /**
     * Records the given local entry and the associated Google entry in the
     * synchronization status table.
     *
     * @param localEntry   the item to record
     * @param googleEntry  the associated Google entry
     * @since               1.0
     */
    protected void insertStatus(E localEntry, G googleEntry) {
        def status = new GoogleDataSyncStatus(
            user: user, type: localEntryClass.name, itemId: localEntry.ident(),
            url: getUrl(googleEntry), etag: getEtag(googleEntry)
        )
        status.save(flush: true)
    }

    /**
     * Loads the OAuth2 credential of the currently logged in user.
     *
     * @return                      the credential
     * @throws GoogleAuthException  if the user currently is not authenticated
     *                              at Google
     * @since                       1.0
     */
    protected Credential loadCredential() {
        Credential credential = googleOAuthService.loadCredential()
        if (!credential) {
            throw new GoogleAuthException('error.googleAuthException.message.noCredentials')
        }
        return credential
    }

    /**
     * Loads the Google entries.
     *
     * @return  a map containing the Google entry URL as key and the entry
     *          itself as value
     * @since   1.0
     */
    protected abstract Map<String, G> loadGoogleEntries()

    /**
     * Deletes the given Google entry.
     *
     * @param googleEntry   the Google entry to delete
     * @since               1.0
     */
    protected void syncDeleteGoogle(G googleEntry) {
        deleteGoogleEntry(googleEntry)
    }

    /**
     * Deletes the given local entry.
     *
     * @param localEntry    the local entry to delete
     * @since               1.0
     */
    protected void syncDeleteLocal(E localEntry) {
        localEntry.delete(flush: true)
    }

    /**
     * Creates (inserts) the given Google entry.
     *
     * @param googleEntry   the Google entry to create
     * @return              a reference to the created Google entry
     * @since               1.0
     */
    protected G syncInsertGoogle(E localEntry) {
        return insertGoogleEntry(convertToGoogle(localEntry))
    }

    /**
     * Creates (inserts) the given local entry.
     *
     * @param localEntry    the local entry to create
     * @return              a reference to the created local entry
     * @since               1.0
     */
    protected E syncInsertLocal(G googleEntry) {
        E localEntry = localEntryClass.newInstance()
        convertToLocal(localEntry, googleEntry)
        localEntry.save(flush: true)
        return localEntry
    }

    /**
     * Updates the given Google entry.
     *
     * @param googleEntry   the Google entry to update
     * @since               1.0
     */
    protected void syncUpdateGoogle(E localEntry, G googleEntry) {
        convertToGoogle(localEntry, googleEntry)
        updateGoogleEntry(googleEntry)
    }

    /**
     * Updates the given local entry.
     *
     * @param localEntry    the local entry to update
     * @since               1.0
     */
    protected void syncUpdateLocal(E localEntry, G googleEntry) {
        convertToLocal(localEntry, googleEntry)
        localEntry.save(flush: true)
    }

    /**
     * Updates the given Google entry.
     *
     * @param entry the entry to update
     * @since       1.0
     */
    protected abstract void updateGoogleEntry(G entry)
}
