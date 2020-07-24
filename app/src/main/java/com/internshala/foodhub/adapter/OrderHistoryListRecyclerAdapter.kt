package com.internshala.foodhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodhub.R
import com.internshala.foodhub.model.FoodItems

class OrderHistoryListRecyclerAdapter(val context: Context,val fooditems:List<FoodItems>):RecyclerView.Adapter<OrderHistoryListRecyclerAdapter.OrderHistoryListViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderHistoryListRecyclerAdapter.OrderHistoryListViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_two_single_row,parent,false)
        return OrderHistoryListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fooditems.size

    }

    override fun onBindViewHolder(
        holder: OrderHistoryListRecyclerAdapter.OrderHistoryListViewHolder,
        position: Int
    ) {
       val foodItems=fooditems[position]
        holder.txtFoodName.text=foodItems.name
        holder.txtFoodPrice.text=foodItems.cost
    }

    class OrderHistoryListViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val txtFoodName:TextView=view.findViewById(R.id.txtFoodName)
        val txtFoodPrice:TextView=view.findViewById(R.id.txtFoodPrice)
    }
}
