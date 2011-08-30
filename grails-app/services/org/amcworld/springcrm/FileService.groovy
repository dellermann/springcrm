package org.amcworld.springcrm

import org.springframework.web.multipart.MultipartFile;

class FileService {

    static transactional = false

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
	 * Gets the base directory of the document space.
	 * 
	 * @return	the base directory
	 */
	protected File getBaseDir() {
		File dir = new File(grailsApplication.config.springcrm.documents.base)
		if (!dir.exists()) {
			dir.mkdirs()
		}
		return dir
	}
}
