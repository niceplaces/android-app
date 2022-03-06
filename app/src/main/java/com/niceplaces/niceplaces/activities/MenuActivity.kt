package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.BuildConfig
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.dialogs.RateDialog
import com.niceplaces.niceplaces.utils.AppRater
import java.util.*

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        supportActionBar!!.hide()
        val thisActivity = this
        FirebaseAnalytics.getInstance(this)
        val placesNearYou = findViewById<Button>(R.id.btn_places_near_you)
        val explore = findViewById<Button>(R.id.btn_virtual_tour)
        val newPlaces = findViewById<Button>(R.id.btn_new_places)
        val quiz = findViewById<Button>(R.id.btn_quiz)
        //ImageView IVlogin = findViewById(R.id.imageview_login);
        val IVInfo = findViewById<ImageView>(R.id.imageview_info)
        val IVNotifications = findViewById<ImageView>(R.id.imageview_notifications)
        val IVWeb = findViewById<ImageView>(R.id.imageview_web)
        val IVInstagram = findViewById<ImageView>(R.id.imageview_instagram)
        val IVFacebook = findViewById<ImageView>(R.id.imageview_facebook)
        val IVTwitter = findViewById<ImageView>(R.id.imageview_twitter)
        val IVDebug = findViewById<ImageView>(R.id.imageview_debug)
        val buttonVisited = findViewById<Button>(R.id.button_visited)
        val buttonWished = findViewById<Button>(R.id.button_wished)
        val buttonFav = findViewById<Button>(R.id.button_fav)
        val textViewPrivacy = findViewById<TextView>(R.id.textview_privacy_policy)
        /*IVlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, LoginActivity.class);
                startActivity(intent);
            }
        });*/
        IVInfo.setOnClickListener {
            val intent = Intent(thisActivity, InfoActivity::class.java)
            startActivity(intent)
        }
        IVNotifications.setOnClickListener {
            val intent = Intent(thisActivity, NotificationsActivity::class.java)
            startActivity(intent)
        }
        IVWeb.setOnClickListener {
            var url = Const.WEBSITE_EN_URL
            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                url = Const.WEBSITE_URL
            }
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }
        IVInstagram.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(Const.INSTAGRAM))
            startActivity(i)
        }
        IVFacebook.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(Const.FACEBOOK))
            startActivity(i)
        }
        IVTwitter.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(Const.TWITTER))
            startActivity(i)
        }
        placesNearYou.setOnClickListener {
            val intent = Intent(thisActivity, MapsActivity::class.java)
            startActivity(intent)
        }
        explore.setOnClickListener {
            val intent = Intent(thisActivity, ExploreActivity::class.java)
            startActivity(intent)
        }
        newPlaces.setOnClickListener {
            val intent = Intent(thisActivity, LatestPlacesActivity::class.java)
            startActivity(intent)
        }
        newPlaces.setOnClickListener {
            val intent = Intent(thisActivity, QuizActivity::class.java)
            startActivity(intent)
        }
        buttonVisited.setOnClickListener {
            val intent = Intent(thisActivity, UserListActivity::class.java)
            intent.putExtra(UserListActivity.Companion.USERLIST, UserListActivity.Companion.VISITED)
            startActivity(intent)
        }
        buttonWished.setOnClickListener {
            val intent = Intent(thisActivity, UserListActivity::class.java)
            intent.putExtra(UserListActivity.Companion.USERLIST, UserListActivity.Companion.WISHED)
            startActivity(intent)
        }
        buttonFav.setOnClickListener {
            val intent = Intent(thisActivity, UserListActivity::class.java)
            intent.putExtra(UserListActivity.Companion.USERLIST, UserListActivity.Companion.FAVOURITE)
            startActivity(intent)
        }
        textViewPrivacy.setOnClickListener {
            var url = "http://www.niceplaces.it/en/privacy"
            if (Locale.getDefault().displayLanguage == Locale.ITALIAN.displayLanguage) {
                url = "http://www.niceplaces.it/privacy"
            }
            val i = Intent(Intent.ACTION_VIEW,
                    Uri.parse(url))
            startActivity(i)
        }
        if (BuildConfig.DEBUG) {
            IVDebug.setOnClickListener {
                val intent = Intent(thisActivity, DebugOptionsActivity::class.java)
                startActivity(intent)
            }
        } else {
            IVDebug.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (AppRater.needToTriggerRateDialog(this)){
            val dialog = RateDialog()
            dialog.show(supportFragmentManager, "rate_dialog")
        } else {
            super.onBackPressed()
        }
    }
}