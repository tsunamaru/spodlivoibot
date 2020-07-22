package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logs {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY HH:mm:ss.SSS");

    public static void log(Object sender, Object message){
        String log = "";
        log += dateFormat.format(new Date()) + ": ";
        log += sender.getClass().getName() + " -> ";
        log += message;
        System.out.println(log);
    }
}
