package supervinicius.bot

import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import supervinicius.commands.common.ChampionCommand
import supervinicius.commands.common.HelpCommand
import supervinicius.commands.admin.LogCommand
import supervinicius.commands.music.JoinCommand
import supervinicius.listeners.DiscordListener

class SuperBot (private val TOKEN: String) {
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