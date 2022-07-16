package com.niceplaces.niceplaces.models

/**
 * Created by Lorenzo on 29/12/2017.
 */
class Place {
    var iD: String
        private set
    var mName: String
    var mArea: String? = null
    var mRegion: String? = null
    var mDescription: String? = null
    @JvmField
    var mAuthor: String? = null
    var mImage: String
    var mCredits: String? = null
    var mWikiUrl: String? = null
    var mSources: String? = null
    var mFacebook: String? = null
    var mInstagram: String? = null
    @JvmField
    var mHasDescription = false
    var mLatitude = 0.0
    var mLongitude = 0.0
    @JvmField
    var mDistance = 0.0
    var mClusterItem: MyClusterItem? = null
    var events: List<Event>? = null
    var links: List<Link>? = null
    var isInfoWindowShown = false

    constructor(id: String, name: String, image: String) {
        iD = id
        mName = name
        mImage = image
    }

    constructor(id: String, name: String, image: String, hasDesc: Boolean, author: String?, wikiUrl: String?) {
        iD = id
        mName = name
        mImage = image
        mHasDescription = hasDesc
        mAuthor = author
        mWikiUrl = wikiUrl
    }

    constructor(id: String, name: String, desc: String?, sources: String?, latitude: Double, longitude: Double, image: String, credits: String?, wikiUrl: String?) {
        iD = id
        mName = name
        mDescription = desc
        mSources = sources
        mLatitude = latitude
        mLongitude = longitude
        mImage = image
        events = ArrayList()
        mCredits = credits
        mWikiUrl = wikiUrl
        isInfoWindowShown = false
    }

    constructor(id: String, name: String, area: String?, region: String?, desc: String?, author: String?, sources: String?, latitude: Double, longitude: Double,
                image: String, credits: String?, wikiUrl: String?, facebook: String?, instagram: String?) {
        iD = id
        mName = name
        mArea = area
        mRegion = region
        mDescription = desc
        mAuthor = author
        mSources = sources
        mLatitude = latitude
        mLongitude = longitude
        mImage = image
        events = ArrayList()
        mCredits = credits
        mWikiUrl = wikiUrl
        mFacebook = facebook
        mInstagram = instagram
    }

    constructor(id: String, name: String, area: String?, desc: String?, sources: String?, latitude: Double, longitude: Double, image: String, credits: String?, wikiUrl: String?) {
        iD = id
        mName = name
        mArea = area
        mDescription = desc
        mSources = sources
        mLatitude = latitude
        mLongitude = longitude
        mImage = image
        events = ArrayList()
        mCredits = credits
        mWikiUrl = wikiUrl
        isInfoWindowShown = false
    }

    constructor(id: String, name: String, latitude: Double, longitude: Double, image: String, hasDescription: Boolean, author: String?) {
        iD = id
        mName = name
        mLatitude = latitude
        mLongitude = longitude
        mImage = image
        events = ArrayList()
        mHasDescription = hasDescription
        mAuthor = author
    }

    constructor(id: String, name: String, area: String?, region: String?, image: String,
                hasDescription: Boolean, author: String?, wikiUrl: String?) {
        iD = id
        mName = name
        mArea = area
        mRegion = region
        mImage = image
        events = ArrayList()
        mHasDescription = hasDescription
        mAuthor = author
        mWikiUrl = wikiUrl
    }

    companion object {
        @JvmStatic
        fun formatDistance(distance: Double): String {
            var distance = distance
            var unit = "m"
            return if (distance > 1000) {
                distance = distance / 1000
                unit = "km"
                String.format("%.02f", distance) + " " + unit
            } else {
                Math.round(distance).toString() + " " + unit
            }
        }
    }
}