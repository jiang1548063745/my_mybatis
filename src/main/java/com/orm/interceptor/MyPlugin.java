package com.orm.interceptor;

import com.orm.mebatis.annotation.Intercepts;
import com.orm.mebatis.plugin.Interceptor;
import com.orm.mebatis.plugin.Invocation;
import com.orm.mebatis.plugin.Plugin;

import java.util.Arrays;

/**
 * 自定义插件
 *
 * @author JiangJian
 * @date 2021/5/25 15:50
 */
@Intercepts("query")
public class MyPlugin implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // SQL
        String statement = (String) invocation.getArgs()[0];
        // 参数
        Object[] parameter = (Object[]) invocation.getArgs()[1];
        // Result Type
        Class<?> resultClass = (Class<?>) invocation.getArgs()[2];

        System.out.println("进入自定义插件: MyPlugin");
        System.out.println("SQL: ["+statement+"]");
        System.out.println("Parameters: " + Arrays.toString(parameter));
        System.out.println("ResultType: " + resultClass.toString());

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
