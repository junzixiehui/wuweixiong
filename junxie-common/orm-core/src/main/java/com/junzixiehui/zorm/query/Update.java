package com.junzixiehui.zorm.query;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 封装基本的update 对于关系数据库，目前只支持简单set操作,暂不支持inc
 *
 * @author zhoutao
 * @non-threadsafe 线程不安全对象，建议只用作方法内部变量使用
 */
public class Update {

    /**
     * set操作集合,大部分情况一次更新字典不会超过4个,默认Map初始容量16
     */
    private Map<String, Object> setMap;

    public Update() {
        setMap = Maps.newLinkedHashMap();
    }

    /**
     * Static factory method to create an Update
     *
     * @return
     */
    public static Update update(String key, Object value) {
        Update update = new Update();
        update.set(key, value);
        return update;
    }

    /**
     * update中的set操作
     *
     * @param key   - 不可为null 或 empty
     * @param value - 可为null
     * @return
     */
    public Update set(String key, Object value) {
        setMap.put(key, value);
        return this;
    }

    /**
     * update中的get操作
     *
     * @param key - 不可为null 或 empty
     * @return
     */
    public Object get(String key) {
        return setMap.get(key);
    }

    /**
     * @return the setMap
     */
    public Map<String, Object> getSetMap() {
        return setMap;
    }

    @Override
    public String toString() {
        return this.setMap.toString();
    }
}
