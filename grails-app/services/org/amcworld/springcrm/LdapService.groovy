/*
 * LdapService.groovy
 *
 * Copyright (c) 2011-2018, Daniel Ellermann
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

import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import javax.naming.NameAlreadyBoundException
import org.amcworld.springcrm.ldap.LdapFactory
import org.apache.directory.groovyldap.LDAP


/**
 * The class {@code LdapService} contains methods to communicate with an LDAP
 * server and store person data.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 */
@CompileStatic
@Transactional(readOnly = false)
class LdapService {

    //-- Fields ---------------------------------

    ConfigService configService
    LdapFactory ldapFactory
    LdapSyncStatusService ldapSyncStatusService


    //-- Public methods -------------------------

    /**
     * Deletes the given person from the LDAP directory.
     *
     * @param person    the person to delete
     */
    void delete(Person person) {
        LDAP ldap = getLdap()
        if (ldap != null) {
            LdapSyncStatus status = getByPerson(person)
            if (status != null) {
                if (ldap.exists(status.dn)) {
                    log.debug "Deleting DN ${status.dn} from LDAP..."
                    ldap.delete status.dn
                }
                ldapSyncStatusService.delete status.id
            }
        }
    }

    /**
     * Saves the given person to the LDAP directory.  The method either creates
     * or updates an entry in the directory.
     *
     * @param person    the person to save
     */
    void save(Person person) {
        LDAP ldap = getLdap()
        if (ldap != null) {
            if (log.debugEnabled) {
                log.debug "Exporting person ${person} to LDAP..."
            }
            LdapSyncStatus status = getByPerson(person)
            if (status == null) {
                status = new LdapSyncStatus(itemId: person.id)
            } else if (ldap.exists(status.dn)) {
                if (log.debugEnabled) {
                    log.debug "DN ${status.dn} already exists -> delete it."
                }
                ldap.delete status.dn
            }
            Map<String, Object> attrs = convertPersonToAttrs(person)
            String baseDn = configService.getString('ldapContactDn')
            StringBuilder dn
            for (int i = 1; i < 10000; i++) {    // 10000 => emergency abort
                dn = new StringBuilder('cn=')
                dn << attrs.cn
                if (i > 1) dn << ' ' << i
                dn << ',' << baseDn
                if (log.debugEnabled) {
                    log.debug "Trying to save DN ${dn} to LDAP..."
                }
                try {
                    ldap.add dn.toString(), attrs
                    status.dn = dn.toString()
                    ldapSyncStatusService.save status
                    break
                } catch (NameAlreadyBoundException ignored) { /* ignored */ }
            }
            if (log.debugEnabled) {
                log.debug "Successfully exported person ${person} to LDAP."
            }
        }
    }


    //-- Non-public methods ---------------------

    /**
     * Converts the given person to an attribute map which can be transferred
     * to the LDAP directory.
     *
     * @param person    the person to convert
     * @return          the attribute map containing the person property values
     *                  suitable for LDAP
     */
    private static Map<String, Object> convertPersonToAttrs(Person person) {
        Map<String, Object> attrs = [
            cn: "${person.lastName} ${person.firstName}".toString(),
            displayname: "${person.firstName} ${person.lastName}".toString(),
            givenname: person.firstName,
            o: person.organization.name,
            objectclass: ['top', 'inetOrgPerson'],
            sn: person.lastName
        ] as Map<String, Object>
        if (person.title) {
            attrs.title = person.title
        }
        if (person.organization.industry) {
            attrs.businesscategory = person.organization.industry.name
        }
        List<String> list = new ArrayList<String>()
        if (person.fax) {
            list.add person.fax
        }
        if (person.organization.fax && person.organization.fax != person.fax) {
            list.add person.organization.fax
        }
        if (!list.isEmpty()) {
            attrs.facsimiletelephonenumber = list.unique {
                it.replaceAll ~/\D/, ''
            }
        }
        if (person.phoneHome) {
            attrs.homephone = person.phoneHome
        }
        if (person.mailingAddr.location) {
            attrs.l = person.mailingAddr.location
        }
        if (person.organization.website) {
            attrs.labeleduri = person.organization.website
        }
        list = new ArrayList<String>()
        if (person.email1) {
            list.add person.email1
        }
        if (person.organization.email1
            && person.organization.email1 != person.email1)
        {
            list.add person.organization.email1
        }
        if (person.email2) {
            list.add person.email2
        }
        if (person.organization.email2
            && person.organization.email2 != person.email2)
        {
            list.add person.organization.email2
        }
        if (!list.empty) {
            attrs.mail = list.unique()
        }
        if (person.mobile) {
            attrs.mobile = person.mobile
        }
        if (person.department) {
            attrs.ou = person.department
        }
        String s = person.mailingAddr
        if (s) {
            attrs.postaladdress = s
        }
        if (person.mailingAddr.postalCode) {
            attrs.postalcode = person.mailingAddr.postalCode
        }
        if (person.mailingAddr.poBox) {
            attrs.postofficebox = person.mailingAddr.poBox
        }
        if (person.mailingAddr.state) {
            attrs.st = person.mailingAddr.state
        }
        if (person.mailingAddr.street) {
            attrs.street = person.mailingAddr.street
        }
        list = new ArrayList<String>()
        if (person.phone) {
            list.add person.phone
        }
        if (person.organization.phone
            && person.organization.phone != person.phone)
        {
            list.add person.organization.phone
        }
        if (person.phoneOther) {
            list.add person.phoneOther
        }
        if (person.organization.phoneOther
            && person.organization.phoneOther != person.phoneOther)
        {
            list.add person.organization.phoneOther
        }
        if (!list.empty) {
            attrs.telephonenumber = list.unique {
                it.replaceAll ~/\D/, ''
            }
        }

        attrs
    }

    /**
     * Gets the LDAP sync status by the given person.
     *
     * @param person    the given person
     * @return          the sync status or {@code null} if no such status exist
     * @since 2.1
     */
    private LdapSyncStatus getByPerson(Person person) {
        ldapSyncStatusService.findByItemId person.id
    }

    /**
     * Gets the connection handle to the LDAP server.  The attributes such as
     * host name, port, bind name, and password are obtained from the
     * configuration holder.
     *
     * @return  the LDAP server connection; {@code null} if no LDAP server is
     *          configured
     */
    private LDAP getLdap() {
        String host = configService.getString('ldapHost')
        if (!host) {
            return null
        }

        ldapFactory.newLdap(
            host, configService.getString('ldapBindDn'),
            configService.getString('ldapBindPasswd'),
            configService.getInteger('ldapPort')
        )
    }
}
