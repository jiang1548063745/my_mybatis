package com.jiangjian.mebatis.session;

import com.jiangjian.mebatis.configuration.Configuration;
import com.jiangjian.mebatis.executor.Executor;

/**
 * Sql 会话类
 * @author JiangJian
 * @date 2021/5/24 16:47
 */
public class SqlSession {

    private Configuration configuration;

    private Executor executor;

    /**
     * 查询单条数据
     * @param statementId SQL表述
     * @param parameter  参数
     * @param <T> 查询对象
     * @return T
     */
    public <T> T selectOne(String statementId, Object parameter) {
        // 暂时使用 statementId 代替
        String sql = statementId;
        return executor.query(sql, parameter);
    }

    /**
     * 获取代理对象
     * @param clazz {@link Class}
     * @param <T> 类泛型
     * @return T
     */
    public <T> T getMapper(Class<T> clazz) {
        return configuration.getMapper(this, clazz);
    }
}
