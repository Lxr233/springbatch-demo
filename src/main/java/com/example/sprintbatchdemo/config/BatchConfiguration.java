package com.example.sprintbatchdemo.config;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @description: 配置这个类让batch不再依赖数据源
 * @author: l00427576
 * @create: 2019-05-21 15:27
 **/
@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
public class BatchConfiguration extends DefaultBatchConfigurer {

    @Override
    public void setDataSource(DataSource dataSource) {
        // override to do not set datasource even if a datasource exist.
        // initialize will use a Map based JobRepository (instead of database)
    }

}