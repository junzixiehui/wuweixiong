package com.junzixiehui.zorm.dao.jdbc;

import com.google.common.collect.Lists;
import com.junzixiehui.zorm.annotation.DaoDescription;
import com.junzixiehui.zorm.annotation.Table;
import com.junzixiehui.zorm.constant.DBConstant;
import com.junzixiehui.zorm.dao.DaoHelper;
import com.junzixiehui.zorm.dao.DatabaseRouter;
import com.junzixiehui.zorm.dao.IBaseDao;
import com.junzixiehui.zorm.dao.jdbc.enums.DialectEnum;

import com.junzixiehui.zorm.entity.LongIdEntity;
import com.junzixiehui.zorm.exception.DaoException;
import com.junzixiehui.zorm.exception.DaoExceptionTranslator;
import com.junzixiehui.zorm.exception.UniqueConstraintException;
import com.junzixiehui.zorm.query.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.junzixiehui.zorm.constant.MixedConstant.*;
import static com.junzixiehui.zorm.dao.DaoHelper.*;


/**
 * 基于JdbcTemplate的Dao实现<br>
 *
 * @author zhoutao
 * @since 2015/11/24
 */
@Slf4j
public abstract class JdbcBaseDao<T> implements ApplicationContextAware, IBaseDao<T> {

    private Class<T> entityClass;
    private EntityMapper<T> entityMapper;
    private JdbcSettings jdbcSettings;
    private DatabaseRouter router;
    private ApplicationContext applicationContext;

    @Override
    public Class<T> getGenericClass() {
        return this.entityClass;
    }

    @Override
    public boolean exists(Serializable id) throws DaoException {
        checkArgumentId(id);

        return this.exists(Criteria.where(DBConstant.PK_NAME, id));
    }

    @Override
    public boolean existsRefresh(Serializable id) throws DaoException {
        checkArgumentId(id);

        return this.exists(Criteria.where(DBConstant.PK_NAME, id));
    }

    @Override
    public boolean existsRefresh(Criteria criteria) throws DaoException {
        checkArgumentCriteria(criteria);
        return null != this.findOne(Arrays.asList(DBConstant.PK_NAME), criteria);
    }

    @Override
    public boolean exists(Criteria criteria) throws DaoException {
        checkArgumentCriteria(criteria);

        return null != this.findOne(Arrays.asList(DBConstant.PK_NAME), criteria);
    }

    @Override
    public long countByCriteria(Criteria criteria) throws DaoException {
        checkArgumentCriteria(criteria);

        List<Object> valueList = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();

        try {
            sql.append(JdbcHelper.SELECT_COUNT());
            sql.append(JdbcHelper.FROM(entityClass));
            sql.append(JdbcHelper.WHERE(criteria, valueList, entityMapper));

            if (log.isDebugEnabled()) {
                log.debug(JdbcHelper.formatSql(sql.toString(), valueList));
            }

            return ((JdbcTemplate) router.readRoute()).queryForObject(sql.toString(), valueList.toArray(), Long.class);
        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public long countAll() throws DaoException {
        StringBuilder sql = new StringBuilder();

        try {
            sql.append(JdbcHelper.SELECT_COUNT());
            sql.append(JdbcHelper.FROM(entityClass));

            if (log.isDebugEnabled()) {
                log.debug(JdbcHelper.formatSql(sql.toString()));
            }
            return ((JdbcTemplate) router.readRoute()).queryForObject(sql.toString(), Long.class);
        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public long countBySql(String sql, LinkedHashMap<String, Object> param) throws DaoException {
        checkArgument(sql);

        List<Object> valueList = MapUtils.isEmpty(param) ? null : Lists.newArrayList(param.values());
        try {
            if (log.isDebugEnabled()) {
                log.debug(JdbcHelper.formatSql(sql, valueList));
            }

            if (CollectionUtils.isEmpty(valueList)) {
                return ((JdbcTemplate) router.readRoute()).queryForObject(sql, Long.class);
            } else {
                return ((JdbcTemplate) router.readRoute()).queryForObject(sql, valueList.toArray(), Long.class);
            }

        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public T findOneById(Serializable id) throws DaoException {
        checkArgumentId(id);

        return this.findOne(Criteria.where(DBConstant.PK_NAME, id));
    }

    @Override
    public T findOneByQuery(Query query) throws DaoException {
        checkArgumentQuery(query);

        query.offset(INT_0).limit(INT_1);
        List<T> entityList = this.findListByQuery(query);
        return CollectionUtils.isEmpty(entityList) ? null : entityList.get(INT_0);
    }

    @Override
    public T findOneBySql(String sqlOrgin, LinkedHashMap<String, Object> param) throws DaoException {
        checkArgument(sqlOrgin);

        List<T> entityList = this.findListBySql(sqlOrgin, param);
        return CollectionUtils.isEmpty(entityList) ? null : entityList.get(INT_0);
    }

    @Override
    public List<T> findListByIds(List<Serializable> ids) throws DaoException {
        checkArgumentIds(ids);

        return this.findList(Criteria.where(DBConstant.PK_NAME, CriteriaOperators.IN, ids));
    }

    @Override
    public List<T> findListByQuery(Query query) throws DaoException {
        checkArgumentQuery(query);

        List<Object> valueList = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();

        try {
            sql.append(JdbcHelper.SELECT(query, entityMapper));
            sql.append(JdbcHelper.FROM(entityClass));
            sql.append(JdbcHelper.WHERE(query.getCriteria(), valueList, entityMapper));
            sql.append(JdbcHelper.GROUP_BY(query.getGroupBys(), entityMapper));
            sql.append(JdbcHelper.ORDER_BY(query.getOrderBys(), entityMapper));
            sql.append(JdbcHelper.LIMIT(query.getOffset(), query.getLimit(), jdbcSettings.getDialectEnum(), sql));

            if (log.isDebugEnabled()) {
                log.debug(JdbcHelper.formatSql(sql.toString(), valueList));
            }

            List<Map<String, Object>> list = ((JdbcTemplate) router.readRoute()).queryForList(sql.toString(), valueList.toArray());
            if (list == null || list.isEmpty()) {
                return null;
            }
            List<T> entityList = Lists.newArrayList();
            for (Map<String, Object> map : list) {
                entityList.add(JdbcHelper.map2Entity(map, entityMapper, entityClass));
            }
            return entityList;
        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public List<T> findListByQuery(Query query, Pageable pageable) throws DaoException {
        checkArgumentQuery(query);
        checkArgumentPageable(pageable);

        int limit = pageable.getPageSize();
        int offset = (pageable.getPageNumber() - INT_1) * limit;
        query.offset(offset).limit(limit);
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findListBySql(String sql, LinkedHashMap<String, Object> param) throws DaoException {
        checkArgument(sql);
        List<Object> valueList = MapUtils.isEmpty(param) ? null : Lists.newArrayList(param.values());

        try {
            if (log.isDebugEnabled()) {
                log.debug(JdbcHelper.formatSql(sql, valueList));
            }

            List<Map<String, Object>> list;
            if (CollectionUtils.isEmpty(valueList)) {
                list = ((JdbcTemplate) router.readRoute()).queryForList(sql);
            } else {
                list = ((JdbcTemplate) router.readRoute()).queryForList(sql, valueList.toArray());
            }

            if (list == null || list.isEmpty()) {
                return null;
            }
            List<T> entityList = Lists.newArrayList();
            for (Map<String, Object> map : list) {
                entityList.add(JdbcHelper.map2Entity(map, entityMapper, entityClass));
            }
            return entityList;
        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public int insert(T entity) throws DaoException {
        checkArgumentEntity(entity);

        final LongIdEntity longIdEntity = (LongIdEntity) entity;
        final Long id = longIdEntity.getId() == null ? Long.valueOf(0) : longIdEntity.getId();
        final List<Object> valueList = Lists.newArrayList();

        try {
            PreparedStatementCreator psc = connection -> {
                String insertSqlToUse = JdbcHelper
						.INSERT(longIdEntity, valueList, entityMapper, entityClass, jdbcSettings.getDialectEnum(), connection);
                PreparedStatement ps;
                if (id.longValue() > LONG_0) {
                    ps = connection.prepareStatement(insertSqlToUse);
                } else {
                    ps = connection.prepareStatement(insertSqlToUse, new String[]{DBConstant.PK_NAME});
                }

                int i = INT_0;
                for (Object value : valueList) {
                    StatementCreatorUtils.setParameterValue(ps, ++i, SqlTypeValue.TYPE_UNKNOWN, value);
                }
                return ps;
            };

            int n;
            if (id.longValue() > LONG_0 || DialectEnum.ORACLE.equals(jdbcSettings.getDialectEnum())) {//KeyHolder不支持oracle
                n = ((JdbcTemplate) router.writeRoute()).update(psc);
            } else {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                n = ((JdbcTemplate) router.writeRoute()).update(psc, keyHolder);
                longIdEntity.setId(keyHolder.getKey().longValue());
            }
            return n;
        } catch (DuplicateKeyException e) { //唯一约束或主键冲突
            throw new UniqueConstraintException(e.getCause().getLocalizedMessage(), e);
        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public int update(T entity) throws DaoException {
        checkArgumentEntity(entity);

        return this.update(entity, null);
    }

    @Override
    public int update(T entity, List<String> propetyList) throws DaoException {
        checkArgumentEntity(entity);

        return this.updateById(((LongIdEntity) entity).getId(), DaoHelper.entity2Update(entity, propetyList));
    }

    @Override
    public int updateById(Serializable id, Update update) throws DaoException {
        checkArgumentId(id);
        checkArgumentUpdate(update);

        return this.updateByCriteria(Criteria.where(DBConstant.PK_NAME, id), update);
    }

    @Override
    public int updateByIds(List<Serializable> ids, Update update) throws DaoException {
        checkArgumentIds(ids);
        checkArgumentUpdate(update);

        return this.updateByCriteria(Criteria.where(DBConstant.PK_NAME, CriteriaOperators.IN, ids), update);
    }

    @Override
    public int updateByCriteria(Criteria criteria, Update update) throws DaoException {
        checkArgumentCriteria(criteria);
        checkArgumentUpdate(update);

        List<Object> valueList = Lists.newArrayList();
        StringBuilder sql = new StringBuilder();

        try {
            sql.append(JdbcHelper.UPDATE(entityClass));
            sql.append(JdbcHelper.SET(update, valueList, entityMapper));
            sql.append(JdbcHelper.WHERE(criteria, valueList, entityMapper));

            if (log.isDebugEnabled()) {
                log.debug(JdbcHelper.formatSql(sql.toString(), valueList));
            }
            return ((JdbcTemplate) router.writeRoute()).update(sql.toString(), valueList.toArray());
        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public int updateBySql(String sql, LinkedHashMap<String, Object> param) throws DaoException {
        checkArgument(sql);

        List<Object> valueList = MapUtils.isEmpty(param) ? null : Lists.newArrayList(param.values());
        try {
            if (log.isDebugEnabled()) {
                log.debug(JdbcHelper.formatSql(sql, valueList));
            }

            if (CollectionUtils.isEmpty(valueList)) {
                return ((JdbcTemplate) router.writeRoute()).update(sql);
            } else {
                return ((JdbcTemplate) router.writeRoute()).update(sql, valueList.toArray());
            }

        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public int deleteById(Serializable id) throws DaoException {
        checkArgumentId(id);

        StringBuilder sql = new StringBuilder();
        try {
            sql.append(JdbcHelper.DELETE(entityClass));
            if (log.isDebugEnabled()) {
                List<Object> valueList = Lists.newArrayList(id);
                log.debug(JdbcHelper.formatSql(sql.toString(), valueList));
            }

            return ((JdbcTemplate) router.writeRoute()).update(sql.toString(), new Object[]{id});
        } catch (RuntimeException e) {
            throw DaoExceptionTranslator.translate(e);
        }
    }

    @Override
    public T findOne(List<String> fields, Criteria criteria) throws DaoException {
        checkArgumentFields(fields);
        checkArgumentCriteria(criteria);

        Query query = Query.query(criteria);
        query.includeField(fields.toArray(new String[fields.size()]));
        return this.findOneByQuery(query);
    }

    @Override
    public T findOne(Criteria criteria) throws DaoException {
        checkArgumentCriteria(criteria);

        Query query = Query.query(criteria);
        return this.findOneByQuery(query);
    }

    @Override
    public List<T> findList(List<String> fields, Criteria criteria) throws DaoException {
        checkArgumentFields(fields);
        checkArgumentCriteria(criteria);

        Query query = Query.query(criteria);
        query.includeField(fields.toArray(new String[fields.size()]));
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findList(List<String> fields, Criteria criteria, List<OrderBy> orderBys) throws DaoException {
        checkArgumentFields(fields);
        checkArgumentCriteria(criteria);
        checkArgumentOrderBys(orderBys);

        Query query = Query.query(criteria);
        query.includeField(fields.toArray(new String[fields.size()]));
        query.withOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findList(List<String> fields, Criteria criteria, List<OrderBy> orderBys, Pageable pageable) throws DaoException {
        checkArgumentFields(fields);
        checkArgumentCriteria(criteria);
        checkArgumentOrderBys(orderBys);
        checkArgumentPageable(pageable);

        Query query = Query.query(criteria);
        query.includeField(fields.toArray(new String[fields.size()]));
        query.withOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
        return this.findListByQuery(query, pageable);
    }

    @Override
    public List<T> findList(Criteria criteria) throws DaoException {
        checkArgumentCriteria(criteria);

        Query query = Query.query(criteria);
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findList(Criteria criteria, List<OrderBy> orderBys) throws DaoException {
        checkArgumentCriteria(criteria);
        checkArgumentOrderBys(orderBys);

        Query query = Query.query(criteria);
        query.withOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findList(Criteria criteria, List<OrderBy> orderBys, Pageable pageable) throws DaoException {
        checkArgumentCriteria(criteria);
        checkArgumentOrderBys(orderBys);
        checkArgumentPageable(pageable);

        Query query = Query.query(criteria);
        query.withOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
        return this.findListByQuery(query, pageable);
    }

    @Override
    public List<T> findAllList() throws DaoException {
        Query query = Query.query();
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findAllList(List<String> fields) throws DaoException {
        checkArgumentFields(fields);

        Query query = Query.query();
        query.includeField(fields.toArray(new String[fields.size()]));
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findAllList(List<String> fields, List<OrderBy> orderBys) throws DaoException {
        checkArgumentFields(fields);
        checkArgumentOrderBys(orderBys);

        Query query = Query.query();
        query.includeField(fields.toArray(new String[fields.size()]));
        query.withOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
        return this.findListByQuery(query);
    }

    @Override
    public List<T> findAllList(List<String> fields, List<OrderBy> orderBys, Pageable pageable) throws DaoException {
        checkArgumentFields(fields);
        checkArgumentOrderBys(orderBys);
        checkArgumentPageable(pageable);

        Query query = Query.query();
        query.includeField(fields.toArray(new String[fields.size()]));
        query.withOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
        return this.findListByQuery(query, pageable);
    }

    @Override
    public List<T> findAllList(List<OrderBy> orderBys, Pageable pageable) throws DaoException {
        checkArgumentOrderBys(orderBys);
        checkArgumentPageable(pageable);

        Query query = Query.query();
        query.withOrderBy(orderBys.toArray(new OrderBy[orderBys.size()]));
        return this.findListByQuery(query, pageable);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    protected void afterPropertiesSet() {
        //得到dao具体类class
        Class daoClass = this.getClass();

        //得到泛型entityClass
        ParameterizedType type = (ParameterizedType) daoClass.getGenericSuperclass();
        Type[] p = type.getActualTypeArguments();
        this.entityClass = (Class<T>) p[0];
        if (this.entityClass == null) {
            throw new DaoException("can not get the entity's Generic Type");
        }
        //表名
        Table tableAnnotation = this.entityClass.getAnnotation(Table.class);
        if (tableAnnotation == null) {
            throw new DaoException(this.entityClass.getName() + " 表注解Table必须指定");
        }

        //得到dao注解描述信息
        DaoDescription daoDescription = (DaoDescription) daoClass.getAnnotation(DaoDescription.class);
        if (daoDescription == null) {
            throw new DaoException("注解DaoDescription必须设置");
        }

        //得到jdbcSettings
        String settingsName = daoDescription.settingBeanName();
        this.jdbcSettings = (JdbcSettings) this.applicationContext.getBean(settingsName);
        if (this.jdbcSettings == null) {
            throw new DaoException("注解DaoDescription的属性settingBeanName[" + settingsName + "]必须对应一个有效的JdbcSettings bean");
        }

        //create router
        JdbcDatabaseRouterFactory.INSTANCE.setDatabaseRouter(this.jdbcSettings);
        this.entityMapper = new EntityMapper(this.entityClass);
        this.router = JdbcDatabaseRouterFactory.INSTANCE.getDatabaseRouter(this.jdbcSettings);
    }
}