package supervinicius.commands

import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import supervinicius.util.Logger

abstract class Command(override val label: String,
                       override val aliases: List<String>): ICommand {
    override val logger: Logger = Logger.getInstance()

    override infix fun handle(event: GuildMessageReceivedEvent): Boolean {
        if(event.author.isBot) return false

        val cmd: ArrayList<String> = try{
            event.message.contentRaw.toLowerCase().split(" ") as ArrayList
        }catch (e: Exception) {
            arrayListOf(event.message.contentRaw.toLowerCase())
        }

        return if(cmd.first() == label || aliases.contains(cmd.first())){
            cmd.removeAt(0)
            run(cmd, event.channel)
            true
        }else false
    }

    override fun sendErrorMessage(error: ICommand.InvalidCommand, channel: TextChannel) {
        when (error){
            ICommand.InvalidCommand.ArgumentMissing -> {
                channel.sendMessage("Comando inválido").queue()
            }
        }
    }
}