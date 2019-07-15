package supervinicius.bot

object SuperBotLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        //    beta token NjAwMTE2NDc3NDQxNDc0NTYy.XSvEbg.hpqj_OYsWQ2am6nAr_OX75q1fZE
        // release token NTk5NDIzMDkzNjg1MDkyMzcy.XSlAOA.0NcSqJpuszdjXm-5sAbBgCgVU_4
        val TOKEN = "NjAwMTE2NDc3NDQxNDc0NTYy.XSvEbg.hpqj_OYsWQ2am6nAr_OX75q1fZE"
        val bot = SuperBot(TOKEN)

        bot.start()
    }
}