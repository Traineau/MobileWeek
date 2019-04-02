package com.example.mobileweek.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import com.example.mobileweek.R

class ProfilActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                goToHome()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_play -> {
                goToPlay()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profil -> {
                goToProfil()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_parameters -> {
                goToParameters()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun goToHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun goToPlay(){
        val intent = Intent(this, PlayActivity::class.java)
        startActivity(intent)
    }

    private fun goToParameters(){
        val intent = Intent(this, ParametersActivity::class.java)
        startActivity(intent)
    }

    private fun goToProfil(){
    }
}
