/*
 * Volume.groovy
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

import org.amcworld.springcrm.elfinder.ConnectorError
import org.amcworld.springcrm.elfinder.ConnectorException


/**
 * The class {@code Volume} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
abstract class Volume {

    //-- Instance variables ---------------------

    VolumeConfig config
    String id
    String root
    protected Map<String, List<String>> dirCache = [: ]
    protected Map<String, Map<String, Object>> statCache = [: ]


    //-- Constructors ---------------------------

    Volume(String id, String root, VolumeConfig config = null) {
        this.id = id
        this.root = root
        this.config = config ?: new VolumeConfig()
    }


    //-- Public methods -------------------------

    /**
     * Converts the given relative path to an absolute one using the root
     * path in {@code root}.
     *
     * @param path  the given relative path
     * @return      the absolute path
     * @see         #root
     */
    abstract String absPath(String path)

    /**
     * Computes the base name of the given path, that is, the last path
     * element.
     *
     * @param path  the given path
     * @return      the base name
     */
    abstract String baseName(String path)

    /**
     * Decodes the given hash code and returns the associated path.
     *
     * @param hash  the given hash code; may be {@code null}
     * @return      the associated path; {@code null} if the hash code is
     *              {@code null} or invalid
     */
    String decode(String hash) {
        if (!hash) {
            return null
        }

        int pos = hash.indexOf(':')
        if ((pos < 1) || (pos == hash.length())) {
            return null
        }
        hash = hash.substring(pos + 1)
        String relPath = new String(hash.tr('-_.', '+/=').decodeBase64())
        if (relPath == '/') {
            relPath = ''
        }
        return absPath(relPath)
    }

    /**
     * Extracts the volume ID from the given hash.
     *
     * @param hash  the given hash
     * @return      the volume ID; {@code null} if the given hash is invalid
     */
    static String decodeVolumeId(String hash) {
        if (!hash) {
            return null
        }
        int pos = hash.indexOf(':')
        return (pos < 1) ? null : hash.substring(0, pos)
    }

    /**
     * Returns statistics about the directory with the given hash code.
     *
     * @param hash                  the given hash
     * @return                      the directory statistics
     * @throws ConnectorException   if no directory with the given hash exists,
     *                              the hash doesn't represent a directory, or
     *                              the represented directory is hidden
     */
    Map<String, Object> dir(String hash) {
        Map<String, Object> dir = file(hash)
        if ((dir.mime != 'directory') || dir.hidden) {
            throw new ConnectorException(ConnectorError.NOT_DIR)
        }
        return dir
    }

    /**
     * Computes the directory path of the given path, that is, all path
     * elements without the last one.
     *
     * @param path  the given path
     * @return      the directory path
     */
    abstract String dirName(String path)

    /**
     * Encodes the given file path.
     *
     * @param path  the given file path; may be {@code null}
     * @return      the computed hash code; {@code null} if the path is
     *              {@code null}
     */
    String encode(String path) {
        if (path == null) {
            return null
        }

        String relPath = relPath(path) ?: '/'
        StringBuilder buf = new StringBuilder(id)
        buf << ':' << relPath.bytes.encodeBase64().toString().tr('+/=', '-_.')
        return buf.toString()
    }

    /**
     * Returns statistics about the file or directory with the given hash code.
     *
     * @param hash                  the given hash
     * @return                      the file or directory statistics
     * @throws ConnectorException   if no file or directory with the given hash
     *                              exists
     */
    Map<String, Object> file(String hash) {
        Map<String, Object> stat = stat(decode(hash))
        if (!stat) {
            throw new ConnectorException(ConnectorError.FILE_NOT_FOUND)
        }
        return stat
    }

    /**
     * Gets the hash code of the default path of this volume.  The default path
     * is either the start path defined in {@link VolumeConfig#startPath} or
     * the root directory.
     *
     * @return  the hash of the default path
     */
    String getDefaultPathHash() {
        return encode(config.startPath ?: root)
    }

    /**
     * Checks whether or not the given path represents the root directory of
     * this volume.
     *
     * @param path  the given path
     * @return      {@code true} if the path represents the root directory;
     *              {@code false} otherwise
     */
    boolean isRoot(String path) {
        return root == path
    }

    /**
     * Returns the names of the files and directories in the directory with the
     * given hash code.
     *
     * @param hash  the given hash code
     * @return      the list of file and directory names
     */
    List<String> ls(String hash) {
        dir(hash)   // ensure the directory exists

        List<String> list = []
        String path = decode(hash)
        for (Map<String, Object> stat : getScanDir(path)) {
            if (!stat.hidden && isMimeTypeAllowed(stat.mime)) {
                list += stat.name
            }
        }
        return list
    }

    /**
     * Returns volume options required by the client.
     *
     * @param hash  the hash of the returned directory
     * @return      the options
     */
    Map<String, Object> options(String hash) {
        return [
            disabled: false,
            path: fsPath(decode(hash)),
            separator: fsSeparator()
        ]
        // TODO add copyOverwrite, archivers, url, and tmbUrl
    }

    /**
     * Returns the parents of the directory with the given hash code up to the
     * root directory.
     *
     * @param hash  the given hash code
     * @return      the statistics of the parent folders and their contents;
     *              {@code null} if any parent folder is either hidden or not
     *              readable
     */
    List<Map<String, Object>> parents(String hash) {
        List<Map<String, Object>> tree = []
        String path = decode(hash)
        if (path) {
            log.debug "Retrieving parent info for ${path}…"
            path = dirName(path)
            while (!isRoot(path)) {
                path = dirName(path)
                Map<String, Object> stat = stat(path)
                if (stat.hidden || !stat.read) {
                    return null
                }
                tree += stat

                for (Map<String, Object> dir : getTree(path)) {
                    tree += dir
                }
            }
            tree.unique { return it.hash }
        }

        if (!tree) {
            tree += dir(hash)
        }
        return tree
    }

    /**
     * Computes the path relative to the root path.
     *
     * @param path  the given path
     * @return      the relative path
     */
    String relPath(String path) {
        return isRoot(path) ? '' : path.substring(root.length() + 1)
    }

    /**
     * Scans the directory with the given hash code and returns the statistics
     * of all files and directories in it.
     *
     * @param hash                  the given hash code
     * @return                      the statistics data; {@code null} if the
     *                              given directory cannot be scanned
     * @throws ConnectorException   if the directory with the given hash does
     *                              not exist
     */
    List<Map<String, Object>> scanDir(String hash) {
        Map<String, Object> dir = dir(hash)
        if (!dir.read) {
            throw new ConnectorException(ConnectorError.PERM_DENIED)
        }
        return getScanDir(decode(hash))
    }

    /**
     * Returns the statistics of the given file or directory.
     *
     * @param path  the given file or directory
     * @return      the statistics; {@code null} if the file or directory does
     *              not exist
     */
    Map<String, Object> stat(String path) {
        Map<String, Object> stat = statCache[path]
        if (stat == null) {
            stat = cacheStat(path)
        }
        return stat
    }

    /**
     * Collects the information about the directory with the given hash code
     * and all subsequent directories.
     *
     * @param hash          the given hash code; if {@code null} the root
     *                      directory is used
     * @param depth         the given depth; zero or negative numbers indicate
     *                      to use the default depth from the configuration
     * @param excludeHash   the hash code of a directory which is to exclude
     *                      from the result; may be {@code null}
     * @return              a list of statistics for each directory in the
     *                      tree; {@code null} if the directory with the given
     *                      hash does not exist or is not readable
     */
    List<Map<String, Object>> tree(String hash = null, int depth = 0,
                                   String excludeHash = null)
    {
        String path = hash ? decode(hash) : root
        Map<String, Object> dir = stat(path)
        if (!dir || (dir.mime != 'directory')) {
            return null
        }

        def dirs = getTree(
            path, (depth > 0) ? depth - 1 : config.treeDepth - 1,
            decode(excludeHash)
        )
        dirs += dir
        return dirs
    }


    //-- Non-public methods ---------------------

    /**
     * Stores content of the given directory in the directory cache.
     *
     * @param path  the given directory
     * @return      a list of names of files and directories residing in the
     *              given directory
     */
    protected List<String> cacheDir(String path) {
        List<String> entries = []
        String [] fileNames = fsScanDir(path)
        for (String fileName : fileNames) {
            def stat = stat(fileName)
            if (stat && !stat.hidden) {
                entries += fileName
            }
        }
        dirCache[path] = entries
        return entries
    }

    /**
     * Returns the statistics of the given file or directory and stores them
     * in the statistics cache.
     *
     * @param path  the given file or directory
     * @return      the statistics; {@code null} if the file or directory does
     *              not exist
     */
    protected Map<String, Object> cacheStat(String path) {
        Map<String, Object> stat = fsStat(path)
        if (stat != null) {
            boolean isDir = stat.mime == 'directory'
            boolean isRoot = isRoot(path)
            boolean isVisible = isVisible(path) && isMimeTypeAllowed(stat.mime)
            stat += [
                date: formatDate(stat.ts),
                hash: encode(path),
                hidden: !isVisible,
                locked: isRoot
            ]
            if (isRoot) {
                stat.volumeid = id
                if (!stat.name) {
                    stat.name = config.alias
                }
            }
            if (!stat.name) {
                stat.name = baseName(path)
            }
            if (!stat.phash && !isRoot) {
                stat.phash = encode(dirName(path))
            }
            // TODO optionally set stat.dirs
            // TODO optionally set stat.tmb
            statCache[path] = stat
        }
        return stat
    }

    /**
     * Formats the given time stamp.
     *
     * @param time  the given time stamp; may be {@code null}
     * @return      the formatted date; {@code null} if the given time stamp is
     *              {@code null}
     */
    protected String formatDate(Long time) {
        return time ? config.dateFormat.format(new Date(time)) : null
    }

    /**
     * Returns whether or not the file or directory with the given path is
     * marked as hidden in the file system.
     *
     * @param path  the given path
     * @return      {@code true} if the file or directory is hidden;
     *              {@code false} otherwise
     */
    protected abstract boolean fsHidden(String path)

    /**
     * Returns a path name relative to the root alias.
     *
     * @param path  the given path
     * @return      the path relative to the root name
     */
    protected abstract String fsPath(String path)

    /**
     * Scans the directory with the given path and returns the names of all
     * files and directories within.
     *
     * @param path  the given path to the directory
     * @return      the names of the files and directories within
     */
    protected abstract String [] fsScanDir(String path)

    /**
     * Returns the separator for path elements.
     *
     * @return  the path element separator
     */
    protected abstract String fsSeparator()

    /**
     * Returns the file system specific statistics of the given file or
     * directory.
     *
     * @param path  the path to the given file or directory
     * @return      the statistics; {@code null} if the file or directory does
     *              not exist
     */
    protected abstract Map<String, Object> fsStat(String path)

    /**
     * Returns the statistics of all files and directories in the directory
     * with the given path.
     *
     * @param path  the given path
     * @return      the statistics of the files and directories
     */
    protected List<Map<String, Object>> getScanDir(String path) {
        List<String> files = dirCache[path]
        if (files == null) {
            files = cacheDir(path)
        }

        List<Map<String, Object>> res = []
        for (String file : files) {
            Map<String, Object> stat = stat(file)
            if (stat && !stat.hidden) {
                res += stat
            }
        }
        return res
    }

    /**
     * Recursively loads the statistics of the tree beginning with the given
     * path up to the stated depth.
     *
     * @param path          the given path to start
     * @param depth         the given depth; zero or negative numbers indicate
     *                      unlimited depth
     * @param excludePath   a directory which is to exclude from the result;
     *                      may be {@code null}
     * @return              a list of statistics for each directory in the tree
     */
    protected List<Map<String, Object>> getTree(String path, int depth = 0,
                                                String excludePath = null)
    {
        List<String> entries = dirCache[path]
        if (entries == null) {
            entries = cacheDir(path)
        }

        List<Map<String, Object>> dirs = []
        for (String entry : entries) {
            def stat = stat(entry)
            if (stat && !stat.hidden && (stat.mime == 'directory') &&
                entry != excludePath)
            {
                dirs += stat
                if (depth > 0) {
                    dirs += getTree(entry, depth - 1)
                }
            }
        }
        return dirs
    }

    /**
     * Checks whether the given MIME type is allowed by configuration.
     *
     * @param mimeType          the MIME type to check
     * @param allowedMimeTypes  a list of allowed MIME types; if {@code null}
     *                          the allowed MIME types specified in the
     *                          configuration
     *                          {@link VolumeConfig#allowedMimeTypes} is used
     * @param allowEmptyList    indicates what to return if the list of allowed
     *                          MIME types, either the given one or the one of
     *                          the configuration, is empty
     * @return                  {@code true} if the given MIME type is allowed,
     *                          {@code false} otherwise
     */
    protected boolean isMimeTypeAllowed(String mimeType,
                                        List<String> allowedMimeTypes = null,
                                        boolean allowEmptyList = true)
    {
        if (allowedMimeTypes == null) {
            allowedMimeTypes = config.allowedMimeTypes
        }
        if (!allowedMimeTypes) {
            return allowEmptyList
        }

        return ('directory' == mimeType) || \
            (mimeType in allowedMimeTypes) || \
            (mimeType.dropWhile({ it != '/' }) in allowedMimeTypes)
    }

    /**
     * Checks whether the file or directory with the given path is visible in
     * listings.  All hidden files are actually hidden if the configuration
     * option {@link VolumeConfig#allowHidden} is {@code false}.  The root
     * directory is always treated visible.
     *
     * @param path  the given path
     * @return      {@code true} if the file or directory is visible;
     *              {@code false} otherwise
     */
    protected boolean isVisible(String path) {
        return isRoot(path) || \
            config.allowHidden || \
            !fsHidden(path)
    }
}
