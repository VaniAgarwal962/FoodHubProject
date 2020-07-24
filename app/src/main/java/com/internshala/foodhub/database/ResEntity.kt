package com.internshala.foodhub.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName="ResList")
data class ResEntity (
    @PrimaryKey val ResId:String,
    @ColumnInfo(name="res_name") val ResName:String,
    @ColumnInfo(name="res_price") val ResPrice:String,
    @ColumnInfo(name="res_rating") val ResRating:String,
    @ColumnInfo(name="res_image") val ResImage:String
):Parcelable