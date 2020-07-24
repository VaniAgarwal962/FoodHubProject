package com.internshala.foodhub.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.internshala.foodhub.R
import com.internshala.foodhub.adapter.OrderHistoryRecyclerAdapter
import com.internshala.foodhub.database.CartEntity
import com.internshala.foodhub.database.ResEntity
import com.internshala.foodhub.model.FoodItems
import com.internshala.foodhub.model.FoodList
import com.internshala.foodhub.model.History
import com.internshala.foodhub.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * A simple [Fragment] subclass.
 */
class OrderFragment(val userid:String) : Fragment() {

    lateinit var History_Fragment: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: OrderHistoryRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar



    lateinit var defaultpage:RelativeLayout
    lateinit var showlist:RelativeLayout


    lateinit var useridtwo: String
    //lateinit var resnametwo:TextView
//    lateinit var foodidtwo:String
//    lateinit var foodnametwo:String
//    lateinit var costtwo:String


    //lateinit var sharedPreferences:SharedPreferences

    val OrderList= arrayListOf<History>(    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_order, container, false)

        History_Fragment=view.findViewById(R.id.txtOrderHistory_Fragment)
        layoutManager=LinearLayoutManager(activity)
        showlist=view.findViewById(R.id.showlist)

        defaultpage=view.findViewById(R.id.default_img)
        showlist.visibility=View.GONE
       // defaultpage.visibility=View.VISIBLE
        defaultpage.visibility=View.GONE

      //  resnametwo=view.findViewById(R.id.txtOrderHistoryResName)


        progressLayout=view.findViewById(R.id.ProgressLayout)
        progressBar=view.findViewById(R.id.ProgressBar)
        progressBar.visibility=View.VISIBLE
        progressLayout.visibility=View.VISIBLE

var f=0;
       useridtwo=userid
        //resnametwo.text=resname

        if(ConnectionManager().checkingConnectivity(activity as Context)) {

            val queue = Volley.newRequestQueue(activity as Context)
            val url="http://13.235.250.119/v2/orders/fetch_result/$useridtwo"

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val dataofRes = it.getJSONObject("data")
                        val success = dataofRes.getBoolean("success")
                          if (success) {

                        val data = dataofRes.getJSONArray("data")

                              progressBar.visibility=View.GONE
                              progressLayout.visibility = View.GONE
//                        Toast.makeText(
//                            activity as Context,
//                            "Vani Agarwal $data",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        for (i in 0 until data.length()) {
                            showlist.visibility=View.VISIBLE
                            f=1;

                        //    val item = i as History?
                            val ResJsonObject = data.getJSONObject(i)

                            val fooditems = ResJsonObject.getJSONArray("food_items")
                          //  var j = 0
                            val foodlist= arrayListOf<FoodItems>(  )
                            for (l in 0 until fooditems.length()) {
                           //     val item = l as FoodItems?

                                val FoodJsonObject = fooditems.getJSONObject(l)

                                val foodvalue=FoodItems(
                                FoodJsonObject.getString("food_item_id"),
                                FoodJsonObject.getString("name"),
                                FoodJsonObject.getString("cost")
                                )
                                //fooditems.put(j, FoodJsonObject)
                                //j++
                                foodlist.add(foodvalue)
                            }

                        //ResInfoList.add(ResObject)
//                                OrderList.addAll(
//                                    Gson().fromJson(foodItems, Array<CartEntity>::class.java).asList()
//                                )
                            val history=History(
                                        ResJsonObject.getString("order_id"),
                                        ResJsonObject.getString("restaurant_name"),
                                        ResJsonObject.getString("total_cost"),
                                        ResJsonObject.getString("order_placed_at"),
                                        foodlist
                            )

                        OrderList.add(history)


                        recyclerAdapter = OrderHistoryRecyclerAdapter(activity as Context, OrderList)
                        History_Fragment.adapter = recyclerAdapter
                        History_Fragment.layoutManager = layoutManager
                           // showlist.visibility=View.VISIBLE

//divider lines
                        History_Fragment.addItemDecoration(
                            DividerItemDecoration(
                                History_Fragment.context,
                                (layoutManager as LinearLayoutManager).orientation
                            )
                        )
                    }
                              if(f!=1)
                              {
                                  defaultpage.visibility=View.VISIBLE
                              }

                        } else {
//                            Toast.makeText(
//                                activity as Context,
//                                "Some Error Occurred!!",
//                                Toast.LENGTH_SHORT
//                            ).show()
                              Toast.makeText(
                                  activity as Context,
                                  "Server Error Occurred!!",
                                  Toast.LENGTH_SHORT
                              ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some Unexpected Error Occurred!! $e",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    if(activity!=null) {
//                        Toast.makeText(
//                            activity as Context,
//                            "Volley Error occurred ",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Toast.makeText(
                            activity as Context,
                            "Server Error occurred ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "baaf18bd73ebad"
                        return headers
                    }
                }

            queue.add(jsonObjectRequest)
        }
        else
        {
            val dialog= AlertDialog.Builder(activity as Context)
            dialog.setTitle("Connection Error")
            dialog.setMessage("Network not available")
            dialog.setPositiveButton("Open Settings")
            {
                    text,listener->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Exit App")
            {
                    text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        return view
    }
}
