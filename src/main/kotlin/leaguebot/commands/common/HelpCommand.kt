package leaguebot.commands.common

import net.dv8tion.jda.api.entities.TextChannel
import leaguebot.bot.LeagueBotLauncher
import leaguebot.commands.Command

class HelpCommand: Command("help", listOf("commands", "comandos", "ajuda")) {
    override fun run(args: ArrayList<String>, channel: TextChannel) {
        var message = ""
        logger.debug("on help, args:$args size:${args.size}")
        when {
            args.size == 0 -> for(cmd in LeagueBotLauncher.bot.commands)
                message += "${cmd.getHelpMessage()}\n\n"
            args.size == 1 -> for(cmd in LeagueBotLauncher.bot.commands) {
                logger.debug("cmd label = ${cmd.label}, args first = ${args.first()}")
                if (args.first() == cmd.label) {
                    message += cmd.getHelpMessage()
                }
            }
            else -> return
        }

        channel.sendMessage(message).queue()
    }

    override fun getHelpMessage(): String {
        return "**$label [comando: opicional]**: exibe esta mensagem ou exibe a descrição do comando selecionado\n" +
                "aliases: $aliases"
    }
}