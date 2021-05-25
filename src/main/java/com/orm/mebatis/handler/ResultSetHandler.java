package com.orm.mebatis.handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.orm.mebatis.constant.CommonsConstant.UNDER_LINE;

/**
 * 结果集处理器
 *
 * @author JiangJian
 * @date 2021/5/25 9:43
 */
public class ResultSetHandler {

    /**
     * 处理结果集
     * @param resultSet 结果集
     * @param type 映射实体类型
     * @param <T> 实体泛型
     * @return T
     */
    public <T> T handle(ResultSet resultSet, Class<T> type) {
        T entity = null;

        try {
            entity = type.newInstance();
            if (resultSet.next()) {
                for (Field field: entity.getClass().getDeclaredFields()) {
                    setValue(entity, field, resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    /**
     * 利用反射给属性赋值
     * @param entity 实体
     * @param field 属性
     * @param resultSet 结果集
     */
    private void setValue(Object entity, Field field, ResultSet resultSet) {
        try {
            Method setMethod = entity.getClass().getMethod("set" + firstWordCapital(field.getName()), field.getType());
            setMethod.invoke(entity, getResult(resultSet, field));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据反射判断类型 从 Result 获取对应类型参数
     * @param resultSet 结果集
     * @param field 属性
     * @return 参数
     */
    private Object getResult(ResultSet resultSet, Field field) throws SQLException {
        // TODO typeHandler
        Class<?> type = field.getType();
        String dataName = humpToUnderline(field.getName());

        // TODO 类型不够全
        if (Integer.class == type) {
            return resultSet.getInt(dataName);
        } else if (String.class == type) {
            return resultSet.getString(dataName);
        } else if (Long.class == type) {
            return resultSet.getLong(dataName);
        } else if (Boolean.class == type) {
            return resultSet.getBoolean(dataName);
        } else if (Double.class == type) {
            return resultSet.getDouble(dataName);
        } else {
            return resultSet.getString(dataName);
        }
    }

    /**
     * 数据库驼峰转 Java 下划线命名
     * @param name 驼峰
     * @return 下划线命名
     */
    private String humpToUnderline(String name) {
        StringBuilder stringBuilder = new StringBuilder(name);
        int temp = 0;

        if (!name.contains(UNDER_LINE)) {
            for (int i = 0; i < name.length(); i++) {
                if (Character.isUpperCase(name.charAt(i))) {
                    stringBuilder.insert(i + temp, UNDER_LINE);
                    temp += 1;
                }
            }
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 单词首字母大写
     * @param word 单词
     * @return 首字母大写后的单词
     */
    private String firstWordCapital(String word) {
        String first = word.substring(0, 1);
        String tail = word.substring(1);
        return first.toUpperCase() + tail;
    }
}
