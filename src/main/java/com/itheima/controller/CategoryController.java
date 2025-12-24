package com.itheima.controller;

import com.itheima.pojo.Category;
import com.itheima.pojo.Result;
import com.itheima.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
@Tag(name = "文章分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 添加文章分类
     * @param category
     * @return
     */
    @RequestMapping
    @Operation(summary = "添加文章分类")
    public Result add(@RequestBody @Validated(Category.Add.class) Category category){
        log.info("添加文章分类，参数：{}",category);
        categoryService.add(category);
        return Result.success();
    }

    /**
     * 查询文章分类列表
     * @return
     */
    @GetMapping
    @Operation(summary = "查询文章分类列表")
    public Result<List<Category>> list(){
        log.info("查询文章分类列表");
        List<Category> list = categoryService.list();
        return Result.success(list);
    }

    /**
     * 修改文章分类
     * @param category
     * @return
     */
    @PutMapping
    @Operation(summary = "修改文章分类")
    public Result update(@RequestBody @Validated(Category.Update.class) Category category){
        log.info("修改文章分类，参数：{}",category);
        categoryService.update(category);
        return Result.success();
    }

    /**
     * 查询文章分类详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        log.info("查询文章分类详情，参数：{}",id);
        Category category = categoryService.findById(id);
        return Result.success(category);
    }

    /**
     * 删除文章分类
     * @param id
     * @return
     */
    @DeleteMapping
    @Operation(summary = "删除文章分类")
    public Result delete(Integer id){
        log.info("删除文章分类，参数：{}",id);
        categoryService.deleteById(id);
        return Result.success();
    }
}
