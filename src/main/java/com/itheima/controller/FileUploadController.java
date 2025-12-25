package com.itheima.controller;

import com.itheima.pojo.Result;
import com.itheima.utils.AliyunOSSOperator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传相关接口")
public class FileUploadController {

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    /**
     * 文件上传
     * @param file 上传的文件
     * @return Result 包含上传后文件URL的响应结果
     */
    @PostMapping
    @Operation(summary = "文件上传")
    public Result upload(MultipartFile file) throws IOException {
        log.info("文件上传，文件名：{}", file.getOriginalFilename());

        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            // 获取文件的原始名称
            String originalFilename = file.getOriginalFilename();
            String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

            // 调用阿里云OSS上传文件方法
            String url = aliyunOSSOperator.upload(file.getBytes(), fileName);
            log.info("文件上传成功，文件地址：{}", url);
            return Result.success(url);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param urls 要删除的文件URL列表
     * @return Result 删除结果
     */
    @DeleteMapping
    @Operation(summary = "删除文件")
    public Result delete(@RequestBody List<String> urls) {
        log.info("删除文件，文件URL列表：{}", urls);

        try {
            // 调用阿里云OSS删除文件方法
            aliyunOSSOperator.deleteBatch(urls);
            log.info("文件删除成功");
            return Result.success();
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return Result.error("文件删除失败: " + e.getMessage());
        }
    }
}