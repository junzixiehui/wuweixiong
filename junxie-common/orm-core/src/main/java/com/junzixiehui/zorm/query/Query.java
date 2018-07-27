package com.junzixiehui.zorm.query;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 封装基本的Query
 *
 * @author zhoutao
 * @non-threadsafe 线程不安全对象，建议只用作方法内部变量使用
 */
public class Query {
    /**
     * 一次查询中的条件
     */
    private Criteria criteria;
    /**
     * 一次查询中需要返回的字段,如果不设置返回所有
     */
    private final List<String> fields = Lists.newArrayList();
    /**
     * 一次查询中GroupBy
     */
    private final List<GroupBy> groupBys = Lists.newArrayList();
    /**
     * 一次查询中order by
     */
    private final List<OrderBy> orderBys = Lists.newArrayList();
    private int offset;
    private int limit;
    private String hint;
    /**
     * 针对mybatis的statmentId
     */
    private String sqlId;

    private Query() {
    }

    private Query(Criteria criteria) {
        this.criteria = criteria;
    }

    public static Query query() {
        return new Query();
    }

    public static Query query(Criteria criteria) {
        return new Query(criteria);
    }

    public Query criteria(Criteria criteria) {
        this.criteria = criteria;
        return this;
    }

    public Query withOrderBy(OrderBy... orderByArr) {
        for (OrderBy orderBy : orderByArr) {
            orderBys.add(orderBy);
        }
        return this;
    }

    public Query withGroupBy(GroupBy... groupByArr) {
        for (GroupBy groupBy : groupByArr) {
            groupBys.add(groupBy);
        }
        return this;
    }

    public Query includeField(String... fieldArr) {
        for (String field : fieldArr) {
            fields.add(field);
        }
        return this;
    }

    public Query offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Query withHint(String hint) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(hint), "Param key was %s, It must be not null or empty", hint);
        this.hint = hint;
        return this;
    }

    public Query withSqlId(String sqlId) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(sqlId), "Param sqlId was %s, It must be not null or empty", sqlId);
        this.sqlId = sqlId;
        return this;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public int getLimit() {
        return limit;
    }

    public String getHint() {
        return hint;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<GroupBy> getGroupBys() {
        return groupBys;
    }

    public List<OrderBy> getOrderBys() {
        return orderBys;
    }

    public String getSqlId() {
        return sqlId;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Query [");
        Criteria c = criteria;
        if (c == null) {
            builder.append("Criteria [null]");
        } else {
            builder.append(c.toString());
        }
        builder.append(", fields=");
        builder.append(fields);
        builder.append(", groupBys=");
        builder.append(groupBys);
        builder.append(", orderBys=");
        builder.append(orderBys);
        builder.append(", offset=");
        builder.append(offset);
        builder.append(", limit=");
        builder.append(limit);
        builder.append(", hint=");
        builder.append(hint);
        builder.append(", sqlId=");
        builder.append(sqlId);
        builder.append("]");
        return builder.toString();
    }
}
