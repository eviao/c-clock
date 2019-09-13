package cn.eviao.cclock.ui.widget

import android.view.ViewManager
import com.robinhood.ticker.TickerView
import org.jetbrains.anko.custom.ankoView


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.tickerView(theme: Int) = tickerView(theme) {}
inline fun ViewManager.tickerView(theme: Int = 0, init: TickerView.() -> Unit) =
    ankoView({ TickerView(it) }, theme, init)