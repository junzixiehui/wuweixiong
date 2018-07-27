package com.junzixiehui.zorm.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * dao层异常转换器
 *
 * @Author zhoutao
 * @Date 2017/6/20
 */
@Slf4j
public class DaoExceptionTranslator {

    private DaoExceptionTranslator() {
    }

    public static DaoException translate(Throwable ex) {
        if (ex instanceof IllegalArgumentException) {
            return new DaoMethodParameterException("Dao Param Exception[" + ex.getMessage() + "]");
        } else if (ex instanceof DaoException) {
            return (DaoException) ex;
        } else {
            log.error("Dao layer error:", ex);
            return new DaoException("Dao Unknown Exception[" + ex.getMessage() + "]", ex);
        }
    }
}
