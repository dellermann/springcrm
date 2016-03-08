/*
 * UserSettings.java
 *
 * Copyright (c) 2011-2016, Daniel Ellermann
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


package org.amcworld.springcrm;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codehaus.groovy.runtime.InvokerHelper;


/**
 * The class {@code UserSettings} is a map implementation which represents the
 * settings of a particular user.  The map implementation allows retrieval and
 * modification of user settings which is backed up by the underlying GORM
 * storage (usually a database).
 *
 * @author  Daniel Ellermann
 * @version 2.0
 * @since   1.2
 */
public class UserSettings extends AbstractMap<String, String> {

    //-- Instance variables ---------------------

    private AbstractSet<Map.Entry<String, String>> entrySet;

    /**
     * The underlying map containing user settings for each setting name.
     */
    protected Map<String, UserSetting> settings;

    /**
     * The user the settings belong to.
     */
    protected User user;


    //-- Constructors ---------------------------

    /**
     * Creates a new instance of user settings for the given user.
     *
     * @param user  the given user
     */
    public UserSettings(User user) {
        this.user = user;
        List<UserSetting> rawSettings = user.getRawSettings();
        if (rawSettings == null) {
            this.settings = new HashMap<String, UserSetting>();
        } else {
            this.settings =
                new HashMap<String, UserSetting>(rawSettings.size() + 16);

            for (UserSetting us : rawSettings) {
                this.settings.put(us.getName(), us);
            }
        }
    }


    //-- Public methods -------------------------

    @Override
    public Set<Map.Entry<String, String>> entrySet() {
        if (entrySet == null) {
            entrySet = new AbstractSet<Map.Entry<String, String>>() {

                //-- Instance variables ---------------------

                private Set<Map.Entry<String, UserSetting>> entries =
                    settings.entrySet();


                //-- Public methods -------------------------

                @Override
                public Iterator<Map.Entry<String, String>> iterator() {
                    return new Iterator<Map.Entry<String, String>>() {

                        //-- Instance variables -----------------

                        private UserSetting current;
                        private Iterator<Map.Entry<String, UserSetting>> iter =
                            entries.iterator();


                        //-- Public methods ---------------------

                        @Override
                        public boolean hasNext() {
                            return iter.hasNext();
                        }

                        @Override
                        public Map.Entry<String, String> next() {
                            current = iter.next().getValue();
                            return new Entry(current);
                        }

                        @Override
                        public void remove() {
                            HashMap<String, Object> params =
                                new HashMap<String, Object>(1);
                            params.put("flush", true);
                            InvokerHelper.invokeMethod(
                                current, "delete", params
                            );
                            iter.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return entries.size();
                }
            };
        }
        return entrySet;
    }

    @Override
    public String get(Object key) {
        UserSetting us = settings.get(key);
        return (us == null) ? null : us.getValue();
    }

    /**
     * Stores a new user setting with the given key and value in the underlying
     * data storage.  The method either updates an underlying
     * {@code UserSetting} object or creates a new one.
     *
     * @param key   the key indicating the setting
     * @param value the value to set
     * @return      the old value; {@code null} if no previous value was
     *              stored
     */
    @Override
    public String put(String key, String value) {
        UserSetting setting = settings.get(key);
        String oldValue = null;
        if (setting == null) {
            setting = new UserSetting();
            setting.setUser(user);
            setting.setName(key);
            setting.setValue(value);
            settings.put(key, setting);
        } else {
            InvokerHelper.invokeMethod(setting, "attach", null);
            oldValue = setting.getValue();
            setting.setValue(value);
        }
        HashMap<String, Object> params = new HashMap<String, Object>(2);
        params.put("flush", true);
        params.put("failOnError", true);
        InvokerHelper.invokeMethod(setting, "save", params);
        return oldValue;
    }


    //-- Inner classes --------------------------

    /**
     * The class {@code Entry} represents an entry in the user settings map.
     * The entry maps a string key to a string value and furthermore stores the
     * underlying UserSetting object for updates when calling {@code setValue}.
     *
     * @author  Daniel Ellermann
     * @version 1.2
     * @since   1.2
     */
    static class Entry implements Map.Entry<String, String> {

        //-- Instance variables ---------------------

        /**
         * The underlying user setting.
         */
        UserSetting setting;


        //-- Constructors -----------------------

        /**
         * Creates a new entry in the user settings map using the given
         * underlying {@code UserSetting} object.
         *
         * @param setting   the given underlying {@code UserSetting} object
         */
        public Entry(UserSetting setting) {
            this.setting = setting;
        }


        //-- Public methods -------------------------

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Entry) {
                return getKey().equals(((Entry) obj).getKey());
            } else {
                return false;
            }
        }

        @Override
        public String getKey() {
            return setting.getName();
        }

        @Override
        public String getValue() {
            return setting.getValue();
        }

        @Override
        public int hashCode() {
            return getKey().hashCode();
        }

        /**
         * Sets the value of the user settings entry and stores it in the
         * underlying {@code UserSetting} object and in the data storage.
         *
         * @param value the value to set
         */
        @Override
        public String setValue(String value) {
            String oldValue = setting.getValue();
            setting.setValue(value);
            HashMap<String, Object> params = new HashMap<String, Object>(2);
            params.put("flush", true);
            params.put("failOnError", true);
            InvokerHelper.invokeMethod(setting, "save", params);
            return oldValue;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder(getKey());
            buf.append('=');
            buf.append(getValue());
            return buf.toString();
        }
    }
}
