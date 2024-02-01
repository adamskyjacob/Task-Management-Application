package com.adamsky;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.adamsky")
@ComponentScan(basePackages = {"com.adamsky.Database", "com.adamsky.RestAPI", "com.adamsky.WebSocket"})
@EnableScheduling
public class TaskApplication {
	public static final Logger logger = LogManager.getLogger(TaskApplication.class);
	public static void main(String[] args) {
		logger.info("Starting TaskApplication...");
		SpringApplication.run(TaskApplication.class, args);
	}

	public static <T> void print(T t) {
		System.out.println(t);
	}
}
