package com.itheima.service.impl;

import com.github.pagehelper.Page;
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
    @Caching(evict = {
            @CacheEvict(value = "articleList", allEntries = true),
            @CacheEvict(value = "article", key = "#article.id")
    })
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
    @Cacheable(value = "articleList", key = "'page_' + #pageNum + '_' + #pageSize + '_' + (#categoryId != null ? #categoryId : 'all') + '_' + (#state != null ? #state : 'all') + '_' + T(String).valueOf(T(com.itheima.utils.ThreadLocalUtil).get().get('id'))")
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //1.创建PageBean对象
        PageBean<Article> pageBean = new PageBean<>();
        //2.开启分页查询pageHelper
        PageHelper.startPage(pageNum,pageSize);
        //3.调用mapper查询
        Map<String,Object> map =ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Article> articleList = articleMapper.list(userId,categoryId,state);
        //page中提供了方法，可以获取PageHelper分页查询后，得到的总记录数和当前页数据
        Page<Article> page = (Page<Article>) articleList;
        //4.封装PageBean对象
        pageBean.setTotal(page.getTotal());//总记录数
        pageBean.setItems(page.getResult());//当前页数据
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
    @CacheEvict(value = "articleList", allEntries = true)
    @Override
    public void deleteByIds(List<Long> ids) {
        articleMapper.deleteByIds(ids);
    }
}
