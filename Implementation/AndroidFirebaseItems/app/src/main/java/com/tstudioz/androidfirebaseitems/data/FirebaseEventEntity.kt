package com.tstudioz.androidfirebaseitems.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirebaseEventEntity(val name: String?, val userUID: String?, val payload: Map<String, Any>?) {
    constructor() : this(null, null, null)
}