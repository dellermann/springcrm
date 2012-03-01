/*
 * AppPropertyEditorRegistrar.groovy
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


package org.amcworld.springcrm.util

import org.amcworld.springcrm.*
import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry


/**
 * The class {@code AppPropertyEditorRegistrar} registers necessary property
 * editors for data binding.
 *
 * @author  Daniel Ellermann
 * @version 0.9
 */
class AppPropertyEditorRegistrar implements PropertyEditorRegistrar {

    //-- Instance variables ---------------------

	def messageSource


    //-- Public methods -------------------------

	@Override
	void registerCustomEditors(PropertyEditorRegistry registry) {
		registry.registerCustomEditor(Date, new DatePropertyEditor(messageSource))
	}
}
