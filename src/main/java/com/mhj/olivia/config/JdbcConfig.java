package com.mhj.olivia.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class JdbcConfig {
	
	private static int MAX_POOL_SIZE = BatchConfiguration.THREAD_POOL * 3;
	private static int MIN_IDLE = BatchConfiguration.THREAD_POOL / 10;
	private static int IDLE_TIMEOUT = 600000;
	
	@Value("spring.batch.datasource.url")
	private String url;
	
	@Value("spring.batch.datasource.username")
	private String username;
	
	@Value("spring.batch.datasource.password")
	private String password;
	
	@Value("spring.batch.datasource.driver-class-name")
	private String driver;
	
	@Value("spring.batch.datasource.schema-username")
	private String schema;
	
	@Bean(name = "h2DataSource")
	@Primary
	public DataSource dataSourceH2() {
		HikariDataSource hikari = new HikariDataSource();
		hikari.setJdbcUrl("jdbc:h2:mem:thing:H2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
		hikari.addDataSourceProperty("driverClassName", "org.h2.Driver");
		hikari.addDataSourceProperty("username", "mhj04");
		hikari.addDataSourceProperty("password", "");
		
		hikari.setMaximumPoolSize(MAX_POOL_SIZE);
		hikari.setMinimumIdle(MIN_IDLE);
		hikari.setIdleTimeout(IDLE_TIMEOUT);
		
		hikari.addDataSourceProperty("cachePrepStmts", true);
		hikari.addDataSourceProperty("prepStmtCacheSize", 250);
		hikari.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
		hikari.addDataSourceProperty("useServerPrepStmts", true);
		hikari.addDataSourceProperty("cacheResultSetMetadata", true);
		return hikari;
	}
	
	@Bean(name = "mySqlDataSource")
	public DataSource dataSourceMySql() {
		HikariDataSource hikari = new HikariDataSource();
		hikari.setJdbcUrl(url);
		hikari.addDataSourceProperty("driverClassName", driver);
		hikari.addDataSourceProperty("username", username);
		hikari.addDataSourceProperty("password", password);
		
		hikari.setMaximumPoolSize(MAX_POOL_SIZE);
		hikari.setMinimumIdle(MIN_IDLE);
		hikari.setIdleTimeout(IDLE_TIMEOUT);
		
		hikari.addDataSourceProperty("cachePrepStmts", true);
		hikari.addDataSourceProperty("prepStmtCacheSize", 250);
		hikari.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
		hikari.addDataSourceProperty("useServerPrepStmts", true);
		hikari.addDataSourceProperty("cacheResultSetMetadata", true);
		return hikari;
	}
	

}
