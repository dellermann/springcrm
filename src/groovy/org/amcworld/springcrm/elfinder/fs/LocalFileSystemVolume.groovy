/*
 * LocalFileSystemVolume.groovy
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


package org.amcworld.springcrm.elfinder.fs

import net.sf.jmimemagic.Magic
import net.sf.jmimemagic.MagicMatchNotFoundException


/**
 * The class {@code LocalFileSystemVolume} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.0
 */
class LocalFileSystemVolume extends Volume {

    //-- Instance variables ---------------------

    File rootFile


    //-- Constructors ---------------------------

    LocalFileSystemVolume(String id, String root, VolumeConfig config = null) {
        super(id, root, config)
        rootFile = new File(root).canonicalFile
        root = rootFile.path
    }


    //-- Public methods -------------------------

    String absPath(String path) {
        return new File(rootFile, path).path
    }

    String baseName(String path) {
        return new File(path).name
    }

    String dirName(String path) {
        return new File(path).parentFile.path
    }


    //-- Non-public methods ---------------------

    protected boolean fsHidden(String path) {
        return new File(path).hidden
    }

    protected String fsPath(String path) {
        StringBuilder buf = new StringBuilder(config.alias ?: '')
        if (!isRoot(path)) {
            buf << File.separator << relPath(path)
        }
        return buf.toString()
    }

    protected String [] fsScanDir(String path) {
        File f = new File(path)
        String [] list = f.list()
        return list.collect { return new File(f, it).path }
    }

    protected String fsSeparator() {
        return File.separator
    }

    protected Map<String, Object> fsStat(String path) {
        File f = new File(path)
        if (!f.exists()) {
            return null
        }

        return [
            mime: f.directory ? 'directory' : getMime(f),
            read: f.canRead(),
            size: f.directory ? 0 : f.length(),
            ts: f.lastModified(),
            write: f.canWrite()
        ]
    }

    protected String getMime(File file) {
        try {
            return Magic.getMagicMatch(file, true).mimeType
        } catch (MagicMatchNotFoundException e) {
            return 'application/octet-stream'
        }
    }
}
