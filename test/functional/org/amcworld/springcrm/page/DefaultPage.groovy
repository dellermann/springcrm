/*
 * DefaultPage.groovy
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


package org.amcworld.springcrm.page

import org.amcworld.springcrm.module.ButtonModule


class DefaultPage extends geb.Page {

    //-- Class variables ------------------------

    static content = {
        flashMessage { $('.flash-message').text() }
        header { $('h1').text() }
        logoutLink { $('div#top-area > p > a') }
        toolbar { $('ul#toolbar') }
        toolbarButtons { moduleList ButtonModule, toolbar.find('li') }
    }


    //-- Public methods -------------------------

    /**
     * Checks the toolbar of the given controller agains the given button
     * definitions.
     *
     * @param controller    the controller to check
     * @param buttons       a list of button definitions; each definition must
     *                      be a map containing the following values:
     *                      <ul>
     *                        <li>{@code action}. The action to call when
     *                        clicking the button. The value is mandatory if
     *                        the {@code url} key is not specified.</li>
     *                        <li>{@code check}. A closure which is called to
     *                        perform additional checks.  The only parameter is
     *                        the {@code WebElement} representing the button.
     *                        </li>
     *                        <li>{@code color}. The color of the button.</li>
     *                        <li>{@code cssClasses}. Any additional CSS
     *                        classes the button must have.</li>
     *                        <li>{@code icon}. The icon (without the
     *                        {@code fa-} prefix) which must be inside the
     *                        button.</li>
     *                        <li>{@code id}. The ID to use in the URL which
     *                        is called when clicking the button.</li>
     *                        <li>{@code label}. The label of the button.</li>
     *                        <li>{@code url}. The URL to call when clicking
     *                        the button.</li>
     *                      </ul>
     * @since               1.4
     */
    void checkToolbar(String controller, List<Map<String, Object>> buttons) {
        for (int i = 0; i < buttons.size(); i++) {
            Map btnDef = buttons[i]
            ButtonModule btn = toolbarButtons[i]

            if (btnDef.color) {
                btn.checkColor btnDef.color
            }
            if (btnDef.cssClasses) {
                btn.checkCssClasses btnDef.cssClasses
            }

            String url = btnDef.url
            if (!url) {
                StringBuilder buf = new StringBuilder(browser.baseUrl)
                buf << controller << '/' << btnDef.action
                if (btnDef.id) buf << '/' << btnDef.id
                url = buf.toString()
            }
            assert url == btn.@href

            assert btnDef.label == btn.text()
            if (btnDef.icon) {
                btn.checkIcon btnDef.icon
            }
            if (btnDef.check) {
                btnDef.check btn
            }
        }
    }
}
