package com.orm.mebatis.handler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 参数处理器
 *
 * @author JiangJian
 * @date 2021/5/25 9:41
 */
public class ParameterHandler {

    private final PreparedStatement preparedStatement;

    public ParameterHandler(PreparedStatement preparedStatement) {
        this.preparedStatement = preparedStatement;
    }

    /**
     * 设置参数到SQL中的 ？ 占位符
     * @param parameters 参数
     */
    public void setParameters(Object[] parameters) {
        try {
            // PreparedStatement 的序号是从1开始的
            for (int i = 0; i < parameters.length; i++) {
                int k = i + 1;

                if (parameters[i] instanceof Integer) {
                    preparedStatement.setInt(k, (Integer) parameters[i]);
                } else if (parameters[i] instanceof Long) {
                    preparedStatement.setLong(k, (Long) parameters[i]);
                } else if (parameters[i] instanceof String) {
                    preparedStatement.setString(k, (String) parameters[i]);
                } else if (parameters[i] instanceof Boolean) {
                    preparedStatement.setBoolean(k, (Boolean) parameters[i]);
                } else {
                    preparedStatement.setString(k, String.valueOf(parameters[i]));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
