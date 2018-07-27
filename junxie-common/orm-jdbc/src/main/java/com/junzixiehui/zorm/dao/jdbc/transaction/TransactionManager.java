package com.junzixiehui.zorm.dao.jdbc.transaction;

import com.junzixiehui.zorm.exception.DaoExceptionTranslator;
import lombok.Setter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 用编程事物限制开发过程随意使用大事物
 * 和普通注解一样可以使用指定的隔离级别和事物超时
 * 但强制要求使用spring默认的事物传播行为即要求
 * PROPAGATION_REQUIRED有事物就加入当前事物，没有就开启一个事物
 *
 * @author zhoutao
 * @since 2015/11/24
 */
public class TransactionManager {
    @Setter
    private PlatformTransactionManager txManager;

    /**
     * 开启一个事物，使用数据库默认的隔离级别
     *
     * @param transactionCallback
     */
    public Object doInTransaction(TransactionCallback transactionCallback) {
        return this.doInTransaction(transactionCallback, TransactionSettings.builder().build());
    }

    /**
     * 开启一个事物，使用指定的transactionDefinition
     *
     * @param transactionCallback
     * @param transactionSettings
     */
    public Object doInTransaction(TransactionCallback transactionCallback, TransactionSettings transactionSettings) {
        Object object;
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(transactionSettings.getIsolationLevel().value());
        def.setTimeout(transactionSettings.getTimeout());
        TransactionStatus ts = txManager.getTransaction(def);
        boolean isNested = TransactionContext.isInTransaction();//是否嵌套事物
        try {
            if (!isNested) {
                TransactionContext.enterTransaction();//嵌套事物由外层标记enter
            }
            object = transactionCallback.doTransaction();
            txManager.commit(ts);
        } catch (Throwable ex) {
            txManager.rollback(ts);
            throw DaoExceptionTranslator.translate(ex);
        } finally {
            if (!isNested) {//嵌套事物由外层标记leave
                TransactionContext.leaveTransaction();
            }
        }
        return object;
    }
}
