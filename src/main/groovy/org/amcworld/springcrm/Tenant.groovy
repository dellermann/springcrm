/*
 * Tenant.groovy
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

import grails.validation.Validateable
import groovy.transform.EqualsAndHashCode


/**
 * The class {@code Tenant} contains data about the tenant which uses this
 * application.  The class does not represent a domain model class but acts as
 * command object for the install and configuration controller.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   1.3
 * @see     InstallController#clientData()
 * @see     InstallController#clientDataSave(Tenant)
 */
@EqualsAndHashCode
class Tenant implements Validateable {

    //-- Class fields -------------------------------

    static constraints = {
        name blank: false
        street blank: false
        postalCode blank: false
        location blank: false
        phone blank: false
        fax nullable: true
        email blank: false, email: true
        website nullable: true, widget: 'url'
        bankName nullable: true
        bankCode nullable: true
        accountNumber nullable: true
    }


    //-- Fields -------------------------------------

    /**
     * The account number or IBAN of the client.
     */
    String accountNumber

    /**
     * The bank code or BIC of the client.
     */
    String bankCode

    /**
     * The name of the client's bank.
     */
    String bankName

    /**
     * The e-mail address of the client.
     */
    String email

    /**
     * The fax number of the client.
     */
    String fax

    /**
     * The location of the client's address.
     */
    String location

    /**
     * The name of the client.
     */
    String name

    /**
     * The phone number of the client.
     */
    String phone

    /**
     * The postal code of the client's address.
     */
    String postalCode

    /**
     * The street of the client's address.
     */
    String street

    /**
     * The URL of the website of the client.
     */
    String website


    //-- Public methods -----------------------------

    @Override
    String toString() {
        name
    }
}
