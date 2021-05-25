package com.orm.mebatis.executor;

import com.orm.mebatis.cache.CacheKey;

import java.util.HashMap;
import java.util.Map;

/**
 * 带缓存的执行器
 *
 * @author JiangJian
 * @date 2021/5/25 11:32
 */
public class CachingExecutor implements Executor {

    /** 装饰的对象 */
    private Executor delegate;
    private static final Map<Integer, Object> CACHE = new HashMap<>();

    public CachingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T query(String statement, Object[] parameter, Class<T> result) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(statement);
        cacheKey.update(objs2Str(parameter));

        if (CACHE.containsKey(cacheKey.getCode())) {
            System.out.println("[" + statement + "]: 命中缓存");
            return (T)CACHE.get(cacheKey.getCode());
        } else {
            T queryResult = delegate.query(statement, parameter, result);
            CACHE.put(cacheKey.getCode(), queryResult);
            return queryResult;
        }
    }

    /**
     * 为了命中缓存，把Object[]转换成逗号拼接的字符串，因为对象的HashCode都不一样
     *
     * @param objs 对线集合
     * @return 对象拼接字符串
     */
    public String objs2Str(Object[] objs){
        StringBuilder sb = new StringBuilder();
        if (null != objs && objs.length > 0) {
            for (Object obj : objs) {
                sb.append(obj.toString()).append(",");
            }
        }

        int len = sb.length();

        if ( len > 0 ) {
            sb.deleteCharAt( len - 1);
        }

        return sb.toString();
    }
}
