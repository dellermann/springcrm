package org.amcworld.springcrm

class OverviewController {

	def lruService

    def index = {
		Map<Integer, List<Panel>> panels = new HashMap()
		for (int i = 0; i < Panel.NUM_COLUMNS; i++) {
			panels[i] = []
		}

		OverviewPanelRepository repository = servletContext.overviewPanelRepository

		List<Panel> l = Panel.findAllByUser(
			session.user, [sort:'col']
		)
		for (Panel panel : l) {
			panel.panelDef = repository.getPanel(panel.panelId)
			panels[panel.col][panel.pos] = panel
		}
		[panels:panels]
	}

	def lruList = {
		def lruList = lruService.retrieveLruEntries()
		[lruList:lruList]
	}

	def movePanel = {
		String panelId = params.panelId
		int col = params.col as Integer
		int pos = params.pos as Integer

		Panel panel = Panel.findByUserAndPanelId(session.user, panelId)
		if (panel) {

			/* move up all successors of the panel to move */
			def c = Panel.createCriteria()
			List<Panel> panels = c.list {
				eq('user', session.user)
				and {
					eq('col', panel.col)
					gt('pos', panel.pos)
				}
			}
			for (Panel p in panels) {
				p.pos--
				p.save(flush:true)
			}

			/* move down all successors of the panel at new position */
			c = Panel.createCriteria()
			panels = c.list {
				eq('user', session.user)
				and {
					eq('col', col)
					ge('pos', pos)
				}
			}
			for (Panel p in panels) {
				p.pos++
				p.save(flush:true)
			}

			/* save the panel */
			panel.col = col
			panel.pos = pos
			panel.save(flush:true)
		}
		render(status:200)
	}

	def removePanel = {
		String panelId = params.panelId
		Panel panel = Panel.findAllByUserAndPanelId(session.user, panelId)
		if (panel) {
			def c = Panel.createCriteria()
			List<Panel> panels = c.list {
				eq('user', session.user)
				and {
					eq('col', panel.col)
					ge('pos', panel.pos)
				}
			}
			for (Panel p in panels) {
				p.pos--
				p.save(flush:true)
			}

			panel.delete(flush:true)
		}
		render(status:200)
	}
}
