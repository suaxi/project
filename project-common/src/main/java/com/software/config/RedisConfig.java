package com.software.config;

import com.software.serializer.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Wang Hao
 * @date 2022/10/5 13:03
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);

        //使用StringRedisSerializer序列化和反序列化redis的key
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        //hash的key也采用StringRedisSerializer方式序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
