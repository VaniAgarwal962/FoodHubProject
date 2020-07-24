package com.internshala.foodhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.internshala.foodhub.R
import com.internshala.foodhub.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class OtpActivity : AppCompatActivity() {

    lateinit var confirmpassword:EditText
    lateinit var password:EditText
    lateinit var otp:EditText

    lateinit var btnsubmit:Button

    lateinit var awesomeValidation: AwesomeValidation

    lateinit var sharedPreferences:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

    btnsubmit=findViewById(R.id.btnSubmit)
        password=findViewById(R.id.txtPassword)
        otp=findViewById(R.id.txtOTP)
        confirmpassword=findViewById(R.id.txtConfirmPassword)


        awesomeValidation= AwesomeValidation(ValidationStyle.BASIC)
        awesomeValidation.addValidation(this@OtpActivity,R.id.txtPassword,".{4,}",R.string.invalid_password)
        awesomeValidation.addValidation(this@OtpActivity,R.id.txtConfirmPassword,R.id.txtPassword,R.string.invalid_confirm_password)




        sharedPreferences=getSharedPreferences(getString(R.string.Food_Application), Context.MODE_PRIVATE)

        val mobilenumber=intent.getStringExtra("mobile_number")


    btnsubmit.setOnClickListener {
            if (ConnectionManager().checkingConnectivity(this@OtpActivity)) {
                if (awesomeValidation.validate()) {
                    val queue = Volley.newRequestQueue(this@OtpActivity)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"

                    val jsonObject = JSONObject()
                    jsonObject.put("mobile_number", mobilenumber.toString())
                    jsonObject.put("password", password.text.toString())
                    jsonObject.put("otp", otp.text.toString())

                    try {

                        val jsonObjectRequest =
                            object : JsonObjectRequest(Request.Method.POST, url, jsonObject,
                                Response.Listener {

                                    val res = it.getJSONObject("data")

                                    val success = res.getBoolean("success")

                                    if (success) {

                                        val successMessage = res.getString("successMessage")
                                        sharedPreferences.edit().clear().apply()


                                        Toast.makeText(
                                            this@OtpActivity,
                                            "$successMessage",
                                            Toast.LENGTH_SHORT
                                        ).show()


                                        val intent =
                                            Intent(this@OtpActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    }

                                    else {
                                        val message=res.getString("errorMessage")
                                        Toast.makeText(
                                            this@OtpActivity,
                                            "$message",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }, Response.ErrorListener {
                                    if (this@OtpActivity != null) {
                                        Toast.makeText(
                                            this@OtpActivity,
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
                            this@OtpActivity,
                            "Some Unexpected Error Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else
            {
                val dialog = AlertDialog.Builder(this@OtpActivity)
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
                    ActivityCompat.finishAffinity(this@OtpActivity)
                }
                dialog.create()
                dialog.show()
            }


        }
    }
}
