package com.internshala.foodhub.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.internshala.foodhub.R
import com.internshala.foodhub.database.ResEntity
import com.internshala.foodhub.fragment.*
import kotlinx.android.synthetic.main.drawer.*


class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigation: NavigationView


    var previousMenuItem: MenuItem?=null

    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigation=findViewById(R.id.navigation)


        ViewToolbar()

        //setting the default fragment on the main page and also in the navigation drawer.
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            HomeFragment()
        ).addToBackStack("Home").commit()
        supportActionBar?.title="Home"
        navigation.setCheckedItem(R.id.home)


        sharedPreferences=getSharedPreferences(getString(R.string.Food_Application), Context.MODE_PRIVATE)

        val hamburgerEnable= ActionBarDrawerToggle(this@MainActivity,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(hamburgerEnable)
        hamburgerEnable.syncState()

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        val headerView : View = navigation.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.txtProfileName)
        val navUserEmail : TextView = headerView.findViewById(R.id.txtEmail)

        navUsername.text = sharedPreferences.getString("name","fff")
        navUserEmail.text = sharedPreferences.getString("mobile_number","4444")


        navigation.setNavigationItemSelectedListener {


            if(previousMenuItem!=null)
            {
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it

            when(it.itemId)
            {
                R.id.home ->
                {

                    openFragment(HomeFragment(),"Home")
                    drawerLayout.closeDrawers()
                }

                R.id.profile ->
                {
                    sharedPreferences=getSharedPreferences(getString(R.string.Food_Application), Context.MODE_PRIVATE)
                    val name=sharedPreferences.getString("name","k")
                    val mobile=sharedPreferences.getString("mobile_number","kk")
                    val email=sharedPreferences.getString("email","kkk")
                    val daddress=sharedPreferences.getString("address","kkkk")
                    openFragment(ProfileFragment(name!!,mobile!!,email!!,daddress!!),"My Profile")
                    drawerLayout.closeDrawers()
                }
                R.id.favourite ->
                {
                    openFragment(FavouriteFragment(),"Favourite Restaurants")
                    drawerLayout.closeDrawers()
                }
                R.id.order ->
                {
                    val userid=sharedPreferences.getString("user_id","userhistory1")
                    openFragment(OrderFragment(userid!!),"My Order History")

                    drawerLayout.closeDrawers()
                }
                R.id.FAQs ->
                {
                    openFragment(FAQFragment(),"Frequently Asked Questions")
                    drawerLayout.closeDrawers()
                }
                R.id.logout->
                {
                    val dialog = AlertDialog.Builder(this@MainActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to logout?")
                    dialog.setPositiveButton("YES")
                    { text, listener ->
                        sharedPreferences.edit().clear().apply()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialog.setNegativeButton("NO")
                    { text, listener ->
                    }
                    dialog.create()
                    dialog.show()
                    drawerLayout.closeDrawers()
                }
            }

            return@setNavigationItemSelectedListener true
        }


    }

    fun ViewToolbar()
    {
        setSupportActionBar(toolbar)
        supportActionBar?.title="TOOLBAR TITLE"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home)
            drawerLayout.openDrawer(GravityCompat.START)
        return super.onOptionsItemSelected(item)
    }
    fun openFragment(fragment: Fragment, title:String?)
    {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit()
        supportActionBar?.title=title
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag)
        {
            !is HomeFragment ->
            {
                openFragment(HomeFragment(),"Home")
                navigation.setCheckedItem(R.id.home)
            }

            else-> {
                super.onBackPressed()
                finish()
            }
        }

    }
}

