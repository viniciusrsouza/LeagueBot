package supervinicius.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import supervinicius.util.FileManager

class HelpCommand: Command("help") {
    override fun run(event: GuildMessageReceivedEvent) {
        val f = FileManager.getInstance().getFileFromResource("texts/commands")
        event.channel.sendMessage(FileManager.getInstance().getFileContent(f)).queue()
    }
}