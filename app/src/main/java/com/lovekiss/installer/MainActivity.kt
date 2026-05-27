package com.lovekiss.installer

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val FIREFOX_PKG = "org.mozilla.firefox"
        private const val TAMPERMONKEY_URL =
            "https://addons.mozilla.org/ru/firefox/addon/tampermonkey/"
        private const val LOVEKISS_URL = "https://download.lovekiss.you/"
        private const val FIREFOX_DIRECT_URL =
            "https://www.mozilla.org/ru/firefox/android/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_firefox).setOnClickListener {
            installFirefox()
        }
        findViewById<Button>(R.id.btn_tampermonkey).setOnClickListener {
            openInFirefox(TAMPERMONKEY_URL)
        }
        findViewById<Button>(R.id.btn_lovekiss).setOnClickListener {
            openInFirefox(LOVEKISS_URL)
        }
    }

    private fun installFirefox() {
        if (isPackageInstalled(FIREFOX_PKG)) {
            toast("Firefox уже установлен. Переходи к шагу 2.")
            return
        }
        val market = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$FIREFOX_PKG")
        )
        try {
            startActivity(market)
            return
        } catch (_: ActivityNotFoundException) {
        }
        val web = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$FIREFOX_PKG")
        )
        try {
            startActivity(web)
            return
        } catch (_: ActivityNotFoundException) {
        }
        val direct = Intent(Intent.ACTION_VIEW, Uri.parse(FIREFOX_DIRECT_URL))
        try {
            startActivity(direct)
        } catch (_: ActivityNotFoundException) {
            toast("Не удалось открыть ссылку. Скачай Firefox с mozilla.org вручную.")
        }
    }

    private fun openInFirefox(url: String) {
        if (!isPackageInstalled(FIREFOX_PKG)) {
            toast("Сначала установи Firefox (Шаг 1).")
            return
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage(FIREFOX_PKG)
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            toast("Не удалось открыть Firefox.")
        }
    }

    private fun isPackageInstalled(pkg: String): Boolean = try {
        packageManager.getPackageInfo(pkg, 0)
        true
    } catch (_: Exception) {
        false
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
