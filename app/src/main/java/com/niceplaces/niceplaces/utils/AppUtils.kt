package com.niceplaces.niceplaces.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.niceplaces.niceplaces.R
import com.niceplaces.niceplaces.localdb.NotificationsDbHelper

object AppUtils {

    fun isFirstInstall(context: Context): Boolean {
        return try {
            val firstInstallTime = context.packageManager.getPackageInfo(context.packageName, 0).firstInstallTime
            val lastUpdateTime = context.packageManager.getPackageInfo(context.packageName, 0).lastUpdateTime
            firstInstallTime == lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    fun getVersionCode(context: Context): Int {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            -1
        }
    }

    fun getVersionName(context: Context): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "NaN"
        }
    }

    val tag: String
        get() {
            var tag = ""
            val ste = Thread.currentThread().stackTrace
            for (i in ste.indices) {
                if (ste[i].methodName == "getTag") {
                    tag = "(" + ste[i + 1].fileName + ":" + ste[i + 1].lineNumber + ")"
                }
            }
            return tag
        }

    val availableHeapSize: String
        get() {
            val runtime = Runtime.getRuntime()
            val usedMemInMB = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L
            val maxHeapSizeInMB = runtime.maxMemory() / 1048576L
            val availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB
            return "$availHeapSizeInMB MB"
        }

    fun fromHtml(source: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(source)
        }
    }
    
    fun notify(context: Context, title: String?, message: String?, imageURL: String?, link: String?){
        val dbHelper = NotificationsDbHelper(context)
        dbHelper.insert(dbHelper.writableDatabase, title, message, imageURL, link, System.currentTimeMillis())
        try {
            createNotificationChannel(context)
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(ContextCompat.getColor(context, R.color.icon_color))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true)
            if (imageURL == null){
                var pendingIntent: PendingIntent?
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(),
                            intent, PendingIntent.FLAG_UPDATE_CURRENT)
                } catch (e: Exception) {
                    pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(),
                            Intent(), PendingIntent.FLAG_UPDATE_CURRENT)
                    e.printStackTrace()
                }
                mBuilder.setContentIntent(pendingIntent)
                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
            } else {
                Glide.with(context)
                        .asBitmap()
                        .load(imageURL)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                mBuilder.setLargeIcon(resource)
                                        .setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                                var pendingIntent: PendingIntent?
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(),
                                            intent, PendingIntent.FLAG_UPDATE_CURRENT)
                                } catch (e: Exception) {
                                    pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(),
                                            Intent(), PendingIntent.FLAG_UPDATE_CURRENT)
                                    e.printStackTrace()
                                }
                                mBuilder.setContentIntent(pendingIntent)
                                val notificationManager = NotificationManagerCompat.from(context)
                                notificationManager.notify(System.currentTimeMillis().toInt(), mBuilder.build())
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                // this is called when imageView is cleared on lifecycle call or for
                                // some other reason.
                                // if you are referencing the bitmap somewhere else too other than this imageView
                                // clear it here as you can no longer have the bitmap
                            }
                        })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private const val CHANNEL_ID: String = "1"

    private fun createNotificationChannel(context: Context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_NAME = context.getString(R.string.news)
            //String description = getString(R.string.channel_description);
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}