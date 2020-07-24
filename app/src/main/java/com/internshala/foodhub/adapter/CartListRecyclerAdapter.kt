package com.internshala.foodhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodhub.R
import com.internshala.foodhub.database.CartEntity

class CartListRecyclerAdapter(val context: Context, val foodItemList: List<CartEntity>):RecyclerView.Adapter<CartListRecyclerAdapter.CartListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CartListViewHolder{
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_screen_single_row,parent,false)
        return CartListViewHolder(view)
    }

    override fun getItemCount(): Int {
       return foodItemList.size
    }

    override fun onBindViewHolder(holder: CartListViewHolder, position: Int) {
        val CartLis=foodItemList[position]
        holder.txtFoodName.text=CartLis.Food_Name
        holder.txtFoodPrice.text=CartLis.Food_Price

    }

    class CartListViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtFoodName:TextView=view.findViewById(R.id.txtFoodName)
        val txtFoodPrice:TextView=view.findViewById(R.id.txtFoodPrice)


    }
}