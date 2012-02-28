/*
 * DataSource.groovy
 *
 * Copyright (c) 2011-2012, AMC World Technologies GmbH
 * Fischerinsel 1, D-10179 Berlin, Deutschland
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of AMC World
 * Technologies GmbH ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with AMC World Technologies GmbH.
 */


/* General data source settings */
dataSource {
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
            dbCreate = 'none'   // updated by database-migration plugin
            url = 'jdbc:mysql://localhost/springcrm?autoreconnect=true'
            username = 'projects'
            password = 'haluni21'
        }
    }

    /* test environment */
    test {
        dataSource {
            driverClassName = 'org.h2.Driver'
            dbCreate = 'update'
            url = 'jdbc:h2:mem:testDb'
            username = 'sa'
            password = ''
        }
    }

    /* production (deployment) environment */
    production {
        dataSource {
            dbCreate = 'update'
            url = 'jdbc:mysql://localhost/springcrm'
            username = 'springcrm'
            password = 'springcrm'
            properties {
                validationQuery = 'select 1'
                testWhileIdle = true
                timeBetweenEvictionRunsMillis = 60000
            }
        }
    }

    /* live enviroment on the AMC World server */
    live {
        dataSource {
            dbCreate = 'none'   // updated by database-migration plugin
            url = 'jdbc:mysql://db.amc-world.home/springcrm?autoreconnect=true'
            username = 'projects'
            password = 'haluni21'
            properties {
                validationQuery = 'select 1'
                testWhileIdle = true
                timeBetweenEvictionRunsMillis = 60000
            }
        }
    }

    /* standalone environment for demonstration purposes */
    standalone {
        dataSource {
            driverClassName = 'org.h2.Driver'
            dbCreate = 'update'
            url = "jdbc:h2:file:${userHome}/.${appName}/database/springcrm"
            username = 'springcrm'
            password = ''
            properties {
                validationQuery = 'select 1'
                testWhileIdle = true
                timeBetweenEvictionRunsMillis = 60000
            }
        }
    }
}
