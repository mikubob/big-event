package com.itheima.service;

import com.itheima.pojo.Category;

import java.util.List;

public interface CategoryService {

    /**
     * 添加文章分类
     * @param category
     */
    void add(Category category);

    /**
     * 查询所有文章分类
     * @return
     */
    List<Category> list();

    /**
     * 修改文章分类
     * @param category
     */
    void update(Category category);

    /**
     * 根据ID查询文章分类
     * @param id
     * @return
     */
    Category findById(Integer id);

    /**
     * 根据ID删除文章分类
     * @param id
     */
    void deleteById(Integer id);
}
