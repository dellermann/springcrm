package org.amcworld.springcrm

class SeqNumberFilters {
	
	def seqNumberService

	def filters = {
		create(controller:'*', action:'create') {
			after = { model ->
				SeqNumber seqNumber =
					seqNumberService.loadSeqNumber(controllerName)
				def inst = model["${controllerName}Instance"]
				if (inst) {
					try {
						inst.number = seqNumber.nextNumber
					} catch (MissingPropertyException) { /* ignored */ }
				}
				if (seqNumber) {
					model.seqNumberPrefix = seqNumber.prefix
				}
			}
		}
		
		save(controller:'*', action:'(save|edit|update)') {
			after = { model ->
				SeqNumber seqNumber =
					seqNumberService.loadSeqNumber(controllerName)
				if (seqNumber) {
					model.seqNumberPrefix = seqNumber.prefix
				}
			}
		}
	}
}
