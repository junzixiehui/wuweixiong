package com.junzixiehui.zorm.query;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.*;

/**
 * 封装基本的查询条件原语 用于封装查询条件，dao需要根据具体的orm框架转换
 *
 * @author zhoutao
 * @non-threadsafe 线程不安全对象，建议只用作方法内部变量使用
 */
public class Criteria {

    /**
     * 一个Criteria实例对应一个key
     */
    private String key;
    /**
     * 一个Criteria实例对应一个操作符
     *
     * @see CriteriaOperators
     */
    private String operator;
    /**
     * 一个Criteria实例对应一个操作值,可以为null
     */
    private Object value;

    /**
     * 一个Criteria实例and链接其他的实例构成一个条件链
     */
    private List<Criteria> criteriaChain;
    /**
     * @see CriteriaOperators
     */
    private String valueType;

    private Criteria(String key, CriteriaOperators criteriaOperators, Object value) {
        this.key = key;
        this.operator = criteriaOperators.getOperators();
        this.valueType = criteriaOperators.getValueType();
        this.value = value;
        this.criteriaChain = null;
    }

    /**
     * 默认使用EQ方式构造一个条件
     *
     * @param key
     * @param value
     * @return
     */
    public static Criteria where(String key, Object value) {
        CriteriaOperators criteriaOperators = CriteriaOperators.EQ;
        checkKey(key);
        Criteria criteria = new Criteria(key, criteriaOperators, value);
        List<Criteria> criteriaChain = new ArrayList<Criteria>();
        criteriaChain.add(criteria);
        criteria.setCriteriaChain(criteriaChain);
        return criteria;
    }

    public static Criteria where(String key, CriteriaOperators criteriaOperators, Object value) {
        checkKey(key);
        Criteria criteria = new Criteria(key, criteriaOperators, value);
        List<Criteria> criteriaChain = new ArrayList<Criteria>();
        criteriaChain.add(criteria);
        criteria.setCriteriaChain(criteriaChain);
        return criteria;
    }

    public static Criteria where(String key, CriteriaOperators criteriaOperators) {
        checkKey(key);
        Criteria criteria = new Criteria(key, criteriaOperators, null);
        List<Criteria> criteriaChain = new ArrayList<Criteria>();
        criteriaChain.add(criteria);
        criteria.setCriteriaChain(criteriaChain);
        return criteria;
    }

    public Criteria lt(String key, Object value) {
        checkCompareCriteria(key, value);
        Criteria c = new Criteria(key, CriteriaOperators.LT, value);
        criteriaChain.add(c);
        return this;
    }

    public Criteria lte(String key, Object value) {
        checkCompareCriteria(key, value);
        Criteria c = new Criteria(key, CriteriaOperators.LTE, value);
        criteriaChain.add(c);
        return this;
    }

    public Criteria gt(String key, Object value) {
        checkCompareCriteria(key, value);
        Criteria c = new Criteria(key, CriteriaOperators.GT, value);
        criteriaChain.add(c);
        return this;
    }

    public Criteria gte(String key, Object value) {
        checkCompareCriteria(key, value);
        Criteria c = new Criteria(key, CriteriaOperators.GTE, value);
        criteriaChain.add(c);
        return this;
    }

    public Criteria eq(String key, Object value) {
        checkEQCriteria(key, value);
        Criteria c = new Criteria(key, CriteriaOperators.EQ, value);
        criteriaChain.add(c);
        return this;
    }

    public Criteria ne(String key, Object value) {
        checkEQCriteria(key, value);
        Criteria c = new Criteria(key, CriteriaOperators.NE, value);
        criteriaChain.add(c);
        return this;
    }

    public Criteria in(String key, Object... values) {
        checkCollectionCriteria(key, values);
        Criteria c = new Criteria(key, CriteriaOperators.IN, Arrays.asList(values));
        criteriaChain.add(c);
        return this;
    }

    public Criteria in(String key, List<?> values) {
        checkCollectionCriteria(key, values);
        Criteria c = new Criteria(key, CriteriaOperators.IN, values);
        criteriaChain.add(c);
        return this;
    }

    public Criteria nin(String key, Object... values) {
        checkCollectionCriteria(key, values);
        Criteria c = new Criteria(key, CriteriaOperators.NIN, Arrays.asList(values));
        criteriaChain.add(c);
        return this;
    }

    public Criteria nin(String key, List<?> values) {
        checkCollectionCriteria(key, values);
        Criteria c = new Criteria(key, CriteriaOperators.NIN, values);
        criteriaChain.add(c);
        return this;
    }

    public Criteria isNull(String key) {
        checkKey(key);
        Criteria c = new Criteria(key, CriteriaOperators.ISNULL, null);
        criteriaChain.add(c);
        return this;
    }

    public Criteria isNotNull(String key) {
        checkKey(key);
        Criteria c = new Criteria(key, CriteriaOperators.ISNOTNULL, null);
        criteriaChain.add(c);
        return this;
    }

    public Criteria like(String key, String value) {
        checkKey(key);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "Param value was %s, It must be not null or empty", key);
        Criteria c = new Criteria(key, CriteriaOperators.LIKE, value);
        criteriaChain.add(c);
        return this;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the operator
     */
    public String getOperator() {
        return operator;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @return the criteriaChain
     */
    public List<Criteria> getCriteriaChain() {
        return criteriaChain;
    }

    /**
     * @param criteriaChain the criteriaChain to set
     */
    private void setCriteriaChain(List<Criteria> criteriaChain) {
        this.criteriaChain = criteriaChain;
    }

    /**
     * @return the valueType
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * 校验条件key
     */
    private static void checkKey(String key) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Param key was %s, It must be not null or empty", key);
    }

    /**
     * 校验比较大小的操作参数
     */
    private static void checkCompareCriteria(String key, Object value) {
        checkKey(key);
        Preconditions.checkArgument(value != null, "Param value must be not null", value);
    }

    /**
     * 校验等于、不等于之类的操作参数
     */
    private static void checkEQCriteria(String key, Object value) {
        checkKey(key);
        Preconditions.checkArgument(value != null, "Param value must be not null", value);
    }

    /**
     * 校验比较集合的操作参数
     */
    private static void checkCollectionCriteria(String key, Object... value) {
        checkKey(key);
        Preconditions.checkArgument(value.length > 0, "Number of parameter values must not be empty");
    }

    /**
     * 校验比较集合的操作参数
     */
    private static void checkCollectionCriteria(String key, Collection<?> c) {
        checkKey(key);
        Preconditions.checkNotNull(c, "Param values must be not null");
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Criteria [");
        List<Criteria> cc = this.getCriteriaChain();
        int size = cc.size();
        for (int i = 0; i < size; i++) {
            Criteria criteria = cc.get(i);
            builder.append(criteria.getKey() + criteria.getOperator() + (criteria.getValue() == null ? "" : criteria.getValue()));
            if (i < size - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        return builder.toString();
    }

}
