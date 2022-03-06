package com.niceplaces.niceplaces.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.niceplaces.niceplaces.Const;
import com.niceplaces.niceplaces.R;
import com.niceplaces.niceplaces.controllers.PrefsController;
import com.niceplaces.niceplaces.models.Place;

/**
 * Created by Lorenzo on 01/01/2018.
 */

public class ImageUtils {

    /*public static void setImageView(Context context, String imageName, ImageView imageView){
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
    }*/

    public static void setImageViewWithGlide(Context context, String imageName, ImageView imageView){
        PrefsController prefs = new PrefsController(context);
        Uri uri = Uri.parse(Const.BASE_URL + "data/photos/" + prefs.getDatabaseMode() + "/" + imageName);
        RequestOptions myOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, imageView.getHeight());
        Glide.with(context)
                .load(uri)
                .apply(myOptions)
                .into(imageView);
    }

    public static void setImageViewWithGlide(Activity activity, Context context, final Marker marker, String imageName, final ImageView imageView){
        PrefsController prefs = new PrefsController(context);
        Uri uri = Uri.parse(Const.BASE_URL + "data/photos/" + prefs.getDatabaseMode() + "/" + imageName);
        RequestOptions myOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, imageView.getHeight());
        Glide.with(activity.getApplicationContext())
                .asBitmap().load(uri).apply(myOptions)
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                  imageView.setImageBitmap(resource);
                                  if (marker.isInfoWindowShown()) {
                                      marker.hideInfoWindow();
                                      marker.showInfoWindow();
                                  }
                                  return false;
                              }
                          }
                ).submit();
    }

    public static void setImageViewWithGlide(Context context, int imageId, ImageView imageView){
        RequestOptions myOptions = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, imageView.getHeight());
        Glide.with(context)
                .load(imageId)
                .apply(myOptions)
                .into(imageView);
    }

    private static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    /*private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
    }*/

    public static BitmapDescriptor bitmapDescriptorFromDrawable(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    public static void setAuthorIcon(Place place, ImageView imageView){
        if (place.mHasDescription){
            imageView.setVisibility(View.VISIBLE);
            switch (place.mAuthor){
                case "1":
                    imageView.setImageResource(R.drawable.app_icon);
                    break;
                case "2":
                    imageView.setImageResource(R.drawable.pro_loco);
                    break;
                case "3":
                    imageView.setImageResource(R.drawable.via_sacra_etrusca);
                    break;
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
}
