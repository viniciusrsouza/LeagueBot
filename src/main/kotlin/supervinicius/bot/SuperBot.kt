package supervinicius.bot

import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDABuilder
import supervinicius.commands.ChampionCommand
import supervinicius.commands.HelpCommand
import supervinicius.commands.LogCommand
import supervinicius.listeners.DiscordListener

class SuperBot (val TOKEN: String) {
    val commands = listOf(
        HelpCommand(),
        ChampionCommand(),
        LogCommand()
    )

    val champions: ArrayList<String> = ArrayList()

    fun start() {
        val discordListener = DiscordListener(this)

        val builder = JDABuilder(AccountType.BOT)
            .setToken(TOKEN)
            .addEventListeners(discordListener)

        builder.build()
    }
}