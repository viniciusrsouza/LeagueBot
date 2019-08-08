package leaguebot.dataclasses

data class Champion( val name: String, val runes: List<String>, val attributes: List<String>, val spells: Pair<String, String>, val icon: String)