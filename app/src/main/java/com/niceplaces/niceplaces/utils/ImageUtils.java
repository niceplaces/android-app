package com.niceplaces.niceplaces.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.models.Place;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Lorenzo on 01/01/2018.
 */

public class ImageUtils {

    public static void setPlacesImageView(Context context, Place place, ImageView imageView){
        setPlacesImageView(context, place.mImage, imageView);
    }

    public static void setPlacesImageView(Context context, String imageName, ImageView imageView){
        try {
            InputStream ims = context.getAssets().open(imageName);
            Bitmap bitmap = BitmapFactory.decodeStream(ims);
            //bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e){
            imageView.setImageResource(R.drawable.marker_outline);
        }
    }

    public static BitmapDescriptor bitmapDescriptorFromDrawable(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
