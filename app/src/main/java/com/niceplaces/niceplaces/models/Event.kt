package com.niceplaces.niceplaces.models

/**
 * Created by Lorenzo on 05/01/2018.
 */
class Event {
    var iD: String? = null
        private set
    var placeID: String? = null
        private set
    var date: String
        private set
    var description: String
        private set

    constructor(id: String?, placeID: String?, date: String, description: String) {
        iD = id
        this.placeID = placeID
        this.date = date
        this.description = description
    }

    constructor(date: String, description: String) {
        this.date = date
        this.description = description
    }

}