package com.internshala.foodhub.fragment


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.internshala.foodhub.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment(val name:String,val mobile:String,val email:String,val daddress:String) : Fragment() {

    lateinit var txtName:Button
    lateinit var txtMobile:Button
    lateinit var txtemail:Button
    lateinit var txtdaddress:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        txtName=view.findViewById(R.id.txtName)
        txtMobile=view.findViewById(R.id.txtMobile)
        txtemail=view.findViewById(R.id.txtemail)
        txtdaddress=view.findViewById(R.id.txtdaddress)

        txtName.text=name
        txtMobile.text="+91-$mobile"
        txtemail.text=email
        txtdaddress.text=daddress




        return view
    }



}
