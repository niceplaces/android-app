package com.niceplaces.niceplaces.controllers;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.niceplaces.niceplaces.R;

public class AlertController {

    private FrameLayout mParent, mFrameLoading, mFrameError;

    public AlertController(Activity activity, int layoutId){
        mParent = activity.findViewById(layoutId);
        mFrameLoading = mParent.findViewById(R.id.frame_loading);
        mFrameError = mParent.findViewById(R.id.frame_loading_error);
        mParent.setVisibility(View.VISIBLE);
        mFrameLoading.setVisibility(View.VISIBLE);
        mFrameError.setVisibility(View.GONE);
    }

    public void loadingSuccess(){
        mParent.setVisibility(View.GONE);
    }

    public void loadingError(){
        mFrameLoading.setVisibility(View.GONE);
        mFrameError.setVisibility(View.VISIBLE);
    }

}
