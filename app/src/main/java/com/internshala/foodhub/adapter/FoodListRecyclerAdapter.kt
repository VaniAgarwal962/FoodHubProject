package com.internshala.foodhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodhub.R
import com.internshala.foodhub.local_databases.DBAsyncTask
import com.internshala.foodhub.database.CartEntity
import com.internshala.foodhub.model.FoodList

class FoodListRecyclerAdapter(val context: Context, val FoodList:ArrayList<FoodList>,val btnProceed:View):RecyclerView.Adapter<FoodListRecyclerAdapter.FoodListViewHolder>()
{

    val addlist= arrayListOf<FoodList>(

    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FoodListRecyclerAdapter.FoodListViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_food_list_single_row,parent,false)
        return FoodListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return FoodList.size
    }

    override fun onBindViewHolder(
        holder: FoodListRecyclerAdapter.FoodListViewHolder, position: Int) {
        val FoodLis = FoodList[position]
        holder.FoodId.text = FoodLis.Food_Id
        holder.FoodName.text = FoodLis.Food_Name
        holder.FoodPrice.text = FoodLis.Food_Price



        val foodentity= CartEntity(
            FoodLis.Food_Id.toInt(),
            FoodLis.Food_Name,
            FoodLis.Food_Price,
            FoodLis.Res_Id

        )


        val checkAdded= DBAsyncTask(context, foodentity, 1).execute()
        val isAdded=checkAdded.get()
        if(isAdded)
        {
            holder.btnAddtoCart.text="REMOVE"
            val addbuttoncolor = ContextCompat.getColor(context, R.color.colorAddToCart)
           holder.btnAddtoCart.setBackgroundColor(addbuttoncolor)
        }
        else
        {
            holder.btnAddtoCart.text="ADD"
            val removebuttoncolor = ContextCompat.getColor(context, R.color.colorAddButton)
           holder.btnAddtoCart.setBackgroundColor(removebuttoncolor)
        }
       holder.btnAddtoCart.setOnClickListener{
            if(!DBAsyncTask(context, foodentity, 1).execute().get())
            {
                val async= DBAsyncTask(
                    context,
                    foodentity,
                    2
                ).execute()
                val result=async.get()
                if(result)
                {

                    holder.btnAddtoCart.text="REMOVE"
                    val addbuttoncolor = ContextCompat.getColor(context, R.color.colorAddToCart)
                    holder.btnAddtoCart.setBackgroundColor(addbuttoncolor)

                    addlist.add(FoodLis)
                }
                else
                {
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }
            else
            {
                val async= DBAsyncTask(
                    context,
                    foodentity,
                    3
                ).execute()
                val result=async.get()
                if(result)
                {
                    Toast.makeText(context,"Removed from cart",Toast.LENGTH_SHORT).show()
                holder.btnAddtoCart.text="ADD"
                    val removebuttoncolor = ContextCompat.getColor(context, R.color.colorAddButton)
                    holder.btnAddtoCart.setBackgroundColor(removebuttoncolor)
                    addlist.remove(FoodLis)
                }
                else
                {
                    Toast.makeText(context,"Some Error Occurred",Toast.LENGTH_SHORT).show()
                }
            }
           if(addlist.isEmpty())
           {
               btnProceed.visibility = View.GONE

                }
           else{
           btnProceed.visibility = View.VISIBLE
       }
        }

    }

    class FoodListViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val llFoodList: LinearLayout = view.findViewById(R.id.llContent)
        val FoodId:TextView=view.findViewById(R.id.txtFoodId)
        val FoodName:TextView=view.findViewById(R.id.txtFoodName)
        val FoodPrice:TextView=view.findViewById(R.id.txtFoodPrice)
        val btnAddtoCart: Button =view.findViewById(R.id.btnAddToCart)

    }


}



