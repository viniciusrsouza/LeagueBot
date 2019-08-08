package leaguebot.commands

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

abstract class AdminCommand(label: String, aliases: List<String>): Command(label, aliases){

    override fun handle(event: GuildMessageReceivedEvent): Boolean {
        return if(event.member?.id == "#1329") super.handle(event)
        else false
    }

}