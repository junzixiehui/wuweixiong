package com.junzixiehui.zorm.dao.jdbc.codegenerate;

import com.junzixiehui.zorm.exception.DaoException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 根据数据库表生成对应的mapper、dao、entity
 *
 * @Author zhoutao
 * @Date 2016/9/18
 */
public class GenerateHelper {
    public GenerateHelper(String modulePackagePath, String modulePackageName, String tableName, JdbcTemplate jdbcTemplate) {

    }

    /**
     * 生成基于mybatis或jdbc的dao、entity代码
     */
    public void genterate() {
        throw new DaoException("默认生成代码是关闭的");
    }
}
