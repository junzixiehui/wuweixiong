package com.junzixiehui.zorm.query;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * 封装基本的order by
 *
 * @author zhoutao
 * @non-threadsafe 线程不安全对象，建议只用作方法内部变量使用
 */
public class OrderBy {

    private String key;
    private String direction;

    private OrderBy(String key, Direction direction) {
        this.key = key;
        this.direction = direction.getDirection();
    }

    /**
     * Static factory method to create an Update
     *
     * @return
     */
    public static OrderBy orderBy(String key, Direction direction) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Param key was %s, It must be not null or empty", key);
        return new OrderBy(key, direction);
    }

    public static OrderBy asc(String key) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Param key was %s, It must be not null or empty", key);
        return new OrderBy(key, Direction.ASC);
    }

    public static OrderBy desc(String key) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "Param key was %s, It must be not null or empty", key);
        return new OrderBy(key, Direction.DESC);
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(key);
        builder.append(" ");
        builder.append(direction);
        return builder.toString();
    }

    /**
     * Enumeration for sort directions.
     *
     * @author Oliver Gierke
     */
    public enum Direction {
        ASC("ASC"), DESC("DESC");

        private String direction;

        private Direction(String direction) {
            this.direction = direction;
        }

        public String getDirection() {
            return direction;
        }
    }
}
