package com.junzixiehui.zorm.dao.jdbc;

import com.google.common.collect.Lists;
import com.junzixiehui.zorm.dao.DatabaseRouter;
import com.junzixiehui.zorm.dao.jdbc.transaction.TransactionContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 简单JdbcTemplate路由器
 * TODO:后续结合sharding-jdbc
 *
 * @Author zhoutao
 * @Date 2017/6/8
 */
public class JdbcDatabaseRouter implements DatabaseRouter {
    private List<JdbcTemplate> writeJdbcTemplate = Lists.newArrayList();
    private List<JdbcTemplate> readJdbcTemplate = Lists.newArrayList();

    public JdbcDatabaseRouter(JdbcSettings jdbcSettings) {
        //write
        List<DataSource> writeList = jdbcSettings.getWriteDataSource();
        if (CollectionUtils.isNotEmpty(writeList)) {
            for (DataSource dataSource : writeList) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                writeJdbcTemplate.add(jdbcTemplate);
            }
        }

        //read
        List<DataSource> readList = jdbcSettings.getReadDataSource();
        if (CollectionUtils.isNotEmpty(readList)) {
            for (DataSource dataSource : readList) {
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                readJdbcTemplate.add(jdbcTemplate);
            }
        }
    }

    @Override
    public Object writeRoute() {
        return writeJdbcTemplate.get(0);
    }

    @Override
    public Object readRoute() {
        if (TransactionContext.isInTransaction() || CollectionUtils.isEmpty(readJdbcTemplate)) {
            return this.writeRoute();
        }
        int randomPos = ThreadLocalRandom.current().nextInt(readJdbcTemplate.size());
        return readJdbcTemplate.get(randomPos);
    }
}
