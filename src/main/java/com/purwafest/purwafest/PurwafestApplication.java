package com.purwafest.purwafest;

import com.purwafest.purwafest.auth.infrastructure.security.JwtConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(JwtConfigProperties.class)
@SpringBootApplication
public class PurwafestApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurwafestApplication.class, args);
	}

}
