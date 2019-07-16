package supervinicius.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import supervinicius.util.FileManager
import java.io.InputStream

class HelpCommand: Command("help") {
    override fun run(event: GuildMessageReceivedEvent) {
        val f: InputStream
        try {
            f = FileManager.getInstance().getFileFromResource("/texts/commands")
            event.channel.sendMessage(FileManager.getInstance().getFileContent(f)).queue()
        }catch (e: Exception) {
            logger.error("could not find the commands file")
            e.printStackTrace()
        }
    }
}