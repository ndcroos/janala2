/*
 * Author: Koushik Sen (ksen@cs.berkeley.edu)
 */

package janala.utils;

import janala.config.Config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.*;

/**
 * Author: Koushik Sen (ksen@cs.berkeley.edu)
 * Date: 6/29/12
 * Time: 1:58 PM
 */
public class MyLogger {
    private static ConsoleHandler handler = new ConsoleHandler();
    private static String testDataDir = "testdata/";

    static {
        handler.setFormatter(new MyFormatter());
    }
    
    public static Logger getLogger(String name) {
        Logger ret = Logger.getLogger(name);
        ret.setUseParentHandlers(false);
        ret.addHandler(handler);
        ret.setLevel(Config.verbose?Level.INFO:Level.WARNING);
        return ret;
    }

    public static Logger getTestLogger(String name) {
        try {
            String filename = testDataDir+name;
            Logger ret = Logger.getLogger(name);
            ret.setUseParentHandlers(false);
            if (ret.getHandlers().length==0) {
                if ((new File(filename).exists())) {
                    FileHandler handler = new FileHandler(filename+".new");
                    handler.setFormatter(new MyFormatter());
                    ret.addHandler(handler);
                } else {
                    FileHandler handler = new FileHandler(filename);
                    handler.setFormatter(new MyFormatter());
                    ret.addHandler(handler);
                }
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static void checkLog(Logger logger) {
        Handler handlers[] = logger.getHandlers();
        if (handlers.length!=0) {
            if (handlers[0] instanceof FileHandler) {
                ((FileHandler)handlers[0]).close();
            }
        }
        String filename = testDataDir+logger.getName();
        File neW = new File(filename+".new");
        File old = new File(filename);

        if (neW.exists() && old.exists()) {
            if (!compareFiles(neW,old)) {
                System.err.println("**************************************************************************");
                System.err.println("************************* Test "+filename+" failed!! *************************");
                System.err.println("**************************************************************************");
            }
        }
    }

    static boolean compareFiles(File f1, File f2){
        try{
            if(f1.length() != f2.length())
                return false;

            BufferedInputStream in1 = new BufferedInputStream(new FileInputStream(f1));
            BufferedInputStream in2 = new BufferedInputStream(new FileInputStream(f2));

            while(true){
                int a = in1.read();
                int b = in2.read();
                if(a != b) return false;
                if(a == -1){
                    return true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

}

class MyFormatter extends SimpleFormatter {
    @Override
    public String format(LogRecord logRecord) {
        return logRecord.getLevel()+": "+formatMessage(logRecord)+"\n";
    }
}