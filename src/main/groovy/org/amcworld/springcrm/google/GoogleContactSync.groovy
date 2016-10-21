/*
 * GoogleContactSync.groovy
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

import com.google.api.client.auth.oauth2.Credential
import com.google.gdata.client.GoogleService
import com.google.gdata.client.Query
import com.google.gdata.client.Service.GDataRequest
import com.google.gdata.client.contacts.ContactsService
import com.google.gdata.data.Link
import com.google.gdata.data.contacts.*
import com.google.gdata.data.extensions.City
import com.google.gdata.data.extensions.Country
import com.google.gdata.data.extensions.Email
import com.google.gdata.data.extensions.FamilyName
import com.google.gdata.data.extensions.FullName
import com.google.gdata.data.extensions.GivenName
import com.google.gdata.data.extensions.Name
import com.google.gdata.data.extensions.NamePrefix
import com.google.gdata.data.extensions.OrgDepartment
import com.google.gdata.data.extensions.OrgJobDescription
import com.google.gdata.data.extensions.OrgName
import com.google.gdata.data.extensions.Organization as GDataOrg
import com.google.gdata.data.extensions.PhoneNumber
import com.google.gdata.data.extensions.PoBox
import com.google.gdata.data.extensions.PostCode
import com.google.gdata.data.extensions.Region
import com.google.gdata.data.extensions.Street
import com.google.gdata.data.extensions.StructuredPostalAddress
import com.google.gdata.util.ContentType
import com.google.gdata.util.InvalidEntryException
import com.google.gdata.util.PreconditionFailedException
import com.google.gdata.util.ResourceNotFoundException
import com.google.gdata.util.ServiceForbiddenException
import groovy.transform.CompileStatic
import net.sf.jmimemagic.Magic
import org.amcworld.springcrm.Address
import org.amcworld.springcrm.Organization
import org.amcworld.springcrm.Person
import org.amcworld.springcrm.User
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code GoogleContactSync} synchronizes person records with Google.
 *
 * @author  Daniel Ellermann
 * @version 2.1
 * @since   1.0
 */
class GoogleContactSync extends AbstractGoogleSync<Person, ContactEntry> {

    //-- Constants ------------------------------

    protected static final URL FEED_URL =
        new URL('https://www.google.com/m8/feeds/contacts/default/full')

    private static final Log log = LogFactory.getLog(this)


    //-- Constructors ---------------------------

    /**
     * Creates a new Google synchronization instance for persons.
     */
    GoogleContactSync() {
        super(Person)
    }


    //-- Non-public methods ---------------------

    /**
     * Converts the given Google address to a local address object.
     *
     * @param localAddr the given local address
     * @param addr      the Google address
     * @since 2.0
     */
    @CompileStatic
    private static void convertToAddress(Address localAddr,
                                         StructuredPostalAddress addr)
    {
        localAddr.street = addr.street?.value
        localAddr.poBox = addr.pobox?.value
        localAddr.postalCode = addr.postcode?.value
        localAddr.location = addr.city?.value
        localAddr.state = addr.region?.value
        localAddr.country = addr.country?.value
    }

    @CompileStatic
    protected ContactEntry convertToGoogle(Person p,
                                           ContactEntry contact = null)
    {
        if (contact == null) {
            contact = new ContactEntry()
        }
        contact.getRepeatingExtension(GDataOrg).clear()
        contact.addOrganization(new GDataOrg(
            orgName: p.organization?.name ? new OrgName(p.organization?.name) : null,
            orgDepartment: p.department ? new OrgDepartment(p.department) : null,
            orgJobDescription: p.jobTitle ? new OrgJobDescription(p.jobTitle) : null,
            primary: true,
            rel: GDataOrg.Rel.WORK
        ))
        contact.name = new Name(
            givenName: new GivenName(p.firstName, null),
            familyName: new FamilyName(p.lastName, null),
            fullName: new FullName(p.toString(), null)
        )
        if (p.title) {
            contact.name.namePrefix = new NamePrefix(p.title)
        }
        contact.getRepeatingExtension(StructuredPostalAddress).clear()
        if (!p.mailingAddr.empty) {
            contact.addStructuredPostalAddress(new StructuredPostalAddress(
                street: p.mailingAddr.street ? new Street(p.mailingAddr.street) : null,
                pobox: p.mailingAddr.poBox ? new PoBox(p.mailingAddr.poBox) : null,
                postcode: p.mailingAddr.postalCode ? new PostCode(p.mailingAddr.postalCode) : null,
                city: p.mailingAddr.location ? new City(p.mailingAddr.location) : null,
                region: p.mailingAddr.state ? new Region(p.mailingAddr.state) : null,
                country: p.mailingAddr.country ? new Country(value: p.mailingAddr.country) : null,
                rel: StructuredPostalAddress.Rel.WORK
            ))
        }
        if (!p.otherAddr.empty) {
            contact.addStructuredPostalAddress(new StructuredPostalAddress(
                street: p.otherAddr.street ? new Street(p.otherAddr.street) : null,
                pobox: p.otherAddr.poBox ? new PoBox(p.otherAddr.poBox) : null,
                postcode: p.otherAddr.postalCode ? new PostCode(p.otherAddr.postalCode) : null,
                city: p.otherAddr.location ? new City(p.otherAddr.location) : null,
                region: p.otherAddr.state ? new Region(p.otherAddr.state) : null,
                country: p.otherAddr.country ? new Country(value: p.otherAddr.country) : null,
                rel: StructuredPostalAddress.Rel.OTHER
            ))
        }
        contact.getRepeatingExtension(PhoneNumber).clear()
        if (p.phone) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.phone,
                primary: true,
                rel: PhoneNumber.Rel.WORK
            ))
        }
        if (p.organization.phone && p.organization.phone != p.phone) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.organization.phone,
                primary: !p.phone,
                rel: PhoneNumber.Rel.COMPANY_MAIN
            ))
        }
        if (p.phoneHome) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.phoneHome,
                rel: PhoneNumber.Rel.HOME
            ))
        }
        if (p.mobile) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.mobile,
                rel: PhoneNumber.Rel.MOBILE
            ))
        }
        if (p.fax) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.fax,
                rel: PhoneNumber.Rel.WORK_FAX
            ))
        }
        if (p.organization.fax && p.organization.fax != p.fax) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.organization.fax,
                rel: PhoneNumber.Rel.FAX
            ))
        }
        if (p.phoneAssistant) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.phoneAssistant,
                rel: PhoneNumber.Rel.ASSISTANT
            ))
        }
        if (p.phoneOther) {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.phoneOther,
                rel: PhoneNumber.Rel.OTHER
            ))
        }
        if (p.organization.phoneOther
            && p.organization.phoneOther != p.phoneOther)
        {
            contact.addPhoneNumber(new PhoneNumber(
                phoneNumber: p.organization.phoneOther,
                label: orgOtherRelLabel
            ))
        }
        contact.getRepeatingExtension(Email).clear()
        if (p.email1) {
            contact.addEmailAddress(new Email(
                address: p.email1,
                primary: true,
                rel: Email.Rel.WORK
            ))
        }
        if (p.organization.email1 && p.organization.email1 != p.email1) {
            contact.addEmailAddress(new Email(
                address: p.organization.email1,
                label: orgRelLabel
            ))
        }
        if (p.email2) {
            contact.addEmailAddress(new Email(
                address: p.email2,
                rel: Email.Rel.OTHER
            ))
        }
        if (p.organization.email2 && p.organization.email2 != p.email2) {
            contact.addEmailAddress(new Email(
                address: p.organization.email2,
                label: orgOtherRelLabel
            ))
        }
        contact.getRepeatingExtension(Relation.class).clear()
        if (p.assistant) {
            contact.addRelation(new Relation(
                value: p.assistant,
                rel: Relation.Rel.ASSISTANT
            ))
        }
        if (p.birthday) {
            contact.birthday = new Birthday(p.birthday.format('yyyy-MM-dd'))
        }
        contact.getRepeatingExtension(Jot).clear()
        if (p.notes) {
            contact.addJot(new Jot(Jot.Rel.WORK, p.notes))
        }
        contact.getRepeatingExtension(Website).clear()
        if (p.organization.website) {
            contact.addWebsite(new Website(
                href: p.organization.website,
                primary: true,
                rel: Website.Rel.WORK
            ))
        }
        contact
    }

    @Override
    protected Person convertToLocal(GoogleService service, Person localEntry,
                                    ContactEntry googleEntry)
        throws RecoverableGoogleSyncException
    {
        List<GDataOrg> organizations = googleEntry.organizations
        assert !organizations.empty
        GDataOrg organization = organizations[0]
        Organization org = Organization.findByName(organization.orgName.value)
        assert org
        localEntry.organization = org

        Name name = googleEntry.name
        if (!name) {
            throw new RecoverableGoogleSyncException(
                'Name is mandatory for synchronization.'
            )
        }
        if (!name.hasGivenName()) {
            throw new RecoverableGoogleSyncException(
                'First name is mandatory for synchronization.'
            )
        }
        localEntry.firstName = name.givenName.value
        if (!name.hasFamilyName()) {
            throw new RecoverableGoogleSyncException(
                'Last name is mandatory for synchronization.'
            )
        }
        localEntry.lastName = name.familyName.value
        if (name.hasNamePrefix()) {
            localEntry.title = name.namePrefix.toString()
        }

        if (localEntry.mailingAddr == null) {
            localEntry.mailingAddr = new Address()
        } else {
            localEntry.mailingAddr.clear()
        }
        if (localEntry.otherAddr == null) {
            localEntry.otherAddr = new Address()
        } else {
            localEntry.otherAddr.clear()
        }
        boolean mailingAddrSet = false
        boolean otherAddrSet = false
        for (StructuredPostalAddress addr : googleEntry.structuredPostalAddresses)
        {
            if (!mailingAddrSet
                && (StructuredPostalAddress.Rel.WORK == addr.rel))
            {
                convertToAddress localEntry.mailingAddr, addr
                mailingAddrSet = true
            } else if (!otherAddrSet
                       && (StructuredPostalAddress.Rel.OTHER == addr.rel))
            {
                convertToAddress localEntry.otherAddr, addr
                otherAddrSet = true
            }
        }

        localEntry.phone = null
        localEntry.phoneHome = null
        localEntry.mobile = null
        localEntry.fax = null
        localEntry.phoneAssistant = null
        localEntry.phoneOther = null
        for (PhoneNumber phoneNumber : googleEntry.phoneNumbers) {
            if (!localEntry.phone && (PhoneNumber.Rel.WORK == phoneNumber.rel))
            {
                localEntry.phone = phoneNumber.phoneNumber
            } else if (!localEntry.phoneHome
                       && (PhoneNumber.Rel.HOME == phoneNumber.rel))
            {
                localEntry.phoneHome = phoneNumber.phoneNumber
            } else if (!localEntry.mobile
                       && (PhoneNumber.Rel.MOBILE == phoneNumber.rel))
            {
                localEntry.mobile = phoneNumber.phoneNumber
            } else if (!localEntry.fax
                       && (PhoneNumber.Rel.FAX == phoneNumber.rel))
            {
                localEntry.fax = phoneNumber.phoneNumber
            } else if (!localEntry.phoneAssistant
                       && (PhoneNumber.Rel.ASSISTANT == phoneNumber.rel))
            {
                localEntry.phoneAssistant = phoneNumber.phoneNumber
            } else if (!localEntry.phoneOther
                       && (PhoneNumber.Rel.OTHER == phoneNumber.rel))
            {
                localEntry.phoneOther = phoneNumber.phoneNumber
            }
        }

        localEntry.email1 = null
        localEntry.email2 = null
        for (Email email : googleEntry.emailAddresses) {
            if (!localEntry.email1 && (Email.Rel.WORK == email.rel)) {
                localEntry.email1 = email.address
            } else if (!localEntry.email2 && (Email.Rel.OTHER == email.rel)) {
                localEntry.email2 = email.address
            }
        }

        localEntry.jobTitle = organization.orgJobDescription?.value
        localEntry.department = organization.orgDepartment?.value
        localEntry.assistant = null
        for (Relation relation : googleEntry.relations) {
            if (Relation.Rel.ASSISTANT == relation.rel) {
                localEntry.assistant = relation.value
                break
            }
        }
        localEntry.birthday = googleEntry.hasBirthday() \
            ? Date.parse('yyyy-MM-dd', googleEntry.birthday.value)
            : null
        localEntry.picture = null
        Link photoLink = googleEntry.contactPhotoLink
        if (photoLink && photoLink.etag) {
            GDataRequest req = service.createLinkQueryRequest(photoLink)
            req.execute()
            localEntry.picture = req.responseStream.bytes
        }
        localEntry.notes = null
        for (Jot jot : googleEntry.jots) {
            if (Jot.Rel.WORK == jot.rel) {
                localEntry.notes = jot.value
                break
            }
        }

        localEntry
    }

    @Override
    @CompileStatic
    protected void deleteGoogleEntry(ContactEntry entry) {
        entry.delete()
    }

    @Override
    @CompileStatic
    protected boolean getAllowLocalCreate() {
        getBooleanSystemConfig 'syncContactsOptionsAllowCreate'
    }

    @Override
    @CompileStatic
    protected boolean getAllowLocalDelete() {
        getBooleanSystemConfig 'syncContactsOptionsAllowDelete'
    }

    @Override
    @CompileStatic
    protected boolean getAllowLocalModify() {
        getBooleanSystemConfig 'syncContactsOptionsAllowModify'
    }

    @Override
    @CompileStatic
    protected String getEtag(ContactEntry entry) {
        entry.etag
    }

    @CompileStatic
    private String getOrgOtherRelLabel() {
        messageSource.getMessage(
            'default.google.rel.orgOther', null, 'Company (other)',
            LCH.getLocale()
        )
    }

    @CompileStatic
    private String getOrgRelLabel() {
        messageSource.getMessage(
            'default.google.rel.org', null, 'Company', LCH.getLocale()
        )
    }

    @Override
    @CompileStatic
    protected GoogleService getService(Credential credential) {
        GoogleService svc = new ContactsService(APPLICATION_NAME)
        svc.protocolVersion = ContactsService.Versions.V3
        svc.OAuth2Credentials = credential

        svc
    }

    @Override
    @CompileStatic
    protected String getUrl(ContactEntry entry) {
        entry.selfLink.href
    }

    @Override
    @CompileStatic
    protected String googleEntryToString(ContactEntry entry) {
        StringBuilder res = new StringBuilder()
        Name name = entry?.name
        if (name) {
            if (name.hasFamilyName()) {
                res << name.familyName.value
            }
            if (name.hasGivenName()) {
                if (res) res << ', '
                res << name.givenName.value
            }
        }

        res.toString()
    }

    @Override
    @CompileStatic
    protected ContactEntry insertGoogleEntry(GoogleService service,
                                             ContactEntry entry)
    {
        service.insert FEED_URL, entry
    }

    @Override
    protected boolean isExcluded(Person localEntry, User user) {
        List<Long> ids =
            user.settings.excludeFromSync?.split(/,/)?.collect { it as Long }
        if (ids == null) {
            return false
        }

        ids.contains localEntry.organization.rating?.id
    }

    @Override
    @CompileStatic
    protected Map<String, ContactEntry> loadGoogleEntries(GoogleService service)
    {
        Map<String, ContactEntry> res = null
        try {
            ContactFeed feed = service.getFeed(FEED_URL, ContactFeed)
            res = [: ]
            int numEntries = feed.totalResults
            for (int i = 0; i < numEntries; i += 20) {
                Query query = new Query(FEED_URL)
                query.startIndex = i + 1
                query.maxResults = 20
                feed = service.query(query, ContactFeed)

                feed.entries.each {
                    res[it.selfLink.href] = it
                }
                if (log.debugEnabled) {
                    log.debug "Loaded ${res.size()}/${numEntries} Google entries."
                }
            }
        } catch (ResourceNotFoundException ignored) {
            /* already handled -> res = null */
        }

        res
    }

    /**
     * Loads the Google contact with the given URL.
     *
     * @param service   the Google contact service instance
     * @param url       the given URL
     * @return          the loaded Google contact
     * @since           2.0
     */
    @CompileStatic
    private static ContactEntry loadGoogleEntry(GoogleService service,
                                                String url)
    {
        service.getEntry new URL(url), ContactEntry
    }

    @Override
    @CompileStatic
    protected ContactEntry syncInsertGoogle(GoogleService service,
                                            Person localEntry)
    {
        ContactEntry googleEntry =
            (ContactEntry) super.syncInsertGoogle(service, localEntry)
        updatePhoto service, localEntry, googleEntry

        googleEntry
    }

    @Override
    @CompileStatic
    protected Person syncInsertLocal(GoogleService service,
                                     ContactEntry googleEntry)
    {
        updateOrganization googleEntry

        super.syncInsertLocal service, googleEntry
    }

    @Override
    @CompileStatic
    protected void syncUpdateGoogle(GoogleService service, Person localEntry,
                                    ContactEntry googleEntry)
    {
        super.syncUpdateGoogle(service, localEntry, googleEntry)

        updatePhoto service, localEntry, googleEntry
    }

    @Override
    @CompileStatic
    protected void syncUpdateLocal(GoogleService service, Person localEntry,
                                   ContactEntry googleEntry)
    {
        updateOrganization googleEntry

        super.syncUpdateLocal service, localEntry, googleEntry
    }

    @Override
    @CompileStatic
    protected void updateGoogleEntry(GoogleService service, ContactEntry entry)
    {
        service.update new URL(entry.editLink.href), entry
    }

    /**
     * Checks whether an organization for the given Google entry exists.  If
     * not a new organization is created.  In each case the organization is
     * updated by the data of the given Google entry.
     *
     * @param googleEntry                       the given Google entry
     * @throws RecoverableGoogleSyncException   if the Google entry does not
     *                                          define an organization name
     */
    private void updateOrganization(ContactEntry googleEntry)
        throws RecoverableGoogleSyncException
    {
        if (!googleEntry.hasOrganizations()) {
            throw new RecoverableGoogleSyncException(
                'Can only sync Google entries with organization.'
            )
        }

        GDataOrg organization = googleEntry.organizations[0]
        String orgName = organization.orgName.value
        Organization org = Organization.findByName(orgName)
        if (!org) {
            org = new Organization(
                name: orgName, recType: 1, shippingAddr: new Address()
            )
            boolean shippingAddrSet = false
            for (StructuredPostalAddress addr :
                 googleEntry.structuredPostalAddresses)
            {
                if (!shippingAddrSet
                    && (StructuredPostalAddress.Rel.WORK == addr.rel))
                {
                    convertToAddress org.shippingAddr, addr
                    shippingAddrSet = true
                }
            }
        }
        String label = orgOtherRelLabel
        org.phone = null
        org.fax = null
        org.phoneOther = null
        for (PhoneNumber phoneNumber : googleEntry.phoneNumbers) {
            if (!org.phone
                && (PhoneNumber.Rel.COMPANY_MAIN == phoneNumber.rel))
            {
                org.phone = phoneNumber.phoneNumber
            } else if (!org.fax
                       && (PhoneNumber.Rel.FAX == phoneNumber.rel))
            {
                org.fax = phoneNumber.phoneNumber
            } else if (!org.phoneOther && (label == phoneNumber.label)) {
                org.phoneOther = phoneNumber.phoneNumber
            }
        }
        org.email1 = null
        org.email2 = null
        for (Email email : googleEntry.emailAddresses) {
            if (!org.email1 && (orgRelLabel == email.label)) {
                org.email1 = email.address
            } else if (!org.email2 && (label == email.label)) {
                org.email2 = email.address
            }
        }
        org.website = null
        for (Website website : googleEntry.websites) {
            if (!org.website && (Website.Rel.WORK == website.rel)) {
                org.website = website.href
            }
        }
        org.save flush: true
    }

    /**
     * Updates the photo of the given local person entry at Google.
     *
     * @param service       the underlying Google service
     * @param localEntry    the given local person entry
     * @param googleEntry   the associated Google entry
     */
    private void updatePhoto(GoogleService service, Person localEntry,
                             ContactEntry googleEntry)
    {
        Link photoLink = googleEntry.contactPhotoLink
        String etag = photoLink.etag
        String href = photoLink.href

        def picture = localEntry.picture
        if (picture) {
            if (log.debugEnabled) {
                log.debug "Updating photo for ${localEntry}…"
            }
            GDataRequest request = service.createRequest(
                GDataRequest.RequestType.UPDATE, new URL(href),
                new ContentType(Magic.getMagicMatch(picture).mimeType)
            )
            if (etag && etag != '*') {
                request.etag = etag
            }
            request.requestStream.write picture
            try {
                request.execute()
            } catch (PreconditionFailedException ignored) { // etag outdated
                log.debug "Photo for ${localEntry} changed by third party."
            } catch (InvalidEntryException e) {
                log.debug "Cannot update picture for person ${localEntry}: ${e.message}"
            } catch (ServiceForbiddenException e) {
                log.warn 'Cannot access update service', e
            } finally {
                request.end()
            }

            ContactEntry newEntry = loadGoogleEntry(service, getUrl(googleEntry))
            log.debug "Changing etag from ${googleEntry.etag} to ${newEntry.etag}"
            googleEntry.etag = newEntry.etag
        } else if (etag) {
            if (log.debugEnabled) {
                log.debug "Deleting photo of ${localEntry}…"
            }
            service.delete new URL(href), etag
        }
    }
}
