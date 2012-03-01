/*
 * Client.groovy
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

import grails.util.GrailsNameUtils as GNU


/**
 * The class {@code Client} contains data about the client which uses this
 * application.  The class does not represent a domain model class but acts as
 * command object for the install and configuration controller.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 * @see     InstallController#clientData()
 * @see     InstallController#clientDataSave(Client)
 */
@grails.validation.Validateable
class Client {

    //-- Class variables ------------------------

    static constraints = {
        name(blank: false)
        street(blank: false)
        postalCode(blank: false)
        location(blank: false)
        phone(blank: false)
        fax(nullable: true)
        email(blank: false, nullable: false, email: true)
        website(nullable: true, url: /\s*/)
        bankName(nullable: true)
        bankCode(nullable: true)
        accountNumber(nullable: true)
    }


    //-- Instance variables ---------------------

    String name
    String street
    String postalCode
    String location
    String phone
    String fax
    String email
    String website
    String bankName
    String bankCode
    String accountNumber


    //-- Public methods -------------------------

    /**
     * Loads data of the client from the configuration table and returns a new
     * {@code Client} object.
     *
     * @return  the client object with loaded data
     */
    static Client load() {
        return new Client(loadAsMap())
    }

    /**
     * Loads data of the client from the configuration table as map.  The keys
     * in the map represent the names of the properties in the {@code Client}
     * class.
     *
     * @return  the client data as map
     */
    static Map<String, String> loadAsMap() {
        def l = Config.findAllByNameLike('client%')
        def data = [: ]
        l.each { data[GNU.getPropertyName(it.name.substring(6))] = it.value }
        return data
    }

    /**
     * Saves data of this client to the configuration table.
     */
    void save() {
        saveConfig('name', name)
        saveConfig('street', street)
        saveConfig('postalCode', postalCode)
        saveConfig('location', location)
        saveConfig('phone', phone)
        saveConfig('fax', fax)
        saveConfig('email', email)
        saveConfig('website', website)
        saveConfig('bankName', bankName)
        saveConfig('bankCode', bankCode)
        saveConfig('accountNumber', accountNumber)
    }


    //-- Non-public methods ---------------------

    /**
     * Saves an entry with the given name and value to the configuration table.
     * If the entry already exists it will be updated.
     *
     * @param name  the name of the entry
     * @param value the value of the entry
     */
    protected void saveConfig(String name, String value) {
        name = 'client' + GNU.getClassName(name)
        def config = Config.findByName(name)
        if (!config) {
            config = new Config(name: name)
        }
        config.value = value
        config.save(flush: true)
    }
}
