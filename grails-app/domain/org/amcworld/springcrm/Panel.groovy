package org.amcworld.springcrm

class Panel {

	public static final int NUM_COLUMNS = 3

    static constraints = {
		user()
		col(range:0..NUM_COLUMNS - 1)
		pos(min:0)
		panelId(blank:false, nullable:false)
    }
	static mapping = {
		version false
	}
	static transients = ['panelDef']

	User user
	int col
	int pos
	String panelId
	OverviewPanel panelDef

	String toString() {
		return "${user.userName}-${panelId}"
	}
}
