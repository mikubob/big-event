package com.itheima.utils;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
public class AliyunOSSOperator {
    @Autowired
    private AliyunOSSProperties aliyunOSSProperties;

    public String upload(byte[] content, String originalFilename) throws Exception {
        String endpoint = aliyunOSSProperties.getEndpoint();
        String bucketName = aliyunOSSProperties.getBucketName();
        String region = aliyunOSSProperties.getRegion();

        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 填写Object完整路径，例如202406/1.png。Object完整路径中不能包含Bucket名称。
        //获取当前系统日期的字符串,格式为 yyyy/MM
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        //生成一个新的不重复的文件名
        String newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = dir + "/" + newFileName;

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        // 增加连接超时和重试设置
        clientBuilderConfiguration.setConnectionTimeout(5000); // 连接超时时间(毫秒)
        clientBuilderConfiguration.setSocketTimeout(10000); // Socket超时时间(毫秒)
        clientBuilderConfiguration.setMaxConnections(100); // 最大连接数
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content));
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + objectName;
    }

    /**
     * 批量删除OSS中的文件
     * @param urls 文件URL列表
     * @throws Exception 删除异常
     */
    public void deleteBatch(List<String> urls) throws Exception {
        if (urls == null || urls.isEmpty()) {
            return;
        }

        String endpoint = aliyunOSSProperties.getEndpoint();
        String bucketName = aliyunOSSProperties.getBucketName();
        String region = aliyunOSSProperties.getRegion();

        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            for (String url : urls) {
                // 从URL中提取Object名称
                String objectName = getObjectKeyFromUrl(url, bucketName, endpoint);
                if (objectName != null && !objectName.isEmpty()) {
                    ossClient.deleteObject(bucketName, objectName);
                }
            }
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 从URL中提取OSS对象的key
     * @param url 完整的文件URL
     * @param bucketName 存储桶名称
     * @param endpoint OSS端点
     * @return 对象key
     */
    private String getObjectKeyFromUrl(String url, String bucketName, String endpoint) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        try {
            // 构造存储桶的完整URL
            String bucketUrl = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/";

            // 检查URL是否属于该存储桶
            if (url.startsWith(bucketUrl)) {
                // 提取对象key（去掉存储桶URL前缀）
                return url.substring(bucketUrl.length());
            }
        } catch (Exception e) {
            // 解析失败，返回null
            return null;
        }

        return null;
    }
}