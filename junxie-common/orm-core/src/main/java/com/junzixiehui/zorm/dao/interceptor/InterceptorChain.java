package com.junzixiehui.zorm.dao.interceptor;


import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * dao操作的拦截器链
 *
 * @Date 2016/10/21
 */
public class InterceptorChain {
    private Set<Interceptor> interceptors = Sets.newLinkedHashSet();
    private final Lock locker = new ReentrantLock();

    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;
    }

    public void addInterceptor(Interceptor interceptor) {
        locker.lock();
        try {
            interceptors.add(interceptor);
        } finally {
            locker.unlock();
        }
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        if (interceptors == null || interceptors.isEmpty()) {
            return;
        }
        for (Interceptor interceptor : interceptors) {
            locker.lock();
            try {
                this.interceptors.add(interceptor);
            } finally {
                locker.unlock();
            }
        }
    }
}
