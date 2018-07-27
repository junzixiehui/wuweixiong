package com.junzixiehui.zorm.dao;

/**
 * 数据源路由器标识
 *
 * @Author
 * @Date 2017/6/8
 */
public interface DatabaseRouter {

    Object writeRoute();

    Object readRoute();
}
