package com.internshala.foodhub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.internshala.foodhub.R
import com.internshala.foodhub.database.CartEntity
import com.internshala.foodhub.model.History

class OrderHistoryRecyclerAdapter(val context: Context,val foodItemList:List<History>):RecyclerView.Adapter<OrderHistoryRecyclerAdapter.OrderHistoryViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_order_history_single_row,parent,false)
        return OrderHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foodItemList.size
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val OrderCartLis=foodItemList[position]
        holder.txtResName.text=OrderCartLis.restaurant_name
        holder.txtorderat.text=OrderCartLis.order_placed_at
        val adapter=OrderHistoryListRecyclerAdapter(context,OrderCartLis.food_items)
        holder.recyclerview.adapter=adapter
        holder.recyclerview.layoutManager=LinearLayoutManager(context)

    }

    class OrderHistoryViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtResName: TextView =view.findViewById(R.id.txtResName)
        val txtorderat: TextView =view.findViewById(R.id.txtorderat)
        val recyclerview:RecyclerView=view.findViewById(R.id.txtHistoryRecyclerView)

    }
}