package leaguebot.commands.common

import com.google.gson.GsonBuilder
import net.dv8tion.jda.api.entities.TextChannel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import leaguebot.bot.LeagueBotLauncher
import leaguebot.commands.Command
import leaguebot.commands.ICommand
import leaguebot.dataclasses.Champion
import leaguebot.util.FileManager
import java.io.File

class ChampionCommand: Command("champion", listOf("champ")) {

    val laneMap = mapOf(
        Pair("top",         "top"),
        Pair("topo",        "top"),
        Pair("jungle",      "jungle"),
        Pair("jg",          "jungle"),
        Pair("selva",       "jungle"),
        Pair("mid",         "mid"),
        Pair("meio",        "mid"),
        Pair("sup",         "support"),
        Pair("suporte",     "support"),
        Pair("support",     "support"),
        Pair("adc",         "bot"),
        Pair("atirador",    "bot"),
        Pair("bot",         "bot"),
        Pair("bottom",      "bot")
    )

    override fun run(args: ArrayList<String>, channel: TextChannel){
        val f = FileManager.getInstance()

        channel.sendMessage("um momento, amigo").queue()
        //message validation

        val lane = args.last()

        logger.debug("size: ${args.size}")


        if( (args.size < 2) or
            !laneMap.containsKey(lane) ) {
            logger.warn("Invalid command: lane doesn't exist or message is missing arguments")
            sendErrorMessage(ICommand.InvalidCommand.ArgumentMissing, channel)
            return
        }

        args.remove(lane)
        logger.debug(args)

        var name = ""
        args.forEach{ name += it }
        name = name.replace("'", "")
            .replace(".", "")
            .toLowerCase()
        logger.debug("Champion name: $name")

        logger.debug("starting decodeChampion")
        val decoder = Runtime.getRuntime().exec("python3 decodeChampion.py $name")
        val decoderExitValue = decoder.waitFor()
        logger.debug("decodeChampion finished with code $decoderExitValue")
        if(decoderExitValue<0){
            logger.error("failed to decode champion name: $name")
            sendErrorMessage(ICommand.InvalidCommand.ArgumentMissing, channel)
            return
        }
        //end of validation

        name = LeagueBotLauncher.champions[decoderExitValue]

        val parser = GsonBuilder().setPrettyPrinting().create()
        val champion = parser.toJson(championParser(name, laneMap.getValue(lane)))

        if(!f.writeToFile(champion, "champion.json")) {
            logger.error("failed to write champion.json")
            sendErrorMessage(ICommand.InvalidCommand.ArgumentMissing, channel)
        }

        logger.debug("starting loadImage")
        val imgLoader = Runtime.getRuntime().exec("python3 loadImage.py")
        val imgLoaderExitValue = imgLoader.waitFor()
        logger.debug("loadImage finished with code $imgLoaderExitValue")

        channel.sendMessage("Ta aí, querido").queue()
        channel.sendFile(File(f.getFullPath("champion.png")), "$name.png").queue()

        if( !f.clearTemp() ){
            logger.warn("failed to delete old champion files! check the log file")
            logger.saveLog()
        }
    }

    private fun championParser(name: String, lane: String): Champion {
        val url = "http://www.op.gg/champion/$name/statistics/$lane"
        logger.debug(url)
        val site = Jsoup.connect(url).get()

        val spells = findSpells(site)
        logger.debug("${spells.first} | ${spells.second}")
        val runes = findRunes(site)
        val icon = findIcon(site)
        val attributes = findAttributes(site)

        return Champion(name, runes, attributes, spells, icon)
    }

    private fun findIcon(site: Document): String{
        return site.getElementsByClass("champion-stats-header-info__image")
            .first()
            .toString()
            .substringBetween("<img src=\"//", "?")
    }

    private fun findRunes(site: Document): List<String>{
        val runeElements = site.getElementsByClass("perk-page__item")
            .filter{ it.hasClass("perk-page__item--active") }

        val runes = ArrayList<String>()
        for(i in 0..5){
            val str = runeElements[i].toString()
                .substringBetween("<img src=\"//", "\" class")
            runes.add(str)
        }

        return runes
    }

    private fun findAttributes(site: Document): List<String>{
        val attributes = ArrayList<String>()
        val elements = site.getElementsByClass("fragment__row")
        for(i in 0..2){
            val attr = elements[i].getElementsByClass("active").first()
            attributes.add(attr.toString().substringBetween(
                "src=\"//",
                "\" class"
            ))
        }
        return attributes
    }

    private fun findSpells(site: Document): Pair<String, String> {
        val summoners = site.getElementsByClass("champion-overview__data")
            .first().getElementsByClass("champion-stats__list__item")

        val spellA = summoners.first().toString()
            .substringBetween("<img src=\"//", "?")
        val spellB = summoners.last().toString()
            .substringBetween("<img src=\"//", "?")

        return Pair(spellA, spellB)
    }

    private fun String.substringBetween(start: String, end: String): String{
        return this.substringAfter(start).substringBefore(end)
    }

    @Deprecated("Not working")
    private fun findWeak(site: Document): List<Pair<String, Double>> {
        val champions = site
            .getElementsByClass("champion-stats-header-matchup__table champion-stats-header-matchup__table--weak")
            .first().getElementsByClass("champion-stats-header-matchup__table__champion")

        val weakness = ArrayList<Pair<String, Double>>()
        for(element in champions){
            val champion = element.toString()
                .substringBetween("<img src=\"//", "?")
            val winrate = element.getElementsByTag("b")
            logger.debug(winrate)
        }

        return weakness
    }

    override fun getHelpMessage(): String {
        return "**$label <champion> <lane>**: Mostra runas e spells do campeão na lane escolhida\n" +
                "aliases: $aliases"
    }
}
