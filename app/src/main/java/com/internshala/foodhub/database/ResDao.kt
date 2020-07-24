package com.internshala.foodhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResDao {
    @Insert
    fun insert(entity: ResEntity)

    @Delete
    fun delete(entity: ResEntity)

    @Query("Select * from ResList")
    fun getList():List<ResEntity>

    @Query("Select * from ResList where ResId=:ResId")
    fun getResList(ResId:String):ResEntity

}