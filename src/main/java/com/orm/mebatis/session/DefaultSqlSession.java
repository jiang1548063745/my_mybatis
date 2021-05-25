package com.orm.mebatis.session;

import com.orm.mebatis.configuration.Configuration;
import com.orm.mebatis.executor.Executor;

/**
 * 应用层使用的API门面
 *
 * @author JiangJian
 * @date 2021/5/25 11:58
 */
public class DefaultSqlSession {

    private final Configuration configuration;

    private final Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        // 根据全局配置决定是否 缓存装饰
        this.executor = configuration.newExecutor();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public <T> T getMapper(Class<T> clazz) {
        return configuration.getMapper(clazz, this);
    }

    public <T> T selectOne(String statement, Object[] parameter, Class<T> entity) {
        String sql = getConfiguration().getMappedStatement(statement);
        // 打印代理对象时会自动调用toString()方法 触发invoke()
        return executor.query(sql, parameter, entity);
    }
}
