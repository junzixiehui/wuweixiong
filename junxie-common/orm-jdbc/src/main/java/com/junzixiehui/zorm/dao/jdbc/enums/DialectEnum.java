package com.junzixiehui.zorm.dao.jdbc.enums;

/**
 * DB名称枚举
 *
 * @author zhoutao
 */
public enum DialectEnum {
    MYSQL("mysql"),
    ORACLE("oracle"),
    ELASTICSEARCH("elasticsearch");

    private final String value;

    DialectEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
