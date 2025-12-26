package com.itheima.service.impl;

import com.github.pagehelper.PageInfo;
import com.itheima.mapper.ArticleMapper;
import com.itheima.pojo.Article;
import com.itheima.pojo.PageBean;
import com.itheima.service.ArticleService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 发布文章
     * @param article
     */
    @Override
    @CacheEvict(value = "articleList", allEntries = true) // 只清除列表缓存，新增操作不需要清除单个文章缓存
    public void add(Article article) {
        //补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);

        articleMapper.add(article);
    }

    /**
     * 查询文章列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param state
     * @return
     */
    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        // 设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        // 获取用户ID
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        // 执行查询
        List<Article> articleList = articleMapper.list(userId, categoryId, state);

        // 使用PageInfo来安全处理分页结果
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);

        // 创建并填充PageBean
        PageBean<Article> pageBean = new PageBean<>();
        pageBean.setTotal(pageInfo.getTotal());
        pageBean.setItems(pageInfo.getList());

        return pageBean;
    }

    /**
     * 根据ID查询文章
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "article", key = "#id")
    public Article findById(Integer id) {
        return articleMapper.findById(id);
    }

    /**
     * 更新文章
     * @param article
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = "articleList", allEntries = true),
            @CacheEvict(value = "article", key = "#article.id")
    })
    public void update(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }

    /**
     * 删除文章
     * @param id
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = "articleList", allEntries = true),
            @CacheEvict(value = "article", key = "#id")
    })
    public void deleteById(Integer id) {
        articleMapper.deleteById(id);
    }

    /**
     * 批量删除文章
     * @param ids
     */
    @Override
    @Caching(evict = {
            @CacheEvict(value = "articleList", allEntries = true),
            @CacheEvict(value = "article", allEntries = true) // 批量删除时清除所有文章缓存，因为无法逐个清除
    })
    public void deleteByIds(List<Long> ids) {
        articleMapper.deleteByIds(ids);
    }
}
