/*
 * Staff.groovy
 *
 * Copyright (c) 2011-2017, Daniel Ellermann
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

import groovy.transform.CompileStatic
import java.time.LocalDate
import org.grails.datastore.gorm.GormEntity


/**
 * The class {@code Staff} represents a staff (employee or freelancer) of a
 * company.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class Staff implements GormEntity<Staff> {

    //-- Constants ----------------------------------

    public static final List<String> SEARCH_FIELDS = [
        'number', 'firstName', 'lastName', 'address.street', 'address.poBox',
        'address.postalCode', 'address.location', 'address.state',
        'address.country', 'phone', 'phoneHome', 'mobile',
        'email1', 'email2', 'bankDetails.bankName', 'bankDetails.bic',
        'bankDetails.iban', 'bankDetails.owner', 'socialSecurityNumber'
    ].asImmutable()


    //-- Class fields -------------------------------

    static constraints = {
        number blank: false, maxSize: 30, unique: true
        title nullable: true, maxSize: 20
        firstName blank: false, maxSize: 50
        lastName blank: false, maxSize: 50
        phone nullable: true, maxSize: 40
        phoneHome nullable: true, maxSize: 40
        mobile nullable: true, maxSize: 40
        email1 blank: false, email: true
        email2 nullable: true, email: true
        department nullable: true
        dateOfBirth nullable: true
        nationality nullable: true
        civilStatus nullable: true
        taxBracket nullable: true
        numChildren nullable: true, min: 0i, max: 99i
        socialSecurityNumber nullable: true
        healthInsurance nullable: true
        graduation nullable: true
        weeklyWorkingTime nullable: true, min: 0.0, max: 168.0
        dateOfEmployment nullable: true
        user nullable: true
    }
    static embedded = ['address', 'bankDetails']
    static mapping = {
        sort 'lastName'
    }
    static transients = ['fullName']


    //-- Fields -------------------------------------

    /**
     * The number of this staff.
     */
    String number

    /**
     * The salutation of this staff.
     */
    Salutation salutation

    /**
     * The title of this staff.
     */
    String title

    /**
     * The first name of this staff.
     */
    String firstName

    /**
     * The last name of this staff.
     */
    String lastName

    /**
     * The address of this staff.  This may be either the home address of an
     * employee or the business address of a freelancer.
     */
    Address address = new Address()

    /**
     * The office phone number of this staff.
     */
    String phone

    /**
     * The home phone number of this staff.
     */
    String phoneHome

    /**
     * The mobile phone number of this staff.
     */
    String mobile

    /**
     * The e-mail address of this staff.
     */
    String email1

    /**
     * Another e-mail address of this staff.
     */
    String email2

    /**
     * The department this staff belongs to.
     */
    Department department

    /**
     * The gender of this staff.
     */
    Gender gender

    /**
     * The date of birth of this staff.
     */
    LocalDate dateOfBirth

    /**
     * The nationality of this staff.
     */
    String nationality

    /**
     * The civil status of this staff.
     */
    CivilStatus civilStatus

    /**
     * The bank details of this staff.
     */
    BankDetails bankDetails = new BankDetails()

    /**
     * The income tax bracket of this staff.
     */
    TaxBracket taxBracket

    /**
     * The number of children of this staff.
     */
    Integer numChildren

    /**
     * The social security number of this staff.
     */
    String socialSecurityNumber

    /**
     * The health insurance of this staff.
     */
    HealthInsurance healthInsurance

    /**
     * Whether or not the health insurance of this staff is private.
     */
    boolean healthInsurancePrivate

    /**
     * The highest graduation of this staff.
     */
    Graduation graduation

    /**
     * The agreed weekly working time of this staff.
     */
    BigDecimal weeklyWorkingTime

    /**
     * The date of employment or first order.
     */
    LocalDate dateOfEmployment

    /**
     * The associated user if the staff may log into the application.
     */
    User user

    /**
     * The timestamp when this staff has been created.
     */
    Date dateCreated

    /**
     * The timestamp when this staff has been modified.
     */
    Date lastUpdated


    //-- Constructors -------------------------------

    Staff() {}

    Staff(Staff staff) {
        number = staff.number
        salutation = staff.salutation
        phone = staff.phone
        email1 = staff.email1
        department = staff.department
        gender = staff.gender
        nationality = staff.nationality
        civilStatus = staff.civilStatus
        taxBracket = staff.taxBracket
        healthInsurance = staff.healthInsurance
        healthInsurancePrivate = staff.healthInsurancePrivate
        graduation = staff.graduation
        weeklyWorkingTime = staff.weeklyWorkingTime
    }


    //-- Properties -----------------------------

    /**
     * Gets the full name of this staff.  The full name consists of the first
     * and the last name of the staff separated by a space character.
     *
     * @return  the full name of the staff
     */
    @CompileStatic
    String getFullName() {
        String fn = firstName?.trim()
        String ln = lastName?.trim()
        StringBuilder buf = new StringBuilder()
        if (fn) buf << fn
        if (fn && ln) buf << ' '
        if (ln) buf << ln

        buf.toString()
    }


    //-- Public methods -------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof Staff && obj.id == id
    }

    @Override
    int hashCode() {
        (id ?: 0i) as int
    }

    @Override
    String toString() {
        fullName ?: ''
    }
}
