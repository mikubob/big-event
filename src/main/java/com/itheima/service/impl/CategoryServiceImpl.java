package com.itheima.service.impl;

import com.itheima.mapper.CategoryMapper;
import com.itheima.pojo.Category;
import com.itheima.pojo.Message;
import com.itheima.service.CategoryService;
import com.itheima.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加文章分类
     * @param category
     */
    @Override
    public void add(Category category) {
        //补充属性值
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        //获取当前用户
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        category.setCreateUser(userId);
        categoryMapper.add(category);
    }

    /**
     * 查询文章分类列表
     * @return
     */
    @Override
    public List<Category> list() {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return categoryMapper.list(userId);
    }

    /**
     * 修改文章分类
     * @param category
     */
    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
    }

    /**
     * 根据ID查询文章分类
     * @param id
     * @return
     */
    @Override
    public Category findById(Integer id) {
        return categoryMapper.findById(id);
    }

    /**
     * 删除文章分类
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        // 检查该分类下是否还存在文章
        Integer articleCount = categoryMapper.selectArticleCountByCategoryId(id);
        if (articleCount > 0) {
            throw new RuntimeException(Message.CATEGORY_NOT_EMPTY);
        }
        
        // 没有文章时才允许删除分类
        categoryMapper.deleteById(id);
    }
}
