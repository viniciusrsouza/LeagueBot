package leaguebot.bot

import leaguebot.util.FileManager
import leaguebot.util.getChampions
import java.util.stream.Collectors

object LeagueBotLauncher {
    lateinit var bot: LeagueBot
    lateinit var champions: ArrayList<String>
    @JvmStatic
    fun main(args: Array<String>) {
        val betaToken = ""
        val mainToken = ""


        champions = getChampions() as ArrayList<String>
        val strChampions = champions.stream().collect(Collectors.joining(","))
        bot = LeagueBot(mainToken)

        FileManager.getInstance().writeToFile(strChampions, "champions.csv", "")

        bot.start()
    }
}
