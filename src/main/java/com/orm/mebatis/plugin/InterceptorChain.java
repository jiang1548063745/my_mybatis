package com.orm.mebatis.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截器链，存放所有拦截器，和对代理对象进行循环代理
 *
 * @author JiangJian
 * @date 2021/5/25 15:39
 */
public class InterceptorChain {

    private final List<Interceptor> interceptors = new ArrayList<>();

    public void addInterceptor(Interceptor interceptor){
        interceptors.add(interceptor);
    }

    /**
     * 对被拦截对象进行层层代理
     * @param target 拦截对象
     * @return 处理结果
     */
    public Object pluginAll(Object target){
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }

        return target;
    }

    /**
     * 是否有插件
     * @return 是否
     */
    public boolean hasPlugin(){
        return interceptors.size() != 0;
    }
}
