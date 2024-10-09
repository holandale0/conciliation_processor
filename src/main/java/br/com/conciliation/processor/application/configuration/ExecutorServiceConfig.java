package br.com.conciliation.processor.application.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExecutorServiceConfig {

	@Value("${thread-executor.min-threads:20}")
	private int minThreads;

	@Value("${thread-executor.max-threads:200}")
	private int maxThreads;

	@Value("${thread-executor.sleep-time:60}")
	private int sleepTime;

	@Value("${thread-executor.queue-capacity:10000}")
	private int queueCapacity;

	@Bean
	public ExecutorService threadPoolExecutor() {
		return new ThreadPoolExecutor(minThreads, // Número mínimo de threads
				maxThreads, // Número máximo de threads
				sleepTime, TimeUnit.SECONDS, // Tempo ocioso entre threads
				new LinkedBlockingQueue<>(queueCapacity), // Tamanho da fila de tarefas
				new ThreadPoolExecutor.CallerRunsPolicy() // Política de rejeição
		);
	}

}
