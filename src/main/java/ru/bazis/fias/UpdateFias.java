package ru.bazis.fias;

import jep.SharedInterpreter;
import jep.Interpreter;

public class UpdateFias {

    public static void update() throws Exception {

        try (Interpreter interp = new SharedInterpreter()) {
            interp.exec("import fiases");
            interp.exec("from fiases.fias_update import updateFias");
	    interp.exec("updateFias()");
	    //interp.exec("print('ADDR_UPDATE_CNT: ', str(updateFias()))");
        }
    }
}
