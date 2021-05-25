package com.orm.mebatis.annotation;

import java.lang.annotation.*;

/**
 * 用于注解接口，以映射返回的实体类
 *
 * @author JiangJian
 * @date 2021/5/25 11:29
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {

    Class<?> value();
}
