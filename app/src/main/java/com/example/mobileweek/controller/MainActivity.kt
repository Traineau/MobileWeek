package com.example.mobileweek.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.mobileweek.R
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var toolbar: ActionBar
    private lateinit var mMap: GoogleMap

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
        setContentView(R.layout.activity_main)

        toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun goToHome(){

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
        val intent = Intent(this, ProfilActivity::class.java)
        startActivity(intent)
    }
}
