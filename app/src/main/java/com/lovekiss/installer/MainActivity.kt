package com.lovekiss.installer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Browser as AndroidBrowser
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_BROWSER_ID = "browser_id"
        private const val LOVEKISS_URL = "https://download.lovekiss.you/"
        private const val INSPIN_URL = "https://inspin.me/"
        private const val PREFS = "lovekiss_installer"
    }

    private lateinit var prefs: SharedPreferences
    private lateinit var browser: Browser
    private lateinit var logoView: ImageView
    private lateinit var browserName: TextView
    private lateinit var stepTitle: TextView
    private lateinit var stepDesc: TextView
    private lateinit var stepStatus: TextView
    private lateinit var installBtn: Button

    private val keyTmOpened get() = "tm_opened_${browser.id}"
    private val keyLkOpened get() = "lk_opened_${browser.id}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        browser = Browser.byId(intent.getStringExtra(EXTRA_BROWSER_ID))
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE)
        logoView = findViewById(R.id.iv_browser_logo)
        browserName = findViewById(R.id.tv_browser_name)
        stepTitle = findViewById(R.id.tv_step_title)
        stepDesc = findViewById(R.id.tv_step_desc)
        stepStatus = findViewById(R.id.tv_step_status)
        installBtn = findViewById(R.id.btn_install)

        logoView.setImageResource(browser.logoRes)
        browserName.text = browser.displayName
        findViewById<ImageView>(R.id.btn_back).setOnClickListener { finish() }
        installBtn.setOnClickListener { runCurrentStep() }
    }

    override fun onResume() {
        super.onResume()
        refreshUi()
    }

    private fun currentStep(): Int = when {
        !isPackageInstalled(browser.packageName) -> 1
        !prefs.getBoolean(keyTmOpened, false) -> 2
        !prefs.getBoolean(keyLkOpened, false) -> 3
        else -> 4
    }

    private fun refreshUi() {
        when (currentStep()) {
            1 -> setStep(getString(R.string.s1_title, browser.displayName),
                getString(R.string.s1_desc, browser.displayName),
                "1 / 3", R.string.btn_install, true)
            2 -> setStep(getString(R.string.s2_title),
                getString(R.string.s2_desc, browser.displayName),
                "2 / 3", R.string.btn_install, true)
            3 -> setStep(getString(R.string.s3_title),
                getString(R.string.s3_desc),
                "3 / 3", R.string.btn_install, true)
            else -> setStep(getString(R.string.done_title),
                getString(R.string.done_desc),
                "✓", R.string.btn_play, true)
        }
    }

    private fun setStep(title: String, desc: String, status: String, btn: Int, enabled: Boolean) {
        stepTitle.text = title
        stepDesc.text = desc
        stepStatus.text = status
        installBtn.setText(btn)
        installBtn.isEnabled = enabled
    }

    private fun runCurrentStep() {
        when (currentStep()) {
            1 -> installBrowser()
            2 -> if (openInBrowser(browser.tampermonkeyUrl)) {
                prefs.edit().putBoolean(keyTmOpened, true).apply()
            }
            3 -> if (openInBrowser(LOVEKISS_URL)) {
                prefs.edit().putBoolean(keyLkOpened, true).apply()
            }
            else -> openInBrowser(INSPIN_URL, newTab = true)
        }
    }

    private fun installBrowser() {
        if (browser.hasPlayStore) {
            val market = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${browser.packageName}")
            )
            try {
                startActivity(market); return
            } catch (_: ActivityNotFoundException) {
            }
            val web = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${browser.packageName}")
            )
            try {
                startActivity(web); return
            } catch (_: ActivityNotFoundException) {
            }
        }
        val direct = Intent(Intent.ACTION_VIEW, Uri.parse(browser.installFallbackUrl))
        try {
            startActivity(direct)
        } catch (_: ActivityNotFoundException) {
            toast("Не удалось открыть страницу установки. Ссылка: ${browser.installFallbackUrl}")
        }
    }

    private fun openInBrowser(url: String, newTab: Boolean = false): Boolean {
        if (!isPackageInstalled(browser.packageName)) {
            toast("Сначала установи ${browser.displayName}")
            return false
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setPackage(browser.packageName)
            if (newTab) {
                putExtra(AndroidBrowser.EXTRA_CREATE_NEW_TAB, true)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        return try {
            startActivity(intent); true
        } catch (_: ActivityNotFoundException) {
            toast("Не удалось открыть ${browser.displayName}"); false
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
