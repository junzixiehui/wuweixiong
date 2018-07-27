package com.junzixiehui.zorm.annotation;


import java.lang.annotation.*;

/**
 * 标注Dao相关描述
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DaoDescription {

    /**
     * 标注dao设置对象的bean
     *
     * @return
     */
    String settingBeanName();
}
