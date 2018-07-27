package com.junzixiehui.wwx.web.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.junzixiehui.wwx.common.config.DbConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <p>Description: JdbcTemplate </p>
 *
 * @author: by qulibin
 * @date: 2018/1/16  16:39
 * @version: 1.0
 */
@Configuration
public class JdbcTemplateConfig {



    @Autowired
    @Qualifier(DbConfig.FIRST_MASTER_DB_DATASOURCE)
    DruidDataSource firstDataSource;
    @Autowired
    @Qualifier(DbConfig.FIRST_SLAVE_DB_DATASOURCE)
    DruidDataSource firstDataSourceSlave;



    @Bean(name = "firstJdbcTemplate")
    public JdbcTemplate firstJdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(firstDataSource);
        return jdbcTemplate;
    }

    @Bean(name = "firstSlaveJdbcTemplate")
    public JdbcTemplate firstSlaveJdbcTemplate(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(firstDataSourceSlave);
        return jdbcTemplate;
    }





}
