package org.amcworld.springcrm

import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder

class LruService {

	//-- Class variables ------------------------

    static transactional = true


	//-- Instance variables ---------------------

	def grailsApplication


	//-- Public methods -------------------------

	/**
	 * Records the item of the given controller and with the given ID in the
	 * LRU list.
	 * 
	 * @param controller	the controller name
	 * @param id			the ID of the item within this controller
	 * @param name			the descriptive name which is displayed in the LRU
	 * 						list
	 */
    def recordItem(String controller, long id, String name) {
		User user = session.user

		/* check whether or not this entry already exists */
		def c = LruEntry.createCriteria()
		def lruEntry = c.get {
			eq('user', user)
			and {
				eq('controller', controller)
				eq('itemId', id)
			}
		}

		/* obtain the maximum position value for further operations */
		c = LruEntry.createCriteria()
		Long maxPos = c.get {
			eq('user', user)
			projections {
				max('pos')
			}
		} ?: 0L

		if (lruEntry) {

			/* save position of the LRU entry to move */
			def oldPos = lruEntry.pos
			lruEntry.pos = -1
			lruEntry.save()

			/* decrement the position number of all entries after */
			c = LruEntry.createCriteria()
			def entriesToMove = c.list {
				eq('user', user)
				and {
					gt('pos', oldPos)
				}
			}
			for (LruEntry entry in entriesToMove) {
				entry.pos--
				entry.save(flush:true)
			}

			/* set the last position for the LRU entry to move */
			lruEntry.pos = maxPos
			lruEntry.save(flush:true)
		} else {

			/* add a new LRU entry */
			lruEntry = new LruEntry(
				user:user, controller:controller, itemId:id, pos:maxPos + 1,
				name:name
			)
			lruEntry.save(flush:true)

			/* delete old entries */
			c = LruEntry.createCriteria()
			def entriesToDel = c.list {
				eq('user', user)
				and {
					le('pos', lruEntry.pos - numOfLruEntries)
				}
			}
			for (LruEntry entry in entriesToDel) {
				entry.delete(flush:true)
			}
		}
    }

	/**
	 * Retrieves a list of LRU entries for the currently logged in user.
	 * 
	 * @return	the list of LRU entries
	 */
	List<LruEntry> retrieveLruEntries() {
		return LruEntry.findAllByUser(
			session.user, [max:numOfLruEntries, sort:'pos', order:'desc']
		)
	}


	//-- Non-public methods ---------------------

	/**
	 * Gets the number of LRU entries which are stored for a user. The value is
	 * obtained from the configuration.
	 * 
	 * @return	the number of simultaneous LRU entries
	 */
	protected int getNumOfLruEntries() {
		return grailsApplication.config.springcrm.lruList.numEntries ?: 10
	}

	/**
	 * Returns access to the user session.
	 *
	 * @return	the session instance
	 */
	protected HttpSession getSession() {
		return RequestContextHolder.currentRequestAttributes().session
	}
}
