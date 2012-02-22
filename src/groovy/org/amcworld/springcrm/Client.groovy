/*
 * Client.groovy
 *
 * $Id: $
 *
 * Copyright (c) 2011, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
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
