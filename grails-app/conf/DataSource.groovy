/*
 * DataSource.groovy
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


/* General data source settings */
dataSource {
    dbCreate = 'none'   // updated by database-migration plugin
    pooled = true
    driverClassName = 'com.mysql.jdbc.Driver'
}

/* Cache settings */
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}

/* environment specific settings */
environments {

    /* development environment */
    development {
        dataSource {
            url = 'jdbc:mysql://localhost/springcrm?autoreconnect=true'
            username = 'projects'
            password = 'haluni21'
//            logSql = true
        }
    }

    /* test environment */
    test {
        dataSource {
            driverClassName = 'org.h2.Driver'
            url = 'jdbc:h2:mem:testDb'
//            url = "jdbc:h2:file:${appName}"
            username = 'sa'
            password = ''
            dbunitXmlType = 'flat'
            jndiName = null
            properties {
                connectionInitSqls = [
                    'SET REFERENTIAL_INTEGRITY FALSE'
                ]
            }
        }
    }

    /* production (deployment) environment */
    production {
        dataSource {
            jndiName = 'java:comp/env/jdbc/springcrmDataSource'
        }
    }

    /* live environment on the AMC World server */
    live {
        dataSource {
            jndiName = 'java:comp/env/jdbc/springcrmDataSource'
        }
    }

    /* standalone environment for demonstration purposes */
    standalone {
        dataSource {
            driverClassName = 'org.h2.Driver'
            url = "jdbc:h2:file:${userHome}/.${appName}/database/${appName}"
            username = appName
            password = ''
            properties {
                connectionInitSqls = [
                    'SET REFERENTIAL_INTEGRITY FALSE'
                ]
                validationQuery = 'select 1'
                testWhileIdle = true
                timeBetweenEvictionRunsMillis = 60000
            }
        }
    }
}
