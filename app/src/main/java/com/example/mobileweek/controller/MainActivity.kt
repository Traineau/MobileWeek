package com.example.mobileweek.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileweek.R
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.widget.Button

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: ActionBar

    private val OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                toolbar.title = "Accueil"
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_play -> {
                toolbar.title = "Jouer"
                val playFragment = PlayFragment.newInstance()
                openFragment(playFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profil -> {
                toolbar.title = "Profil"
                val profilFragment = ProfilFragment.newInstance()
                openFragment(profilFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_parameters -> {
                toolbar.title = "Parametres"
                val parametersFragment = ParametersFragment.newInstance()
                openFragment(parametersFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener)

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
