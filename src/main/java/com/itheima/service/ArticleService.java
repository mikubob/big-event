package com.itheima.service;

import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import java.util.List;

public interface ArticleService {

    /**
     * 发布文章
     * @param article
     */
    void add(Article article);

    /**
     * 查询文章列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param state
     * @return
     */
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    /**
     * 根据ID查询文章
     * @param id
     * @return
     */
    Article findById(Integer id);

    /**
     * 更新文章
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
