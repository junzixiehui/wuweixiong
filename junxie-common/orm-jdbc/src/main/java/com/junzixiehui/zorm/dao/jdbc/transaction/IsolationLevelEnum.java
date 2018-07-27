package com.junzixiehui.zorm.dao.jdbc.transaction;

public enum IsolationLevelEnum {
    DEFAULT(-1),
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);

    private final int value;

    IsolationLevelEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
