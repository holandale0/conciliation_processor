package br.com.conciliation.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication
@EnableScheduling
public class ConciliationProcessorApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ConciliationProcessorApplication.class);
		app.setDefaultProperties(Collections.singletonMap("spring.profiles.default", "dev"));
		app.run(args);
	}

}
