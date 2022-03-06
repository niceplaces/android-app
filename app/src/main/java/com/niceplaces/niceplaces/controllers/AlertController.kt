package com.niceplaces.niceplaces.controllers

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import com.niceplaces.niceplaces.R

class AlertController(activity: Activity, layoutId: Int) {
    private val mParent: FrameLayout
    private val mFrameLoading: FrameLayout
    private val mFrameError: FrameLayout
    private val mFrameNoData: FrameLayout
    fun loadingSuccess() {
        mParent.visibility = View.GONE
    }

    fun loadingError() {
        mFrameLoading.visibility = View.GONE
        mFrameError.visibility = View.VISIBLE
    }

    fun loadingNoData() {
        mFrameLoading.visibility = View.GONE
        mFrameNoData.visibility = View.VISIBLE
    }

    init {
        mParent = activity.findViewById(layoutId)
        mFrameLoading = mParent.findViewById(R.id.frame_loading)
        mFrameError = mParent.findViewById(R.id.frame_loading_error)
        mFrameNoData = mParent.findViewById(R.id.frame_nodata)
        mParent.visibility = View.VISIBLE
        mFrameLoading.visibility = View.VISIBLE
        mFrameError.visibility = View.GONE
        mFrameNoData.visibility = View.GONE
    }
}