package com.niceplaces.niceplaces.adapters;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.utils.ImageUtils;

public class ViewPagerAdapter extends PagerAdapter {

    private LayoutInflater mInflater;
    private int[] mLayouts;
    private Context mContext;

    public ViewPagerAdapter(Context context, int[] layouts){
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayouts = layouts;
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(mLayouts[position], container, false);
        ImageView imageView = view.findViewById(R.id.imageview_screenshoot);
        switch (position){
            case 2:
                ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen2, imageView);
                break;
            case 3:
                ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen3, imageView);
                break;
            case 4:
                ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen4, imageView);
                break;
            case 5:
                ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen4, imageView);
                break;
            case 6:
                ImageUtils.setImageViewWithGlide(mContext, R.drawable.screen5, imageView);
                break;
                default:
                    break;
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mLayouts.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        view = null;
    }
}
