package com.tstudioz.androidfirebaseitems.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DataItemEntity(var name: String?, var count: Int?) {
    constructor() : this(null, null) {}
}