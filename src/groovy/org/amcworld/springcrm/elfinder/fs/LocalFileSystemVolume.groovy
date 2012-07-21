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

import org.apache.commons.io.FileUtils;
import java.io.InputStream;
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

    String concatPath(String path, String name) {
        return new File(path, name).path
    }

    String dirName(String path) {
        return new File(path).parentFile.path
    }


    //-- Non-public methods ---------------------

    protected String fsCopy(String source, String targetDir, String name) {
        try {
            File target = new File(targetDir, name)
            FileUtils.copyFile(new File(source), target)
            return target.path
        } catch (IOException e) {
            return null
        }
    }

    protected boolean fsHasSubdirs(String path) {
        File dir = new File(path)
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                return true
            }
        }
        return false
    }

    protected boolean fsHidden(String path) {
        return new File(path).hidden
    }

    protected boolean fsIsDescendant(String path, String ancestor) {
        return (path == ancestor) || path.startsWith(ancestor + File.separator)
    }

    protected String fsLoadContent(String path) {
        return new File(path).text
    }

    protected String fsMkDir(String path, String name) {
        File dir = new File(path, name)
        return dir.mkdir() ? dir.path : null
    }

    protected String fsMkFile(String path, String name) {
        File file = new File(path, name)
        return file.createNewFile() ? file.path : null
    }

    protected String fsMove(String source, String targetDir, String name) {
        File sourceFile = new File(source)
        File targetFile = new File(targetDir, name)
        return sourceFile.renameTo(targetFile) ? targetFile.path : null
    }

    protected InputStream fsOpen(String path) {
        return new File(path).newInputStream()
    }

    protected String fsPath(String path) {
        StringBuilder buf = new StringBuilder(config.alias ?: '')
        if (!isRoot(path)) {
            buf << File.separator << relPath(path)
        }
        return buf.toString()
    }

    protected boolean fsRemove(String path) {
        return new File(path).delete()
    }

    protected boolean fsRmDir(String path) {
        return new File(path).delete()
    }

    protected String fsSave(InputStream stream, String path, String name) {
        File f = new File(path, name)
        f << stream
        return f.path
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
            read: f.canRead() ? 1 : 0,
            size: f.directory ? 0 : f.length(),
            ts: f.lastModified(),
            write: f.canWrite() ? 1 : 0
        ]
    }

    protected void fsStoreContent(String path, String content) {
        new File(path).text = content
    }

    protected String getMime(File file) {
        try {
            if (file.path.toLowerCase().endsWith('.txt')) {
                return 'text/simple'
            }
            return Magic.getMagicMatch(file, true).mimeType
        } catch (MagicMatchNotFoundException e) {
            return 'application/octet-stream'
        }
    }

    protected String uniqueName(String path, String name) {
        def matches = name =~ /^(\.*)([^.]+)(\..*)?$/
        if (!matches) {
            throw new IllegalStateException("File name '${name}' doesn't match the regular expression.")
        }
        String rawName = matches[0][2]
        for (int i = 1; i < 1000000; i++) {
            String tempName = "${rawName}-${i}"
            File f = new File(path, tempName)
            if (!f.exists()) {
                return "${matches[0][1]}${tempName}${matches[0][3]}"
            }
        }
        return null
    }
}
