package com.junzixiehui.wwx.web.config.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: qulibin
 * @description: db
 * @date: 17:41 2017/12/7
 * @modify：
 */
@Data
@ConfigurationProperties(prefix = "druid")
public class DruidSettings {
    private int initialSize;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private boolean useUnfairLock;
    private long timeBetweenEvictionRunsMillis;
    private long minEvictableIdleTimeMillis;
    private boolean poolPreparedStatements;
    private int maxPoolPreparedStatementPerConnectionSize;
    private String validationQuery;
    private boolean testWhileIdle;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private String profileEnable;
    private String allow;
}
