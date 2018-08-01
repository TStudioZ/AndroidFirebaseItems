package com.tstudioz.androidfirebaseitems.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirebaseUserEntity(val role: String?) {
    constructor() : this(null) {}
}