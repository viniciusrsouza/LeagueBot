package supervinicius.bot

object SuperBotLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val TOKEN = "NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4"
        val bot = SuperBot(TOKEN)

        bot.start()
    }
}