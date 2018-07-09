package com.tstudioz.androidfirebaseitems.data

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirebaseUserEntity(val role: String?) {
    constructor() : this(null) {}
}