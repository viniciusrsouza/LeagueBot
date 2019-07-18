package supervinicius.bot

object SuperBotLauncher {
    lateinit var bot: SuperBot
    @JvmStatic
    fun main(args: Array<String>) {
        val betaToken = "NjAwMTE2NDc3NDQxNDc0NTYy.XS4gMQ.3_9LFpuGMW9kwvuBJgAAGe6WVxE"
        val mainToken = "NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4"
        bot = SuperBot(mainToken)

        bot.start()
    }
}
