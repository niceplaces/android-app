package com.niceplaces.niceplaces.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.adapters.ViewPagerAdapter
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.utils.AppUtils

class IntroActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager
    private lateinit var mDots: Array<TextView?>
    private var mLayoutDots: LinearLayout? = null
    private lateinit var mButtonNext: Button
    private lateinit var mButtonSkip: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val thisActivity = this
        setContentView(R.layout.activity_intro)
        supportActionBar!!.hide()
        val prefs = PrefsController(this)
        var layouts = intArrayOf(
                R.layout.activity_intro1,
                R.layout.activity_intro2,
                R.layout.activity_intro3,
                R.layout.activity_intro4,
                R.layout.activity_intro5,
                R.layout.activity_intro6,
                R.layout.activity_intro7,
//                R.layout.activity_intro_update,
                R.layout.activity_intro8)
        if (!prefs.isFistOpenAfterInstall && prefs.isFistOpenAfterUpdate){
            layouts = intArrayOf(R.layout.activity_intro_update)
        }
        prefs.firstOpenDone()
        mLayoutDots = findViewById(R.id.layout_dots)
        mViewPager = findViewById(R.id.view_pager)
        mButtonNext = findViewById(R.id.button_next)
        mButtonSkip = findViewById(R.id.button_skip)
        val adapter = ViewPagerAdapter(this, layouts)
        mViewPager.adapter = adapter
        addBottomDots(0, layouts.size)
        if (layouts.size == 1){
            mButtonNext.setText(R.string.go)
            mButtonSkip.visibility = View.GONE
        }
        mViewPager.addOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                addBottomDots(position, layouts.size)
                if (position == layouts.size - 1) {
                    mButtonNext.setText(R.string.go)
                    mButtonSkip.visibility = View.GONE
                } else {
                    mButtonNext.setText(R.string.next)
                    mButtonSkip.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        mButtonSkip.setOnClickListener {
            if (prefs.isPrivacyAccepted) {
                val i = Intent(thisActivity, MenuActivity::class.java)
                startActivity(i)
            } else {
                val i = Intent(thisActivity, PrivacyActivity::class.java)
                startActivity(i)
            }
            thisActivity.finish()
        }
        mButtonNext.setOnClickListener {
            val next = mViewPager.currentItem + 1
            if (next < layouts.size) {
                mViewPager.currentItem = next
            } else if (prefs.isPrivacyAccepted) {
                val i = Intent(thisActivity, MenuActivity::class.java)
                startActivity(i)
                thisActivity.finish()
            } else {
                val i = Intent(thisActivity, PrivacyActivity::class.java)
                startActivity(i)
                thisActivity.finish()
            }
        }
    }

    fun addBottomDots(position: Int, number: Int) {
        mDots = arrayOfNulls(number)
        mLayoutDots!!.removeAllViews()
        for (i in mDots.indices) {
            mDots[i] = TextView(this)
            mDots[i]!!.text = AppUtils.fromHtml("&#8226;")
            mDots[i]!!.textSize = 35f
            if (i == position) {
                mDots[i]!!.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            } else {
                mDots[i]!!.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryAlpha2))
            }
            mLayoutDots!!.addView(mDots[i])
        }
    }
}