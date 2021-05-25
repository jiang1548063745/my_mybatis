package com.orm.mebatis.executor;

import com.orm.mebatis.handler.StatementHandler;

/**
 * 简单执行器
 *
 * @author JiangJian
 * @date 2021/5/25 14:35
 */
public class SimpleExecutor implements Executor {

    @Override
    public <T> T query(String statement, Object[] parameter, Class<T> result) {
        StatementHandler statementHandler = new StatementHandler();
        return statementHandler.query(statement, parameter, result);
    }
}
