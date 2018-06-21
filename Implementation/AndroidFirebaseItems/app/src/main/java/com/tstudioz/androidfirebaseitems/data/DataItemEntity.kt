package com.tstudioz.androidfirebaseitems.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DataItemEntity(val name: String?) {
    constructor() : this(null) {}
}