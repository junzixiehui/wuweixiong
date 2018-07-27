package com.junzixiehui.wwx.web.config.db;

import com.junzixiehui.wwx.common.config.DbConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: qulibin
 * @description:
 * @date: 17:52 2017/12/7
 * @modify：
 */
@ConfigurationProperties(prefix = DbConfig.FIRST_DB_PREFIX_SLAVE)
public class DataSourceSettingsSlaveFirst extends DataSourceSettings{

}
