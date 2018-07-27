package com.junzixiehui.zorm.dao.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.junzixiehui.zorm.annotation.Table;
import com.junzixiehui.zorm.constant.DBConstant;
import com.junzixiehui.zorm.dao.DaoHelper;
import com.junzixiehui.zorm.dao.jdbc.enums.DialectEnum;

import com.junzixiehui.zorm.entity.LongIdEntity;
import com.junzixiehui.zorm.exception.DaoException;
import com.junzixiehui.zorm.query.*;
import com.junzixiehui.zorm.utils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.junzixiehui.zorm.constant.MixedConstant.*;
import static com.junzixiehui.zorm.constant.SymbolConstant.*;


/**
 * jdbcBaseDao内部帮助类
 * 用于拼装sql
 *
 * @author zhoutao
 * @since 2015/11/24
 */
public final class JdbcHelper {
    private static final String DEBUG_SQL_PREFIX = "==========Dao Layer Generate SQL:";

    private JdbcHelper() {
    }

    static String SELECT_COUNT() {
        return "SELECT COUNT(*) ";
    }

    static String SELECT(Query query, EntityMapper<?> entityMapper) {
        Map<String, String> propertyToColumnMapper = entityMapper.getPropertyToColumnMapper();
        List<String> fields = query.getFields();
        List<GroupBy> groupBys = query.getGroupBys();

        StringBuilder sb = new StringBuilder("SELECT ");

        if (CollectionUtils.isNotEmpty(groupBys)) {
            for (GroupBy groupBy : groupBys) {
                String groupByKey = groupBy.getKey();
                sb.append(propertyToColumnMapper.get(groupByKey)).append(COMMA);

                String groupCountAlias = groupBy.getGroupCountAlias();
                if (StringUtils.isNotBlank(groupCountAlias)) {   //需要每组数量groupCountAlias是entity中定义的非持久化字段
                    sb.append("COUNT(*) AS " + propertyToColumnMapper.get(groupCountAlias)).append(COMMA);
                }
            }
        } else {
            if (CollectionUtils.isNotEmpty(fields)) {
                for (String field : fields) {
                    String column = propertyToColumnMapper.get(field);
                    sb.append(column).append(COMMA);
                }
            } else {
                for (String column : propertyToColumnMapper.values()) {
                    sb.append(column).append(COMMA);
                }
            }
        }

        sb.deleteCharAt(sb.length() - INT_1);//去掉最后一个,
        sb.append(BLANK);
        return sb.toString();
    }

    static String DELETE(Class<?> entityClass) {
        return "DELETE " + FROM(entityClass) + " WHERE id = ?";
    }

    static String FROM(Class<?> entityClass) {
        return "FROM " + getTableName(entityClass) + BLANK;
    }

    static String GROUP_BY(List<GroupBy> groupByList, EntityMapper entityMapper) {
        if (CollectionUtils.isEmpty(groupByList)) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder("GROUP BY ");
        for (GroupBy groupBy : groupByList) {
            sb.append(entityMapper.getPropertyToColumnMapper().get(groupBy.getKey())).append(COMMA);
        }
        sb.deleteCharAt(sb.length() - INT_1);//去掉最后一个,
        sb.append(BLANK);
        return sb.toString();
    }

    static String ORDER_BY(List<OrderBy> orderByList, EntityMapper entityMapper) {
        if (CollectionUtils.isEmpty(orderByList)) {
            return EMPTY;
        }

        StringBuilder sb = new StringBuilder("ORDER BY ");
        for (OrderBy orderBy : orderByList) {
            sb.append(entityMapper.getPropertyToColumnMapper().get(orderBy.getKey())).append(BLANK).append(orderBy.getDirection()).append(COMMA);
        }
        sb.deleteCharAt(sb.length() - INT_1);//去掉最后一个,
        sb.append(BLANK);
        return sb.toString();
    }

    static String LIMIT(int offset, int limit, DialectEnum dialectEnum, final StringBuilder sql) {
        if (offset < INT_0 || limit <= INT_0) {
            return EMPTY;
        }
        StringBuilder newSql = new StringBuilder();
        if (DialectEnum.MYSQL.equals(dialectEnum)) {
            newSql.append(sql.toString());
            newSql.append("LIMIT ");
            newSql.append(offset).append(COMMA).append(limit);
        } else if (DialectEnum.ORACLE.equals(dialectEnum)) {
            newSql.append("SELECT * FROM (")
                    .append("SELECT ROWNUM AS RN,table_alias.* FROM (")
                    .append(sql.toString())
                    .append(") table_alias ")
                    .append("WHERE ROWNUM <= ")
                    .append(offset + limit)
                    .append(") ")
                    .append("WHERE RN > ").append(offset);

        }
        sql.setLength(INT_0);
        return newSql.toString();
    }

    static String UPDATE(Class<?> entityClass) {
        return "UPDATE " + getTableName(entityClass) + BLANK;
    }

    static String SET(Update update, List<Object> valueList, EntityMapper<?> entityMapper) {
        Map<String, String> propertyToColumnMapper = entityMapper.getPropertyToColumnMapper();
        Set<String> notNeedTransientPropertySet = entityMapper.getNotNeedTransientPropertySet();

        StringBuilder sb = new StringBuilder("SET ");
        Set<String> keySet = update.getSetMap().keySet();
        for (String key : keySet) {
            if (notNeedTransientPropertySet.contains(key)) {
                continue;
            }
            sb.append(propertyToColumnMapper.get(key)).append("=?,");
            valueList.add(update.get(key));
        }
        sb.deleteCharAt(sb.length() - INT_1);//去掉最后一个,
        sb.append(BLANK);
        return sb.toString();
    }

    static <T> T map2Entity(Map<String, Object> map, EntityMapper<T> entityMapper, Class<T> entityClass) {
        Map<String, String> columnToPropertyMapper = entityMapper.getColumnToPropertyMapper();

        try {
            HashMap<String, Object> propertyMap = Maps.newHashMap();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();
                //如果select出来的字段有而entity没有对应的属性，需要忽略该字段
                String propertyName = columnToPropertyMapper.get(k.toLowerCase());
                if (propertyName == null) {
                    continue;
                }
                propertyMap.put(propertyName, v);
            }
            return BeanUtils.mapToBean(propertyMap, entityClass);
        } catch (Exception e) {
            throw new DaoException("map2Entity无法将数据库中行记录[" + map + "]转换成Entity对象[" + entityClass.getSimpleName() + "]", e);
        }
    }

    /**
     * 根据criteria拼装sql where
     *
     * @param criteria     - 条件对象
     * @param valueList    - 值列表
     * @param entityMapper - entityMapper
     * @return where sql
     */
    static String WHERE(Criteria criteria, List<Object> valueList, EntityMapper<?> entityMapper) {
        if (criteria == null) {
            return EMPTY;
        }

        Map<String, String> propertyToColumnMapper = entityMapper.getPropertyToColumnMapper();

        StringBuilder whereSB = new StringBuilder();
        //criteria一定不为null，且criteria.getCriteriaChain()一定不为empty
        List<Criteria> criterias = criteria.getCriteriaChain();
        for (Criteria c : criterias) {
            if (CriteriaOperators.isNoValueOperator(c.getOperator())) {
                whereSB.append(" AND ").append(propertyToColumnMapper.get(c.getKey())).append(BLANK).append(c.getOperator());
            } else if (CriteriaOperators.isSingleValueOperator(c.getOperator())) {
                whereSB.append(" AND ").append(propertyToColumnMapper.get(c.getKey())).append(BLANK).append(c.getOperator()).append(" ?");
                valueList.add(c.getValue());
            } else if (CriteriaOperators.isMultiValueOperator(c.getOperator())) {
                whereSB.append(" AND ").append(propertyToColumnMapper.get(c.getKey())).append(BLANK).append(c.getOperator()).append(" (");
                Collection collection = (Collection) c.getValue();
                for (Object item : collection) {
                    whereSB.append(QUESTION).append(COMMA);
                    valueList.add(item);
                }
                whereSB.deleteCharAt(whereSB.length() - INT_1);
                whereSB.append(")");
            }
        }
        String whereSql = "WHERE " + whereSB.delete(INT_0, INT_5).toString() + BLANK;//去掉第一个 and
        return whereSql;
    }

    /**
     * 生成oracle id
     *
     * @param sequence   - sequence名字
     * @param connection - 链接
     * @return - id
     */
    private static Long genOracleId(String sequence, Connection connection) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement("SELECT " + sequence + ".NEXTVAL AS ID FROM DUAL");
            rs = pstmt.executeQuery();
            rs.next();
            return rs.getLong(INT_1);
        } catch (SQLException e) {
            throw new DaoException("无法获取oracle的sequence:" + sequence);
        } finally {
            JdbcUtils.closeResultSet(rs);
            JdbcUtils.closeStatement(pstmt);
        }
    }

    static String INSERT(LongIdEntity longIdEntity, List<Object> valueList, EntityMapper<?> entityMapper, Class<?> entityClass, DialectEnum dialectEnum, Connection connection) {
        Map<String, String> propertyToColumnMapper = entityMapper.getPropertyToColumnMapper();
        Set<String> notNeedTransientPropertySet = entityMapper.getNotNeedTransientPropertySet();

        StringBuilder sb = new StringBuilder(" INSERT INTO ");

        //组装表名
        Class beanClass = longIdEntity.getClass();
        sb.append(getTableName(entityClass)).append(" (");

        //组装字段
        List<Field> fieldList = Lists.newArrayList();
        List<String> pList = Lists.newArrayList();

        //IdEntity字段
        Long id = longIdEntity.getId() == null ? Long.valueOf(INT_0) : longIdEntity.getId();
        if (id.longValue() > LONG_0) {
            pList.add(QUESTION);
            sb.append(propertyToColumnMapper.get(DBConstant.PK_NAME)).append(COMMA);
            try {
                valueList.add(DaoHelper
						.getColumnValue(LongIdEntity.class.getDeclaredField(DBConstant.PK_NAME), longIdEntity));
            } catch (NoSuchFieldException e) {
                throw new DaoException("insert error", e);
            }
        } else {
            if (DialectEnum.ORACLE.equals(dialectEnum)) {
                sb.append(propertyToColumnMapper.get(DBConstant.PK_NAME)).append(COMMA);
                if (StringUtils.isNotBlank(getSequenceName(entityClass))) {
                    pList.add(QUESTION);
                    Long oracleId = genOracleId(getSequenceName(entityClass), connection);
                    valueList.add(oracleId);
                    longIdEntity.setId(oracleId);
                } else {
                    throw new DaoException("连接ORACLE,实体Table注解必须设置sequence");
                }
            }
        }


        //本类字段
        Field[] fields = beanClass.getDeclaredFields();
        fieldList.addAll(Arrays.asList(fields));


        for (Field field : fieldList) {
            if (DaoHelper.isFinalOrStatic(field)) {
                continue;
            }
            //过滤掉不需要持久化的变量
            String propertyName = field.getName();
            if (notNeedTransientPropertySet.contains(propertyName)) {
                continue;
            }

            sb.append(propertyToColumnMapper.get(propertyName)).append(COMMA);
            pList.add(QUESTION);
            valueList.add(DaoHelper.getColumnValue(field, longIdEntity));
        }
        sb.deleteCharAt(sb.length() - INT_1).append(")");

        //组装值
        sb.append(" VALUES (");
        for (String p : pList) {
            sb.append(p).append(COMMA);
        }
        sb.deleteCharAt(sb.length() - INT_1).append(") ");
        return sb.toString();
    }

    /**
     * 根据entity的class获取对应的表名
     *
     * @param entityClass - entityClass
     * @return - 注解标注的表名
     */
    static String getTableName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        return tableAnnotation.value();
    }

    /**
     * 根据entity的class获取对应的sequence
     *
     * @param entityClass - entityClass
     * @return - 注解标注的sequence
     */
    static String getSequenceName(Class<?> entityClass) {
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        return tableAnnotation.sequence();
    }

    /**
     * 格式化执行的sql
     *
     * @param sql
     * @param valueList
     * @return
     */
    public static String formatSql(String sql, final List<Object> valueList) {
        if (CollectionUtils.isEmpty(valueList)) {
            return sql;
        }

        StringBuilder sb = new StringBuilder();
        int length = sql.length();
        int questionIndex = INT_0;
        for (int i = INT_0; i < length; i++) {
            char c = sql.charAt(i);
            if (c == '?') {
                Object value = valueList.get(questionIndex);
                if (value instanceof String) {
                    sb.append("'").append(value.toString()).append("'");
                } else {
                    sb.append(value.toString());
                }
                questionIndex++;
            } else {
                sb.append(c);
            }
        }
        return DEBUG_SQL_PREFIX + sb.toString();
    }

    /**
     * 格式化执行的sql
     *
     * @param sql
     * @return
     */
    public static String formatSql(String sql) {
        return formatSql(sql, Lists.newArrayList());
    }
}
