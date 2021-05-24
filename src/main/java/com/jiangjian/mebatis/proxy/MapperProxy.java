package com.jiangjian.mebatis.proxy;

import com.jiangjian.mebatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mapper 代理对象
 *
 * @author JiangJian
 * @date 2021/5/24 17:20
 */
public class MapperProxy implements InvocationHandler {

    private SqlSession session;

    public MapperProxy(SqlSession session) {
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 接口
        String mapperInterface = method.getDeclaringClass().getName();

        // 方法名
        String methodName = method.getName();

        String statementId = mapperInterface + "." + methodName;

        return session.selectOne(statementId, args[0]);
    }
}
