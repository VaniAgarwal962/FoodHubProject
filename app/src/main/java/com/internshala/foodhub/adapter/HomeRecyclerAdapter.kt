package com.internshala.foodhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodhub.R
import com.internshala.foodhub.local_databases.DBAsyncTask
import com.internshala.foodhub.activity.FoodListActivity
import com.internshala.foodhub.database.ResEntity
import com.internshala.foodhub.local_databases.DBAsyncTaskResList
import com.internshala.foodhub.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context,val ResList:ArrayList<Restaurant>):RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_home_single_row,parent,false)
        return HomeViewHolder(view)
    }
    override fun getItemCount(): Int {
        return ResList.size
    }
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val Res=ResList[position]
        holder.ResName.text=Res.ResName
        holder.ResPrice.text=Res.ResPrice
        holder.ResRating.text=Res.ResRating
        Picasso.get().load(Res.ResImage).error(R.drawable.foodhub_icon).into(holder.imgRes)


        val favlistentity= ResEntity(
            Res.ResId,
            Res.ResName,
            Res.ResPrice,
            Res.ResRating,
            Res.ResImage
        )

        holder.llRes.setOnClickListener {
            val intent=Intent(context, FoodListActivity::class.java)
            intent.putExtra("id",Res.ResId)
            intent.putExtra("name",Res.ResName)

            intent.putExtra("Resobj",favlistentity)

            context.startActivity(intent)
        }


        val checkAdded= DBAsyncTaskResList(context, favlistentity, 1).execute().get()

        if(checkAdded)
        {
                holder.fill.visibility=View.VISIBLE

        }
        else
        {
                holder.fill.visibility=View.GONE


        }
        holder.emptyimage.setOnClickListener{
            if(!DBAsyncTaskResList(context, favlistentity, 1).execute().get())
            {
                val async= DBAsyncTaskResList(
                    context,
                    favlistentity,
                    2
                ).execute()
                val result=async.get()
                if(result)
                {
                        holder.fill.visibility=View.VISIBLE
                }
                else
                {
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                val async= DBAsyncTaskResList(
                    context,
                    favlistentity,
                    3
                ).execute()
                val result=async.get()
                if(result)
                {
                    Toast.makeText(context,"Removed from favourites",Toast.LENGTH_SHORT).show()
                     holder.fill.visibility=View.GONE

                }
                else
                {
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val llRes:LinearLayout=view.findViewById(R.id.llContent)
        val imgRes:ImageView=view.findViewById(R.id.imgFoodImage)
        val ResName:TextView=view.findViewById(R.id.txtResName)
        val ResPrice:TextView=view.findViewById(R.id.txtResPrice)
        val ResRating:TextView=view.findViewById(R.id.txtResRating)
        val emptyimage:ImageView=view.findViewById(R.id.imgAdd_To_Favourite)
        val fill:ImageView=view.findViewById(R.id.imgAdd_To_Fill_Favourite)

    }
}