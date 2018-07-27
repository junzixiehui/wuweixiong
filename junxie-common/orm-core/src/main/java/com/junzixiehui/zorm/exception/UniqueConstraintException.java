package com.junzixiehui.zorm.exception;

/**
 * 唯一约束异常
 */
@SuppressWarnings("serial")
public class UniqueConstraintException extends DaoException {

    public UniqueConstraintException(String message) {
        super(message);
    }

    public UniqueConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
