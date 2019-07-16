package supervinicius.bot

object SuperBotLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val betaToken = "NjAwMTE2NDc3NDQxNDc0NTYy.XS4gMQ.3_9LFpuGMW9kwvuBJgAAGe6WVxE"
        val mainToken = "NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4"

        val bot = SuperBot(mainToken)

        bot.start()
    }
}