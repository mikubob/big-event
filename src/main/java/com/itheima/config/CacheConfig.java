package com.itheima.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        // 创建ObjectMapper用于处理时间类型
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // 自定义序列化器，处理时间类型
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 配置缓存的默认设置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // 设置默认过期时间为1小时
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 设置key的序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)) // 设置value的序列化方式
                .disableCachingNullValues(); // 不缓存null值

        // 为不同的缓存区域设置不同的过期时间
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        // 为articleList设置1小时过期时间
        cacheConfigurations.put("articleList", config.entryTtl(Duration.ofHours(1)));
        // 为article设置2小时过期时间
        cacheConfigurations.put("article", config.entryTtl(Duration.ofHours(2)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}