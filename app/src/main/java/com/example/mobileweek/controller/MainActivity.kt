package com.example.mobileweek.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileweek.R
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    /*private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_play -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profil -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_parameters -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        //bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
