/*
 * BankDetails.groovy
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


/**
 * The class {@code BankDetails} stores data of a bank account.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
class BankDetails {

    //-- Class fields -------------------------------

    static constraints = {
        bankName nullable: true, maxSize: 50
        bic nullable: true, maxSize: 11
        iban nullable: true, maxSize: 34
        owner nullable: true, maxSize: 50
    }
    static transients = ['empty']


    //-- Fields -------------------------------------

    /**
     * The name of the bank.
     */
    String bankName

    /**
     * The Business Identifier Code (BIC).
     */
    String bic

    /**
     * The International Bank Account Number (IBAN).
     */
    String iban

    /**
     * The name of the owner of the account.
     */
    String owner


    //-- Constructors ---------------------------

    /**
     * Creates empty bank details.
     */
    BankDetails() {}

    /**
     * Creates a copy of the given bank details.
     *
     * @param details   the given bank details
     */
    BankDetails(BankDetails details) {
        bankName = details.bankName
        bic = details.bic
        iban = details.iban
        owner = details.owner
    }


    //-- Properties -----------------------------

    /**
     * Checks whether or not these bank details are empty.
     *
     * @return  {@code true} if these bank details are empty; {@code false}
     *          otherwise
     */
    boolean isEmpty() {
        !bankName && !bic && !iban && !owner
    }


    //-- Public methods -----------------------------

    @Override
    boolean equals(Object obj) {
        obj instanceof BankDetails && obj.iban == iban
    }

    @Override
    int hashCode() {
        iban?.hashCode() ?: 0i
    }

    @Override
    String toString() {
        iban
    }
}
