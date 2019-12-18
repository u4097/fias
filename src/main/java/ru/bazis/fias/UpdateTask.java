package ru.bazis.fias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class UpdateTask {
    private static final Logger logger = LoggerFactory.getLogger(UpdateTask.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    //    @Scheduled(cron = "0 * * * * *")
    //@Scheduled(cron = "0 0 21 * * 2,5")
    @Scheduled(cron = "0 45 9 * * *")
    public void updateTask() {
        logger.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
            UpdateFias.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
