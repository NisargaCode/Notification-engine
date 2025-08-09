package com.sasken.NotificationEngine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling; // ✅ add this

@SpringBootApplication
@EnableScheduling  // ✅ this enables scheduled tasks like email notifications
public class NotificationEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationEngineApplication.class, args);
	}

	// ✅ CORS Configuration to allow React frontend
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE")
						.allowedHeaders("*");
			}
		};
	}
}
