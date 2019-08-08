package supervinicius.util

import org.jsoup.Jsoup

fun getChampions(): List<String>{
    val champions = ArrayList<String>()
    val elements = Jsoup.connect("http://br.op.gg/champion/statistics").get()
        .getElementsByAttribute("data-champion-name")


    for(e in elements){
        champions.add( e.attr("data-champion-name").replace("'", "") )
    }

    return champions
}