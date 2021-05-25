package com.orm.mebatis.plugin;

import com.orm.mebatis.annotation.Intercepts;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理类，用于代理被拦截对象
 * 同时提供了创建代理类的方法
 *
 * @author JiangJian
 * @date 2021/5/25 15:41
 */
public class Plugin implements InvocationHandler {

    /** 被代理对象 */
    private final Object target;

    /** 拦截器（插件） */
    private final Interceptor interceptor;

    public Plugin(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }

    /**
     * 对被代理对象进行代理，返回代理类
     *
     * @param obj 代理对象
     * @param interceptor 插件
     * @return 代理对象
     */
    public static Object wrap(Object obj, Interceptor interceptor) {
        Class<?> clazz = obj.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new Plugin(obj, interceptor));
    }

    /**
     * 指定代理方法
     * @param proxy  代理对象
     * @param method 代理方法
     * @param args   方法参数
     * @return       执行结果
     * @throws Throwable 处理异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 自定义的插件上有@Intercepts注解，指定了拦截的方法
        if (interceptor.getClass().isAnnotationPresent(Intercepts.class)) {
            // 如果是被拦截的方法，则进入自定义拦截器的逻辑
            if (method.getName().equals(interceptor.getClass().getAnnotation(Intercepts.class).value())) {
                return interceptor.intercept(new Invocation(target, method, args));
            }
        }

        // 非被拦截方法，执行原逻辑
        return method.invoke(target, method, args);
    }
}
