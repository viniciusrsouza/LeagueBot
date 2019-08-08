package leaguebot.bot

import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import leaguebot.commands.common.ChampionCommand
import leaguebot.commands.common.HelpCommand
import leaguebot.commands.admin.LogCommand
import leaguebot.commands.music.JoinCommand
import leaguebot.listeners.DiscordListener

class LeagueBot (private val TOKEN: String) {
    val commands = listOf(
        HelpCommand(),
        ChampionCommand(),
        LogCommand(),
        JoinCommand()
    )

    fun start() {
        val discordListener = DiscordListener(this)

        val builder = JDABuilder(AccountType.BOT)
            .setToken(TOKEN)
            .addEventListeners(discordListener)

        builder.build()
    }
}