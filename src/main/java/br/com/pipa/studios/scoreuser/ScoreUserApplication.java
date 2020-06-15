package br.com.pipa.studios.scoreuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ScoreUserApplication {
	public static void main(String... args) {
		SpringApplication.run(ScoreUserApplication.class, args);
	}
}
