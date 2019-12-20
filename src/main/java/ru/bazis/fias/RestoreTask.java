package ru.bazis.fias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jep.SharedInterpreter;
import jep.Interpreter;


@Component
public class RestoreTask {
    private static final Logger logger = LoggerFactory.getLogger(RestoreTask.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    //@Scheduled(cron = "0 * * * * *")
    //@Scheduled(cron = "0 20 3 * * 2,5")
    @Scheduled(cron = "0 50 12 * * *")
    //@Scheduled(cron = "0 * * * * *")
    public void updateTask() {
        logger.info("Restore Task :: Started Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        try {
	    restore();
            //UpdateFias.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void restore() throws Exception {

        try (Interpreter interp = new SharedInterpreter()) {
	    interp.exec("import fiases");
	    interp.exec("from java.lang import System");
            //interp.exec("from fiases.fias_data import HOST, TIME_OUT");
	    //interp.exec("fiases.fias_data.HOST='es01'");
	    //interp.exec("host = HOST");
	    //interp.exec("timeout = fiases.fias_data.TIME_OUT");
	    //interp.exec("System.out.println('ELASTICSEARCH HOST: ' + fiases.fias_data.HOST)");
	    //interp.exec("System.out.println('ELASTICSEARCH TIMEOUT: ' + str(timeout))");

	    interp.exec("System.out.println('Starting restore task...')");
	    interp.exec("from fiases.snapshot import register,restore");
	    interp.exec("System.out.println('1.register')");
	    interp.exec("register()");
	    interp.exec("System.out.println('2.restore')");
	    interp.exec("restore()");
	    //interp.exec("System.out.println('finish')");
        }
    }
}
