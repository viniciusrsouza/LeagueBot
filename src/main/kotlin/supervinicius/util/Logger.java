package supervinicius.util;

import java.text.SimpleDateFormat;
import java.util.*;

public class Logger
{
    public enum LogTypes
    {
        COMMAND,
        DEBUG
    }

    private static Logger instance;
    private String history;
    private final int MAX_SIZE = 10000;
    private Map<LogTypes, String> logMap = new HashMap<>();

    private Logger(){
        logMap.put(LogTypes.COMMAND, "Command");
        logMap.put(LogTypes.DEBUG, "Debug");
    }

    public static Logger getInstance()
    {
        if(instance == null) instance = new Logger();
        return instance;
    }

    public void log(LogTypes logType, Object message)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        String print = "[" +
                formatter.format(date) +
                " | " +
                logMap.get(logType) +
                "] " +
                message.toString() +
                "\n";

        System.out.print(print);
        history += print;
    }

    public void logCommand(Object message){ log(LogTypes.COMMAND, message); }
    public void debug(Object message){ log(LogTypes.DEBUG, message); }

}
