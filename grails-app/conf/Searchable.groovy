/*
 * Searchable.groovy
 *
 * Copyright (c) 2011-2013, Daniel Ellermann
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


/**
 * This {@link groovy.util.ConfigObject} script provides Grails searchable
 * plugin configuration.
 *
 * You can use the "environments" section at the end of the file to define
 * per-environment configuration.
 *
 * Note it is NOT required to add a reference to this file in Config.groovy;
 * it is loaded by the plugin itself.
 *
 * Available properties in the binding are:
 *
 * @param userHome      the current user's home directory; same as
 *                      {@code System.properties['user.home']}
 * @param appName       the Grails environment (i. e., "development", "test",
 *                      "production"); same as
 *                      {@code System.properties['app.name']}
 * @param appVersion    the version of your application
 * @param grailsEnv     the Grails environment (i. e., "development", "test",
 *                      "production"); same as
 *                      {@code System.properties['grails.env']}
 *
 * You can also use System.properties to refer to other JVM properties.
 *
 * This file is created by "grails install-searchable-config", and replaces
 * the previous "SearchableConfiguration.groovy"
 */
searchable {

    StringBuilder path = new StringBuilder()
    String s = System.properties[appName + '.dir.base']
    if (s) {
        path << s
    } else {
        s = System.getenv('SPRINGCRM_HOME')
        if (s) {
            path << s
        } else {
            path << userHome << '/.' << appName
        }
    }
    path << '/searchable-index'

    /**
     * The location of the Compass index
     *
     * Examples: "/home/app/compassindex", "ram://app-index" or null to use the
     * default
     *
     * The default is "${userHome}/.grails/projects/${appName}/searchable-index/${grailsEnv}"
     */
    compassConnection = new File(path.toString()).absolutePath

    /**
     * Any settings you wish to pass to Compass
     *
     * Use this to configure custom/override default analyzers, query parsers,
     * e. g.
     *
     *     Map compassSettings = [
     *         'compass.engine.analyzer.german.type': 'German'
     *     ]
     *
     * gives you an analyzer called "german" you can then use in mappings and
     * queries, like
     *
     *    class Book {
     *        static searchable = { content analyzer: 'german' }
     *        String content
     *    }
     *
     *    Book.search("unter", analyzer: 'german')
     *
     * Documentation for Compass settings is here:
     * http://www.compass-project.org/docs/2.1.0M2/reference/html/core-settings.html
     */
    compassSettings = [: ]

    /**
     * Default mapping property exclusions
     *
     * No properties matching the given names will be mapped by default
     * ie, when using "searchable = true"
     *
     * This does not apply for classes using
     * "searchable = [only/except: [...]]" or mapping by closure
     */
    defaultExcludedProperties = ['password']

    /**
     * Default property formats
     *
     * Value is a Map between Class and format string, eg
     *
     *     [(Date): "yyyy-MM-dd'T'HH:mm:ss"]
     *
     * Only applies to class properties mapped as "searchable properties",
     * which are typically simple class types that can be represented as
     * Strings (rather than references or components) AND only required if
     * overriding the built-in format.
     */
    defaultFormats = [: ]

    /**
     * Set default options for each SearchableService/Domain-class method, by
     * method name.
     *
     * These can be overriden on a per-query basis by passing the method a Map
     * of options containing those you want to override.
     *
     * You may want to customise the options used by the search method, which
     * are:
     *
     * @param reload            whether to reload domain class instances from
     *                          the DB: true|false.  If true, the search  will
     *                          be slower but objects will be associated with
     *                          the current Hibernate session
     * @param escape            whether to escape special characters in string
     *                          queries: true|false
     * @param offset            the 0-based hit offset of the first page of
     *                          results.  Normally you wouldn't change it from
     *                          0, it's only here because paging works by using
     *                          an offset + max combo for a specific page
     * @param max               the page size, for paged search results
     * @param defaultOperator   if the query does not otherwise indicate, then
     *                          the default operator applied: "or" or "and".
     *                          If "and" means all terms are required for a
     *                          match, if "or" means any term is required for a
     *                          match
     * @param suggestQuery      if true and search method is returning a
     *                          search-result object (rather than a domain
     *                          class instance, list or count) then a
     *                          "suggestedQuery" property is also added to the
     *                          search-result.  This can also be a Map of
     *                          options as supported by the suggestQuery method
     *                          itself
     *
     * For the options supported by other methods, please see the documentation
     * http://grails.org/Searchable+Plugin
     */
    defaultMethodOptions = [
        search: [
            reload: false, escape: false, offset: 0, max: 10,
            defaultOperator: 'and'
        ],
        suggestQuery: [userFriendly: true]
    ]

    /**
     * Should changes made through GORM/Hibernate be mirrored to the index
     * automatically (using Compass::GPS)?
     *
     * If false, you must manage the index manually using index/unindex/reindex
     *
     * XXX This value had to be set to false because the plugin threw an error
     *      when one-to-many relations were saved
     */
    mirrorChanges = false

    /**
     * Should the database be indexed at startup (using Compass:GPS)?
     *
     * Possible values: true|false|"fork"
     *
     * The value may be a boolean true|false or a string "fork", which means
     * true, and fork a thread for it
     *
     * If you use BootStrap.groovy to insert your data then you should use
     * "true", which means do a non-forking, otherwise "fork" is recommended
     */
    bulkIndexOnStartup = 'fork'

    /**
     * Should index locks be removed (if present) at startup?
     */
    releaseLocksOnStartup = true
}

/* environment specific settings */
environments {

    /* development environment */
    development {
        searchable {
            bulkIndexOnStartup = false
        }
    }

    /* CloudFoundry environment */
    cloud {
        searchable {
            bulkIndexOnStartup = false
            compassConnection = 'ram://test-index'
        }
    }

    /* test environment */
    test {
        searchable {
            bulkIndexOnStartup = false
            compassConnection = 'ram://test-index'
        }
    }
}
