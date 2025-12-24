package com.itheima.mapper;

import com.itheima.pojo.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 添加文章分类
     * @param category
     */
    void add(Category category);

    /**
     * 查询文章分类列表
     * @param userId
     * @return
     */
    List<Category> list(Integer userId);

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
