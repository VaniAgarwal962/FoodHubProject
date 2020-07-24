package com.internshala.foodhub.fragment


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodhub.R
import com.internshala.foodhub.activity.CartActivity
import com.internshala.foodhub.adapter.HomeRecyclerAdapter
import com.internshala.foodhub.database.ResDatabase
import com.internshala.foodhub.database.ResEntity
import com.internshala.foodhub.model.Restaurant
import com.internshala.foodhub.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_favourite.*
import org.json.JSONException

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    lateinit var Fav_Fragment: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter

//    lateinit var progressLayout: RelativeLayout
//    lateinit var progressBar: ProgressBar

    lateinit var fav_default_layout:RelativeLayout


    var dbresinfo2= arrayListOf<Restaurant>()

    var dbResInfoList =listOf<ResEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        Fav_Fragment = view.findViewById(R.id.txtFav_Fragment)
        layoutManager = LinearLayoutManager(activity)



//        progressLayout=view.findViewById(R.id.ProgressLayout)
//        progressBar=view.findViewById(R.id.ProgressBar)
//        progressLayout.visibility=View.VISIBLE


        fav_default_layout=view.findViewById(R.id.fav_default_layout)
       // fav_default_layout.visibility=View.VISIBLE


        dbResInfoList=RetrieveFavourites(activity as Context).execute().get()

        if(activity!=null)
        {

            for(i in dbResInfoList)
            {
                fav_default_layout.visibility=View.GONE
               // progressLayout.visibility = View.GONE

                val rest=Restaurant(
                    i.ResId,
                    i.ResName,
                    i.ResPrice,
                    i.ResRating,
                    i.ResImage
                    )

                dbresinfo2.add(rest)

            }

            recyclerAdapter= HomeRecyclerAdapter(activity as Context,dbresinfo2)
            Fav_Fragment.adapter=recyclerAdapter
            Fav_Fragment.layoutManager=layoutManager
        }

        return view
    }
    class RetrieveFavourites(val context: Context):AsyncTask<Void,Void,List<ResEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<ResEntity> {
            val db= Room.databaseBuilder(context,ResDatabase::class.java,"res-db").build()
            return db.restDao().getList()
        }

    }
}

