dataSource {
	pooled = true
	driverClassName = "org.gjt.mm.mysql.Driver"
	username = "root"
	password = "password"
}
hibernate {
	cache.use_second_level_cache = true
	cache.use_query_cache = true
	cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
	dialect = 'org.hibernate.dialect.MySQLDialect'
}
// environment specific settings
environments {
	development {
		dataSource { //dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost:3306/algoTrader" }
	}
}
