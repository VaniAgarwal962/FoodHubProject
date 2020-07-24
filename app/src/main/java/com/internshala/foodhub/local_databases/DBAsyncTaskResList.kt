package com.internshala.foodhub.local_databases

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import com.internshala.foodhub.database.CartDatabase
import com.internshala.foodhub.database.ResDatabase
import com.internshala.foodhub.database.ResEntity

class DBAsyncTaskResList(val context: Context, val favlistentity:ResEntity, val mode:Int):AsyncTask<Void,Void,Boolean>() {

    val db= Room.databaseBuilder(context, ResDatabase::class.java,"res-db").build()


    override fun doInBackground(vararg p0: Void?): Boolean {
        when(mode)
        {
            1->
            {
                val reslist:ResEntity?=db.restDao().getResList(favlistentity.ResId)
                db.close()
                return reslist!=null
            }
            2->
            {
                db.restDao().insert(favlistentity)
                db.close()
                return true
            }
            3->
            {
                db.restDao().delete(favlistentity)
                db.close()
                return true
            }
            4->
            {
                db.restDao().getList()
                db.close()
                return true
            }
        }
        return false
    }



}