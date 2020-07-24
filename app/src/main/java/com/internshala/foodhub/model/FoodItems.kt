package com.internshala.foodhub.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodItems(
    val foodid:String,
    val name:String,
    val cost:String
):Parcelable