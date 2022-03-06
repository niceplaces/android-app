package com.niceplaces.niceplaces.controllers

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.niceplaces.niceplaces.models.Notification.Companion.LINK
import com.niceplaces.niceplaces.utils.AppUtils

class FirebaseMsgHandler : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val context = this
        AppUtils.notify(context, remoteMessage.notification!!.title, remoteMessage.notification!!.body,
                remoteMessage.notification!!.imageUrl.toString(), remoteMessage.data[LINK])
    }

}