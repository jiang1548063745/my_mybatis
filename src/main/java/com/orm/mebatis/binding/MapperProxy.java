package com.orm.mebatis.binding;

import com.orm.mebatis.session.DefaultSqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * MapperProxy代理类，用于代理Mapper接口
 *
 * @author JiangJian
 * @date 2021/5/24 17:20
 */
public class MapperProxy implements InvocationHandler {

    private final DefaultSqlSession session;

    private final Class<?> object;

    public MapperProxy(DefaultSqlSession session, Class<?> object) {
        this.session = session;
        this.object = object;
    }

    /**
     * 所有Mapper接口的方法调用都会走到这里
     * @param proxy  代理对象
     * @param method 代理方法
     * @param args   参数
     * @return       代理返回结果
     * @throws Throwable 反射调用异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 接口
        String mapperInterface = method.getDeclaringClass().getName();

        // 方法名
        String methodName = method.getName();

        // 全类名
        String statementId = mapperInterface + "." + methodName;

        // 如果根据接口类型+方法名能找到映射的SQL，则执行SQL
        if (session.getConfiguration().hasStatement(statementId)) {
            return session.selectOne(statementId, args, object);
        }

        // 否则直接执行被代理对象的原方法
        return method.invoke(proxy, args);
    }
}
