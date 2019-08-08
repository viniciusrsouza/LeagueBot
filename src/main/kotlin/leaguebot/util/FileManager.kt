package leaguebot.util

import java.io.*

class FileManager private constructor() {

    @Throws(IOException::class)
    fun getFileFromResource(fileName: String): InputStream {
        val `is` = javaClass.getResourceAsStream(fileName)
        return `is` ?: throw IOException("Failed to open file $fileName")
    }

    fun getFileContent(stream: InputStream?): String {
        if (stream == null) return "null"

        var result = ""

        try {
            BufferedReader(InputStreamReader(stream)).use { br ->
                var line: String

                while (true){
                    line = br.readLine()
                    if(line == null) break

                    result += (line + "\n")
                }

            }
        } catch (e: IOException) {
            Logger.getInstance().error("Failed to load file")
        }

        return result
    }

    @JvmOverloads
    fun writeToFile(str: String, name: String, path: String = "temp"): Boolean {
        try {
            val fullPath: String
            if (path != "") {
                fullPath = path + File.separator + name

                val dir = File(path)
                val mkdir = dir.mkdir()
                if (mkdir)
                    Logger.getInstance().debug("$path dir created!")
                else
                    Logger.getInstance().warn("$path dir already exist or could not be created!")
            } else {
                fullPath = name
            }

            val writer = PrintWriter(File(fullPath), "UTF-8")
            writer.println(str)
            writer.close()
            return true
        } catch (e: Exception) {
            return false
        }

    }

    fun getFullPath(str: String): String {
        return "temp" + File.separator + str
    }

    fun clearTemp(): Boolean {
        val folder = File("temp")
        val files = folder.listFiles() ?: return true

        for (f in files) {
            if (!f.delete()) return false
        }
        return true
    }

    companion object {
        private var instance: FileManager? = null

        fun getInstance(): FileManager {
            if (instance == null) instance = FileManager()
            return instance as FileManager
        }
    }
}
