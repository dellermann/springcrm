/*
 * SeqNumberFilters.groovy
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
 * The class {@code SeqNumberFilters} generates sequence numbers.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class SeqNumberFilters {

	def dependsOn = [LoginFilters]
	def seqNumberService

	def filters = {
		loadSeqNumberDefault(controller: '*', action: 'create|copy|edit') {
			after = { model ->
				if (model) {
					SeqNumber seqNumber =
						seqNumberService.loadSeqNumber(controllerName)
					if (seqNumber) {
						if (actionName != 'edit') {
							def inst = model["${controllerName}Instance"]
							if (inst) {
								inst.number =
									seqNumberService.nextNumber(controllerName)
							}
						}
						model.seqNumberPrefix = seqNumber.prefix
						model.seqNumberSuffix = seqNumber.suffix
					}
				}
			}
		}

		loadSeqNumberAtFailure(controller: '*', action: 'save|update') {
			after = { model ->
				if (model != null) {
					SeqNumber seqNumber =
						seqNumberService.loadSeqNumber(controllerName)
					if (seqNumber) {
						model.seqNumberPrefix = seqNumber.prefix
						model.seqNumberSuffix = seqNumber.suffix
					}
				}
			}
		}

		handleAutoNumber(controller: '*', action: 'save') {
			before = {
				if ((params.number != null) && params.autoNumber) {
					params.number = 0
				}
			}
			after = { model ->
				if (model) {
                    try {
    					def inst = model["${controllerName}Instance"]
    					if (inst) {
    						if (inst.number == 0) {
    							inst.number = seqNumberService.nextNumber(controllerName)
    						}
    					}
                    } catch (MissingPropertyException e) { /* ignored */ }
				}
			}
		}
	}
}
