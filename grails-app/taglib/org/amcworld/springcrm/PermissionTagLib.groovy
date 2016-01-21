/*
 * PermissionTagLib.groovy
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


/**
 * The class {@code PermissionTagLib} contains to check for login and
 * permissions.
 *
 * @author  Daniel Ellermann
 * @version 2.0
 */
class PermissionTagLib {

    //-- Public methods -------------------------

    /**
     * Creates a link to a controller but checks if the user has permissions
     * for it. If so the link is generated using <code>createLink</code>,
     * otherwise an empty string is returned.
     *
     * @attr controller REQUIRED    the controller to link to
     * @attr action                 the action to link to
     */
    def createControllerLink = { attrs, body ->
        if (session.user.checkAllowedControllers([attrs.controller] as Set)) {
            out << createLink(attrs)
        }
    }

    /**
     * Renders the body of the tag if the currently logged in user is
     * administrator.
     */
    def ifAdmin = { attrs, body ->
        if (session.user?.admin) {
            out << body()
        }
    }

    /**
     * Renders the body of the tag if the currently logged in user has
     * permission to view at least one of the controllers with the given names.
     * Administrators always have access to the specified modules.
     *
     * @attr controllers REQUIRED  the given controller names; you can specify either a single name or a collection of names or white space separated string
     */
    def ifControllerAllowed = { attrs, body ->
        def c = attrs.controllers
        Set<String> controllers
        if (c instanceof CharSequence) {
            controllers = c.split() as Set
        } else if (c instanceof Collection) {
            controllers = new HashSet<String>(c.size())
            for (def item : c) {
                controllers << item.toString()
            }
        } else {
            controllers = [c] as Set<String>
        }

        if (session.user?.checkAllowedControllers(controllers)) {
            out << body()
        }
    }

    /**
     * Renders the body of the tag if the currently logged in user has
     * permission to view at least one of the modules with the given names.
     * Administrators always have access to the specified modules.
     *
     * @attr modules REQUIRED  the given module names; you can specify either a single module, a collection of modules or module names or a white space separated string of module names
     */
    def ifModuleAllowed = { attrs, body ->
        def m = attrs.modules
        Set<Module> modules
        if (m instanceof CharSequence) {
            modules = Module.modulesByName(m.split())
        } else if (m instanceof Iterable) {
            modules = EnumSet.noneOf(Module)
            for (def item : m) {
                if (item instanceof Module) {
                    modules << item
                } else if (item instanceof CharSequence) {
                    modules << Module.valueOf(item.toString())
                }
            }
        } else if (m instanceof Module) {
            modules = EnumSet.of(m)
        } else {
            throw new IllegalArgumentException(
                "Attribute 'modules' is of invalid type: ${m.getClass()}."
            )
        }

        if (session.user?.checkAllowedModules(modules)) {
            out << body()
        }
    }
}
