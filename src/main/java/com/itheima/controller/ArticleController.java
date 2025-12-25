package com.itheima.controller;

import com.itheima.pojo.Article;
import com.itheima.pojo.Message;
import com.itheima.pojo.PageBean;
import com.itheima.pojo.Result;
import com.itheima.service.ArticleService;
import com.itheima.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/article")
@Tag(name = "文章相关接口")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    /**
     * 发布文章
     * @param article
     * @return
     */
    @PostMapping
    @Operation(summary = "发布文章")
    public Result add(@RequestBody @Validated Article article){
        log.info("发布文章，参数：{}",article);
        articleService.add(article);
        return Result.success();
    }

    /**
     * 查询文章列表
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param state
     * @return
     */
    @GetMapping
    @Operation(summary = "查询文章列表")
    public Result<PageBean<Article>> list(
            Integer pageNum,
            Integer pageSize,
            @RequestParam (required = false) Integer categoryId,
            @RequestParam (required = false) String  state
    ){
        log.info("查询文章列表，参数：pageNum:{},pageSize:{},categoryId:{},state:{}",pageNum,pageSize,categoryId,state);
        PageBean<Article> pageBean = articleService.list(pageNum,pageSize,categoryId,state);
        return Result.success(pageBean);
    }
}
