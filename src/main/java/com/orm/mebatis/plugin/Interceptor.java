package com.orm.mebatis.plugin;

/**
 * 拦截器接口，所有自定义拦截器必须实现此接口
 *
 * @author JiangJian
 * @date 2021/5/25 15:36
 */
public interface Interceptor {

    /**
     * 插件的核心逻辑实现
     * @param  invocation 代理对象
     * @return 代理返回结果
     * @throws Throwable 反射异常
     */
    Object intercept(Invocation invocation) throws Throwable;

    /**
     * 对被拦截对象进行代理
     * @param target 代理对象
     * @return 代理结果
     */
    Object plugin(Object target);
}
