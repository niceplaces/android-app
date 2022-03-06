package com.niceplaces.niceplaces.models

class Notification(pDate: String, pTitle: String?, pMessage: String?, pImage: String?, pLink: String?) {

    val date: String = pDate
    val title: String? = pTitle
    val message: String? = pMessage
    val image: String? = pImage
    val link: String? = pLink

    companion object {

        const val TITLE = "title"
        const val MESSAGE = "message"
        const val IMAGE_URL = "image_url"
        const val LINK = "link"

    }

}