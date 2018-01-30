/*
 * AbstractGoogleSync.groovy
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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
import com.google.gdata.client.GoogleService
import groovy.transform.CompileStatic
import org.amcworld.springcrm.ConfigService
import org.amcworld.springcrm.GoogleDataSyncStatus
import org.amcworld.springcrm.GoogleOAuthService
import org.amcworld.springcrm.Organization
import org.amcworld.springcrm.User
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.grails.datastore.gorm.GormEntity
import org.springframework.context.MessageSource


/**
 * The class {@code AbstractGoogleSync} synchronizes local data entries with Google API
 * (former: Google Data).
 *
 * @param <E>   the type of local domain models which are handled by this
 *              service
 * @param <G>   the type of Google entries which are handled by this service
 * @author      Daniel Ellermann
 * @version     2.1
 * @since       1.0
 */
abstract class AbstractGoogleSync<E extends GormEntity<E>, G>
    implements GoogleSync
{

    //-- Constants ------------------------------

    /**
     * The identifier for this application used in connections to Google.
     */
    protected static final String APPLICATION_NAME = 'amcworld-springcrm-2.0'

    private static final Log log = LogFactory.getLog(this)


    //-- Fields ---------------------------------

    ConfigService configService

    /**
     * The OAuth service to authenticate at Google.
     */
    GoogleOAuthService googleOAuthService   // injected

    /**
     * The message resource used to obtain localized strings.
     */
    MessageSource messageSource             // injected

    /**
     * The type of the local entries.
     */
    protected Class<E> localEntryClass


    //-- Constructors ---------------------------

    /**
     * Creates a new Google synchronization instance for the given type of
     * local entries.
     *
     * @param localEntryClass   the class representing type of the local
     *                          entries
     */
    AbstractGoogleSync(Class<E> localEntryClass) {
        this.localEntryClass = localEntryClass
    }


    //-- Public methods -------------------------

    /**
     * Synchronizes local entries with Google ones for the given user.  If the
     * user has no credential information stored no synchronization will be
     * performed.
     *
     * @param user                  the given user
     * @throws GoogleAuthException  if the user currently is not authenticated
     *                              at Google
     */
    void sync(User user) throws GoogleAuthException {
        Credential credential = loadCredential(user.username)
        if (credential == null) {
            return
        }

        Organization.withTransaction {
            GoogleService service = getService(credential)
            Map<Long, E> localEntries = getLocalEntries()
            Map<String, G> googleEntries = loadGoogleEntries(service)
            List<GoogleDataSyncStatus> syncEntries =
                GoogleDataSyncStatus.findAllByType(localEntryClass.name)
            SyncSource syncSource = getPrimarySyncSource(user)

            /* update and delete entries */
            log.debug 'Start syncing with Google, part I (L = local entry, G = Google entry, S = sync source)'
            for (GoogleDataSyncStatus status : syncEntries) {
                try {
                    Long id = status.itemId
                    String url = status.url
                    E localEntry = localEntries[id]
                    G googleEntry = googleEntries.get(url)

                    if (localEntry && !isExcluded(localEntry, user)) {
                        if (localEntry.lastUpdated > status.lastSync) {
                            if (!googleEntry) {
                                if (syncSource != LOCAL) {
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
                                if (syncSource == LOCAL) {
                                    if (log.debugEnabled) {
                                        log.debug "L modified, G modified, S = local → update G: L = ${localEntry}, G = ${googleEntryToString(googleEntry)}"
                                    }
                                    syncUpdateGoogle service, localEntry, googleEntry
                                    status.updateToCurrent getEtag(googleEntry)
                                    status.save flush: true
                                } else {
                                    if (log.debugEnabled) {
                                        log.debug "L modified, G modified, S = remote → update L: L = ${localEntry}, G = ${googleEntryToString(googleEntry)}"
                                    }
                                    if (allowLocalModify) {
                                        syncUpdateLocal service, localEntry, googleEntry
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
                                syncUpdateGoogle service, localEntry, googleEntry
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
                                    syncUpdateLocal service, localEntry, googleEntry
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
                            || (syncSource == LOCAL))
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
                if (isExcluded(localEntry, user)) {
                    continue
                }

                try {
                    if (log.debugEnabled) {
                        log.debug "L exists, G doesn't exist → create G: L = ${localEntry}"
                    }
                    G googleEntry = syncInsertGoogle(service, localEntry)
                    insertStatus user, localEntry, googleEntry
                } catch (RecoverableGoogleSyncException ignored) { /* ignore */ }
            }
            log.debug 'Start syncing with Google, part III (L = local entry, G = Google entry)'
            if (allowLocalCreate) {
                for (G googleEntry : googleEntries.values()) {
                    try {
                        if (log.debugEnabled) {
                            log.debug "L doesn't exist, G exists → create L: G = ${googleEntryToString(googleEntry)}"
                        }
                        E localEntry = syncInsertLocal(service, googleEntry)
                        insertStatus user, localEntry, googleEntry
                    } catch (RecoverableGoogleSyncException ignored) { /* ignore */ }
                }
            } else if (log.debugEnabled && googleEntries.size()) {
                log.debug "    Creating local entries denied by administrator (${googleEntries.size()} entries)."
            }
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
     * @param service                           the underlying Google service
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
    protected abstract E convertToLocal(GoogleService service, E localEntry,
                                        G googleEntry)
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

        (GoogleDataSyncStatus) c.get {
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
    @CompileStatic
    protected boolean getBooleanSystemConfig(String key,
                                             boolean defaultValue = false)
    {
        Boolean b = configService.getBoolean(key)

        b == null ? defaultValue : b
    }

    /**
     * Gets the ETag of the given Google entry.
     *
     * @param entry the given entry
     * @return      the ETag
     */
    protected abstract String getEtag(G entry)

    /**
     * Obtains a map containing the IDs and the local entries of the element
     * type.
     *
     * @return  the local entries
     * @since   2.0
     */
    private Map<Long, E> getLocalEntries() {
        Map<Long, E> entries = [: ]
        localEntryClass.'list'().each {
            E entry -> entries[(Long) entry.ident()] = entry
        }

        entries
    }

    /**
     * Gets the primary synchronization source as defined by the given user.
     *
     * @param user  the given user
     * @return      the primary synchronization source
     */
    @CompileStatic
    private SyncSource getPrimarySyncSource(User user) {
        String s = user.settings['primarySyncSource' + localEntryClass.name]
        (s == null) ? LOCAL : SyncSource.valueOf(SyncSource, s)
    }

    /**
     * Gets the service to exchange data with Google.
     *
     * @param credential    the credential used for authorization
     * @return              the Google service
     * @since               2.0
     */
    protected abstract GoogleService getService(Credential credential)

    /**
     * Gets the URL of the given Google entry.
     *
     * @param entry the given entry
     * @return      the URL
     */
    protected abstract String getUrl(G entry)

    /**
     * Checks whether or not the given Google entry has been changed since the
     * last synchronization.
     *
     * @param entry the given entry
     * @param etag  the ETag of the entry from the last synchronization
     * @return      {@code true} if the entry has changed; {@code false}
     *              otherwise
     */
    @CompileStatic
    private boolean hasChanged(G entry, String etag) {
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
     * @param service   the underlying Google service
     * @param entry     the entry to insert
     * @return          the inserted entry
     */
    protected abstract G insertGoogleEntry(GoogleService service, G entry)

    /**
     * Records the given local entry and the associated Google entry in the
     * synchronization status table.
     *
     * @param user          the user who inserts the entry
     * @param localEntry    the item to record
     * @param googleEntry   the associated Google entry
     */
    private void insertStatus(User user, E localEntry, G googleEntry) {
        def status = new GoogleDataSyncStatus(
            user: user, type: localEntryClass.name,
            itemId: (Long) localEntry.ident(),
            url: getUrl(googleEntry), etag: getEtag(googleEntry)
        )
        status.save flush: true
    }

    /**
     * Determines whether or not the given local entry should be excluded from
     * synchronization.
     *
     * @param localEntry    the local entry which should be checked
     * @param user          the user who synchronizes
     * @return              {@code true} to exclude the local entry from
     *                      synchronization; {@code false} otherwise
     * @since               2.0
     */
    protected abstract boolean isExcluded(E localEntry, User user)

    /**
     * Loads the OAuth2 credential of the given user name.
     *
     * @param username              the given user name
     * @return                      the credential
     * @throws GoogleAuthException  if the user currently is not authenticated
     *                              at Google
     */
    @CompileStatic
    private Credential loadCredential(String username) {
        Credential credential = googleOAuthService.loadCredential(username)
        if (!credential) {
            throw new GoogleAuthException(
                'error.googleAuthException.message.noCredentials'
            )
        }

        credential
    }

    /**
     * Loads the Google entries.
     *
     * @param service   the underlying Google service
     * @return          a map containing the Google entry URL as key and the
     *                  entry itself as value
     */
    protected abstract Map<String, G> loadGoogleEntries(GoogleService service)

    /**
     * Deletes the given Google entry.
     *
     * @param googleEntry   the Google entry to delete
     */
    @CompileStatic
    private void syncDeleteGoogle(G googleEntry) {
        deleteGoogleEntry googleEntry
    }

    /**
     * Deletes the given local entry.
     *
     * @param localEntry    the local entry to delete
     */
    private static void syncDeleteLocal(E localEntry) {
        localEntry.delete flush: true
    }

    /**
     * Creates (inserts) the given Google entry.
     *
     * @param service       the underlying Google service
     * @param googleEntry   the Google entry to create
     * @return              a reference to the created Google entry
     */
    @CompileStatic
    protected G syncInsertGoogle(GoogleService service, E localEntry) {
        insertGoogleEntry service, convertToGoogle(localEntry)
    }

    /**
     * Creates (inserts) the given local entry.
     *
     * @param service       the underlying Google service
     * @param localEntry    the local entry to create
     * @return              a reference to the created local entry
     */
    protected E syncInsertLocal(GoogleService service, G googleEntry) {
        E localEntry = localEntryClass.newInstance()
        convertToLocal service, localEntry, googleEntry

        localEntry.save flush: true
    }

    /**
     * Updates the given Google entry.
     *
     * @param service       the underlying Google service
     * @param googleEntry   the Google entry to update
     */
    @CompileStatic
    protected void syncUpdateGoogle(GoogleService service, E localEntry,
                                    G googleEntry)
    {
        convertToGoogle localEntry, googleEntry
        updateGoogleEntry service, googleEntry
    }

    /**
     * Updates the given local entry.
     *
     * @param localEntry    the local entry to update
     */
    protected void syncUpdateLocal(GoogleService service, E localEntry,
                                   G googleEntry)
    {
        convertToLocal service, localEntry, googleEntry
        localEntry.save flush: true
    }

    /**
     * Updates the given Google entry.
     *
     * @param service   the underlying Google service
     * @param entry     the entry to update
     */
    protected abstract void updateGoogleEntry(GoogleService service, G entry)
}
