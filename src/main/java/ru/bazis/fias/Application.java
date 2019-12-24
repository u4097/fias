package ru.bazis.fias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;



@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    
    @Autowired
    private RestoreTask restoreTask;

    @Autowired
    private UpdateTask updateTask;

    @Scheduled(cron = "${update.cron.exr}")
    public void execute() {
	    updateTask.updateTask();
    }

    @Value("${restore.on.start}")
    private String  restoreOnStart;

    @Override
    public void run(String...args) throws Exception {

	    if (restoreOnStart != null) {
		    if (restoreOnStart.equals("1")) {
			    logger.info("RESTORE ON START IS ON");
			    restoreTask.restore();
			    //restoreTask.status();
		    } else {
			  System.out.println("RESTORE ON START IS OFF");
		    }
	    }

    }

    
    public static void main(String[] args) {
	
        SpringApplication.run(Application.class, args);

    }

}


