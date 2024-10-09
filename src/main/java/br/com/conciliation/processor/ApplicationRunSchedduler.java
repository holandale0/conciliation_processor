package br.com.conciliation.processor;

import br.com.conciliation.processor.application.ConciliationProcessorApplicationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunSchedduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunSchedduler.class);

	@Autowired
	private ConciliationProcessorApplicationService applicationService;

	@Value("${scheduler.thread.pool.size:20}")
	private Integer poolSize;

	@PostConstruct
	public void runOnStartup() {
		LOGGER.info("ROOT - Initial run of Conciliation Processor");
		executeJob();
	}

	@Scheduled(cron = "${scheduler.cron}")
	public void scheduleTaskWithCronExpression() throws Exception {
		LOGGER.info("ROOT - Scheduled run of Conciliation Processor");
		executeJob();
	}

	private void executeJob() {
		LOGGER.info("ROOT - Job started");
		applicationService.startConciliationProcessor();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(poolSize);
		taskScheduler.setThreadNamePrefix("ScheduledTask-");
		return taskScheduler;
	}

}