/*
 * ProjectDocumentDiffSet.groovy
 *
 * Copyright (c) 2011-2014, Daniel Ellermann
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


package org.amcworld.springcrm.install.diffset

import org.amcworld.springcrm.ProjectDocument


/**
 * The class {@code ProjectDocumentDiffSet} represents a difference set that is
 * executed during startup decodes the path values of project documents.
 *
 * @author  Daniel Ellermann
 * @version 1.4
 * @since   1.4
 */
class ProjectDocumentDiffSet implements StartupDiffSet {

    //-- Public methods -------------------------

    @Override
    public void execute() {
        List<ProjectDocument> docs = ProjectDocument.list()
        for (ProjectDocument doc in docs) {
			String hash = doc.path
	        int pos = hash.indexOf('_')
	        if ((pos > 0) && (pos != hash.length())) {
		        hash = hash.substring(pos + 1)
		        String path = new String(hash.tr('-_.', '+/=').decodeBase64())
		        if (path == '/') {
		            path = ''
		        }
				doc.path = path
				doc.save flush: true
	        }
        }
    }
}
