package com.junzixiehui.zorm.annotation;

import java.lang.annotation.*;

/**
 * 标注entity中的属性对应的数据库字段
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
    //对应的列名
    String value() default "";
    //是否持久化
    boolean isTransient() default true;
}
