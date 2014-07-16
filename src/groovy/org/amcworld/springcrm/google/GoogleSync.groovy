/*
 * GoogleSync.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


package org.amcworld.springcrm.google

import static org.amcworld.springcrm.google.SyncSource.LOCAL
import com.google.api.client.auth.oauth2.Credential
import org.amcworld.springcrm.Config
import org.amcworld.springcrm.ConfigHolder
import org.amcworld.springcrm.GoogleDataSyncStatus
import org.amcworld.springcrm.User
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory


/**
 * The class {@code GoogleSync} synchronizes local data entries with Google API
 * (former: Google Data).
 *
 * @param <E>   the type of local domain models which are handled by this
 *              service
 * @param <G>   the type of Google entries which are handled by this service
 * @author      Daniel Ellermann
 * @version     1.4
 * @since       1.0
 */
abstract class GoogleSync<E, G> implements GoogleService {

    //-- Constants ------------------------------

    /**
     * The identifier for this application used in connections to Google.
     */
    protected static final String APPLICATION_NAME = 'amcworld-springcrm-1.0'

    private static final Log log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    def googleOAuthService              // injected
    def messageSource                   // injected
    def String userName
    protected Class<E> localEntryClass


    //-- Constructors ---------------------------

    /**
     * Creates a new Google synchronization instance for the given type of
     * local entries.
     *
     * @param localEntryClass   the class representing type of the local
     *                          entries
     */
    GoogleSync(Class<E> localEntryClass) {
        this.localEntryClass = localEntryClass
    }


    //-- Public methods -------------------------

    /**
     * Synchronizes local entries with Google ones.
     */
    void sync() {
        Map<Long, E> localEntries = [: ]
        localEntryClass.'list'().each { localEntries[it.ident()] = it }
        Map<String, G> googleEntries = loadGoogleEntries()
        List<GoogleDataSyncStatus> syncEntries =
            GoogleDataSyncStatus.findAllByType(localEntryClass.name)

        /* update and delete entries */
        log.debug 'Start syncing with Google, part I (L = local entry, G = Google entry, S = sync source)'
        for (GoogleDataSyncStatus status : syncEntries) {
            try {
                Long id = status.itemId
                String url = status.url
                E localEntry = localEntries[id]
                G googleEntry = googleEntries.get(url)
                if (localEntry) {
                    if (localEntry.lastUpdated > status.lastSync) {
                        if (!googleEntry) {
                            if (primarySyncSource != LOCAL) {
                                if (log.debugEnabled) {
                                    log.debug "L modified, G deleted, S = remote → delete L: L = ${localEntry}"
                                }
                                if (allowLocalDelete) {
                                    syncDeleteLocal localEntry
                                    status.delete flush: true
                                } else if (log.debugEnabled) {
                                    log.debug '    Deleting local entries denied by administrator.'
                                }
                                localEntries.remove id
                            } else if (log.debugEnabled) {
                                log.debug "L modified, G deleted, S = local → leave in L1 for later recreation at Google: L = ${localEntry}"
                                status.delete flush: true
                            }
                        } else if (hasChanged(googleEntry, status.etag)) {
                            if (primarySyncSource == LOCAL) {
                                if (log.debugEnabled) {
                                    log.debug "L modified, G modified, S = local → update G: L = ${localEntry}, G = ${googleEntryToString(googleEntry)}"
                                }
                                syncUpdateGoogle(localEntry, googleEntry)
                                status.updateToCurrent getEtag(googleEntry)
                                status.save flush: true
                            } else {
                                if (log.debugEnabled) {
                                    log.debug "L modified, G modified, S = remote → update L: L = ${localEntry}, G = ${googleEntryToString(googleEntry)}"
                                }
                                if (allowLocalModify) {
                                    syncUpdateLocal localEntry, googleEntry
                                    status.updateToCurrent getEtag(googleEntry)
                                    status.save flush: true
                                } else if (log.debugEnabled) {
                                    log.debug '    Modifying local entries denied by administrator.'
                                }
                            }
                            localEntries.remove id
                            googleEntries.remove url
                        } else {
                            if (log.debugEnabled) {
                                log.debug "L modified, G unmodified → update G: L = ${localEntry}, G = ${googleEntryToString(googleEntry)}"
                            }
                            syncUpdateGoogle localEntry, googleEntry
                            status.updateToCurrent getEtag(googleEntry)
                            status.save flush: true
                            localEntries.remove id
                            googleEntries.remove url
                        }
                    } else {
                        if (!googleEntry) {
                            if (log.debugEnabled) {
                                log.debug "L unmodified, G deleted → delete L: L = ${localEntry}"
                            }
                            if (allowLocalDelete) {
                                syncDeleteLocal localEntry
                                status.delete flush: true
                            } else if (log.debugEnabled) {
                                log.debug '    Deleting local entries denied by administrator.'
                            }
                            localEntries.remove id
                        } else if (hasChanged(googleEntry, status.etag)) {
                            if (log.debugEnabled) {
                                log.debug "L unmodified, G modified → update L: L = ${localEntry}, G = ${googleEntryToString(googleEntry)}"
                            }
                            if (allowLocalModify) {
                                syncUpdateLocal localEntry, googleEntry
                                status.updateToCurrent getEtag(googleEntry)
                                status.save flush: true
                            } else if (log.debugEnabled) {
                                log.debug '    Modifying local entries denied by administrator.'
                            }
                            localEntries.remove id
                            googleEntries.remove url
                        } else {
                            if (log.debugEnabled) {
                                log.debug "L unmodified, G unmodified → nothing to do: L = ${localEntry}, G = ${googleEntryToString(googleEntry)}"
                            }
                            localEntries.remove id
                            googleEntries.remove url
                        }
                    }
                } else {
                    if (googleEntry && !hasChanged(googleEntry, status.etag)
                        || (primarySyncSource == LOCAL))
                    {
                        if (log.debugEnabled) {
                            log.debug "L deleted, G unmodified or S = local → delete G: G = ${googleEntryToString(googleEntry)}"
                        }
                        if (googleEntry) {
                            syncDeleteGoogle googleEntry
                        }
                        googleEntries.remove url
                    } else if (log.debugEnabled) {
                        if (!googleEntry) {
                            log.debug 'L deleted, G deleted → nothing to do'
                        } else if (hasChanged(googleEntry, status.etag)) {
                            log.debug "L deleted, G modified, S = remote → leave in L2 for later local recreation: G = ${googleEntryToString(googleEntry)}"
                        }
                    }
                    status.delete flush: true
                }
            } catch (RecoverableGoogleSyncException ignored) { /* ignore */ }
        }

        /* create new entries */
        log.debug 'Start syncing with Google, part II (L = local entry, G = Google entry)'
        for (E localEntry : localEntries.values()) {
            try {
                if (log.debugEnabled) {
                    log.debug "L exists, G doesn't exist → create G: L = ${localEntry}"
                }
                G googleEntry = syncInsertGoogle(localEntry)
                insertStatus(localEntry, googleEntry)
            } catch (RecoverableGoogleSyncException ignored) { /* ignore */ }
        }
        log.debug 'Start syncing with Google, part III (L = local entry, G = Google entry)'
        if (allowLocalCreate) {
            for (G googleEntry : googleEntries.values()) {
                try {
                    if (log.debugEnabled) {
                        log.debug "L doesn't exist, G exists → create L: G = ${googleEntryToString(googleEntry)}"
                    }
                    E localEntry = syncInsertLocal(googleEntry)
                    insertStatus(localEntry, googleEntry)
                } catch (RecoverableGoogleSyncException ignored) { /* ignore */ }
            }
        } else if (log.debugEnabled && googleEntries.size()) {
            log.debug "    Creating local entries denied by administrator (${googleEntries.size()} entries)."
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
        c.get {
            eq('user', user)
            eq('type', item.class.name)
            eq('itemId', item.id)
        }
    }

    /**
     * Returns whether or not local entries can be created during
     * synchronization.
     *
     * @return  {@code true} if creating local entries is allowed;
     *          {@code false} otherwise
     */
    protected abstract boolean getAllowLocalCreate()

    /**
     * Returns whether or not local entries can be deleted during
     * synchronization.
     *
     * @return  {@code true} if deleting local entries is allowed;
     *          {@code false} otherwise
     */
    protected abstract boolean getAllowLocalDelete()

    /**
     * Returns whether or not local entries can be modified during
     * synchronization.
     *
     * @return  {@code true} if modifying local entries is allowed;
     *          {@code false} otherwise
     */
    protected abstract boolean getAllowLocalModify()

    /**
     * Gets the boolean system configuration value with the given key.
     *
     * @param key           the given key
     * @param defaultValue  the default value if no configuration with the
     *                      given key exists
     * @return              the configuration value
     */
    protected boolean getBooleanSystemConfig(String key,
                                             boolean defaultValue = false)
    {
        Config config = getSystemConfig(key)
        (config == null) ? defaultValue : (config as Boolean)
    }

    /**
     * Gets the ETag of the given Google entry.
     *
     * @param entry the given entry
     * @return      the ETag
     */
    protected abstract String getEtag(G entry)

    /**
     * Gets the primary synchronization source as defined by the user.
     *
     * @return  the primary synchronization source
     */
    protected SyncSource getPrimarySyncSource() {
        String s = user.settings['primarySyncSource' + localEntryClass.name]
        (s == null) ? LOCAL : SyncSource.valueOf(SyncSource, s)
    }

    /**
     * Gets the system configuration object with the given key.
     *
     * @param key   the given key
     * @return      the configuration object; {@code null} if no configuration
     *              for the given key exists
     */
    protected Config getSystemConfig(String key) {
        ConfigHolder.instance.getConfig(key)
    }

    /**
     * Gets the URL of the given Google entry.
     *
     * @param entry the given entry
     * @return      the URL
     */
    protected abstract String getUrl(G entry)

    /**
     * Gets the user instance for the user name specified in property
     * {@code userName}.
     *
     * @return  the user instance; {@code null} if no user with the user name
     *          exists
     */
    protected User getUser() {
        User.findByUserName userName
    }

    /**
     * Checks whether or not the given Google entry has been changed since the
     * last synchronization.
     *
     * @param entry the given entry
     * @param etag  the ETag of the entry from the last synchronization
     * @return      {@code true} if the entry has changed; {@code false}
     *              otherwise
     */
    protected boolean hasChanged(G entry, String etag) {
        getEtag(entry) != etag
    }

    /**
     * Converts the given Google entry to a string.  The method is needed
     * because the classes representing the Google entries don't return a
     * meaningful value when calling {@code toString()}.
     *
     * @param entry the given Google entry
     * @return      the string representation of the entry
     */
    protected abstract String googleEntryToString(G entry)

    /**
     * Inserts the given Google entry.
     *
     * @param entry the entry to insert
     * @return      the inserted entry
     */
    protected abstract G insertGoogleEntry(G entry)

    /**
     * Records the given local entry and the associated Google entry in the
     * synchronization status table.
     *
     * @param localEntry   the item to record
     * @param googleEntry  the associated Google entry
     */
    protected void insertStatus(E localEntry, G googleEntry) {
        def status = new GoogleDataSyncStatus(
            user: user, type: localEntryClass.name, itemId: localEntry.ident(),
            url: getUrl(googleEntry), etag: getEtag(googleEntry)
        )
        status.save flush: true
    }

    /**
     * Loads the OAuth2 credential of the user defined in the constructor.
     *
     * @return                      the credential
     * @throws GoogleAuthException  if the user currently is not authenticated
     *                              at Google
     */
    protected Credential loadCredential() {
        Credential credential = googleOAuthService.loadCredential(userName)
        if (!credential) {
            throw new GoogleAuthException('error.googleAuthException.message.noCredentials')
        }
        credential
    }

    /**
     * Loads the Google entries.
     *
     * @return  a map containing the Google entry URL as key and the entry
     *          itself as value
     */
    protected abstract Map<String, G> loadGoogleEntries()

    /**
     * Deletes the given Google entry.
     *
     * @param googleEntry   the Google entry to delete
     */
    protected void syncDeleteGoogle(G googleEntry) {
        deleteGoogleEntry googleEntry
    }

    /**
     * Deletes the given local entry.
     *
     * @param localEntry    the local entry to delete
     */
    protected void syncDeleteLocal(E localEntry) {
        localEntry.delete flush: true
    }

    /**
     * Creates (inserts) the given Google entry.
     *
     * @param googleEntry   the Google entry to create
     * @return              a reference to the created Google entry
     */
    protected G syncInsertGoogle(E localEntry) {
        insertGoogleEntry(convertToGoogle(localEntry))
    }

    /**
     * Creates (inserts) the given local entry.
     *
     * @param localEntry    the local entry to create
     * @return              a reference to the created local entry
     */
    protected E syncInsertLocal(G googleEntry) {
        E localEntry = localEntryClass.newInstance()
        convertToLocal localEntry, googleEntry
        localEntry.save flush: true
        localEntry
    }

    /**
     * Updates the given Google entry.
     *
     * @param googleEntry   the Google entry to update
     */
    protected void syncUpdateGoogle(E localEntry, G googleEntry) {
        convertToGoogle localEntry, googleEntry
        updateGoogleEntry googleEntry
    }

    /**
     * Updates the given local entry.
     *
     * @param localEntry    the local entry to update
     */
    protected void syncUpdateLocal(E localEntry, G googleEntry) {
        convertToLocal localEntry, googleEntry
        localEntry.save flush: true
    }

    /**
     * Updates the given Google entry.
     *
     * @param entry the entry to update
     */
    protected abstract void updateGoogleEntry(G entry)
}
