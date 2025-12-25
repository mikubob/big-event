package com.itheima.mapper;

import com.itheima.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {

    /**
     * 添加文章
     * @param article
     */
    void add(Article article);

    /**
     * 查询文章列表
     * @param userId
     * @param categoryId
     * @param state
     * @return
     */
    List<Article> list(Integer userId, Integer categoryId, String state);
}
