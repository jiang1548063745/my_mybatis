package com.orm.mebatis.annotation;

import java.lang.annotation.*;

/**
 * 查询注解
 *
 * @author JiangJian
 * @date 2021/5/25 11:30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {

    String value();
}
