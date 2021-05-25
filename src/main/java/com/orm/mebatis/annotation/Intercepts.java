package com.orm.mebatis.annotation;

import java.lang.annotation.*;

/**
 * 用于注解拦截器，指定拦截的方法
 *
 * @author JiangJian
 * @date 2021/5/25 11:56
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercepts {

    String value();
}
