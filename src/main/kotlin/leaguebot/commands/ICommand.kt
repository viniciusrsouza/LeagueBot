package supervinicius.commands

import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import supervinicius.util.Logger

interface ICommand {
    enum class InvalidCommand{
        ArgumentMissing
    }

    val label: String
    val aliases: List<String>
    val logger: Logger

    infix fun handle(event: GuildMessageReceivedEvent): Boolean
    fun run(args: ArrayList<String>, channel: TextChannel)
    fun sendErrorMessage(error: InvalidCommand, channel: TextChannel)
    fun getHelpMessage(): String
}