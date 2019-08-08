package leaguebot.commands.music

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import leaguebot.commands.Command

class JoinCommand: Command("chega+", ArrayList()) {

    lateinit var event: GuildMessageReceivedEvent

    override fun handle(event: GuildMessageReceivedEvent): Boolean {
        this.event = event
        return super.handle(event)
    }

    override fun run(args: ArrayList<String>, channel: TextChannel) {
        val audioManager = event.guild.audioManager

        if(audioManager.isConnected){
            channel.sendMessage("Ja colei mô vei").queue()
            return
        }

        val memberVoiceState = event.member?.voiceState
        if(!memberVoiceState!!.inVoiceChannel()){
            channel.sendMessage("Tu tem q tá num canal de voz, parcero").queue()
            return
        }

        val voiceChannel = memberVoiceState.channel!!
        val selfMember = event.guild.selfMember

        if(!selfMember.hasPermission(voiceChannel, Permission.VOICE_CONNECT)){
            channel.sendMessageFormat("to sem permissão pra entrar no %s", voiceChannel).queue()
            return
        }
        audioManager.openAudioConnection(voiceChannel)
        channel.sendMessage("penetrei").queue()
    }

    override fun getHelpMessage(): String {
        return "**$label**: Entra em um canal de voz\n"
    }
}