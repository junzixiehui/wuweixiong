package com.junzixiehui.zorm.entity;


import com.junzixiehui.zorm.annotation.Column;
import com.junzixiehui.zorm.constant.DBConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 基于long主键id的所有实体对象基类
 *
 * @author zhoutao
 * @version 0.0.1
 * @since 2015/11/24
 */
@Getter
@Setter
public abstract class LongIdEntity implements IdEntity {
    @Column(DBConstant.PK_NAME)
    protected Long id;

    @Override
    public abstract String toString();
}
