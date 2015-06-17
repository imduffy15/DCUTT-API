package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    private RedisServer redisServer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
