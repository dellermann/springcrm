/*
 * CallFormPage.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


package org.amcworld.springcrm.page

import org.amcworld.springcrm.module.AutocompleteExModule
import org.amcworld.springcrm.module.AutocompleteModule
import org.amcworld.springcrm.module.SelectModule


class CallFormPage extends DefaultFormPage {

    //-- Class variables ------------------------

    static content = {
        organization { module AutocompleteExModule, $('#organization').parent() }
        person { module AutocompleteExModule, $('#person').parent() }
        phone { module AutocompleteModule, $('#phone') }
        status { module SelectModule, form.status() }
        type { module SelectModule, form.type() }
    }
}
