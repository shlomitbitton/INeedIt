package i.need.it.IneedIt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource getDataSource() {
    DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.username("shlomitbitton");
        dataSourceBuilder.password("ineedit");
        dataSourceBuilder.url("jdbc:postgresql:ineedit");
    return dataSourceBuilder.build();
    }
}
