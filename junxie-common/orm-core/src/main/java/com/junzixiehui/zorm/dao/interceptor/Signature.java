package com.junzixiehui.zorm.dao.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * BaseDao拦截器注解的签名
 *
 * @Date 2016/10/21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Signature {
    //要拦截的Dao类,此处必须是接口,统一用IBaseDao.class
    Class<?> type();

    String method();

    Class<?>[] args();
}
