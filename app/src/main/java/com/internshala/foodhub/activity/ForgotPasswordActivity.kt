package com.internshala.foodhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
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

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var btnNext:Button

    lateinit var email: EditText
    lateinit var mobilenumber: EditText
    lateinit var awesomeValidation: AwesomeValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnNext=findViewById(R.id.btnNext)
        email=findViewById(R.id.txtEmailAddress)
        mobilenumber=findViewById(R.id.txtMobileNumber)




        awesomeValidation= AwesomeValidation(ValidationStyle.BASIC)
        awesomeValidation.addValidation(this@ForgotPasswordActivity,R.id.txtMobileNumber,"[5-9]{1}[0-9]{9}$",R.string.invalid_mobile_number)
        awesomeValidation.addValidation(this@ForgotPasswordActivity,R.id.txtEmailAddress,
            Patterns.EMAIL_ADDRESS,R.string.invalid_email)

        btnNext.setOnClickListener{
            if (ConnectionManager().checkingConnectivity(this@ForgotPasswordActivity)) {
                if (awesomeValidation.validate()) {

                    val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
                    val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

                    val jsonObject = JSONObject()
                    jsonObject.put("mobile_number", mobilenumber.text.toString())
                    jsonObject.put("email", email.text.toString())
                    try {
                        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST, url, jsonObject, Response.Listener {
                                    val res = it.getJSONObject("data")
                                    val success = res.getBoolean("success")
                            if (success) {
                                val first = res.getBoolean("first_try")
                                val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                                dialog.setTitle("Information")
                                dialog.setMessage("Kindly refer to the email for the OTP")
                                dialog.setPositiveButton("OK")
                                { text, listener ->
                                    val startintent =
                                        Intent(this@ForgotPasswordActivity, OtpActivity::class.java)
                                    startintent.putExtra(
                                        "mobile_number",
                                        mobilenumber.text.toString()
                                    )
                                    startActivity(startintent)
                                    finish()
                                }
                                dialog.create()
                                dialog.show()
                            }  else {
                                      val message=res.getString("errorMessage")
                                      Toast.makeText(
                                          this@ForgotPasswordActivity,
                                          "$message",
                                          Toast.LENGTH_SHORT
                                      ).show()
                                  }
                                }, Response.ErrorListener {
                                    if (this@ForgotPasswordActivity != null) {

                                        Toast.makeText(
                                            this@ForgotPasswordActivity,
                                            "Server Error Occurred $it",
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
                            this@ForgotPasswordActivity,
                            "Some Unexpected Error Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else
            {
                val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
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
                    ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                }
                dialog.create()
                dialog.show()
            }

        }

    }
}


