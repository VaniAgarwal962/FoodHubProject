package com.internshala.foodhub.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
//CartEntity are used to store data in the form of tables hence we make it a "data class"
@Entity(tableName="CartList")
data class CartEntity(
    @PrimaryKey val Food_Id: Int,
    @ColumnInfo(name = "food_name") val Food_Name: String,
    @ColumnInfo(name = "food_price") val Food_Price: String,
    @ColumnInfo(name = "res_id") val Res_Id: String
):Parcelable