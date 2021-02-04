package com.internshala.foodhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.internshala.foodhub.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)
        Handler().postDelayed(
            {
            val startAct= Intent(this@SplashActivity,
                LoginActivity::class.java)
                startActivity(startAct)
                    finish()
            },1000) // in milliseconds  

    }
}
