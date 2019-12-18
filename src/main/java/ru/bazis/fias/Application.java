package ru.bazis.fias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import jep.SharedInterpreter;
import jep.Interpreter;


@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        try {
            restore();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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


