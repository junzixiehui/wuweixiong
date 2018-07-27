package com.junzixiehui.zorm.dao.jdbc.transaction;

/**
 * 事务回调接口
 *
 * @author zhoutao
 */
public interface TransactionCallback {

    /**
     * doTransaction中不要吞掉异常
     */
    Object doTransaction();
}
