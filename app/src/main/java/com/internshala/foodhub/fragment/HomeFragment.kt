package com.internshala.foodhub.fragment


import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodhub.adapter.HomeRecyclerAdapter
import com.internshala.foodhub.R
import com.internshala.foodhub.model.Restaurant
import com.internshala.foodhub.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_otp.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


/**
 * A simple [Fragment] subclass.
 */

class HomeFragment(): Fragment() {
    lateinit var Home_Fragment: RecyclerView
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter


    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    val ResInfoList= arrayListOf<Restaurant>(
    )




    var Comparator= Comparator<Restaurant> { price1, price2 ->


        if ((price1.ResPrice.compareTo(price2.ResPrice, true)) == 0) {
            price1.ResName.compareTo(price2.ResName, true)

        } else {

            price1.ResPrice.compareTo(price2.ResPrice, true)
        }
    }


    var Comparator2= Comparator<Restaurant> { price1, price2 ->
        if((price1.ResRating.compareTo(price2.ResRating, true))==0)
        {
            price1.ResName.compareTo(price2.ResName, true)
        }

        else{
            price1.ResRating.compareTo(price2.ResRating,true)
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_home, container, false)


        setHasOptionsMenu(true)

        Home_Fragment=view.findViewById(R.id.txtHome_Fragment)
        layoutManager=LinearLayoutManager(activity)


        progressLayout=view.findViewById(R.id.ProgressLayout)
        progressBar=view.findViewById(R.id.ProgressBar)
        progressLayout.visibility=View.VISIBLE



        val queue = Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkingConnectivity(activity as Context)) {

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val dataofRes = it.getJSONObject("data")
                        val success = dataofRes.getBoolean("success")
                        if (success) {
                            val data=dataofRes.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val ResJsonObject = data.getJSONObject(i)
                                val ResObject = Restaurant(
                                    ResJsonObject.getString("id"),
                                    ResJsonObject.getString("name"),
                                    ResJsonObject.getString("cost_for_one"),
                                    ResJsonObject.getString("rating"),
                                    ResJsonObject.getString("image_url")
                                )
                                ResInfoList.add(ResObject)
                                recyclerAdapter = HomeRecyclerAdapter(activity as Context, ResInfoList)
                                Home_Fragment.adapter = recyclerAdapter
                                Home_Fragment.layoutManager = layoutManager

                            }
                        } else {
//                            Toast.makeText(
////                                activity as Context,
////                                "Some Error Occurred!!",
////                                Toast.LENGTH_SHORT
////                            ).show()
                            Toast.makeText(
                                activity as Context,
                                "Server Error Occurred!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
//                        Toast.makeText(
//                            activity as Context,
//                            "Some Unexpected Error Occurred!! $e",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Toast.makeText(
                            activity as Context,
                            "Some Unexpected Error Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    if(activity!=null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley Error occurred ",
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
            val dialog=AlertDialog.Builder(activity as Context)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_sorting,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item?.itemId
        if(id==R.id.action_sort1)
        {
            Collections.sort(ResInfoList,Comparator)
        }
        else if(id==R.id.action_sort2)
        {
            Collections.sort(ResInfoList,Comparator)
            ResInfoList.reverse()
        }
        else if(id==R.id.action_sort3)
        {
            Collections.sort(ResInfoList,Comparator2)
        }

        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }



}
