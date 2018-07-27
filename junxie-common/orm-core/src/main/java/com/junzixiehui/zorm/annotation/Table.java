package com.junzixiehui.zorm.annotation;

import java.lang.annotation.*;

/**
 * 标注entity对应的表名
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    /**
     * 表名
     *
     * @return
     */
    String value();

    /**
     * oracle表对应的sequence
     *
     * @return
     */
    String sequence() default "";
}
