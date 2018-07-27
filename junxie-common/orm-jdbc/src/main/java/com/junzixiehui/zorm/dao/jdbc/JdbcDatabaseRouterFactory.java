package com.junzixiehui.zorm.dao.jdbc;

import com.google.common.collect.Maps;
import com.junzixiehui.zorm.dao.DaoSettings;
import com.junzixiehui.zorm.dao.DatabaseRouter;
import com.junzixiehui.zorm.dao.DatabaseRouterFactory;
import com.junzixiehui.zorm.exception.DaoException;


import java.util.HashMap;

/**
 * @Author zhoutao
 * @Date 2017/5/17
 */
public class JdbcDatabaseRouterFactory implements DatabaseRouterFactory {
    static final JdbcDatabaseRouterFactory INSTANCE = new JdbcDatabaseRouterFactory();
    private HashMap<JdbcSettings, JdbcDatabaseRouter> jdbcTemplateRouterMap = Maps.newHashMap();

    /**
     * JdbcTemplateRouter的获取发生在项目运行中
     *
     * @param daoSettings - daoSettings
     */
    @Override
    public DatabaseRouter getDatabaseRouter(DaoSettings daoSettings) {
        return jdbcTemplateRouterMap.get(daoSettings);
    }

    /**
     * JdbcTemplateRouter的创建工作发生在项目启动过程
     *
     * @param daoSettings - daoSettings
     */
    @Override
    public synchronized void setDatabaseRouter(DaoSettings daoSettings) {
        if (getDatabaseRouter(daoSettings) != null) {
            return;
        }
        JdbcSettings jdbcSettings = (JdbcSettings) daoSettings;
        try {
            jdbcTemplateRouterMap.put(jdbcSettings, new JdbcDatabaseRouter(jdbcSettings));
        } catch (RuntimeException e) {
            throw new DaoException("无法生产JdbcTemplateRouter[" + jdbcSettings + "]", e);
        }
    }
}
