/*
 * FileService.groovy
 *
 * Copyright (c) 2011-2012, Daniel Ellermann
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


package org.amcworld.springcrm

import org.springframework.web.multipart.MultipartFile


/**
 * The class {@code FileService} handles files in the document space.
 *
 * @author	Daniel Ellermann
 * @version 0.9
 */
class FileService {

    //-- Class variables ------------------------

    static transactional = false


    //-- Instance variables ---------------------

    def grailsApplication


	//-- Public methods -------------------------

	/**
	 * Removes the file with the given name from the document space.
	 *
	 * @param fileName	the given file name
	 */
	void removeFile(String fileName) {
		retrieveFile(fileName)?.delete()
	}

	/**
	 * Retrieves the file with the given name from the document space.
	 *
	 * @param fileName	the given file name
	 * @return			the file object representing the required document
	 */
	File retrieveFile(String fileName) {
		return new File(baseDir, fileName)
	}

	/**
	 * Stores the given uploaded file to the document space. The file name is
	 * obtained from the original file name as specified by the client. If the
	 * file name already exists the method computes a unique file name by
	 * appending numbers to the base name.
	 *
	 * @param f	the uploaded file
	 * @return	the name of the stored file in the document space
	 */
    String storeFile(MultipartFile f) {
		String fileName = f.originalFilename
		StringBuilder fn = new StringBuilder(fileName)
		File dest = new File(baseDir, fileName)
		for (int i = 1; dest.exists(); i++) {
			fn = new StringBuilder(fileName)
			int pos = fn.lastIndexOf('.')
			if (pos < 0) {
				fn << '-' << i
			} else {
				fn.insert(pos, "-${i}")
			}
			dest = new File(baseDir, fn.toString())
		}
		f.transferTo(dest)
		return fn
    }


	//-- Non-public methods ---------------------

	/**
	 * Gets the base directory of the document space as specified in the
	 * configuration file in key {@code springcrm.dir.data}.
	 *
	 * @return	the base directory
	 */
	protected File getBaseDir() {
		File dir = new File(grailsApplication.config.springcrm.dir.data)
		if (!dir.exists()) {
			dir.mkdirs()
		}
		return dir
	}
}
