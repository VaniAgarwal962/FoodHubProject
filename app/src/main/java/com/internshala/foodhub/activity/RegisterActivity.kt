package com.internshala.foodhub.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.internshala.foodhub.R
import com.internshala.foodhub.fragment.HomeFragment
import com.internshala.foodhub.fragment.ProfileFragment
import com.internshala.foodhub.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {

    lateinit var btnRegister:Button
    lateinit var toolbar:Toolbar

    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var mobilenumber:EditText
    lateinit var deliveryaddress:EditText
    lateinit var password:EditText
    lateinit var confirmpassword:EditText

    lateinit var awesomeValidation: AwesomeValidation

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        btnRegister=findViewById(R.id.btnRegister)

        name=findViewById(R.id.txtName)
        email=findViewById(R.id.txtEmailAddress)
        mobilenumber=findViewById(R.id.txtMobileNumber)
        deliveryaddress=findViewById(R.id.txtDeliveryAddress)
        password=findViewById(R.id.txtPassword)
        confirmpassword=findViewById(R.id.txtConfirmPassword)



        awesomeValidation= AwesomeValidation(ValidationStyle.BASIC)


        awesomeValidation.addValidation(this@RegisterActivity,R.id.txtName,RegexTemplate.NOT_EMPTY,R.string.invalid_name)
        awesomeValidation.addValidation(this@RegisterActivity,R.id.txtMobileNumber,"[5-9]{1}[0-9]{9}$",R.string.invalid_mobile_number)
        awesomeValidation.addValidation(this@RegisterActivity,R.id.txtEmailAddress,Patterns.EMAIL_ADDRESS,R.string.invalid_email)
        awesomeValidation.addValidation(this@RegisterActivity,R.id.txtPassword,".{4,}",R.string.invalid_password)
        awesomeValidation.addValidation(this@RegisterActivity,R.id.txtConfirmPassword,R.id.txtPassword,R.string.invalid_confirm_password)
        awesomeValidation.addValidation(this@RegisterActivity,R.id.txtDeliveryAddress,RegexTemplate.NOT_EMPTY,R.string.invalid_delivery)



        toolbar=findViewById(R.id.carttoolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Register Yourself"
        ViewHamburgerIcon()

        sharedPreferences=getSharedPreferences(getString(R.string.Food_Application), Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false);
        if(isLoggedIn){
            val intent= Intent(this@RegisterActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnRegister.setOnClickListener{

            if (ConnectionManager().checkingConnectivity(this@RegisterActivity)) {
                if (awesomeValidation.validate()) {
                    val queue = Volley.newRequestQueue(this@RegisterActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result"
                    try {
                        val jsonObject = JSONObject()
                        jsonObject.put("name", name.text.toString())
                        jsonObject.put("mobile_number", mobilenumber.text.toString())
                        jsonObject.put("password", password.text.toString())
                        jsonObject.put("address", deliveryaddress.text.toString())
                        jsonObject.put("email", email.text.toString())
                        val jsonObjectRequest =
                            object : JsonObjectRequest(Request.Method.POST, url, jsonObject,
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
                                            Intent(this@RegisterActivity, MainActivity::class.java)

                                        startActivity(startintent)
                                        finish()
                                    } else {
                                        val message=res.getString("errorMessage")
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "$message",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }, Response.ErrorListener {
                                    if (this@RegisterActivity != null) {

                                        Toast.makeText(
                                            this@RegisterActivity,
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
                            this@RegisterActivity,
                            "Some Unexpected Error Occurred!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else
            {
                val dialog = AlertDialog.Builder(this@RegisterActivity)
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
                    ActivityCompat.finishAffinity(this@RegisterActivity)
                }
                dialog.create()
                dialog.show()
            }


        }

    }
    fun ViewHamburgerIcon()
    {
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        val intent=Intent(this@RegisterActivity,LoginActivity::class.java)
        startActivity(intent)
        finish()

    }
}
