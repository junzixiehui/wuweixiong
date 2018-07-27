package com.junzixiehui.zorm.exception;

/**
 * Dao层方法参数异常
 */
@SuppressWarnings("serial")
public class DaoMethodParameterException extends DaoException {

    public DaoMethodParameterException(String message) {
        super(message);
    }

    public DaoMethodParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
