package supervinicius.bot

object SuperBotLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        // beta NjAwMTE2NDc3NDQxNDc0NTYy.XS4gMQ.3_9LFpuGMW9kwvuBJgAAGe6WVxE
        // main NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4
        val token = "NjAwMTE2NDc3NDQxNDc0NTYy.XS4gMQ.3_9LFpuGMW9kwvuBJgAAGe6WVxE"
        val bot = SuperBot(token)

        bot.start()
    }
}