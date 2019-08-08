package leaguebot.util

import java.text.SimpleDateFormat
import java.util.*

class Logger private constructor() {
    private var history: String = ""
    private val maxSize = 10000
    private val logMap = HashMap<LogTypes, String>()

    enum class LogTypes {
        COMMAND,
        DEBUG,
        WARNING,
        ERROR
    }

    init {
        logMap[LogTypes.COMMAND] = "Command"
        logMap[LogTypes.DEBUG] = "Debug"
        logMap[LogTypes.WARNING] = "Warn"
        logMap[LogTypes.ERROR] = "Error"
    }

    private fun log(logType: LogTypes, message: Any, color: String) {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val date = Date(System.currentTimeMillis())

        val logStr = "[" +
                formatter.format(date) +
                " | " +
                logMap[logType] +
                "] " +
                message.toString() +
                "\n"
        val print = color + logStr + RESET

        print(print)

        history += logStr
        if (history.length == maxSize) saveLog()
    }

    fun saveLog(): Boolean {
        val formatter = SimpleDateFormat("dd-MM-yyyy HH-mm-ss")
        val date = Date(System.currentTimeMillis())
        val success = FileManager.getInstance()
            .writeToFile(history, "log" + formatter.format(date) + ".txt", "logs")
        history = ""
        return success
    }

    fun logCommand(message: Any) {
        log(LogTypes.COMMAND, message, BLUE)
    }

    fun debug(message: Any) {
        log(LogTypes.DEBUG, message, WHITE)
    }

    fun warn(message: Any) {
        log(LogTypes.WARNING, message, YELLOW)
    }

    fun error(message: Any) {
        log(LogTypes.ERROR, message, RED)
    }

    companion object {

        private val RESET = "\u001B[0m"
        private val BLACK = "\u001B[30m"
        private val RED = "\u001B[31m"
        private val GREEN = "\u001B[32m"
        private val YELLOW = "\u001B[33m"
        private val BLUE = "\u001B[34m"
        private val PURPLE = "\u001B[35m"
        private val CYAN = "\u001B[36m"
        private val WHITE = "\u001B[37m"

        private var instance: Logger? = null

        fun getInstance(): Logger {
            if (instance == null) instance = Logger()
            return instance as Logger
        }
    }

}
