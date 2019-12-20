package ru.bazis.fias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import jep.SharedInterpreter;
import jep.Interpreter;


@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private Environment env;

    @Override
    public void run(String...args) throws Exception {
	    logger.info("JAVA_HOME: {}", env.getProperty("JAVA_HOME"));
	    logger.info("APP.NAME: {}", env.getProperty("app.name"));
    }

    
    public static void main(String[] args) {
	//try {
	    ////restore();
	    //System.out.println("Start app in main()");
	//} catch (Exception e) {
	    //System.out.println(e.getMessage());
	//}
	
        SpringApplication.run(Application.class, args);

    }

    public static void restore() throws Exception {

        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("from fiases.snapshot import register,restore");
            interp.exec("register()");
            interp.exec("restore()");
        }
    }
}


