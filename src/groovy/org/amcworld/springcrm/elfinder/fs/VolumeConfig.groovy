/*
 * VolumeConfig.groovy
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

import java.text.DateFormat


/**
 * The class {@code VolumeConfig} represents ...
 *
 * @author	Daniel Ellermann
 * @version 1.2
 * @since   1.2
 */
class VolumeConfig {

    //-- Instance variables ---------------------

    /**
     * An alias for the root directory of the volume.
     */
    String alias

    /**
     * A list of allowed MIME types.  If {@code null} or empty all MIME types
     * are allowed.
     */
    List<String> allowedMimeTypes = null

    /**
     * Indicates whether hidden files or directories are displayed.
     */
    boolean allowHidden = false

    /**
     * The format instance used to format dates and times.
     */
    DateFormat dateFormat = DateFormat.getDateTimeInstance()

    /**
     * A path where to start browsing the volume.  If unset the root path is
     * used instead.
     */
    String startPath

    /**
     * The default depth when obtaining trees.
     */
    int treeDepth = 1
}
