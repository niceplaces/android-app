package com.niceplaces.niceplaces.models

class Region {
    var iD: String
        private set
    var name: String
        private set
    var count: String? = null
        private set

    constructor(id: String, name: String) {
        iD = id
        this.name = name
    }

    constructor(id: String, name: String, count: String?) {
        iD = id
        this.name = name
        this.count = count
    }

}