/*
 * InstallerDisableTask.groovy
 *
 * Copyright (c) 2011-2015, Daniel Ellermann
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

import java.util.TimerTask


/**
 * The class {@code InstallerDisableTask} represents a task which repeatedly
 * checks for the installer enable file and removes it after a particular
 * amount of time.
 *
 * @author	Daniel Ellermann
 * @version 2.0
 * @since   2.0
 */
class InstallerDisableTask extends TimerTask {

    //-- Fields ---------------------------------

    /**
     * The service to access installer specific components.
     */
    InstallService installService


    //-- Public methods -------------------------

    @Override
    void run() {
        if (installService.enableFileExpired) {
            installService.disableInstaller()
        }
    }
}
