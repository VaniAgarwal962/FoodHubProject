package com.internshala.foodhub.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodhub.R
import com.internshala.foodhub.adapter.FoodListRecyclerAdapter
import com.internshala.foodhub.database.CartEntity
import com.internshala.foodhub.database.ResEntity
import com.internshala.foodhub.local_databases.DBAsyncTask
import com.internshala.foodhub.local_databases.DBAsyncTaskResList
import com.internshala.foodhub.model.FoodList
import com.internshala.foodhub.util.ConnectionManager
import org.json.JSONException

class FoodListActivity : AppCompatActivity() {


    lateinit var txtchoose: TextView
    lateinit var fill: ImageView
    lateinit var emptyimage: ImageView
    lateinit var toolbar: Toolbar
    lateinit var btnProceed: Button
    lateinit var recyclerAdapter: FoodListRecyclerAdapter
    lateinit var foodListActivity: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    val FoodListInfo = arrayListOf<FoodList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        val cartEntity = CartEntity(
            1,
            "vani",
            "4444",
            "1111"
        )
        val async = DBAsyncTask(
            this@FoodListActivity,
            cartEntity,
            4
        ).execute().get()

        btnProceed = findViewById(R.id.btnProceedToCart)
        txtchoose = findViewById(R.id.textchoose)
        fill = findViewById(R.id.imgAdd_To_Fill_Favourite)
        emptyimage = findViewById(R.id.imgAdd_To_Favourite)


        btnProceed.visibility = View.GONE



        toolbar = findViewById(R.id.toolbar)
        val toolbar = intent.getStringExtra("name")
        ViewToolbar()
        supportActionBar?.title = toolbar



        foodListActivity = findViewById(R.id.txtFoodListRecyclerView)
        layoutManager = LinearLayoutManager(this@FoodListActivity)


        val id = intent.getStringExtra("id")

        val queue = Volley.newRequestQueue(this@FoodListActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$id"


        val rest = intent.getParcelableExtra<ResEntity>("Resobj")



        if (ConnectionManager().checkingConnectivity(this@FoodListActivity)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        val dataoffood = it.getJSONObject("data")
                        val success = dataoffood.getBoolean("success")

                        if (success) {
                            val data = dataoffood.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val FoodJsonObject = data.getJSONObject(i)
                                val FoodObject = FoodList(
                                    FoodJsonObject.getString("id"),
                                    FoodJsonObject.getString("name"),
                                    FoodJsonObject.getString("cost_for_one"),
                                    FoodJsonObject.getString("restaurant_id")
                                )
                                FoodListInfo.add(FoodObject)
                                recyclerAdapter = FoodListRecyclerAdapter(
                                    this@FoodListActivity,
                                    FoodListInfo,
                                    btnProceed
                                )
                                foodListActivity.adapter = recyclerAdapter
                                foodListActivity.layoutManager = layoutManager
                                val checkAdded =
                                    DBAsyncTaskResList(this@FoodListActivity, rest!!, 1).execute()
                                        .get()

                                if (checkAdded) {
                                    fill.visibility = View.VISIBLE
                                }
                                else {
                                    fill.visibility = View.GONE
                                }
                                emptyimage.setOnClickListener {
                                    if (!DBAsyncTaskResList(
                                            this@FoodListActivity,
                                            rest!!,
                                            1
                                        ).execute().get()
                                    ) {
                                        val async = DBAsyncTaskResList(
                                            this@FoodListActivity,
                                            rest!!,
                                            2
                                        ).execute()
                                        val result = async.get()
                                        if (result) {
                                            fill.visibility = View.VISIBLE
                                        } else {
                                            Toast.makeText(
                                                this@FoodListActivity,
                                                "Some Error Occurred",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        val async = DBAsyncTaskResList(
                                            this@FoodListActivity,
                                            rest!!,
                                            3
                                        ).execute()
                                        val result = async.get()
                                        if (result) {
                                            Toast.makeText(
                                                this@FoodListActivity,
                                                "Removed from favourites",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            fill.visibility = View.GONE

                                        } else {
                                            Toast.makeText(
                                                this@FoodListActivity,
                                                "Some Error Occurred",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }

                            }
                        } else {
                            Toast.makeText(
                                this@FoodListActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@FoodListActivity,
                            "Some unexpected error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    btnProceed.setOnClickListener{
                        Toast.makeText(this@FoodListActivity,"HURRAY!!!PROCEEDING TO CART",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this@FoodListActivity,CartActivity::class.java)
                        intent.putExtra("name",rest)
                        intent.putExtra("restaurant_id",rest)
                        startActivity(intent)
                    }
                },
                    Response.ErrorListener {
                        if (this@FoodListActivity != null) {
                            Toast.makeText(
                                this@FoodListActivity,
                                "Server Error occurred",
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
        else {
            val dialog = AlertDialog.Builder(this@FoodListActivity)
            dialog.setTitle("Connection Error")
            dialog.setMessage("Network not available")
            dialog.setPositiveButton("Open Settings")
            { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()

            }
            dialog.setNegativeButton("Exit App")
            { text, listener ->
                ActivityCompat.finishAffinity(this@FoodListActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    fun ViewToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val intent=Intent(this@FoodListActivity,MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

}

