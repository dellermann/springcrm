dataSource {
    pooled = true
	driverClassName = 'com.mysql.jdbc.Driver'
    username = 'projects'
    password = 'haluni21'
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = 'update' // one of 'create', 'create-drop','update'
            //url = 'jdbc:hsqldb:mem:devDB'
			url = 'jdbc:mysql://localhost/springcrm?autoreconnect=true'
        }
    }
    test {
        dataSource {
			driverClassName = 'org.h2.Driver'
            dbCreate = 'update'
            url = 'jdbc:h2:mem:testDb'
			username = 'sa'
			password = ''
        }
    }
    production {
        dataSource {
            dbCreate = 'update'
			url = 'jdbc:mysql://localhost/springcrm'
//			dialect = org.hibernate.dialect.MySQLInnoDBDialect
			properties {
				validationQuery = 'select 1'
				testWhileIdle = true
				timeBetweenEvictionRunsMillis = 60000
			}
        }
    }
    live {
        dataSource {
            dbCreate = 'none'
            url = 'jdbc:mysql://localhost/springcrm_old?autoreconnect=true'
        }
    }
}
