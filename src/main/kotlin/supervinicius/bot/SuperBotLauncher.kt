package supervinicius.bot

import supervinicius.util.FileManager
import supervinicius.util.getChampions
import java.io.File
import java.io.FileNotFoundException

object SuperBotLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        //    beta token NjAwMTE2NDc3NDQxNDc0NTYy.XSvEbg.hpqj_OYsWQ2am6nAr_OX75q1fZE
        // release token NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4
        val TOKEN = "NjAwMTE2NDc3NDQxNDc0NTYy.XSvEbg.hpqj_OYsWQ2am6nAr_OX75q1fZE"
        val bot = SuperBot(TOKEN)

        try{
            val file = File("champions.csv")
            bot.champions.addAll( file.readText().split(",") )

        }catch (e: FileNotFoundException){
            bot.champions.addAll( getChampions() )
            var str = ""
            for(i in 0 until (bot.champions.size-1))
                str += "${bot.champions[i]},"
            str+=bot.champions.last()

            FileManager.getInstance().writeToFile(str, "champions.csv")

        }

        bot.start()
    }
}