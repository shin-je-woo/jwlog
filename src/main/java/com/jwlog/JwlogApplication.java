package com.jwlog;

import com.jwlog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class JwlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwlogApplication.class, args);
	}

}
