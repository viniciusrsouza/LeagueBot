package supervinicius.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Logger
{
    public enum LogTypes
    {
        COMMAND,
        DEBUG,
        WARNING,
        ERROR
    }

    private static final String RESET = "\u001B[0m";
    private static final String BLACK = "\u001B[30m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[37m";

    private static Logger instance;
    private String history;
    private final int MAX_SIZE = 10000;
    private Map<LogTypes, String> logMap = new HashMap<>();

    private Logger(){
        logMap.put(LogTypes.COMMAND, "Command");
        logMap.put(LogTypes.DEBUG, "Debug");
        logMap.put(LogTypes.WARNING, "Warn");
        logMap.put(LogTypes.ERROR, "Error");
    }

    public static Logger getInstance()
    {
        if(instance == null) instance = new Logger();
        return instance;
    }

    private void log(LogTypes logType, Object message, String color)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        String logStr = "[" +
                formatter.format(date) +
                " | " +
                logMap.get(logType) +
                "] " +
                message.toString() +
                "\n";
        String print = color + logStr + RESET;

        System.out.print(print);

        history += logStr;
        if(history.length() == MAX_SIZE) saveLog();
    }

    public boolean saveLog(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date = new Date(System.currentTimeMillis());
        boolean success = FileManager.getInstance()
                .writeToFile(history, "log"+ formatter.format(date) +".txt" , "logs");
        history = "";
        return success;
    }

    public void logCommand(Object message){ log(LogTypes.COMMAND, message, BLUE); }
    public void debug(Object message){ log(LogTypes.DEBUG, message, WHITE); }
    public void warn(Object message){ log(LogTypes.WARNING, message, YELLOW); }
    public void error(Object message){ log(LogTypes.ERROR, message, RED); }

}
