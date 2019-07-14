package supervinicius.commands

import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import supervinicius.util.Logger

abstract class Command(val label: String) {

    enum class InvalidCommand{
        ArguementMissing
    }

    infix fun handle(event: GuildMessageReceivedEvent): Boolean {

        return if(event.message.contentRaw.startsWith(label)) {
            Logger.getInstance().logCommand(event.message.contentRaw)
            run(event)
            true
        }else false
    }

    abstract fun run(event: GuildMessageReceivedEvent)

    fun sendErrorMessage(error: InvalidCommand, channel: TextChannel)
    {
        when (error){
            InvalidCommand.ArguementMissing -> {
                channel.sendMessage("Comando inválido").queue()
            }
        }
    }

    fun printdbg(message: Any){
        Logger.getInstance().debug(message)
    }
}