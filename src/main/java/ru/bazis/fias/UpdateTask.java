package ru.bazis.fias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class UpdateTask {
    private static final Logger logger = LoggerFactory.getLogger(UpdateTask.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


    public void updateTask() {
        logger.info("Cron Update Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            UpdateFias.update();
	    System.out.println("finish");
        } catch (Exception e) {
            logger.error("Cron Update Task failed: {}",e);
        }
    }
}
