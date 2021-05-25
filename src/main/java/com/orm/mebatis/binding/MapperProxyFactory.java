package com.orm.mebatis.binding;

import com.orm.mebatis.session.DefaultSqlSession;

import java.lang.reflect.Proxy;

/**
 * 用于产生MapperProxy代理类
 *
 * @author JiangJian
 * @date 2021/5/25 13:54
 */
public class MapperProxyFactory<T> {

    /** Mapper接口 */
    private final Class<T> mapperInterface;

    /** 结果映射 */
    private final Class<?> result;

    public MapperProxyFactory(Class<T> mapperInterface, Class<?> result) {
        this.mapperInterface = mapperInterface;
        this.result = result;
    }

    /**
     * 生成代理对象
     * @param session {@link DefaultSqlSession}
     * @return T
     */
    @SuppressWarnings("unchecked")
    public T newInstance(DefaultSqlSession session) {
        return (T) Proxy.newProxyInstance(
                mapperInterface.getClassLoader(),
                new Class[] { mapperInterface },
                new MapperProxy(session, result));
    }
}
