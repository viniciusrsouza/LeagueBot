package leaguebot.listeners

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import leaguebot.bot.LeagueBot
import leaguebot.util.Logger

class DiscordListener(val bot: LeagueBot): ListenerAdapter() {
    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        if(event.author.isBot) return

        bot.commands.forEach {
            if( it handle event ) {
                Logger.getInstance().debug("(Chat) ${event.author.name}: ${event.message.contentRaw}")
                return
            }


        }
    }
}
