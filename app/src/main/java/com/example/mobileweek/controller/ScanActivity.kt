package com.example.mobileweek.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import com.example.mobileweek.R
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.Result
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult


class ScanActivity : AppCompatActivity(){

    private var mScannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView = ZXingScannerView(this)   // Programmatically initialize the scanner view
        setContentView(mScannerView)

        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

        IntentIntegrator(this@ScanActivity).initiateScan();

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                val intent = Intent(this@ScanActivity, GameActivity::class.java);
                intent.putExtra("productCode", result.contents.toLong())
                startActivity(intent);
            } else {
                println("scan failed")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
