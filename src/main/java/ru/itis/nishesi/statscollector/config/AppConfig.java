package ru.itis.nishesi.statscollector.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.itis.nishesi.statscollector")
@PropertySource("classpath:app.properties")
public class AppConfig {

    @Bean(destroyMethod = "close")
    public HikariDataSource dataSource(@Value("${db.url}") String url,
                                       @Value("${db.username}") String username,
                                       @Value("${db.password}") String password,
                                       @Value("${db.maxPoolSize}") int maxPoolSize
    ) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(maxPoolSize);

        return new HikariDataSource(hikariConfig);
    }


}
