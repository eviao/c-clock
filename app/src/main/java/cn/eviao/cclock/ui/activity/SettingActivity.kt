package cn.eviao.cclock.ui.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.preference.PreferenceFragmentCompat
import cn.eviao.cclock.R
import org.jetbrains.anko.*
import androidx.preference.Preference
import org.jetbrains.anko.support.v4.toast


class SettingActivity : BaseActivity() {

    private lateinit var ui: SettingActivityUi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ui = SettingActivityUi()
        ui.setContentView(this)

        supportFragmentManager.beginTransaction()
            .add(SettingActivityUi.ID_CONTAINER, SettingFragmentCompat())
            .commit()
    }
}

class SettingActivityUi : AnkoComponent<SettingActivity> {

    companion object {
        const val ID_CONTAINER = 0x0001
    }

    override fun createView(ui: AnkoContext<SettingActivity>) = with(ui) {
        verticalLayout {
            layoutParams = LinearLayout.LayoutParams(matchParent, matchParent)

            frameLayout {
                id = Companion.ID_CONTAINER
            }.lparams(width = matchParent, height = matchParent)
        }
    }
}

class SettingFragmentCompat : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_setting)

        val settingBackgroundKey = getString(R.string.setting_background_key)

        findPreference<Preference>(settingBackgroundKey).setOnPreferenceChangeListener { preference, newValue ->
            toast("已保存")
            true
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return super.onPreferenceTreeClick(preference)
    }
}