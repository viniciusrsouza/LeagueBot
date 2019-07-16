package supervinicius.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class LogCommand : Command("savelog"){
    override fun run(event: GuildMessageReceivedEvent) {
        if(logger.saveLog())
            event.channel.sendMessage("Log salvo").queue()
    }

}