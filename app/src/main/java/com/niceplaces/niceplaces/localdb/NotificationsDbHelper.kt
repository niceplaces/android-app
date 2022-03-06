package com.niceplaces.niceplaces.localdb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.niceplaces.niceplaces.models.Notification
import com.niceplaces.niceplaces.utils.StringUtils

class NotificationsDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val mContext = context

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getAll(db: SQLiteDatabase): MutableList<Notification> {
       val projection = arrayOf(NotificationsContract.NotificationsEntry.COLUMN_NAME_TIMESTAMP,
               NotificationsContract.NotificationsEntry.COLUMN_NAME_TITLE,
               NotificationsContract.NotificationsEntry.COLUMN_NAME_MESSAGE,
               NotificationsContract.NotificationsEntry.COLUMN_NAME_IMAGE,
               NotificationsContract.NotificationsEntry.COLUMN_NAME_LINK)
        val cursor = db.query(
                NotificationsContract.NotificationsEntry.TABLE_NAME,   // The table to query
                projection,                 // The array of columns to return (pass null to get all)
                null,               // The columns for the WHERE clause
                null,            // The values for the WHERE clause
                null,               // don't group the rows
                null,                // don't filter by row groups
                "${NotificationsContract.NotificationsEntry.COLUMN_NAME_TIMESTAMP} DESC")
        val notifications = mutableListOf<Notification>()
        with(cursor) {
            while (moveToNext()) {
                val date = StringUtils.timestampToDate(getLong(
                        getColumnIndexOrThrow(NotificationsContract.NotificationsEntry.COLUMN_NAME_TIMESTAMP)))
                val notification = date?.let {
                    Notification(
                            it,
                            getString(getColumnIndexOrThrow(NotificationsContract.NotificationsEntry.COLUMN_NAME_TITLE)),
                            getString(getColumnIndexOrThrow(NotificationsContract.NotificationsEntry.COLUMN_NAME_MESSAGE)),
                            getString(getColumnIndexOrThrow(NotificationsContract.NotificationsEntry.COLUMN_NAME_IMAGE)),
                            getString(getColumnIndexOrThrow(NotificationsContract.NotificationsEntry.COLUMN_NAME_LINK)))
                }
                if (notification != null) {
                    notifications.add(notification)
                }
            }
        }
        return notifications
    }

    fun insert(db: SQLiteDatabase, title: String?, message: String?, imageURL: String?, link: String?, timestamp: Long){
        val values = ContentValues().apply {
            put(NotificationsContract.NotificationsEntry.COLUMN_NAME_TITLE, title)
            put(NotificationsContract.NotificationsEntry.COLUMN_NAME_MESSAGE, message)
            put(NotificationsContract.NotificationsEntry.COLUMN_NAME_IMAGE, imageURL)
            put(NotificationsContract.NotificationsEntry.COLUMN_NAME_LINK, link)
            put(NotificationsContract.NotificationsEntry.COLUMN_NAME_TIMESTAMP, timestamp)
        }
        db.insert(NotificationsContract.NotificationsEntry.TABLE_NAME, null, values)
    }

    fun delete(){
        mContext.deleteDatabase(DATABASE_NAME)
    }

    companion object {

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Notifications.db"

        const val SQL_CREATE_TABLE = "CREATE TABLE ${NotificationsContract.NotificationsEntry.TABLE_NAME} ( " +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${NotificationsContract.NotificationsEntry.COLUMN_NAME_TITLE} TEXT, " +
                "${NotificationsContract.NotificationsEntry.COLUMN_NAME_MESSAGE} TEXT, " +
                "${NotificationsContract.NotificationsEntry.COLUMN_NAME_IMAGE} TEXT, " +
                "${NotificationsContract.NotificationsEntry.COLUMN_NAME_LINK} TEXT, " +
                "${NotificationsContract.NotificationsEntry.COLUMN_NAME_TIMESTAMP} LONG " +
                ");"

        const val SQL_DELETE_TABLE = "DROP TABLE ${NotificationsContract.NotificationsEntry.TABLE_NAME};"

        const val SQL_GET_ALL = "SELECT * FROM ${NotificationsContract.NotificationsEntry.TABLE_NAME};"
    }

}