package leaguebot.bot

import leaguebot.util.FileManager
import leaguebot.util.getChampions
import java.util.stream.Collectors

object LeagueBotLauncher {
    lateinit var bot: LeagueBot
    lateinit var champions: ArrayList<String>
    @JvmStatic
    fun main(args: Array<String>) {
        val betaToken = "NjAwMTE2NDc3NDQxNDc0NTYy.XUx0Gw.SLtRfFld_PFbnC_VnHwWgIjEDZE"
        val mainToken = "NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4"


        champions = getChampions() as ArrayList<String>
        val strChampions = champions.stream().collect(Collectors.joining(","))
        bot = LeagueBot(betaToken)

        FileManager.getInstance().writeToFile(strChampions, "champions.csv", "")

        bot.start()
    }
}
