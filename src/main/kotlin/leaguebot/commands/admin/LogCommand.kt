package supervinicius.commands.admin

import net.dv8tion.jda.api.entities.TextChannel
import supervinicius.commands.AdminCommand

class LogCommand : AdminCommand("savelog", listOf("exportlog", "log")){
    override fun run(args: ArrayList<String>, channel: TextChannel) {
        if(logger.saveLog())
            channel.sendMessage("Log salvo").queue()
    }

    override fun getHelpMessage(): String {
        return "**$label**[vinicin apenas]: salva o log atual do bot\n" +
                "aliases: $aliases"
    }

}