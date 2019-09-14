package cn.eviao.cclock.ui.activity

import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity.*
import android.view.KeyEvent
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.animation.*
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.preference.PreferenceManager
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
import org.w3c.dom.Text
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {

    private var compositeDisposable: CompositeDisposable

    private lateinit var ui: MainActivityUi
    private lateinit var preferences: SharedPreferences

    private var lastExitTimestamp = 0L

    init {
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = MainActivityUi()
        ui.setContentView(this)

        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        ui.settingButton.setOnClickListener {
            startActivity<SettingActivity>()
        }
    }

    override fun onResume() {
        super.onResume()
        startTiming()
        startSeparatorAnimation()
        applySetting()
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            exit()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if ((System.currentTimeMillis() - lastExitTimestamp) > 2000) {
            toast("再按一次退出应用")
            lastExitTimestamp = System.currentTimeMillis()
        } else {
            finish()
            System.exit(0)
        }
    }

    private fun applySetting() {
        val backgroundKey = getString(R.string.setting_background_key)

        val backgroundValue = preferences.getInt(backgroundKey, ContextCompat.getColor(this, R.color.backgroundColorDefault));
        window.setBackgroundDrawable(ColorDrawable(backgroundValue))
    }

    private fun startTiming() {
        compositeDisposable.add(Observable.interval(1, TimeUnit.SECONDS)
            .map {
                val calendar = Calendar.getInstance()
                val hours = calendar.get(Calendar.HOUR_OF_DAY)
//                val minutes = calendar.get(Calendar.MINUTE)
                val minutes = calendar.get(Calendar.SECOND)
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

    lateinit var settingButton: ImageButton

    lateinit var hoursText: TickerView
    lateinit var minutesText: TickerView
    lateinit var separatorView: TimeSeparatorView

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        frameLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)

            settingButton = imageButton(R.drawable.ic_setting_24_ffffff) {
                background = null
                alpha = 0.512f
            }.lparams {
                gravity = TOP or END
                setMargins(dip(8))
            }

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
