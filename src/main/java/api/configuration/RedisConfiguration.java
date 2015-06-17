package api.configuration;

import api.model.Event;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;

@Configuration
public class RedisConfiguration {

    private RedisServer redisServer;

    @PostConstruct
    public void setup() throws IOException {
        if (redisServer == null) redisServer = new redis.embedded.RedisServer();
        redisServer.start();
    }

    @PreDestroy
    public void tearDown() {
        redisServer.stop();
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, Map<Long, SortedSet<Event>>> redisTemplate() {
        final RedisTemplate<String, Map<Long, SortedSet<Event>>> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new JacksonJsonRedisSerializer<>(TreeMap.class));
        template.setValueSerializer(new JacksonJsonRedisSerializer<>(TreeMap.class));
        return template;
    }

}
