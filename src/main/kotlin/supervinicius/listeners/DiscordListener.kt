package supervinicius.listeners

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import supervinicius.bot.SuperBot

class DiscordListener(val bot: SuperBot): ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if(event.author.isBot) return

        bot.commands.forEach { if( it handle event ) return }
    }
}
