package com.software.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.software.serializer.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Wang Hao
 * @date 2022/10/5 13:03
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        //序列化
        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        //value的序列化使用FastJsonRedisSerializer
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        //key的序列化使用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // fastjson 升级到 1.2.83 后需要指定序列化白名单
        //common模块
        ParserConfig.getGlobalInstance().addAccept("com.software.dto");
        ParserConfig.getGlobalInstance().addAccept("com.software.entity");
        //security模块
        ParserConfig.getGlobalInstance().addAccept("com.software.security.dto");
        ParserConfig.getGlobalInstance().addAccept("com.software.security.entity");
        ParserConfig.getGlobalInstance().addAccept("org.springframework.security.core.authority");
        TypeUtils.addMapping("org.springframework.security.core.authority.SimpleGrantedAuthority", SimpleGrantedAuthority.class);
        //system模块
        ParserConfig.getGlobalInstance().addAccept("com.software.system.dto");
        ParserConfig.getGlobalInstance().addAccept("com.software.system.entity");

        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
