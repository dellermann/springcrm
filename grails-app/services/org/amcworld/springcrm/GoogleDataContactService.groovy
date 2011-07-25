package org.amcworld.springcrm

import javax.servlet.http.HttpSession

import org.springframework.web.context.request.RequestContextHolder

import com.google.gdata.client.contacts.ContactsService
import com.google.gdata.data.contacts.*
import com.google.gdata.data.contacts.ContactEntry
import com.google.gdata.data.extensions.*
import com.google.gdata.data.extensions.Organization as GDataOrg
import com.google.gdata.util.ResourceNotFoundException

class GoogleDataContactService extends GoogleDataService<Person, ContactEntry> {

	static scope = 'session'
    static transactional = true
	
	private ContactsService contactsService
	
	/**
	 * Converts the given person instance to a Google Data contact object.
	 *
	 * @param p			the given person instance
	 * @param contact	a contact object which is to fill with the property
	 * 					values; if <code>null</code> a new contact entry is
	 * 					created
	 * @return			the converted contact object
	 */
	ContactEntry convertToContact(Person p, ContactEntry contact = null) {
		if (contact == null) {
			contact = new ContactEntry()
		}
		contact.getRepeatingExtension(GDataOrg).clear()
		contact.addOrganization(new GDataOrg(
			orgName:p.organization?.name ? new OrgName(p.organization?.name) : null,
			orgDepartment:p.department ? new OrgDepartment(p.department) : null,
			orgJobDescription:p.jobTitle ? new OrgJobDescription(p.jobTitle) : null,
			primary:true,
			rel:GDataOrg.Rel.WORK
		))
		contact.name = new Name(
			namePrefix:p.salutation?.name ? new NamePrefix(p.salutation?.name) : null,
			givenName:new GivenName(p.firstName, null),
			familyName:new FamilyName(p.lastName, null)
		)
		contact.getRepeatingExtension(StructuredPostalAddress).clear()
		if (p.mailingAddrStreet || p.mailingAddrPoBox
			|| p.mailingAddrPostalCode || p.mailingAddrLocation
			|| p.mailingAddrState || p.mailingAddrCountry)
		{
			contact.addStructuredPostalAddress(new StructuredPostalAddress(
				street:p.mailingAddrStreet ? new Street(p.mailingAddrStreet) : null,
				pobox:p.mailingAddrPoBox ? new PoBox(p.mailingAddrPoBox) : null,
				postcode:p.mailingAddrPostalCode ? new PostCode(p.mailingAddrPostalCode) : null,
				city:p.mailingAddrLocation ? new City(p.mailingAddrLocation) : null,
				region:p.mailingAddrState ? new Region(p.mailingAddrState) : null,
				country:p.mailingAddrCountry ? new Country(value:p.mailingAddrCountry) : null,
				rel:StructuredPostalAddress.Rel.WORK
			))
		}
		if (p.otherAddrStreet || p.otherAddrPoBox
			|| p.otherAddrPostalCode || p.otherAddrLocation
			|| p.otherAddrState || p.otherAddrCountry)
		{
			contact.addStructuredPostalAddress(new StructuredPostalAddress(
				street:p.otherAddrStreet ? new Street(p.otherAddrStreet) : null,
				pobox:p.otherAddrPoBox ? new PoBox(p.otherAddrPoBox) : null,
				postcode:p.otherAddrPostalCode ? new PostCode(p.otherAddrPostalCode) : null,
				city:p.otherAddrLocation ? new City(p.otherAddrLocation) : null,
				region:p.otherAddrState ? new Region(p.otherAddrState) : null,
				country:p.otherAddrCountry ? new Country(value:p.otherAddrCountry) : null,
				rel:StructuredPostalAddress.Rel.OTHER
			))
		}
		contact.getRepeatingExtension(PhoneNumber).clear()
		if (p.phone) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.phone,
				primary:true,
				rel:PhoneNumber.Rel.WORK
			))
		}
		if (p.organization.phone && p.organization.phone != p.phone) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.organization.phone,
				primary:!p.phone,
				rel:PhoneNumber.Rel.WORK
			))
		}
		if (p.phoneHome) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.phoneHome,
				rel:PhoneNumber.Rel.HOME
			))
		}
		if (p.mobile) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.mobile,
				rel:PhoneNumber.Rel.MOBILE
			))
		}
		if (p.fax) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.fax,
				rel:PhoneNumber.Rel.FAX
			))
		}
		if (p.organization.fax && p.organization.fax != p.fax) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.organization.fax,
				rel:PhoneNumber.Rel.FAX
			))
		}
		if (p.phoneAssistant) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.phoneAssistant,
				rel:PhoneNumber.Rel.ASSISTANT
			))
		}
		if (p.phoneOther) {
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.phoneOther,
				rel:PhoneNumber.Rel.OTHER
			))
		}
		if (p.organization.phoneOther
			&& p.organization.phoneOther != p.phoneOther)
		{
			contact.addPhoneNumber(new PhoneNumber(
				phoneNumber:p.organization.phoneOther,
				rel:PhoneNumber.Rel.OTHER
			))
		}
		contact.getRepeatingExtension(Email).clear()
		if (p.email1) {
			contact.addEmailAddress(new Email(
				address:p.email1,
				primary:true,
				rel:Email.Rel.WORK
			))
		}
		if (p.organization.email1 && p.organization.email1 != p.email1) {
			contact.addEmailAddress(new Email(
				address:p.organization.email1,
				primary:!p.email1,
				rel:Email.Rel.WORK
			))
		}
		if (p.email2) {
			contact.addEmailAddress(new Email(
				address:p.email2,
				rel:Email.Rel.OTHER
			))
		}
		if (p.organization.email2 && p.organization.email2 != p.email2) {
			contact.addEmailAddress(new Email(
				address:p.organization.email2,
				rel:Email.Rel.OTHER
			))
		}
		contact.getRepeatingExtension(Relation.class).clear()
		if (p.assistant) {
			contact.addRelation(new Relation(
				value:p.assistant,
				rel:Relation.Rel.ASSISTANT
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
				href:p.organization.website,
				primary:true,
				rel:Website.Rel.WORK
			))
		}
		return contact
	}
	
	/**
	 * Inserts the given Google Data contact into the remote repository.
	 * 
	 * @param contact	the contact to insert
	 * @return			the inserted contact with additional data such as ID
	 */
	ContactEntry insert(ContactEntry contact) {
		return getContactsService().insert(
			new URL('https://www.google.com/m8/feeds/contacts/default/full'), 
			contact
		)
	}
	
	/**
	 * Retrieves the Google Data contact with the given URL.
	 * 
	 * @param url	the given URL
	 * @return		the contact; <code>null</code> if no contact with the given
	 * 				URL is available
	 */
	ContactEntry retrieve(String url) {
		def res = null
		try {
			res = getContactsService().getEntry(new URL(url), ContactEntry)
		} catch (ResourceNotFoundException) {
			/* already handled -> res = null */
		}
		return res
	}
	
	/**
	 * Synchronizes the given person with Google Data. If an associated Google
	 * data contact is set already it is updated. Otherwise, an new contact is
	 * created.
	 * 
	 * @param p	the person to synchronize
	 */
	void sync(Person p) {
		def status = findSyncStatus(p)
		if (status) {
			def contact = retrieve(status.url)
			if (contact) {
				contact = convertToContact(p, contact)
				update(contact)
			} else {
				contact = convertToContact(p)
				contact = insert(contact)
				status.url = contact.selfLink.href
			}
			afterUpdate(status)
		} else {
			def contact = convertToContact(p)
			contact = insert(contact)
			afterInsert(p, contact)
		}
	}
	
	/**
	 * Updates the given Google Data contact.
	 * 
	 * @param contact	the contact to update
	 * @param url		an optional different URL used for update; if
	 * 					<code>null</code> the update URL is obtained from the
	 * 					given contact
	 */
	void update(ContactEntry contact, String url = null) {
		if (url == null) {
			url = contact.editLink.href
		}
		getContactsService().update(new URL(url), contact)
	}
	
	/**
	 * Initializes and returns the Google Data service for contacts. The method
	 * expects the session variable <code>gdataToken</code> to be set.
	 * 
	 * @return	the Google Data contacts service
	 */
	protected ContactsService getContactsService() {
		if (!this.contactsService) {
			this.contactsService = new ContactsService(APPLICATION_NAME)
			this.contactsService.setAuthSubToken(session.gdataToken, null)
		}
		return this.contactsService
	}
}
