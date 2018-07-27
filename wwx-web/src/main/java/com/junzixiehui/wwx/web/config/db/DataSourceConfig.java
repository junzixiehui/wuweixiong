package com.junzixiehui.wwx.web.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.junzixiehui.wwx.common.config.DbConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.sql.SQLException;

/**
 * <p>Description: 配置数据源</p>
 *
 * @author: by qulibin
 * @date: 2017/12/07  16:34
 * @version: 1.0
 */
@Configuration
@EnableConfigurationProperties(value = {
        DruidSettings.class
})
public class DataSourceConfig {

    @Resource
    DruidSettings druidSettings;
    @Resource
	DataSourceSettingsMasterFirst dataSourceSettingsMasterFirst;
    @Resource
	DataSourceSettingsSlaveFirst dataSourceSettingsSlaveFirst;






    /**
     * @author: qulibin
     * @description: 主库
     * @date: 17:55 2017/12/7
     * @return:
     */
    @Bean(name = DbConfig.FIRST_MASTER_DB_DATASOURCE)
    @Primary
    public DruidDataSource firstDataSource() throws SQLException {
        return createDataSource(dataSourceSettingsMasterFirst);
    }

    /**
     * @author: qulibin
     * @description: 从库
     * @date: 17:55 2017/12/7
     * @return:
     */
    @Bean(name = DbConfig.FIRST_SLAVE_DB_DATASOURCE)
    public DruidDataSource firstDataSourceSlave() throws SQLException {
        return createDataSource(dataSourceSettingsSlaveFirst);
    }



    private  DruidDataSource createDataSource(DataSourceSettings dataSourceSettings){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dataSourceSettings.getUrl());
        druidDataSource.setDriverClassName(dataSourceSettings.getDriverClassName());
        druidDataSource.setUsername(dataSourceSettings.getUsername());
        druidDataSource.setPassword(dataSourceSettings.getPassword());
        try {
            druidDataSource.setFilters("stat,wall");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        druidDataSource.setInitialSize(druidSettings.getInitialSize());
        druidDataSource.setMinIdle(druidSettings.getMinIdle());
        druidDataSource.setMaxActive(druidSettings.getMaxActive());
        druidDataSource.setUseUnfairLock(druidSettings.isUseUnfairLock());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidSettings.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(druidSettings.getMinEvictableIdleTimeMillis());
        druidDataSource.setPoolPreparedStatements(druidSettings.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidSettings.getMaxPoolPreparedStatementPerConnectionSize());
        druidDataSource.setValidationQuery(druidSettings.getValidationQuery());
        druidDataSource.setTestWhileIdle(druidSettings.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidSettings.isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidSettings.isTestOnReturn());
        return druidDataSource;
    }




}
