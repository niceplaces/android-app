package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R

class AreaIntroActivity : AppCompatActivity() {

    //private val mExtras: Bundle? = null
    private val mActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area_intro)
        FirebaseAnalytics.getInstance(this)
        val mExtras = intent.extras
        supportActionBar!!.hide()
        val textViewAreaName = findViewById<TextView>(R.id.explore_area_name)
        val textViewDescription = findViewById<TextView>(R.id.textview_intro)
        val button = findViewById<Button>(R.id.btn_find_places)
        if (mExtras != null){
            if (mExtras.getString(Const.AREA_ID) != null) {
                textViewAreaName.text = mExtras.getString("AREA_NAME")
                textViewDescription.text = mExtras.getString("AREA_DESC")
                button.setOnClickListener {
                    val intent = Intent(mActivity, PlacesListActivity::class.java)
                    intent.putExtra(Const.AREA_ID, mExtras.getString(Const.AREA_ID))
                    intent.putExtra("AREA_NAME", mExtras.getString("AREA_NAME"))
                    startActivity(intent)
                    finish()
                }
            } else {
                textViewAreaName.text = mExtras.getString("LIST_NAME")
                textViewDescription.text = mExtras.getString("LIST_DESC")
                button.setOnClickListener {
                    val intent = Intent(mActivity, PlacesListActivity::class.java)
                    intent.putExtra(Const.LIST_ID, mExtras.getString(Const.LIST_ID) as String)
                    intent.putExtra("LIST_NAME", mExtras.getString("LIST_NAME") as String)
                    startActivity(intent)
                    finish()
                }
                val badge: LinearLayout
                when (mExtras.getString(Const.LIST_ID)) {
                    "2" -> {
                        badge = findViewById(R.id.badge_proloco)
                        val listener = View.OnClickListener {
                            val i = Intent(Intent.ACTION_VIEW,
                                    Uri.parse(Const.PROLOCO_URL))
                            startActivity(i)
                        }
                        badge.setOnClickListener(listener)
                        badge.visibility = View.VISIBLE
                    }
                    "3" -> {
                        badge = findViewById(R.id.badge_viasacra)
                        val listener1 = View.OnClickListener {
                            val i = Intent(Intent.ACTION_VIEW,
                                    Uri.parse(Const.VIASACRA_URL))
                            startActivity(i)
                        }
                        badge.setOnClickListener(listener1)
                        badge.setVisibility(View.VISIBLE)
                    }
                }
            }
        }
    }
}