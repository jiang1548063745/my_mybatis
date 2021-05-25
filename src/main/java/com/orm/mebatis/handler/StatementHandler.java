package com.orm.mebatis.handler;

import com.orm.mebatis.configuration.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * SQL处理器
 *
 * @author JiangJian
 * @date 2021/5/25 9:43
 */
public class StatementHandler {

    private final ResultSetHandler resultSetHandler = new ResultSetHandler();

    /**
     * 查询方法
     * @param statement  语句
     * @param parameters 参数
     * @param result     返回实体
     * @param <T>        返回实体泛型
     * @return T
     */
    public <T> T query(String statement, Object[] parameters, Class<T> result) {
        T data = null;

        try ( Connection connection = getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(statement))
        {
            // 处理参数
            ParameterHandler parameterHandler = new ParameterHandler(preparedStatement);
            parameterHandler.setParameters(parameters);
            preparedStatement.execute();

            // 处理结果映射
            data = resultSetHandler.handle(preparedStatement.getResultSet(), result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * 获取链接
     * @return {@link Connection}
     */
    private Connection getConnection() {
        String driver = Configuration.CONFIG.getString("jdbc.driver");
        String url = Configuration.CONFIG.getString("jdbc.url");
        String username = Configuration.CONFIG.getString("jdbc.username");
        String password = Configuration.CONFIG.getString("jdbc.password");

        Connection connection = null;

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
