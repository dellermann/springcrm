/*
 * RoleGroupService.groovy
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

import grails.gorm.services.Service
import org.bson.types.ObjectId


/**
 * The class {@code RoleGroupService} handles the persistence of user groups in
 * the underlying data store.
 *
 * @author  Daniel Ellermann
 * @version 3.0
 * @since   3.0
 */
@Service(RoleGroup)
interface RoleGroupService {

    //-- Public methods -------------------------

    Long count()

    RoleGroup delete(ObjectId id)

    RoleGroup get(ObjectId id)

    List<RoleGroup> list(Map args)

    RoleGroup save(RoleGroup roleGroup)
}