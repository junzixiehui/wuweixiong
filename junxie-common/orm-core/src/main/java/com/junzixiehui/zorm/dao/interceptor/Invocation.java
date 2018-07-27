package com.junzixiehui.zorm.dao.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 定义了dao操作过程中的一次调用
 *
 * @Date 2016/10/21
 */
public class Invocation {
    //某个dao的具体实例
    private Object target;
    //某个dao的方法
    private Method method;
    //某个dao的方法的参数
    private Object[] args;

    public Invocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    //执行真正的dao方法
    public Object proceed() throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, args);
    }
}
