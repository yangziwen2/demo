package com.example.demo.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "db.default")
@MapperScan(value = "com.example.demo.dal.dao",
		annotationClass = Repository.class,
		sqlSessionFactoryRef = "defaultSqlSessionFactory")
public class DbConfig {

	private String username;

	private String password;

	private String url;

	@Bean
    @Primary
    public DataSource defaultDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(100);
        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    public SqlSessionFactory defaultSqlSessionFactory(
            @Qualifier("defaultDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setConfigLocation(new ClassPathResource("mybatis_config.xml"));
        bean.setVfs(SpringBootVFS.class);
        return bean.getObject();
    }

}
