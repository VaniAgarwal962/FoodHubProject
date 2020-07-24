package com.internshala.foodhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodhub.R
import com.internshala.foodhub.adapter.CartListRecyclerAdapter
import com.internshala.foodhub.adapter.FoodListRecyclerAdapter
import com.internshala.foodhub.database.CartDatabase
import com.internshala.foodhub.database.CartEntity
import com.internshala.foodhub.database.ResEntity
import com.internshala.foodhub.model.FoodList
import com.internshala.foodhub.model.Restaurant
import com.internshala.foodhub.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    lateinit var recyclerAdapter: CartListRecyclerAdapter
    lateinit var cartActivity: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager


    lateinit var btnPlaceOrder: Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtOrderingFrom:TextView
    lateinit var toolbar:androidx.appcompat.widget.Toolbar


    var count: Int = 0

    var dbCartlist = listOf<CartEntity>(   )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)


        cartActivity = findViewById(R.id.CartRecyclerView)
        layoutManager = LinearLayoutManager(this@CartActivity)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        txtOrderingFrom=findViewById(R.id.txtOrderingFrom)




        toolbar = findViewById(R.id.carttoolbar)
        val toolbar = "My Cart"
        ViewToolbar()
        supportActionBar?.title = toolbar





        val restaurantid=intent.getParcelableExtra<ResEntity>("restaurant_id")
        val ResName=intent.getParcelableExtra<ResEntity>("name")



        dbCartlist = RetrieveOrderData(this@CartActivity).execute().get()
        if (dbCartlist != null && this@CartActivity != null) {

            recyclerAdapter = CartListRecyclerAdapter(this@CartActivity, dbCartlist)
            cartActivity.adapter = recyclerAdapter
            cartActivity.layoutManager = layoutManager
            for (i in dbCartlist) {
                val price = i
                val s = price.Food_Price
                count = count + s.toInt()
            }
        }


        txtOrderingFrom.text="Ordering from: ${ResName!!.ResName}"
        btnPlaceOrder.text = "Place Order(Total:Rs.$count)"


        sharedPreferences=getSharedPreferences(getString(R.string.Food_Application), Context.MODE_PRIVATE)

        val userid=sharedPreferences.getString("user_id","user99")


        val rest = intent.getParcelableExtra<ResEntity>("Resobj")


        btnPlaceOrder.setOnClickListener {
            Toast.makeText(this@CartActivity, "Placing Order", Toast.LENGTH_SHORT).show()

            if (ConnectionManager().checkingConnectivity(this@CartActivity)) {
                val queue = Volley.newRequestQueue(this@CartActivity)
                val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                try {

                    val jsonObject = JSONObject()
                    val food=JSONArray()
                    var j=0
                    for (i in dbCartlist) {
                        val item=i as CartEntity?
                        val FoodJsonObject =JSONObject()
                        FoodJsonObject.put("food_item_id",item?.Food_Id)
                        food.put(j,FoodJsonObject)
                        j++
                    }
                    jsonObject.put("user_id",userid)
                    jsonObject.put("restaurant_id", restaurantid!!.ResId.toString())
                    jsonObject.put("total_cost", count.toString())
                    jsonObject.put("food",food)


                    val jsonObjectRequest =
                        object : JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonObject,
                            Response.Listener {

                                val res = it.getJSONObject("data")
                                val success = res.getBoolean("success")
                                if (success) {
                                    val startintent = Intent(this@CartActivity, PlacedActivity::class.java)
                                    intent.putExtra("res_name",rest)
                                    startActivity(startintent)
                                    finish()

                                } else {
                                    Toast.makeText(
                                        this@CartActivity,
                                        "Some Error Occurred!!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            Response.ErrorListener {
                                if (this@CartActivity != null) {
                                    Toast.makeText(
                                        this@CartActivity,
                                        "Server Error Occurred",
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
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@CartActivity,
                        "Some Unexpected Error Occurred!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            else {
                val dialog = AlertDialog.Builder(this@CartActivity)
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
                    ActivityCompat.finishAffinity(this@CartActivity)
                }
                dialog.create()
                dialog.show()
            }

        }
    }
        class RetrieveOrderData(val context: Context) : AsyncTask<Void, Void, List<CartEntity>>() {
            override fun doInBackground(vararg p0: Void?): List<CartEntity> {
                val db = Room.databaseBuilder(context, CartDatabase::class.java, "carts-db").build()
                return db.cartDao().getlist()
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

    }



