package cn.eviao.cclock.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.emoji.widget.EmojiTextView
import cn.eviao.cclock.R
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView


class TimeSeparatorView : FrameLayout {

    enum class SeparatorType {
        DOT, EMOJI
    }

    var type: SeparatorType = SeparatorType.DOT
        get() = field

    private lateinit var dotText: TextView
    private lateinit var emojiText: EmojiTextView

    private fun init() = AnkoContext.createDelegate(this).apply {
        layoutParams = FrameLayout.LayoutParams(wrapContent, wrapContent)

        val fontfamily = ResourcesCompat.getFont(context, R.font.arialbd)

        dotText = textView {
            text = context.resources.getString(R.string.time_separator_dot_text)
            textColor = Color.WHITE
            textSize = context.resources.getDimension(R.dimen.time_separator_dot_textsize)
            typeface = fontfamily
        }
        emojiText = emojiTextView {
            textSize = context.resources.getDimension(R.dimen.time_separator_emoji_textsize)
            textColor = Color.RED
            typeface = fontfamily
            visibility = GONE
        }
    }

    fun show() {
        type = SeparatorType.DOT
        dotText.visibility = VISIBLE
        emojiText.visibility = GONE
    }

    fun show(emoji: String) {
        type = SeparatorType.EMOJI
        emojiText.text = emoji
        emojiText.visibility = VISIBLE
        dotText.visibility = GONE
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.timeSeparatorView(theme: Int = 0) = timeSeparatorView(theme) {}
inline fun ViewManager.timeSeparatorView(theme: Int = 0, init: TimeSeparatorView.() -> Unit) = ankoView(::TimeSeparatorView, theme, init)
