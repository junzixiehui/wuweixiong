package com.junzixiehui.zorm.dao.interceptor;

/**
 * 基于jdk动态代理实现的dao层方法拦截器
 *
 * 注意(由于jdk动态代理的限制)：
 * 1、声明注解Signature时，type只能是IBaseDao.class
 * 2、声明注解Signature时，method无法拦截到被代理对象自己调用自己的方法
 *
 * @Date 2016/10/21
 */
public interface Interceptor {

    /**
     * 处理一次拦截
     *
     * @param invocation
     * @return - 返回目标方法的返回值
     * @throws Throwable
     */
    Object intercept(Invocation invocation) throws Throwable;

    /**
     * 注册一个拦截
     *
     * @param target
     * @return - 返回的是代理对象或者目标对象本身
     */
    Object plugin(Object target);
}
