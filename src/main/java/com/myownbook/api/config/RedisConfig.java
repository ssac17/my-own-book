package com.myownbook.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myownbook.api.dto.BookResponseDTO;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching //스프링부트 애플리케이션 캐싱을 활성화
public class RedisConfig {

    @Bean
    public ObjectMapper cacheObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory cf, ObjectMapper cacheObjectMapper) {
        var keySer = new StringRedisSerializer();

        // 1) 타입 고정 직렬화기: 생성자에서 타입과 Mapper를 함께 지정
        var booksValueSer = new Jackson2JsonRedisSerializer<>(cacheObjectMapper, BookResponseDTO.class);

        RedisCacheConfiguration booksConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(booksValueSer))
                .entryTtl(Duration.ofMinutes(10));

        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("books", booksConfig);

        return RedisCacheManager.builder(cf)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }
}
