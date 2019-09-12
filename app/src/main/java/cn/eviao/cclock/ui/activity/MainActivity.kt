package cn.eviao.cclock.ui.activity

import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cn.eviao.cclock.R
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

    private fun settingTime(step: Long) {
        val calendar = Calendar.getInstance()
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)

        ui.hoursText.text = hours.toString().padStart(2, '0')
        ui.minutesText.text = minutes.toString().padStart(2, '0')
    }
}

class MainActivityUi : AnkoComponent<MainActivity> {

    lateinit var hoursText: TextView
    lateinit var minutesText: TextView
    lateinit var separatorText: TextView

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)

            linearLayout {
                gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL

                hoursText = themedTextView(R.style.time) {
                    text = "00"
                }

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

                minutesText = themedTextView(R.style.time) {
                    text = "00"
                }
            }.lparams(width = matchParent, height = matchParent) {
                topMargin = dip(-56)
            }
        }
    }
}
