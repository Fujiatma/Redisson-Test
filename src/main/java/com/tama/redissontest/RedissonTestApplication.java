package com.tama.redissontest;

import id.co.homecredit.redis.client.annotation.EnableRedisson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableRedisson(config = "classpath:redisson/single-server-config.yaml")
@ComponentScan(basePackages = {"id.co.homecredit.redis.client", "com.tama"})
public class RedissonTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedissonTestApplication.class, args);
	}

}
