package ksbysample.lib.doma2lib.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jmx.export.MBeanExporter;

import javax.sql.DataSource;

@Configuration
@PropertySource(value = "classpath:db-${spring.profiles.active}.properties")
public class DataSourceConfig {

    private final MBeanExporter mbeanExporter;

    public DataSourceConfig(@Autowired(required = false) MBeanExporter mbeanExporter) {
        this.mbeanExporter = mbeanExporter;
    }

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        if (mbeanExporter != null) {
            mbeanExporter.addExcludedBean("dataSource");
        }
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

}
