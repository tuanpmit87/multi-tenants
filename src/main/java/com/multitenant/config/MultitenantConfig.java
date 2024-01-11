package com.multitenant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class MultiTenantConfig {
    @Value("${defaultTenant}")
    private String defaultTenant;

    @Bean
    @ConfigurationProperties(prefix = "tenants")
    public DataSource dataSource() {
        File[] files = Paths.get("src/main/all_tenants").toFile().listFiles();
        Map<Object, Object> resolvedDataSources = new HashMap<>();

        if (files != null) {
            for (File propertyFile : files) {
                Properties tenantProperties = new Properties();
                DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

                try {
                    tenantProperties.load(new FileInputStream(propertyFile));
                    String tenantId = tenantProperties.getProperty("name");

                    dataSourceBuilder.driverClassName(tenantProperties.getProperty("datasource.driver-class-name"));
                    dataSourceBuilder.username(tenantProperties.getProperty("datasource.username"));
                    dataSourceBuilder.password(tenantProperties.getProperty("datasource.password"));
                    dataSourceBuilder.url(tenantProperties.getProperty("datasource.url"));
                    resolvedDataSources.put(tenantId, dataSourceBuilder.build());
                } catch (IOException exp) {
                    throw new RuntimeException("có lỗi trong datasource của tenant:" + exp);
                }
            }
        }

        AbstractRoutingDataSource dataSource = new RoutingDataSource();
        dataSource.setDefaultTargetDataSource(resolvedDataSources.get(defaultTenant));
        dataSource.setTargetDataSources(resolvedDataSources);

        dataSource.afterPropertiesSet();
        return dataSource;
    }
}
