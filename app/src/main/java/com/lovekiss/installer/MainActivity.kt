package com.lovekiss.installer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FIREFOX_PKG = "org.mozilla.firefox"
        private const val TAMPERMONKEY_URL =
            "https://addons.mozilla.org/ru/firefox/addon/tampermonkey/"
        private const val LOVEKISS_URL = "https://download.lovekiss.you/"
        private const val INSPIN_URL = "https://inspin.me/"
        private const val FIREFOX_DIRECT_URL =
            "https://www.mozilla.org/ru/firefox/android/"

        private const val PREFS = "lovekiss_installer"
        private const val KEY_TM_OPENED = "tampermonkey_opened"
        private const val KEY_LK_OPENED = "lovekiss_opened"
    }

    private lateinit var prefs: SharedPreferences
    private lateinit var stepTitle: TextView
    private lateinit var stepDesc: TextView
    private lateinit var stepStatus: TextView
    private lateinit var installBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        stepTitle = findViewById(R.id.tv_step_title)
        stepDesc = findViewById(R.id.tv_step_desc)
        stepStatus = findViewById(R.id.tv_step_status)
        installBtn = findViewById(R.id.btn_install)

        installBtn.setOnClickListener { runCurrentStep() }
    }

    override fun onResume() {
        super.onResume()
        refreshUi()
    }

    private fun currentStep(): Int = when {
        !isPackageInstalled(FIREFOX_PKG) -> 1
        !prefs.getBoolean(KEY_TM_OPENED, false) -> 2
        !prefs.getBoolean(KEY_LK_OPENED, false) -> 3
        else -> 4
    }

    private fun refreshUi() {
        when (currentStep()) {
            1 -> setStep(R.string.s1_title, R.string.s1_desc, "1 / 3", R.string.btn_install, true)
            2 -> setStep(R.string.s2_title, R.string.s2_desc, "2 / 3", R.string.btn_install, true)
            3 -> setStep(R.string.s3_title, R.string.s3_desc, "3 / 3", R.string.btn_install, true)
            else -> setStep(R.string.done_title, R.string.done_desc, "✓", R.string.btn_play, true)
        }
    }

    private fun setStep(title: Int, desc: Int, status: String, btn: Int, enabled: Boolean) {
        stepTitle.setText(title)
        stepDesc.setText(desc)
        stepStatus.text = status
        installBtn.setText(btn)
        installBtn.isEnabled = enabled
    }

    private fun runCurrentStep() {
        when (currentStep()) {
            1 -> installFirefox()
            2 -> if (openInFirefox(TAMPERMONKEY_URL)) {
                prefs.edit().putBoolean(KEY_TM_OPENED, true).apply()
            }
            3 -> if (openInFirefox(LOVEKISS_URL)) {
                prefs.edit().putBoolean(KEY_LK_OPENED, true).apply()
            }
            else -> openInFirefox(INSPIN_URL, newTab = true)
        }
    }

    private fun installFirefox() {
        val market = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$FIREFOX_PKG"))
        try {
            startActivity(market); return
        } catch (_: ActivityNotFoundException) {
        }
        val web = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$FIREFOX_PKG")
        )
        try {
            startActivity(web); return
        } catch (_: ActivityNotFoundException) {
        }
        val direct = Intent(Intent.ACTION_VIEW, Uri.parse(FIREFOX_DIRECT_URL))
        try {
            startActivity(direct)
        } catch (_: ActivityNotFoundException) {
            toast("Скачай Firefox с mozilla.org вручную")
        }
    }

    private fun openInFirefox(url: String, newTab: Boolean = false): Boolean {
        if (!isPackageInstalled(FIREFOX_PKG)) {
            toast("Сначала установи Firefox")
            return false
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setPackage(FIREFOX_PKG)
            if (newTab) {
                putExtra(Intent.EXTRA_OPEN_NEW_TAB, true)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        return try {
            startActivity(intent); true
        } catch (_: ActivityNotFoundException) {
            toast("Не удалось открыть Firefox"); false
        }
    }

    private fun isPackageInstalled(pkg: String): Boolean = try {
        packageManager.getPackageInfo(pkg, 0); true
    } catch (_: Exception) {
        false
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
