/*
 * GoogleContactSync.groovy
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


package org.amcworld.springcrm.google

import com.google.gdata.client.GoogleService
import com.google.gdata.client.Query;
import com.google.gdata.client.Service.GDataRequest
import com.google.gdata.client.contacts.ContactsService
import com.google.gdata.data.contacts.*
import com.google.gdata.data.contacts.ContactEntry
import com.google.gdata.data.extensions.*
import com.google.gdata.data.extensions.Organization as GDataOrg
import com.google.gdata.data.Link
import com.google.gdata.util.ContentType
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.ResourceNotFoundException
import net.sf.jmimemagic.Magic
import org.amcworld.springcrm.Person
import org.amcworld.springcrm.google.RecoverableGoogleSyncException
import org.apache.commons.logging.LogFactory
import org.springframework.context.i18n.LocaleContextHolder as LCH


/**
 * The class {@code GoogleContactSync} synchronizes person records with Google.
 *
 * @author	Daniel Ellermann
 * @version 1.0
 * @since   1.0
 */
class GoogleContactSync extends GoogleSync<Person, ContactEntry> {

    //-- Constants ------------------------------

    protected static final URL FEED_URL =
        new URL('https://www.google.com/m8/feeds/contacts/default/full')

    private static final log = LogFactory.getLog(this)


    //-- Instance variables ---------------------

    protected GoogleService svc


    //-- Constructors ---------------------------

    /**
     * Creates a new Google synchronization instance for persons.
     */
    GoogleContactSync() {
        super(Person)
    }


    //-- Non-public methods ---------------------

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
            familyName: new FamilyName(p.lastName, null)
        )
        contact.getRepeatingExtension(StructuredPostalAddress).clear()
        if (p.mailingAddrStreet || p.mailingAddrPoBox
            || p.mailingAddrPostalCode || p.mailingAddrLocation
            || p.mailingAddrState || p.mailingAddrCountry)
        {
            contact.addStructuredPostalAddress(new StructuredPostalAddress(
                street: p.mailingAddrStreet ? new Street(p.mailingAddrStreet) : null,
                pobox: p.mailingAddrPoBox ? new PoBox(p.mailingAddrPoBox) : null,
                postcode: p.mailingAddrPostalCode ? new PostCode(p.mailingAddrPostalCode) : null,
                city: p.mailingAddrLocation ? new City(p.mailingAddrLocation) : null,
                region: p.mailingAddrState ? new Region(p.mailingAddrState) : null,
                country: p.mailingAddrCountry ? new Country(value: p.mailingAddrCountry) : null,
                rel: StructuredPostalAddress.Rel.WORK
            ))
        }
        if (p.otherAddrStreet || p.otherAddrPoBox
            || p.otherAddrPostalCode || p.otherAddrLocation
            || p.otherAddrState || p.otherAddrCountry)
        {
            contact.addStructuredPostalAddress(new StructuredPostalAddress(
                street: p.otherAddrStreet ? new Street(p.otherAddrStreet) : null,
                pobox: p.otherAddrPoBox ? new PoBox(p.otherAddrPoBox) : null,
                postcode: p.otherAddrPostalCode ? new PostCode(p.otherAddrPostalCode) : null,
                city: p.otherAddrLocation ? new City(p.otherAddrLocation) : null,
                region: p.otherAddrState ? new Region(p.otherAddrState) : null,
                country: p.otherAddrCountry ? new Country(value: p.otherAddrCountry) : null,
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
        return contact
    }

    @Override
    protected Person convertToLocal(Person localEntry, ContactEntry googleEntry)
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

        localEntry.mailingAddrStreet = null
        localEntry.mailingAddrPoBox = null
        localEntry.mailingAddrPostalCode = null
        localEntry.mailingAddrLocation = null
        localEntry.mailingAddrState = null
        localEntry.mailingAddrCountry = null
        localEntry.otherAddrStreet = null
        localEntry.otherAddrPoBox = null
        localEntry.otherAddrPostalCode = null
        localEntry.otherAddrLocation = null
        localEntry.otherAddrState = null
        localEntry.otherAddrCountry = null
        boolean mailingAddrSet = false
        boolean otherAddrSet = false
        for (StructuredPostalAddress addr : googleEntry.structuredPostalAddresses)
        {
            if (!mailingAddrSet
                && (StructuredPostalAddress.Rel.WORK == addr.rel))
            {
                localEntry.mailingAddrStreet = addr.street?.value
                localEntry.mailingAddrPoBox = addr.pobox?.value
                localEntry.mailingAddrPostalCode = addr.postcode?.value
                localEntry.mailingAddrLocation = addr.city?.value
                localEntry.mailingAddrState = addr.region?.value
                localEntry.mailingAddrCountry = addr.country?.value
                mailingAddrSet = true
            } else if (!otherAddrSet
                       && (StructuredPostalAddress.Rel.OTHER == addr.rel))
            {
                localEntry.otherAddrStreet = addr.street?.value
                localEntry.otherAddrPoBox = addr.pobox?.value
                localEntry.otherAddrPostalCode = addr.postcode?.value
                localEntry.otherAddrLocation = addr.city?.value
                localEntry.otherAddrState = addr.region?.value
                localEntry.otherAddrCountry = addr.country?.value
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
        localEntry.birthday = googleEntry.hasBirthday() ? Date.parse('yyyy-MM-dd', googleEntry.birthday.value) : null
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

        return localEntry
    }

    @Override
    protected void deleteGoogleEntry(ContactEntry entry) {
        entry.delete()
    }

    @Override
    protected boolean getAllowLocalCreate() {
        return getBooleanSystemConfig('syncContactsOptionsAllowCreate')
    }

    @Override
    protected boolean getAllowLocalDelete() {
        return getBooleanSystemConfig('syncContactsOptionsAllowDelete')
    }

    @Override
    protected boolean getAllowLocalModify() {
        return getBooleanSystemConfig('syncContactsOptionsAllowModify')
    }

    @Override
    protected String getEtag(ContactEntry entry) {
        return entry.etag
    }

    private String getOrgOtherRelLabel() {
        return messageSource.getMessage(
            'default.google.rel.orgOther', null, 'Company (other)',
            LCH.getLocale()
        )
    }

    private String getOrgRelLabel() {
        return messageSource.getMessage(
            'default.google.rel.org', null, 'Company', LCH.getLocale()
        )
    }

    /**
     * Gets access to the underlying Google API service.  The service is fully
     * authenticated.
     *
     * @return  the Google API service instance
     */
    protected synchronized GoogleService getService() {
        if (!svc) {
            svc = new ContactsService(APPLICATION_NAME)
            svc.protocolVersion = ContactsService.Versions.V3
        }
        svc.OAuth2Credentials = loadCredential()
        return svc
    }

    protected String getUrl(ContactEntry entry) {
        return entry.selfLink.href
    }

    @Override
    protected String googleEntryToString(ContactEntry entry) {
        String res = ''
        Name name = entry.name
        if (name) {
            if (name.hasFamilyName()) {
                res = name.familyName.value
            }
            if (name.hasGivenName()) {
                if (res) res += ', '
                res += name.givenName.value
            }
        }
        return res
    }

    @Override
    protected ContactEntry insertGoogleEntry(ContactEntry entry) {
        return service.insert(FEED_URL, entry)
    }

    @Override
    protected Map<String, ContactEntry> loadGoogleEntries() {
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
        } catch (ResourceNotFoundException e) {
            /* already handled -> res = null */
        }
        return res
    }

    @Override
    protected ContactEntry syncInsertGoogle(Person localEntry) {
        ContactEntry googleEntry = super.syncInsertGoogle(localEntry);
        updatePhoto(localEntry, googleEntry)
        return googleEntry
    }

    @Override
    protected Person syncInsertLocal(ContactEntry googleEntry) {
        updateOrganization(googleEntry)
        return super.syncInsertLocal(googleEntry);
    }

    @Override
    protected void syncUpdateGoogle(Person localEntry, ContactEntry googleEntry)
    {
        super.syncUpdateGoogle(localEntry, googleEntry)
    }

    @Override
    protected void syncUpdateLocal(Person localEntry, ContactEntry googleEntry)
    {
        updateOrganization(googleEntry)
        super.syncUpdateLocal(localEntry, googleEntry);
    }

    @Override
    protected void updateGoogleEntry(ContactEntry entry) {
        service.update(new URL(entry.editLink.href), entry)
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
            org = new Organization(name: orgName, recType: 1)
            boolean shippingAddrSet = false
            for (StructuredPostalAddress addr : googleEntry.structuredPostalAddresses) {
                if (!shippingAddrSet
                    && (StructuredPostalAddress.Rel.WORK == addr.rel))
                {
                    org.shippingAddrStreet = addr.street?.value
                    org.shippingAddrPoBox = addr.pobox?.value
                    org.shippingAddrPostalCode = addr.postcode?.value
                    org.shippingAddrLocation = addr.city?.value
                    org.shippingAddrState = addr.region?.value
                    org.shippingAddrCountry = addr.country?.value
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
        org.save(flush: true)
    }

    /**
     * Updates the photo of the given local person entry at Google.
     *
     * @param localEntry    the given local person entry
     * @param googleEntry   the associated Google entry
     */
    private void updatePhoto(Person localEntry, ContactEntry googleEntry) {
        Link photoLink = googleEntry.contactPhotoLink
        def picture = localEntry.picture
        if (picture) {
            GDataRequest request = service.createRequest(
                GDataRequest.RequestType.UPDATE, new URL(photoLink.href),
                new ContentType(Magic.getMagicMatch(picture).mimeType)
            )
            request.setEtag(photoLink.getEtag())
            request.requestStream.write(picture)
            try {
                request.execute()
            } catch (InvalidEntryException e) {
                log.debug "Cannot update picture for person ${localEntry}: ${e.message}"
            }
        } else {
            service.delete(new URL(photoLink.href), photoLink.getEtag())
        }
    }
}
