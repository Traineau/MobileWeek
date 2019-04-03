package com.example.mobileweek.controller

import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.mobileweek.R
import com.example.mobileweek.model.Product

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.os.Looper
import android.os.Handler
import android.widget.ImageView
import com.squareup.picasso.Picasso


class GameActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Show BackArrow in Tab Bar
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);

        // TODO : Put code we got from scanning
        val product = Product()
        val client = OkHttpClient()

        val productName: TextView = findViewById(R.id.productName) as TextView
        val brandName: TextView = findViewById(R.id.productBrand) as TextView
        val productImage = findViewById(R.id.productImage) as ImageView

        product.code = 3564700014677

        val request = Request.Builder()
            .url("https://fr.openfoodfacts.org/api/v0/produit/${product.code}.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {}
            override fun onResponse(call: Call?, response: Response){
                val jsonData = response.body()?.string()
                val Jobject = JSONObject(jsonData)

                product.name = Jobject.getJSONObject("product")?.getString("product_name")
                product.imageUrl = Jobject.getJSONObject("product")?.getString("image_front_url")
                product.brand = Jobject.getJSONObject("product")?.getJSONArray("brands_tags")?.getString(0)
                product.packerCode = Jobject.getJSONObject("product")?.getJSONArray("emb_code_tags")?.getInt(0)
                product.packerCity = Jobject.getJSONObject("product")?.getJSONArray("cities_tags")?.getString(0)

                Handler(Looper.getMainLooper()).post(Runnable {
                    productName.text = product.name
                    brandName.text = product.brand

                    Picasso.get()
                        .load(product.imageUrl)
                        .into(productImage);
                })

            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapClickListener {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(it))
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        // Creating AlertDialog to prevent from quiting

        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(R.string.quit_dialog_ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        // Returning to scan page
                        onBackPressed()
                    })
                setNegativeButton(R.string.quit_dialog_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }

            builder.setMessage(R.string.dialog_message)?.setTitle(R.string.dialog_title)
            builder.create()
            builder.show()
        }

        return true
    }

}
