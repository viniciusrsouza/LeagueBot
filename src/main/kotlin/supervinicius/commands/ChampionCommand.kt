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
        event.channel.sendMessage("um momento, amigo").queue()
        //message validation
        val splitMessage: ArrayList<String>
        try{
            splitMessage = event.message.contentRaw.split(" ") as ArrayList
        }catch (e: Exception) {
            sendErrorMessage(InvalidCommand.ArguementMissing, event.channel)
            return
        }

        val lane = splitMessage.last()

        printdbg("size: ${splitMessage.size}")


        if( (splitMessage.size < 3) or
            !laneMap.containsKey(lane) ) {
            sendErrorMessage(InvalidCommand.ArguementMissing, event.channel)
            return
        }

        splitMessage.remove(lane)
        splitMessage.removeAt(0)
        printdbg(splitMessage)

        var name = ""
        splitMessage.forEach{ name += it }
        name = name.replace("'", "")
            .replace(".", "")
            .toLowerCase()
        printdbg("Champion name: $name")
        //end of validation

        val parser = GsonBuilder().setPrettyPrinting().create()
        val champion = parser.toJson(championParser(name, laneMap.getValue(lane)))

        if(!FileManager.getInstance().writeToFile(champion, "champion.json"))
            sendErrorMessage(InvalidCommand.ArguementMissing, event.channel)

        printdbg("starting loadImage")
        val p = Runtime.getRuntime().exec("python loadImage.py")
        val exitValue = p.waitFor()
        printdbg("loadImage finished with code $exitValue")

        event.channel.sendMessage("Ta aí, querido").queue()
        event.channel.sendFile(File("champion.png"), "$name.png").queue()

//        File("champion.json").delete()
//        File("champion.png").delete()
//        File("first_spell.png").delete()
//        File("second_spell.png").delete()
//        File("icon.png").delete()
//        File("main_rune.png").delete()
//        File("rune1.png").delete()
//        File("rune2.png").delete()
//        File("rune3.png").delete()
//        File("rune4.png").delete()
//        File("rune5.png").delete()

    }

    private fun championParser(name: String, lane: String): Champion {
        val url = "http://op.gg/champion/$name/statistics/$lane"
        printdbg(url)
        val site = Jsoup.connect(url).get()

        val spells = findSpells(site)
        printdbg("${spells.first} | ${spells.second}")
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
            printdbg(winrate)
        }

        return weakness
    }

}
