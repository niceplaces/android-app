package com.niceplaces.niceplaces.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
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
            int newHeight = (int) dipToPixels(context, (float) 100);
            int newWidth = bitmap.getWidth() * newHeight / bitmap.getHeight();
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
            // height : width = 100 : x
            imageView.setImageBitmap(bitmap);
        } catch (IOException e){
            imageView.setImageResource(R.drawable.marker_outline);
        }
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
