package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.localdb.NotificationsDbHelper
import com.niceplaces.niceplaces.models.Notification
import com.niceplaces.niceplaces.utils.AppUtils
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val notification = intent.extras
            if (notification != null) {
                val dbHelper = NotificationsDbHelper(this)
                dbHelper.insert(dbHelper.writableDatabase,
                        notification.getString(Notification.TITLE),
                        notification.getString(Notification.MESSAGE),
                        notification.getString(Notification.IMAGE_URL),
                        notification.getString(Notification.LINK),
                        System.currentTimeMillis())
            }
            val link = notification?.getString(Notification.LINK)
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(i)
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
            setContentView(R.layout.activity_splash)
            supportActionBar!!.hide()
            val splashIcon = findViewById<ImageView>(R.id.splash_icon)
            val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein)
            splashIcon.startAnimation(fadeInAnimation)
            close()
        }
    }

    private fun close() {
        val thisActivity = this
        val prefsController = PrefsController(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (prefsController.isFistOpenAfterInstall || prefsController.isFistOpenAfterUpdate) {
                if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                    AppUtils.notify(this,
                            "Ecco la versione 3.3 di Nice Places!",
                            "Apri per leggere tutte le novità.",
                            "https://i1.wp.com/www.niceplaces.it/blog/wp-content/uploads/2020/07/v3.3_cover.jpg",
                            "https://www.niceplaces.it/blog/2020/07/le-novita-della-versione-3-3-per-android/")
                } else {
                    AppUtils.notify(this,
                            "Here is the version 3.3 of Nice Places!",
                            "Open to read what's new.",
                            "https://i1.wp.com/www.niceplaces.it/blog/wp-content/uploads/2020/07/v3.3_cover.jpg",
                            "https://www.niceplaces.it/blog/en/2020/07/what's-new-in-version-3-3-for-android/")
                }
                val intent = Intent(thisActivity, IntroActivity::class.java)
                startActivity(intent)
            } else {
                val prefs = PrefsController(thisActivity)
                if (prefs.isPrivacyAccepted) {
                    val i = Intent(thisActivity, MenuActivity::class.java)
                    startActivity(i)
                } else {
                    val i = Intent(thisActivity, PrivacyActivity::class.java)
                    startActivity(i)
                }
            }
            thisActivity.finish()
        }, 3000)
    }
}