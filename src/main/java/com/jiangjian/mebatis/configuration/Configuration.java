package com.jiangjian.mebatis.configuration;

import com.jiangjian.mebatis.proxy.MapperProxy;
import com.jiangjian.mebatis.session.SqlSession;

import java.lang.reflect.Proxy;

/**
 * 配置类
 * @author JiangJian
 * @date 2021/5/24 16:47
 */
public class Configuration {

    /**
     * 获取 Mapper
     * @param clazz {@link Class}
     * @param <T> 类范型
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T getMapper(SqlSession session, Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{ clazz },
                new MapperProxy(session));
    }
}
