package com.orm.mybatis;
import com.orm.entity.Blog;
import com.orm.mapper.BlogMapper;
import com.orm.mebatis.session.DefaultSqlSession;
import com.orm.mebatis.session.SqlSessionFactory;
import org.junit.Test;

/**
 * @author JiangJian
 * @date 2021/5/25 9:16
 */
public class MyBatisTest {

    @Test
    public void test1() {
        SqlSessionFactory sessionFactory = new SqlSessionFactory();
        DefaultSqlSession sqlSession = sessionFactory.build().openSqlSession();

        BlogMapper blogMapper = sqlSession.getMapper(BlogMapper.class);
        Blog blog = blogMapper.selectBlogById(1);
        System.out.println("1: " + blog);

        blog = blogMapper.selectBlogById(1);
        System.out.println("2: " + blog);
    }
}
