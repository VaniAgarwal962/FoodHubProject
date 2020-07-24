package com.internshala.foodhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.internshala.foodhub.R
import com.internshala.foodhub.database.ResEntity

class PlacedActivity : AppCompatActivity() {

    lateinit var btnok:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_placed)
        btnok=findViewById(R.id.btnOrderPlacedOk)


        val rest = intent.getParcelableExtra<ResEntity>("Resobj")

        btnok.setOnClickListener {
            val intent= Intent(this@PlacedActivity,MainActivity::class.java)
            intent.putExtra("user_id","")
            intent.putExtra("res_name",rest)
            startActivity(intent)
            finishAffinity()
        }

    }
}
