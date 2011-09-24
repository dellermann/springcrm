package org.amcworld.springcrm

import org.codehaus.groovy.grails.commons.ApplicationHolder

/**
 * The class {@code OverviewPanel} stores information about a panel which can
 * be used on the overview page.
 * 
 * @author	Daniel Ellermann
 * @version 0.9.9
 * @since	0.9.9
 */
class OverviewPanel {

	//-- Instance variables ---------------------

	private String controller
	private String action
	private String url
	private String defTitle
	private String style
	private Map<Locale, String> localizedTitles
	
	
	//-- Constructors ---------------------------

	/**
	 * Creates a new overview page panel instance with the given data.
	 * 
	 * @param controller	the name of the controller which is called to
	 * 						generate the content of the panel
	 * @param action		the name of the action which is called to generate
	 * 						the content of the panel
	 * @param defTitle		the title in the default language
	 * @param style			any CSS style attributes which are applied to the
	 * 						panel
	 */
	OverviewPanel(String controller, String action, String defTitle,
				  String style) {
		this.controller = controller
		this.action = action
		this.defTitle = defTitle
		this.style = style
		localizedTitles = new HashMap<Locale, String>()
	}


	//-- Properties -----------------------------

	/**
	 * Gets the name of the controller which is called to generate the content
	 * of the panel.
	 * 
	 * @return	the name of the controller
	 */
	String getController() {
		return controller
	}

	/**
	 * Gets the name of the action which is called to generate the content of
	 * the panel.
	 * 
	 * @return	the name of the action
	 */
	String getAction() {
		return action
	}

	/**
	 * Gets any CSS style attributes which are applied to the panel.
	 * 
	 * @return	the CSS styles
	 */
	String getStyle() {
		return style
	}

	String getUrl() {
		if (!url) {
			def g = ApplicationHolder.application.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
			url = g.createLink(controller:controller, action:action)
		}
		return url
	}


	//-- Public methods -------------------------

	/**
	 * Adds a title in the given language.
	 * 
	 * @param locale	the locale representing the given language
	 * @param title		the title
	 */
	void addLocalizedTitle(Locale locale, String title) {
		localizedTitles[locale] = title
	}

	/**
	 * Adds a title in the given language.
	 * 
	 * @param locale	the locale ID representing the given language; the ID
	 * 					must be specified in the form
	 * 					{@code language[-country[-variant]]}
	 * @param title		the title
	 */
	void addLocalizedTitle(String locale, String title) {
		addLocalizedTitle(locale.tokenize('-') as Locale, title)
	}

	/**
	 * Gets the title in the given language or in the default language.
	 * 
	 * @param locale	the locale representing the given language; if
	 * 					<code>null</code> the default locale is used
	 * @return			the title in that language
	 */
	String getTitle(Locale locale = null) {
		locale = locale ?: Locale.default
		String title = localizedTitles[locale]
		if (!title) {
			locale = new Locale(locale.language, locale.country)
		}
		title = localizedTitles[locale]
		if (!title) {
			locale = new Locale(locale.language)
		}
		return localizedTitles[locale] ?: defTitle
	}
}
