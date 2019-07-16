package supervinicius.commands

import com.google.gson.GsonBuilder
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import supervinicius.dataclasses.Champion
import supervinicius.util.FileManager
import java.io.File

class ChampionCommand: Command("champion") {

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

    override fun run(event: GuildMessageReceivedEvent) {
        val f = FileManager.getInstance()

        event.channel.sendMessage("um momento, amigo").queue()
        //message validation
        val splitMessage: ArrayList<String>
        try{
            splitMessage = event.message.contentRaw.split(" ") as ArrayList
        }catch (e: Exception) {
            logger.error("failed to cast message to ArrayList")
            sendErrorMessage(InvalidCommand.ArgumentMissing, event.channel)
            return
        }

        val lane = splitMessage.last()

        logger.debug("size: ${splitMessage.size}")


        if( (splitMessage.size < 3) or
            !laneMap.containsKey(lane) ) {
            logger.warn("Invalid command: lane doesn't exist or message is missing arguments")
            sendErrorMessage(InvalidCommand.ArgumentMissing, event.channel)
            return
        }

        splitMessage.remove(lane)
        splitMessage.removeAt(0)
        logger.debug(splitMessage)

        var name = ""
        splitMessage.forEach{ name += it }
        name = name.replace("'", "")
            .replace(".", "")
            .toLowerCase()
        logger.debug("Champion name: $name")
        //end of validation

        val parser = GsonBuilder().setPrettyPrinting().create()
        val champion = parser.toJson(championParser(name, laneMap.getValue(lane)))

        if(!f.writeToFile(champion, "champion.json")) {
            logger.error("failed to write champion.json")
            sendErrorMessage(InvalidCommand.ArgumentMissing, event.channel)
        }

        logger.debug("starting loadImage")
        val p = Runtime.getRuntime().exec("python3 loadImage.py")
        val exitValue = p.waitFor()
        logger.debug("loadImage finished with code $exitValue")

        event.channel.sendMessage("Ta aí, querido").queue()
        event.channel.sendFile(File(f.getFullPath("champion.png")), "$name.png").queue()

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

}
