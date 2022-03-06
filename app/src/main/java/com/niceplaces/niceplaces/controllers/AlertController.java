package com.niceplaces.niceplaces.controllers;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.niceplaces.niceplaces.R;

public class AlertController {

    private FrameLayout mParent, mFrameLoading, mFrameError, mFrameNoData;

    public AlertController(Activity activity, int layoutId){
        mParent = activity.findViewById(layoutId);
        mFrameLoading = mParent.findViewById(R.id.frame_loading);
        mFrameError = mParent.findViewById(R.id.frame_loading_error);
        mFrameNoData = mParent.findViewById(R.id.frame_nodata);
        mParent.setVisibility(View.VISIBLE);
        mFrameLoading.setVisibility(View.VISIBLE);
        mFrameError.setVisibility(View.GONE);
        mFrameNoData.setVisibility(View.GONE);
    }

    public void loadingSuccess(){
        mParent.setVisibility(View.GONE);
    }

    public void loadingError(){
        mFrameLoading.setVisibility(View.GONE);
        mFrameError.setVisibility(View.VISIBLE);
    }

    public void loadingNoData(){
        mFrameLoading.setVisibility(View.GONE);
        mFrameNoData.setVisibility(View.VISIBLE);
    }

}
