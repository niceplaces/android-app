package com.niceplaces.niceplaces.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.utils.ImageUtils

class ViewPagerAdapter(context: Context, layouts: IntArray) : PagerAdapter() {
    private val mInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val mLayouts: IntArray = layouts
    private val mContext: Context = context
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mInflater.inflate(mLayouts[position], container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageview_screenshoot)
        when (position) {
            2 -> ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen2, imageView)
            3 -> ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen3, imageView)
            4 -> ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen4, imageView)
            5 -> ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen4, imageView)
            6 -> ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen5, imageView)
            else -> {
            }
        }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int {
        return mLayouts.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        var view: View? = `object` as View
        container.removeView(view)
        view = null
    }

}