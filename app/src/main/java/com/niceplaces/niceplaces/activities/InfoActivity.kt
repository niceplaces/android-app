package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.niceplaces.niceplaces.R
import java.util.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        supportActionBar!!.hide()
        val thisActivity = this
        val textViewVersion = findViewById<TextView>(R.id.textview_version_number)
        try {
            textViewVersion.text = "v${packageManager.getPackageInfo(packageName, 0).versionName}"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val iVInstagram = findViewById<ImageView>(R.id.imageview_instagram)
        val iVFacebook = findViewById<ImageView>(R.id.imageview_facebook)
        val iVTwitter = findViewById<ImageView>(R.id.imageview_twitter)
        val textViewWebsite = findViewById<TextView>(R.id.textview_website)
        textViewWebsite.setOnClickListener {
            var url = "http://www.niceplaces.it/en/"
            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                url = "http://www.niceplaces.it/"
            }
            val i = Intent(Intent.ACTION_VIEW,
                    Uri.parse(url))
            startActivity(i)
        }
        iVInstagram.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.instagram.com/niceplacesapp/"))
            startActivity(i)
        }
        iVFacebook.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/niceplacesapp/"))
            startActivity(i)
        }
        iVTwitter.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/niceplacesapp"))
            startActivity(i)
        }
        val buttonTutorial = findViewById<Button>(R.id.button_tutorial)
        buttonTutorial.setOnClickListener {
            val intent = Intent(thisActivity, IntroActivity::class.java)
            startActivity(intent)
        }
        val buttonEmail = findViewById<Button>(R.id.button_email)
        buttonEmail.setOnClickListener {
            val i = Intent(Intent.ACTION_SENDTO,
                    Uri.parse("mailto:niceplacesit@gmail.com"))
            startActivity(i)
        }
    }
}