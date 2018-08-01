package com.tstudioz.androidfirebaseitems.domain.response

data class UpdateCountResponse<Model>(val origItem: Model, val consumed: Boolean) {
}