package cn.eviao.cclock

import android.app.Application
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initEmoji()
    }

    private fun initEmoji() {
        val config = BundledEmojiCompatConfig(applicationContext).apply {
            setReplaceAll(true)
        }
        EmojiCompat.init(config)
    }
}