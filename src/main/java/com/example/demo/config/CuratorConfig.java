package com.example.demo.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CuratorConfig {

    @Bean(destroyMethod = "close")
    public CuratorFramework curator(Environment env) {
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString(env.getProperty("zookeeper.connect.address"))
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .build();
        curator.start();
        return curator;
    }

}
