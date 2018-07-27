package com.junzixiehui.wwx.infrastructure.tunnel.example;

import com.junzixiehui.wwx.common.config.DbConfig;
import com.junzixiehui.wwx.domain.example.TestExample;
import com.junzixiehui.zorm.annotation.DaoDescription;
import com.junzixiehui.zorm.dao.jdbc.JdbcBaseDao;
import org.springframework.stereotype.Repository;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2018/7/27  下午5:33
 * @version: 1.0
 */
@DaoDescription(settingBeanName = DbConfig.JDBCSETTINGS_FIRST)
@Repository
public class TestExampleDao extends JdbcBaseDao<TestExample> {




}
