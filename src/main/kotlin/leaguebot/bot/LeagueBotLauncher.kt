package leaguebot.bot

import leaguebot.util.FileManager
import leaguebot.util.getChampions
import java.util.stream.Collectors

object SuperBotLauncher {
    lateinit var bot: SuperBot
    lateinit var champions: ArrayList<String>
    @JvmStatic
    fun main(args: Array<String>) {
        val betaToken = "NjAwMTE2NDc3NDQxNDc0NTYy.XS4gMQ.3_9LFpuGMW9kwvuBJgAAGe6WVxE"
        val mainToken = "NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4"


        champions = getChampions() as ArrayList<String>
        val strChampions = champions.stream().collect(Collectors.joining(","))
        bot = SuperBot(mainToken)

        FileManager.getInstance().writeToFile(strChampions, "champions.csv", "")

        bot.start()
    }
}
