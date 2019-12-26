package ru.bazis.fias;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Bean(name = "taskExecutor")
	public Executor taskExecutor() {
		logger.debug("Creating Async Task Executor");
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("RestoreTaskThread-");
		executor.initialize();
		return executor;
	}

	@Autowired
	private RestoreServiceTask restoreTask;

	@Autowired
	private UpdateTask updateTask;

	@Scheduled(cron = "${update.cron.exr}")
	public void execute() {
		updateTask.updateTask();
	}

	@Value("${restore.on.start}")
	private String restoreOnStart;

	@Override
	public void run(String... args) throws Exception {
		//restoreTask.getOSInfo();
		String status = "";

		if (restoreOnStart != null) {
			if (restoreOnStart.equals("1")) {
				logger.info("RESTORE ON START IS ON");
				logger.info("Starting restore task.");
				restoreTask.restore();
				status = restoreTask.getStatus();
				Instant start = Instant.now();
				while (status.contains("INDEX")) {
					status = restoreTask.getStatus();
				}
				Instant end = Instant.now();
				long duration = Duration.between(start, end).getSeconds();

				logger.info("Finish restore task for {} min. \n Service ready", duration/60);
				logger.info("Kibana: http://localhost:5601");

			} else {
				System.out.println("RESTORE ON START IS OFF");
			}
		}

	}

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

	}

}
