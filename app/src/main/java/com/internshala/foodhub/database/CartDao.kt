package com.internshala.foodhub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
     fun insert(entity:CartEntity)

    @Delete
    fun delete(entity: CartEntity)

    @Query("Select * from CartList")
    fun getlist():List<CartEntity>

    @Query("Select * from CartList where food_id=:foodId")
    fun getFood(foodId:String):CartEntity

    @Query("Delete from CartList")
    fun deletepreviousitems()

}
