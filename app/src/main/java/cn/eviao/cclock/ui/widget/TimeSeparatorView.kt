package cn.eviao.cclock.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import cn.eviao.cclock.R
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.wrapContent


class TimeSeparatorView : FrameLayout {

    private val DOT_TEXT = ":"

    enum class SeparatorType {
        DOT, EMOJI
    }

    var type: SeparatorType = SeparatorType.DOT
        get() = field

    private lateinit var dotText: TextView
    private lateinit var emojiText: TextView

    private fun init() = AnkoContext.createDelegate(this).apply {
        layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent)

        val fontfamily = ResourcesCompat.getFont(context, R.font.arialbd)

        dotText = textView {
            text = DOT_TEXT
            textColor = Color.WHITE
            textSize = context.resources.getDimension(R.dimen.time_separator_dot_textsize)
            typeface = fontfamily
        }
        emojiText = textView {
            textSize = context.resources.getDimension(R.dimen.time_separator_emoji_textsize)
            typeface = fontfamily
            visibility = GONE
        }
    }

    fun show() {
        type = SeparatorType.DOT
        dotText.visibility = VISIBLE
        emojiText.visibility = GONE
    }

    fun show(@StringRes resId: Int) {
        type = SeparatorType.EMOJI
        emojiText.text = context.getString(resId)
        emojiText.visibility = VISIBLE
        dotText.visibility = GONE
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.timeSeparatorView(theme: Int = 0) = timeSeparatorView(theme) {}
inline fun ViewManager.timeSeparatorView(theme: Int = 0, init: TimeSeparatorView.() -> Unit) = ankoView(::TimeSeparatorView, theme, init)
