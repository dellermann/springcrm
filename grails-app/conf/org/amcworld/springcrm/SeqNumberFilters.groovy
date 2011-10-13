package org.amcworld.springcrm

class SeqNumberFilters {
	
	def dependsOn = [LoginFilters]
	def seqNumberService

	def filters = {
		loadSeqNumberDefault(controller:'*', action:'create|copy|edit') {
			after = { model ->
				if (model) {
					if (actionName != 'edit') {
						def inst = model["${controllerName}Instance"]
						if (inst) {
							inst.number = seqNumberService.nextNumber(controllerName)
						}
					}
					SeqNumber seqNumber =
						seqNumberService.loadSeqNumber(controllerName)
					if (seqNumber) {
						model.seqNumberPrefix = seqNumber.prefix
						model.seqNumberSuffix = seqNumber.suffix
					}
				}
			}
		}

		loadSeqNumberAtFailure(controller:'*', action:'save|update') {
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

		handleAutoNumber(controller:'*', action:'save') {
			before = {
				if ((params.number != null) && params.autoNumber) {
					params.number = 0
				}
			}
			after = { model ->
				if (model) {
					def inst = model["${controllerName}Instance"]
					if (inst) {
						if (inst.number == 0) {
							inst.number = seqNumberService.nextNumber(controllerName)
						}
					}
				}
			}
		}
	}
}
