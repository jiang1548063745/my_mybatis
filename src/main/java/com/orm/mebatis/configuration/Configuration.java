package com.orm.mebatis.configuration;

import com.orm.mapper.BlogMapper;
import com.orm.mebatis.annotation.Entity;
import com.orm.mebatis.annotation.Select;
import com.orm.mebatis.binding.MapperRegistry;
import com.orm.mebatis.executor.CachingExecutor;
import com.orm.mebatis.executor.Executor;
import com.orm.mebatis.executor.SimpleExecutor;
import com.orm.mebatis.plugin.Interceptor;
import com.orm.mebatis.plugin.InterceptorChain;
import com.orm.mebatis.session.DefaultSqlSession;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static com.orm.mebatis.constant.CommonsConstant.CLASS_SUFFIX;
import static com.orm.mebatis.constant.CommonsConstant.TRUE;
import static com.orm.mebatis.constant.ConfigConstant.CACHE_ENABLED;
import static com.orm.mebatis.constant.ConfigConstant.PLUGIN_PATH;

/**
 * 全局配置类
 * @author JiangJian
 * @date 2021/5/24 16:47
 */
public class Configuration {

    /** 配置信息 */
    public static final ResourceBundle CONFIG;

    /** SQL映射信息 */
    public static final ResourceBundle SQL_MAPPINGS;

    /** 维护接口与工厂类关系 */
    public static final MapperRegistry MAPPER_REGISTRY = new MapperRegistry();

    /** 维护接口方法与SQL关系 */
    public static final Map<String, String> MAPPED_STATEMENTS = new HashMap<>();

    /** 插件 */
    private final InterceptorChain interceptorChain = new InterceptorChain();

    /** 所有Mapper接口 */
    private final List<Class<?>> mapperList = new ArrayList<>();

    /** 类所有文件 */
    private final List<String> classPaths = new ArrayList<>();

    static {
        CONFIG = ResourceBundle.getBundle("config");
        SQL_MAPPINGS = ResourceBundle.getBundle("sqlMapper");
    }

    /**
     * 初始化时解析全局配置文件
     */
    public Configuration() {
        // Note：在properties和注解中重复配置SQL会覆盖
        // 解析配置
        for (String key: SQL_MAPPINGS.keySet()) {
            // Mapper接口
            Class<?> mapper = null;

            // 配置的SQL语句
            String statement;

            // 返回类名称
            String entityName;

            // 返回类型
            Class<?> entityClass = null;

            // properties中的value用--隔开，第一个是SQL语句
            statement = SQL_MAPPINGS.getString(key).split("--")[0];

            // properties中的value用--隔开，第二个是需要转换的POJO类型
            entityName = SQL_MAPPINGS.getString(key).split("--")[1];

            try {
                // properties中的key是接口类型+方法
                // 从接口类型+方法中截取接口类型
                mapper = Class.forName(key.substring(0, key.lastIndexOf(".")));
                entityClass = Class.forName(entityName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            // 接口与返回的实体类关系
            MAPPER_REGISTRY.addMapper(mapper, entityClass);
            // 接口与SQL的关系
            MAPPED_STATEMENTS.put(key, statement);
        }

        // 解析接口上的注解
        String mapperPath = CONFIG.getString("mapper.path");
        scanPackage(mapperPath);
        for (Class<?> mapper : mapperList) {
            parsingClass(mapper);
        }

        // 解析插件
        String pluginPathValue = CONFIG.getString(PLUGIN_PATH);

        String[] pluginPaths = pluginPathValue.split(",");

        if (pluginPaths.length > 0) {
            // 将插件添加到interceptorChain中
            for (String plugin : pluginPaths) {
                Interceptor interceptor = null;
                try {
                    interceptor = (Interceptor) Class.forName(plugin).newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                interceptorChain.addInterceptor(interceptor);
            }
        }
    }

    /**
     * 根据statement判断是否存在映射的SQL
     * @param statementId Mapper 方法名
     * @return boolean
     */
    public boolean hasStatement(String statementId) {
        return MAPPED_STATEMENTS.containsKey(statementId);
    }

    /**
     * 根据statement ID获取SQL
     * @param statementId Mapper 方法名
     * @return SQL语句
     */
    public String getMappedStatement(String statementId) {
        return MAPPED_STATEMENTS.get(statementId);
    }

    public <T> T getMapper(Class<T> clazz, DefaultSqlSession sqlSession) {
        return MAPPER_REGISTRY.getMapper(clazz, sqlSession);
    }

    /**
     * 创建执行器，当开启缓存时使用缓存装饰
     * 当配置插件时，使用插件代理
     * @return {@link Executor}
     */
    public Executor newExecutor() {
        Executor executor;
        if (TRUE.equals(CONFIG.getString(CACHE_ENABLED))) {
            executor = new CachingExecutor(new SimpleExecutor());
        } else{
            executor = new SimpleExecutor();
        }

        // 目前只拦截了Executor，所有的插件都对Executor进行代理，没有对拦截类和方法签名进行判断
        if (interceptorChain.hasPlugin()) {
            return (Executor) interceptorChain.pluginAll(executor);
        }

        return executor;
    }

    /**
     * 解析Mapper接口上配置的注解（SQL语句）
     * @param mapper Mapper接口
     */
    private void parsingClass(Class<?> mapper) {
        // 1.解析类上的注解
        // 如果有Entity注解，说明是查询数据库的接口
        if (mapper.isAnnotationPresent(Entity.class)) {
            for (Annotation annotation : mapper.getAnnotations()) {
                if (annotation.annotationType().equals(Entity.class)) {
                    // 注册接口与实体类的映射关系
                    MAPPER_REGISTRY.addMapper(mapper, ((Entity)annotation).value());
                }
            }
        }

        // 2.解析方法上的注解
        Method[] methods = mapper.getMethods();
        for (Method method : methods) {
            //TODO 其他操作

            // 解析@Select注解的SQL语句
            if (method.isAnnotationPresent(Select.class)) {
                for (Annotation annotation : method.getDeclaredAnnotations()) {
                    if (annotation.annotationType().equals(Select.class)) {
                        // 注册接口类型+方法名和SQL语句的映射关系
                        String statement = method.getDeclaringClass().getName() + "." +method.getName();
                        MAPPED_STATEMENTS.put(statement, ((Select) annotation).value());
                    }
                }
            }
        }
    }

    /**
     * 根据全局配置文件的Mapper接口路径，扫描所有接口
     * @param mapperPath Mapper映射路径
     */
    private void scanPackage(String mapperPath) {
        String classPath = Objects.requireNonNull(BlogMapper.class.getResource("/")).getPath();
        mapperPath = mapperPath.replace(".", File.separator);

        // 扫描路径
        String mainPath = classPath + mapperPath;

        doScannerClass(new File(mainPath));

        for (String className : classPaths) {
            className = className
                    .replace(classPath.replace("/","\\").replaceFirst("\\\\",""),"")
                    .replace("\\",".")
                    .replace(".class","");

            try {
                Class<?> clazz = Class.forName(className);

                if (clazz.isInterface()){
                    mapperList.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 遍历文件夹 获取 .class文件
     * @param file 文件
     */
    private void doScannerClass(File file) {
        // 文件夹，遍历
        if (file.isDirectory()) {
            File[] files = file.listFiles();

            assert files != null;

            for (File subFile : files) {
                doScannerClass(subFile);
            }
        } else {
            // 文件，直接添加
            if (file.getName().endsWith(CLASS_SUFFIX)) {
                classPaths.add(file.getPath());
            }
        }
    }
}
