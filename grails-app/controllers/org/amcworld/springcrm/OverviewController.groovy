package org.amcworld.springcrm

import grails.converters.JSON

class OverviewController {

	def lruService

    def index() {
		Map<Integer, List<Panel>> panels = new HashMap()
		for (int i = 0; i < Panel.NUM_COLUMNS; i++) {
			panels[i] = []
		}

		OverviewPanelRepository repository = OverviewPanelRepository.instance

		List<Panel> l = Panel.findAllByUser(
			session.user, [sort: 'col']
		)
		for (Panel panel : l) {
			panel.panelDef = repository.getPanel(panel.panelId)
			panels[panel.col][panel.pos] = panel
		}
		return [panels: panels]
	}

	def listAvailablePanels() {
		OverviewPanelRepository repository = OverviewPanelRepository.instance
		Map<String, OverviewPanel> panels = repository.getPanels()
		render panels as JSON
	}

	def lruList() {
		def lruList = lruService.retrieveLruEntries()
		return [lruList: lruList]
	}

	def addPanel() {
		String panelId = params.panelId
		int col = params.col as Integer
		int pos = params.pos as Integer

		/* move down all successors of the panel at new position */
		def c = Panel.createCriteria()
		def panels = c.list {
			eq('user', session.user)
			and {
				eq('col', col)
				ge('pos', pos)
			}
		}
		for (Panel p in panels) {
			p.pos++
			p.save(flush: true)
		}

		/* insert new panel */
		Panel panel = new Panel(
			user: session.user, col: col, pos: pos, panelId: panelId
		)
		panel.save(flush: true)
	}

	def movePanel() {
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
				p.save(flush: true)
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
				p.save(flush: true)
			}

			/* save the panel */
			panel.col = col
			panel.pos = pos
			panel.save(flush: true)
		}
		render(status: 200)
	}

	def removePanel() {
		String panelId = params.panelId
		Panel panel = Panel.findByUserAndPanelId(session.user, panelId)
		if (panel) {
			panel.delete(flush: true)

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
				p.save(flush: true)
			}
		}
		render(status: 200)
	}
}
