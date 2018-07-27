package com.junzixiehui.zorm.exception;

/**
 * dao层统一用此异常
 */
@SuppressWarnings("serial")
public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
