package com.tstudioz.androidfirebaseitems.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DataItemEntity(var name: String?, var count: Int?) {
    constructor() : this(null, null) {}
}