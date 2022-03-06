package com.niceplaces.niceplaces.localdb

import android.provider.BaseColumns

object NotificationsContract {

    object NotificationsEntry : BaseColumns {
        const val TABLE_NAME = "notifications"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_MESSAGE = "message"
        const val COLUMN_NAME_IMAGE = "image"
        const val COLUMN_NAME_LINK = "link"
        const val COLUMN_NAME_TIMESTAMP = "timestamp"
    }

}