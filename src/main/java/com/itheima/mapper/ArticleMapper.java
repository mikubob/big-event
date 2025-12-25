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

    /**
     * 根据ID查询文章
     * @param id
     * @return
     */
    Article findById(Integer id);

    /**
     * 修改文章
     * @param article
     */
    void update(Article article);

    /**
     * 删除文章
     * @param id
     */
    void deleteById(Integer id);

    /**
     * 批量删除文章
     * @param ids
     */
    void deleteByIds(List<Long> ids);
}
