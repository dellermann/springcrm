package org.amcworld.springcrm

class LruFilters {

	def lruService

    def filters = {
        lruRecord(controller: '*', action: 'show|edit') {
            after = { model ->
				if (model) {
	                def inst = model["${controllerName}Instance"]
					if (inst) {
						lruService.recordItem(
							controllerName, params.id as long, inst.toString()
						)
					}
				}
            }
        }

        lruRemove(controller: '*', action: 'delete') {
            before = {
                if (params.confirmed) {
                    lruService.removeItem(controllerName, params.id as long)
                }
            }
        }
    }
}
