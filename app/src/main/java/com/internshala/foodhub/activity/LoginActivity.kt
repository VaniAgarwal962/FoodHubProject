package com.internshala.foodhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.internshala.foodhub.R
import com.internshala.foodhub.adapter.HomeRecyclerAdapter
import com.internshala.foodhub.model.Restaurant
import com.internshala.foodhub.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {
    lateinit var btnlogin:Button
    lateinit var forgotpassword:TextView
    lateinit var signup:TextView
    lateinit var mobilenumber:EditText
    lateinit var password:EditText

    lateinit var awesomeValidation: AwesomeValidation

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnlogin = findViewById(R.id.btnLogin)
        forgotpassword = findViewById(R.id.txtforgotpassword)
        signup = findViewById(R.id.txtSignUp)
        mobilenumber=findViewById(R.id.txtMobileNumber)
        password=findViewById(R.id.txtPassword)

        awesomeValidation= AwesomeValidation(ValidationStyle.BASIC)

        awesomeValidation.addValidation(this@LoginActivity,R.id.txtMobileNumber,"[5-9]{1}[0-9]{9}$",R.string.invalid_mobile_number)
        awesomeValidation.addValidation(this@LoginActivity,R.id.txtPassword,".{4,}",R.string.invalid_password)



        sharedPreferences=getSharedPreferences(getString(R.string.Food_Application), Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        if(isLoggedIn){
            val intent= Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        forgotpassword.setOnClickListener {
            val startintent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(startintent)
            finish()
        }
        signup.setOnClickListener {
            val startintent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(startintent)
            finish()
        }
        btnlogin.setOnClickListener {

            if (ConnectionManager().checkingConnectivity(this@LoginActivity)) {
                if(awesomeValidation.validate()) {
                    val queue = Volley.newRequestQueue(this@LoginActivity)
                    val url = "http://13.235.250.119/v2/login/fetch_result"
                    try {
                        val jsonObject = JSONObject()
                        jsonObject.put("mobile_number", mobilenumber.text.toString())
                        jsonObject.put("password", password.text.toString())

                        val jsonObjectRequest =
                            object : JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                jsonObject,
                                Response.Listener {

                                    val res = it.getJSONObject("data")
                                    val success = res.getBoolean("success")
                                    if (success) {
                                        val data = res.getJSONObject("data")
                                        sharedPreferences.edit().putBoolean("isLoggedIn", true)
                                            .apply()
                                        sharedPreferences.edit()
                                            .putString("name", data.getString("name")).apply()
                                        sharedPreferences.edit()
                                            .putString("user_id", data.getString("user_id")).apply()
                                        sharedPreferences.edit()
                                            .putString("email", data.getString("email")).apply()
                                        sharedPreferences.edit().putString(
                                            "mobile_number",
                                            data.getString("mobile_number")
                                        ).apply()
                                        sharedPreferences.edit()
                                            .putString("address", data.getString("address")).apply()
                                        val startintent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(startintent)
                                        finish()
                                    } else {
                                        val message=res.getString("errorMessage")
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "$message",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                Response.ErrorListener {
                                    if (this@LoginActivity != null) {
                                        Toast.makeText(
                                            this@LoginActivity,
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
                            this@LoginActivity,
                            "Some Unexpected Error Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else {
            val dialog = AlertDialog.Builder(this@LoginActivity)
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
                ActivityCompat.finishAffinity(this@LoginActivity)
            }
            dialog.create()
            dialog.show()
        }

        }
        }


    }



