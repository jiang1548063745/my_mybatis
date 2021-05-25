package com.orm.mebatis.session;

import com.orm.mebatis.configuration.Configuration;

/**
 * 会话工厂类，用于解析配置文件，产生SqlSession
 *
 * @author JiangJian
 * @date 2021/5/25 15:24
 */
public class SqlSessionFactory {

    private Configuration configuration;

    /**
     * build方法用于初始化Configuration，解析配置文件的工作在Configuration的构造函数中
     *
     * @return {@link SqlSessionFactory}
     */
    public SqlSessionFactory build() {
        configuration = new Configuration();
        return this;
    }

    /**
     * 获取DefaultSqlSession
     *
     * @return {@link DefaultSqlSession}
     */
    public DefaultSqlSession openSqlSession(){
        return new DefaultSqlSession(configuration);
    }
}
