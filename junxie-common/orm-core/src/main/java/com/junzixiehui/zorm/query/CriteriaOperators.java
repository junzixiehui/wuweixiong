package com.junzixiehui.zorm.query;

/**
 * 封装查询条件中的操作符定义
 *
 * @author zhoutao
 */
public enum CriteriaOperators {
    GT(">", "single"),
    GTE(">=", "single"),
    LT("<", "single"),
    LTE("<=", "single"),
    EQ("=", "single"),
    NE("!=", "single"),
    LIKE("LIKE", "single"),
    IN("IN", "list"),
    NIN("NOT IN", "list"),
    ISNULL("IS NULL", "no"),
    ISNOTNULL("IS NOT NULL", "no"),
    //以下match匹配支持ES查询
    MATCH("match", "match"),
    MATCH_AND("match_and", "match_and"),
    MATCH_PHRASE("match_phrase", "match_phrase");

    private String operators;
    private String valueType;

    private CriteriaOperators(String operators, String valueType) {
        this.operators = operators;
        this.valueType = valueType;
    }

    /**
     * @return the operators
     */
    public String getOperators() {
        return operators;
    }

    /**
     * @return the valueType
     */
    public String getValueType() {
        return valueType;
    }

    public boolean match(String operators) {
        return getOperators().equals(operators);
    }

    public static boolean isSingleValueOperator(String operator) {
        if (GT.match(operator) || GTE.match(operator)) {
            return true;
        }
        if (LT.match(operator) || LTE.match(operator)) {
            return true;
        }
        if (EQ.match(operator) || NE.match(operator) || LIKE.match(operator)) {
            return true;
        }
        return false;
    }

    public static boolean isNoValueOperator(String operator) {
        if (ISNOTNULL.match(operator)
                || ISNULL.match(operator)) {
            return true;
        }
        return false;
    }

    public static boolean isMultiValueOperator(String operator) {
        if (IN.match(operator)
                || NIN.match(operator)) {
            return true;
        }
        return false;
    }
}
