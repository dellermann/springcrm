/*
 * Module.groovy
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


package org.amcworld.springcrm

import groovy.transform.CompileStatic


/**
 * The enumeration {@code Module} defines modules which can be used for
 * permission control in this application.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.2
 */
@CompileStatic
enum Module {

    //-- Values ---------------------------------

    CALENDAR(['calendarEvent', 'organization']),
    CALL(['call']),
    CONTACT(['organization', 'person']),
    CREDIT_MEMO(['organization', 'creditMemo']),
    DOCUMENT(['document']),
    DUNNING(['organization', 'invoice', 'dunning']),
    HELPDESK(['helpdesk']),
    INVOICE(['organization', 'invoice']),
    NOTE(['note']),
    PRODUCT(['product']),
    PROJECT(['organization', 'project']),
    PURCHASE_INVOICE(['purchaseInvoice']),
    QUOTE(['organization', 'quote']),
    REPORT(['report', 'organization', 'invoice', 'dunning', 'creditMemo']),
    SALES_ORDER(['organization', 'salesOrder']),
    SERVICE(['service']),
    TICKET(['ticket']),
    USER(['user'])


    //-- Fields ---------------------------------

    /**
     * The controllers which are associated with this module.
     *
     * @since 2.0
     */
    final List<String> controllers


    //-- Constructors ---------------------------

    /**
     * Creates a new module using the given list of controllers.
     *
     * @param controllers   the given list of controllers which are associated
     *                      to this module
     */
    Module(List<String> controllers) {
        this.controllers = controllers
    }


    //-- Properties -----------------------------

    /**
     * Gets an immutable list of controllers which are associated to this
     * module.
     *
     * @return  the associated controllers
     * @since   2.0
     */
    List<String> getControllers() {
        controllers.asImmutable()
    }


    //-- Public methods -------------------------

    /**
     * Gets all modules with the associated names.
     *
     * @param names the given module names; may be {@code null}
     * @return      the set of modules
     * @since       2.0
     */
    static EnumSet<Module> modulesByName(Iterable<String> names) {
        EnumSet<Module> res = EnumSet.noneOf(Module)
        if (names) {
            for (String name : names) {
                res << Module.valueOf(name.trim())
            }
        }

        res
    }

    /**
     * Resolves the given modules to a set of associated controller names.
     *
     * @param modules   the given modules
     * @return          the set of controller names associated with all the
     *                  given modules
     */
    static Set<String> resolveModules(EnumSet<Module> modules) {
        if (!modules) {
            return [] as Set
        }

        Set<String> res = new HashSet<String>(modules.size() * 2)
        for (Module module : modules) {
            res.addAll module.controllers   // don't use "<<" instead of addAll!
        }

        res
    }
}
