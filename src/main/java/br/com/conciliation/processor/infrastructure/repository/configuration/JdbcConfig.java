package br.com.conciliation.processor.infrastructure.repository.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConfig.class);

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.host}")
	private String host;

	@Value("${jdbc.port}")
	private int port;

	@Value("${jdbc.database}")
	private String database;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Value("${jdbc.properties.schema}")
	private String schema; // Schema public

	@Value("${datasource.driver-class-name}")
	private String driverClassName;

	@Bean(name = "dataSource")
	public DataSource jdbcDataSource() {
		try {
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			dataSource.setSchema(schema);
			dataSource.setDriverClassName("org.postgresql.Driver");
			return dataSource;
		}
		catch (Exception e) {
			LOGGER.error("Error creating DataSource: {}", e.getMessage(), e);
			throw e;
		}
	}

}
