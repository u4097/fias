package ru.bazis.fias;

import org.springframework.stereotype.Component;
import jep.SharedInterpreter;
import jep.Interpreter;


@Component
public class RestoreTask {

    public  void restore() throws Exception {

        try (Interpreter interp = new SharedInterpreter()) {
	    interp.exec("import fiases");
	    interp.exec("from java.lang import System");

	    interp.exec("from fiases.fias_data import createAddrSearchTemplate");
	    interp.exec("createAddrSearchTemplate()");

	    interp.exec("System.out.println('Starting restore task...')");
	    interp.exec("from fiases.snapshot import register,restore");
	    interp.exec("from fiases.fias_update import getRestoreStatus");
	    interp.exec("System.out.println('1.register')");
	    interp.exec("register()");
	    interp.exec("System.out.println('2.restore')");
	    interp.exec("restore()");
	    //interp.exec("getRestoreStatus()");
	    interp.exec("System.out.print('finish')");
        }
    }

    public void status() throws Exception {

        try (Interpreter interp = new SharedInterpreter()) {
	    interp.exec("import fiases");
	    interp.exec("from fiases.fias_update import getStatus");
	    interp.exec("getStatus()");
        }
    }
}
