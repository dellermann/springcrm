/*
 * SeqNumber.groovy
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


/**
 * The class {@code SeqNumber} represents a sequence number which is used to
 * number various content items.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class SeqNumber {

    //-- Class variables ------------------------

    static constraints = {
		controllerName(nullable: false, blank: false)
		prefix(maxSize: 5)
		suffix(maxSize: 5)
		startValue(min: 0)
		endValue(min: 0)
    }


    //-- Instance variables ---------------------

	String controllerName
	String prefix = ''
	String suffix = ''
	int startValue = 10000i
	int endValue = 99999i
}
