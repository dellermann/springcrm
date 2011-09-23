package org.amcworld.springcrm

/**
 * The class {@code OverviewPanelRepository} represents a repository of panels
 * which are used on the overview page. The repository is read from an XML file
 * by calling method {@code initialize()}.
 * 
 * @author	Daniel Ellermann
 * @version 0.9.9
 * @since	0.9.9
 */
class OverviewPanelRepository {

	//-- Class variables ------------------------

	private static OverviewPanelRepository instance


	//-- Instance variables ---------------------

	private Map<String, OverviewPanel> repository


	//-- Constructors ---------------------------

	private OverviewPanelRepository() {}


	//-- Public methods -------------------------

	/**
	 * Gets the only instance of this class.
	 * 
	 * @return	the instance
	 */
	static OverviewPanelRepository getInstance() {
		if (instance == null) {
			instance = new OverviewPanelRepository()
		}
		return instance
	}

	/**
	 * Gets the panel with the given ID.
	 * 
	 * @param id	the given ID
	 * @return		the panel definition; {@code null} if no panel definition
	 * 				with the given ID exists
	 */
	OverviewPanel getPanel(String id) {
		return repository[id]
	}

	/**
	 * Gets all panels of the repository.
	 * 
	 * @return	a map containing all the panels; the key contains the ID of the
	 * 			panel, the value the panel definition
	 */
	Map<String, OverviewPanel> getPanels() {
		return repository
	}

	/**
	 * Initializes the panel repository by reading the XML repository
	 * definitions from the given input stream.
	 * 
	 * @param is	the input stream containing the XML repository definitions
	 */
	void initialize(InputStream is) {
		def slurper = new XmlSlurper()
		def rep = slurper.parse(is)
		HashMap<String, OverviewPanel> m = 
			new HashMap<String, OverviewPanel>(rep.panel.size())
		for (def p in rep.panel) {
			def panel = new OverviewPanel(
				p.controller.text(), p.action.text(),
				p.title.findAll { !it.@lang }[0].text(),
				p.style?.text()
			)
			p.title.each {
				if (it.@lang.text()) {
					panel.addLocalizedTitle(it.@lang.text(), it.text())
				}
			}
			m[p.@id.text()] = panel
		}
		repository = Collections.unmodifiableMap(m)
	}
}
