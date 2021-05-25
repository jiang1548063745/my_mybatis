package com.orm.mapper;

import com.orm.entity.Blog;

/**
 * @author JiangJian
 * @date 2021/5/24 16:44
 */
public interface BlogMapper {

    /**
     * 根据主键查询
     * @param bid 博客主键
     * @return {@link Blog}
     */
    Blog selectBlogById(Integer bid);
}
