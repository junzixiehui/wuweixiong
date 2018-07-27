package com.junzixiehui.zorm.dao;

import com.junzixiehui.zorm.exception.DaoException;
import com.junzixiehui.zorm.query.*;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 基础DAO接口 封装常用的CRUD操作,与具体orm框架无关
 *
 * @author
 * @since 2015/11/24
 */
public interface IBaseDao<T> {

    Class<T> getGenericClass();

    boolean exists(Serializable id) throws DaoException;

    boolean exists(Criteria criteria) throws DaoException;

    boolean existsRefresh(Serializable id) throws DaoException;

    boolean existsRefresh(Criteria criteria) throws DaoException;

    long countByCriteria(Criteria criteria) throws DaoException;

    long countAll() throws DaoException;

    long countBySql(String sql, LinkedHashMap<String, Object> param) throws DaoException;

    T findOneById(Serializable id) throws DaoException;

    T findOneByQuery(Query query) throws DaoException;

    T findOneBySql(String sql, LinkedHashMap<String, Object> param) throws DaoException;

    List<T> findListByIds(List<Serializable> ids) throws DaoException;

    List<T> findListByQuery(Query query) throws DaoException;

    List<T> findListByQuery(Query query, Pageable pageable) throws DaoException;

    List<T> findListBySql(String sql, LinkedHashMap<String, Object> param) throws DaoException;

    int insert(T entity) throws DaoException;

    //更新实体所有属性
    int update(T entity) throws DaoException;

    //更新实体中指定的属性
    int update(T entity, List<String> propetyList) throws DaoException;

    int updateById(Serializable id, Update update) throws DaoException;

    int updateByIds(List<Serializable> ids, Update update) throws DaoException;

    int updateByCriteria(Criteria criteria, Update update) throws DaoException;

    int updateBySql(String sql, LinkedHashMap<String, Object> param) throws DaoException;

    int deleteById(Serializable id) throws DaoException;

    T findOne(List<String> fields, Criteria criteria) throws DaoException;

    T findOne(Criteria criteria) throws DaoException;

    List<T> findList(List<String> fields, Criteria criteria) throws DaoException;

    List<T> findList(List<String> fields, Criteria criteria, List<OrderBy> orderBys) throws DaoException;

    List<T> findList(List<String> fields, Criteria criteria, List<OrderBy> orderBys, Pageable pageable) throws DaoException;

    List<T> findList(Criteria criteria) throws DaoException;

    List<T> findList(Criteria criteria, List<OrderBy> orderBys) throws DaoException;

    List<T> findList(Criteria criteria, List<OrderBy> orderBys, Pageable pageable) throws DaoException;

    List<T> findAllList() throws DaoException;

    List<T> findAllList(List<String> fields) throws DaoException;

    List<T> findAllList(List<String> fields, List<OrderBy> orderBys) throws DaoException;

    List<T> findAllList(List<String> fields, List<OrderBy> orderBys, Pageable pageable) throws DaoException;

    List<T> findAllList(List<OrderBy> orderBys, Pageable pageable) throws DaoException;

}
