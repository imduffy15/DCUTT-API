package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import redis.embedded.RedisServer;

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    private RedisServer redisServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
