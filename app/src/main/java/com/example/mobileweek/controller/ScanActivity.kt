package com.example.mobileweek.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.mobileweek.R

class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

        var playButton = findViewById(R.id.playButton) as ImageButton

        playButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val intent = Intent(this@ScanActivity, GameActivity::class.java);
                startActivity(intent);
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
