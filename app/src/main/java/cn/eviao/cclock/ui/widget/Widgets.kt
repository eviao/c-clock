package cn.eviao.cclock.ui.widget

import android.view.ViewManager
import androidx.emoji.widget.EmojiTextView
import com.robinhood.ticker.TickerView
import org.jetbrains.anko.custom.ankoView


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.tickerView(theme: Int) = tickerView(theme) {}
inline fun ViewManager.tickerView(theme: Int = 0, init: TickerView.() -> Unit) =
    ankoView({ TickerView(it) }, theme, init)

inline fun ViewManager.emojiTextView(theme: Int) = emojiTextView(theme) {}
inline fun ViewManager.emojiTextView(theme: Int = 0, init: EmojiTextView.() -> Unit) =
    ankoView({ EmojiTextView(it) }, theme, init)
