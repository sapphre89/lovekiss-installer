package com.lovekiss.installer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picker)
        bindCard(R.id.card_firefox, Browser.FIREFOX)
        bindCard(R.id.card_yandex, Browser.YANDEX)
        bindCard(R.id.card_cromite, Browser.CROMITE)
        bindCard(R.id.card_iceraven, Browser.ICERAVEN)
    }

    private fun bindCard(rootId: Int, browser: Browser) {
        val card: View = findViewById(rootId)
        card.findViewById<ImageView>(R.id.logo).setImageResource(browser.logoRes)
        card.findViewById<TextView>(R.id.name).text = browser.displayName
        card.findViewById<TextView>(R.id.desc).text = browser.shortDesc
        card.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java)
                    .putExtra(MainActivity.EXTRA_BROWSER_ID, browser.id)
            )
        }
    }
}
