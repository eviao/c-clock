package cn.eviao.cclock.ui.activity

import android.os.Bundle
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import cn.eviao.cclock.R
import cn.eviao.cclock.ui.widget.tickerView
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
        window.setBackgroundDrawableResource(R.drawable.black)

        ui = MainActivityUi()
        ui.setContentView(this)
    }

    override fun onResume() {
        super.onResume()
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::settingTime))
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    private fun showFace() {
        ui.separatorText.visibility = GONE
        ui.faceText.visibility = VISIBLE
    }

    private fun hideFace() {
        ui.separatorText.visibility = VISIBLE
        ui.faceText.visibility = GONE
    }

    private fun settingTime(step: Long) {
        val calendar = Calendar.getInstance()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
//        val minutes = calendar.get(Calendar.MINUTE)
        val minutes = calendar.get(Calendar.SECOND)

        ui.hoursText.text = hours.toString().padStart(2, '0')
        ui.minutesText.text = minutes.toString().padStart(2, '0')
    }
}

class MainActivityUi : AnkoComponent<MainActivity> {

    private val timeAnimationDuration = 500L

    lateinit var hoursText: TickerView
    lateinit var minutesText: TickerView
    lateinit var separatorText: TextView
    lateinit var faceText: TextView

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)

            linearLayout {
                gravity = CENTER or CENTER_VERTICAL

                val font = ResourcesCompat.getFont(context, R.font.arialbd)

                hoursText = tickerView(R.style.time) {
                    animationDuration = timeAnimationDuration
                    setCharacterLists(TickerUtils.provideNumberList())
                    text = "00"
                    typeface = font
                }.lparams(height = wrapContent)

                separatorText = themedTextView(R.style.time) {
                    text = ":"
                }.lparams {
                    leftMargin = dip(8)
                    rightMargin = dip(8)
                }
                separatorText.startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
                    duration = 1000
                    repeatCount = Animation.INFINITE
                })

                faceText= themedTextView(R.style.face) {
                    text = "❤"
                    visibility = GONE
                }.lparams {
                    leftMargin = dip(8)
                    rightMargin = dip(8)
                }
                faceText.startAnimation(AlphaAnimation(0.0f, 1.0f).apply {
                    duration = 1000
                    repeatCount = Animation.INFINITE
                })

                minutesText = tickerView(R.style.time) {
                    animationDuration = timeAnimationDuration
                    setCharacterLists(TickerUtils.provideNumberList())
                    text = "00"
                    typeface = font
                }.lparams(height = wrapContent)
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}
