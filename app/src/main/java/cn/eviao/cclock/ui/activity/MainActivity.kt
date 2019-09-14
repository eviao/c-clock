package cn.eviao.cclock.ui.activity

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.view.animation.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.emoji.text.EmojiCompat
import cn.eviao.cclock.R
import cn.eviao.cclock.ui.widget.TimeSeparatorView
import cn.eviao.cclock.ui.widget.tickerView
import cn.eviao.cclock.ui.widget.timeSeparatorView
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private var compositeDisposable: CompositeDisposable
    private lateinit var ui: MainActivityUi

    init {
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.setBackgroundDrawableResource(R.drawable.green)

        ui = MainActivityUi()
        ui.setContentView(this)

        ui.separatorView.show("\uD83D\uDE0D")
    }

    override fun onResume() {
        super.onResume()
        startTiming()
        startSeparatorAnimation()
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun startTiming() {
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .map {
                val calendar = Calendar.getInstance()
                val hours = calendar.get(Calendar.HOUR_OF_DAY)
                val minutes = calendar.get(Calendar.MINUTE)
//                val minutes = calendar.get(Calendar.SECOND)
                arrayOf(hours, minutes)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ui.hoursText.text = it[0].toString().padStart(2, '0')
                ui.minutesText.text = it[1].toString().padStart(2, '0')
            })
    }

    private fun startSeparatorAnimation() {
        compositeDisposable.add(Observable.interval(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ui.separatorView.visibility = if (ui.separatorView.visibility == VISIBLE)
                    INVISIBLE
                else
                    VISIBLE
            })
    }
}

class MainActivityUi : AnkoComponent<MainActivity> {

    private val timeAnimationDuration = 500L
    private val timeInterpolator = OvershootInterpolator()
    private val timeScrollingDirection = TickerView.ScrollingDirection.DOWN

    lateinit var hoursText: TickerView
    lateinit var minutesText: TickerView
    lateinit var separatorView: TimeSeparatorView

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)

            linearLayout {
                gravity = CENTER or CENTER_VERTICAL

                val fontFamily = ResourcesCompat.getFont(context, R.font.arialbd)
                val fontSize = context.resources.getDimension(R.dimen.time_textsize)

                hoursText = tickerView(R.style.time) {
                    animationDuration = timeAnimationDuration
                    animationInterpolator = timeInterpolator
                    setPreferredScrollingDirection(timeScrollingDirection)
                    setCharacterLists(TickerUtils.provideNumberList())
                    textSize = fontSize
                    typeface = fontFamily

                    text = context.getString(R.string.time_default_text)
                }.lparams(height = wrapContent)

                separatorView = timeSeparatorView {}.lparams {
                    leftMargin = dip(8)
                    rightMargin = dip(8)
                }

                minutesText = tickerView(R.style.time) {
                    animationDuration = timeAnimationDuration
                    animationInterpolator = timeInterpolator
                    setPreferredScrollingDirection(timeScrollingDirection)
                    setCharacterLists(TickerUtils.provideNumberList())
                    textSize = fontSize
                    typeface = fontFamily

                    text = context.getString(R.string.time_default_text)
                }.lparams(height = wrapContent)
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}
