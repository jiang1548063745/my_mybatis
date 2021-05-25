package com.orm.mebatis.executor;

/**
 * 执行器接口
 *
 * @author JiangJian
 * @date 2021/5/24 16:50
 */
public interface Executor {

    /**
     * 查询方法
     * @param statement SQL语句
     * @param parameter 查询参数
     * @param result    返回结果
     * @param <T>       返回结果泛型
     * @return T
     */
    <T> T query(String statement, Object[] parameter, Class<T> result);
}
