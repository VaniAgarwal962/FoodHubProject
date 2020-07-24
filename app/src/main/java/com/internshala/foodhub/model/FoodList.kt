package com.internshala.foodhub.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodList (
    val Food_Id:String,
    val Food_Name:String,
    val Food_Price:String,
    val Res_Id:String
):Parcelable
