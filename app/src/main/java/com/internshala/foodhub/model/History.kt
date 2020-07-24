package com.internshala.foodhub.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class History(
    val order_id:String,
    val  restaurant_name:String,
    val total_cost:String,
    val order_placed_at:String,
    val food_items:List<FoodItems>
):Parcelable