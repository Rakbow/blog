// package com.rakbow.website.config;
//
// import org.apache.commons.lang3.StringUtils;
// import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisConfiguration;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
// import redis.clients.jedis.JedisPooled;
// import redis.clients.jedis.UnifiedJedis;
//
// /**
//  * @Project_name: website
//  * @Author: Rakbow
//  * @Create: 2023-05-10 22:59
//  * @Description:
//  */
// @Configuration
// public class RedisSearchConfig implements RedisConfiguration {
//
//     @Value("${spring.redis.host}")
//     private String host;
//     @Value("${spring.redis.port}")
//     private int port;
//     @Value("${spring.redis.password}")
//     private String password;
//     @Value("${spring.redis.database}")
//     private int database;
//     @Value("${spring.redis.timeout}")
//     private int timeout;
//
//     @Bean
//     public UnifiedJedis unifiedJedis(GenericObjectPoolConfig jedisPoolConfig) {
//         UnifiedJedis client;
//         if (StringUtils.isNotEmpty(password)) {
//             client = new JedisPooled(jedisPoolConfig, host, port, timeout, password, database);
//         } else {
//             client = new JedisPooled(jedisPoolConfig, host, port, timeout, null, database);
//         }
//         return client;
//     }
//
// }
