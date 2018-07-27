package com.junzixiehui.zorm.dao.jdbc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.junzixiehui.zorm.annotation.Column;
import com.junzixiehui.zorm.constant.DBConstant;
import com.junzixiehui.zorm.dao.DaoHelper;
import com.junzixiehui.zorm.entity.LongIdEntity;
import com.junzixiehui.zorm.exception.DaoException;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.junzixiehui.zorm.constant.MixedConstant.INT_0;


/**
 * 实体和数据库的映射
 *
 * @Author zhoutao
 * @Date 2017/6/8
 */
@Getter
public final class EntityMapper<T> {
    private String entityName;
    //属性名到字段名映射
    private Map<String, String> propertyToColumnMapper = Maps.newLinkedHashMap();
    //字段名到属性名映射
    private Map<String, String> columnToPropertyMapper = Maps.newLinkedHashMap();
    //不需要持久化的字段
    private Set<String> notNeedTransientPropertySet = Sets.newHashSet();

    public EntityMapper(Class<T> entityClass) {
        this.entityName = entityClass.getCanonicalName();

        try {
            //该entity所有属性
            List<Field> fieldList = Lists.newArrayList();

            //id字段
            fieldList.add(LongIdEntity.class.getDeclaredField(DBConstant.PK_NAME));

            //本类字段
            Field[] fields = entityClass.getDeclaredFields();
            if (fields == null || fields.length == INT_0) {
                throw new DaoException(getEntityName() + " have no property");
            }
            fieldList.addAll(Arrays.asList(fields));

            for (Field field : fieldList) {
                if (DaoHelper.isFinalOrStatic(field)) {
                    continue;
                }
                String propertyName = field.getName();
                //istransient=true的加入到忽略持久化列表
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null && !columnAnnotation.isTransient()) {
                    notNeedTransientPropertySet.add(propertyName);
                }
                String columnName = DaoHelper.getColumnName(field);
                propertyToColumnMapper.put(propertyName, columnName);
                columnToPropertyMapper.put(columnName, propertyName);

            }
        } catch (Exception e) {
            throw new DaoException("无法创建Entity[" + getEntityName() + "]对应的EntityMapper", e);
        }
    }
}
