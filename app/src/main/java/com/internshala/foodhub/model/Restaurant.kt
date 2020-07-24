package com.internshala.foodhub.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Restaurant (
    val ResId:String,
    val ResName:String,
    val ResPrice:String,
    val ResRating:String,
    val ResImage:String
):Parcelable

