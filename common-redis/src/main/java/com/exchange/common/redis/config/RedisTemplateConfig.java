package com.exchange.common.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {


    @Bean
    public RedisTemplate<String,Object> redisTemplate(
            RedisConnectionFactory factory
    ){

        ObjectMapper mapper = new ObjectMapper();
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL
        );


        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //key 统一使用string
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        //value 使用Jackson
        Jackson2JsonRedisSerializer<Object> valueSerializer =
                new Jackson2JsonRedisSerializer<>(mapper,Object.class);

        template.setKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(keySerializer);

        template.afterPropertiesSet();
        return template;

    }
}
