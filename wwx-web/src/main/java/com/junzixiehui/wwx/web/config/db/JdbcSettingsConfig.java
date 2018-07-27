package com.junzixiehui.wwx.web.config.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import com.junzixiehui.wwx.common.config.DbConfig;
import com.junzixiehui.zorm.dao.jdbc.JdbcSettings;
import com.junzixiehui.zorm.dao.jdbc.enums.DialectEnum;
import com.junzixiehui.zorm.dao.jdbc.transaction.TransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * dao配置对象
 * 所有的dao对象需要对应的setting bean才能具备数据库访问能力
 */
@Configuration
@EnableConfigurationProperties(value = {
        DataSourceSettingsMasterFirst.class,
        DataSourceSettingsSlaveFirst.class,
})
public class JdbcSettingsConfig {

    @Autowired
    @Qualifier(DbConfig.FIRST_MASTER_DB_DATASOURCE)
    DruidDataSource firstDataSource;
    @Autowired
    @Qualifier(DbConfig.FIRST_SLAVE_DB_DATASOURCE)
    DruidDataSource firstDataSourceSlave;







    @Bean(name = DbConfig.JDBCSETTINGS_FIRST)
    public JdbcSettings jdbcSettings_first() {
        List<DataSource> dataSourceList = Lists.newArrayList(firstDataSource);
        List<DataSource> dataSourceSlaveList = Lists.newArrayList(firstDataSourceSlave);
        return newJdbcSettings(dataSourceList, dataSourceSlaveList);
    }



    @Bean(name = DbConfig.TRANSACTION_MANAGER_FIRST)
    public TransactionManager transactionManager_first() {
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(firstDataSource);
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.setTxManager(platformTransactionManager);

        return transactionManager;
    }

    private JdbcSettings newJdbcSettings(List<DataSource> masterDataSourceList, List<DataSource> slaveDataSourceList) {
        JdbcSettings jdbcSettings = new JdbcSettings();
        jdbcSettings.setDialectEnum(DialectEnum.MYSQL);
        jdbcSettings.setWriteDataSource(Lists.newArrayList(masterDataSourceList));
        jdbcSettings.setReadDataSource(Lists.newArrayList(slaveDataSourceList));
        return jdbcSettings;
    }

}
