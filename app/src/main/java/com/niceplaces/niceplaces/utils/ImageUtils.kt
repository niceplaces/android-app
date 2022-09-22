package com.niceplaces.niceplaces.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.niceplaces.niceplaces.Const
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.controllers.PrefsController
import com.niceplaces.niceplaces.models.Place


/**
 * Created by Lorenzo on 01/01/2018.
 */
object ImageUtils {
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

    fun setImageViewWithGlideFullURL(context: Context?, imageURL: String, imageView: ImageView) {
        val prefs = PrefsController(context!!)
        val uri = Uri.parse(imageURL)
        val myOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, imageView.height)
        Glide.with(context)
                .load(uri)
                .apply(myOptions)
                .into(imageView)
    }

    fun setImageViewWithGlide(context: Context?, imageName: String, imageView: ImageView) {
        val prefs = PrefsController(context!!)
        val uri = Uri.parse(Const.BASE_URL + "data/photos/" + prefs.databaseMode + "/" + imageName)
        val myOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .override(Target.SIZE_ORIGINAL, imageView.height)
        Glide.with(context)
            .load(uri)
            .apply(myOptions)
            .into(imageView)
    }

    fun setImageViewWithGlide(context: Context?, imageName: String, imageView: ImageView,
                              width: Int, height: Int) {
        val prefs = PrefsController(context!!)
        // https://www.niceplaces.it/data/image.php?mode=debug&file=torri.jpg&w=200&h=200
        val uri = Uri.parse(Const.BASE_URL + "data/image.php?mode=" + prefs.databaseMode +
                "&file=" + imageName + "&w=" + width + "&h=" + height).toString()
        setImageViewFromURL(context, uri, imageView)
    }

    fun setImageViewFromURL(context: Context?, url: String, imageView: ImageView) {
        val uri = Uri.parse(url)
        val myOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .override(Target.SIZE_ORIGINAL, imageView.height)
        Glide.with(context!!)
            .load(uri)
            .apply(myOptions)
            .into(imageView)
    }

    fun setImageViewWithGlide(activity: Activity, context: Context?, marker: Marker, imageName: String, imageView: ImageView) {
        val prefs = PrefsController(context!!)
        val uri = Uri.parse(Const.BASE_URL + "data/photos/" + prefs.databaseMode + "/" + imageName)
        val myOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, imageView.height)
        Glide.with(activity.applicationContext)
                .asBitmap().load(uri).apply(myOptions)
                .listener(object : RequestListener<Bitmap?> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap?>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap?, model: Any, target: Target<Bitmap?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        imageView.setImageBitmap(resource)
                        if (marker.isInfoWindowShown) {
                            marker.hideInfoWindow()
                            marker.showInfoWindow()
                        }
                        return false
                    }
                }
                ).submit()
    }

    fun setImageViewWithGlide(context: Context?, imageId: Int, imageView: ImageView) {
        val myOptions = RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .override(Target.SIZE_ORIGINAL, imageView.height)
        Glide.with(context!!)
                .load(imageId)
                .apply(myOptions)
                .into(imageView)
    }

    fun dipToPixels(context: Context, dipValue: Int): Int {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dipValue.toFloat(), metrics
        ).toInt()
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

    fun bitmapDescriptorFromDrawable(context: Context?, vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context!!, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    fun getBitmapFromURL(context: Context, src: String?): Bitmap? {
        return Glide.with(context).asBitmap().load(src).into(-1, -1).get()
    }

    fun setAuthorIcon(place: Place, imageView: ImageView) {
        if (place.mHasDescription) {
            imageView.visibility = View.VISIBLE
            when (place.mAuthor) {
                "1" -> imageView.setImageResource(R.drawable.app_icon)
                "2" -> imageView.setImageResource(R.drawable.pro_loco)
                "3" -> imageView.setImageResource(R.drawable.via_sacra_etrusca)
                "4" -> imageView.setImageResource(R.drawable.proloco_murlo)
            }
        } else {
            imageView.visibility = View.GONE
        }
    }
}