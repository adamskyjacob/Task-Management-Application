package com.adamsky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.adamsky")
@ComponentScan(basePackages = {"com.adamsky.Database", "com.adamsky.RestAPI"})
public class TaskApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	public static <T> void print(T t) {
		System.out.println(t);
	}
}
