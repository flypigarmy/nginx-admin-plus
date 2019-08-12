package com.jslsolucoes.nginx.admin.jpa;

import br.com.caelum.vraptor.jpa.DefaultEntityManagerConfiguration;
import com.jslsolucoes.nginx.admin.ui.config.Configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Specializes @ApplicationScoped public class OverrideEntityManagerConfiguration
		extends DefaultEntityManagerConfiguration {

	@Inject private Configuration configuration;

	@Override public Map<String, Object> properties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", dialect());
		properties.put("hibernate.default_schema", schema());
		return properties;
	}

	private String schema() {
		String driver = configuration.getDatabase().getDriver();
		if (driver.equals("h2")) {
			return configuration.getDatabase().getName();
		} else if (driver.equals("mysql") || driver.equals("oceanbase")) {
			return configuration.getDatabase().getName();
		} else if (driver.equals("mariadb")) {
			return configuration.getDatabase().getName();
		} else if (driver.equals("oracle")) {
			return configuration.getDatabase().getUsername();
		} else if (driver.equals("postgresql")) {
			return configuration.getDatabase().getName();
		} else if (driver.equals("sqlserver")) {
			return configuration.getDatabase().getName();
		}
		throw new RuntimeException("Could not find schema for driver " + driver);
	}

	public String dialect() {
		String driver = configuration.getDatabase().getDriver();
		if (driver.equals("h2")) {
			return "org.hibernate.dialect.H2Dialect";
		} else if (driver.equals("mysql") || driver.equals("oceanbase")) {
			return "org.hibernate.dialect.MySQL5Dialect";
		} else if (driver.equals("mariadb")) {
			return "org.hibernate.dialect.MySQL5Dialect";
		} else if (driver.equals("oracle")) {
			return "org.hibernate.dialect.Oracle10gDialect";
		} else if (driver.equals("postgresql")) {
			return "org.hibernate.dialect.PostgreSQLDialect";
		} else if (driver.equals("sqlserver")) {
			return "org.hibernate.dialect.SQLServer2008Dialect";
		}
		throw new RuntimeException("Could not find dialect for driver " + driver);
	}
}
