package com.internshala.foodhub.local_databases

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.internshala.foodhub.database.CartDatabase
import com.internshala.foodhub.database.CartEntity

class DBAsyncTask(val context: Context, val foodentity: CartEntity, val mode: Int):AsyncTask<Void,Void,Boolean>(){


    val db= Room.databaseBuilder(context, CartDatabase::class.java,"carts-db").build()

    override fun doInBackground(vararg p0: Void?): Boolean {
        when(mode)
        {
            1->
            {
                val food:CartEntity?=db.cartDao().getFood(foodentity.Food_Id.toString())
                db.close()
                return food!=null
            }
            2->
            {
                db.cartDao().insert(foodentity)
                db.close()
                return true
            }
            3->
            {
                db.cartDao().delete(foodentity)
                db.close()
                return true
            }
            4->{
                db.cartDao().deletepreviousitems()
                db.close()
                return true
            }

        }
        return false

    }

}
