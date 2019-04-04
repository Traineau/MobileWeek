package com.example.mobileweek.controller

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.example.mobileweek.R
import com.example.mobileweek.model.Product

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import android.os.Looper
import android.os.Handler
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.squareup.picasso.Picasso
import android.location.Geocoder
import android.text.Html
import android.util.Log
import android.view.View
import com.google.android.gms.maps.model.*
import java.util.*
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.collections.ArrayList
import kotlin.math.*


class GameActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    var product = Product()
    var myMarkers: MutableList<Marker> = mutableListOf<Marker>()
    var distance: Int = 0

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
        val client = OkHttpClient()

        val productName: TextView = findViewById(R.id.productName) as TextView
        val brandName: TextView = findViewById(R.id.productBrand) as TextView
        val productImage = findViewById(R.id.productImage) as ImageView

        product.code = 3564700014677

        val request = Request.Builder()
            .url("https://fr.openfoodfacts.org/api/v0/produit/${product.code}.json")
            .build()

        // API call to get the product informations
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException) {}
            override fun onResponse(call: Call?, response: Response){
                val jsonData = response.body()?.string()
                val Jobject = JSONObject(jsonData)

                // Settings the product variables
                product.name = Jobject.getJSONObject("product")?.getString("product_name")
                product.imageUrl = Jobject.getJSONObject("product")?.getString("image_front_url")
                product.brand = Jobject.getJSONObject("product")?.getJSONArray("brands_tags")?.getString(0)
                product.packerCode = Jobject.getJSONObject("product")?.getString("emb_codes_tags")
                product.packerCity = Jobject.getJSONObject("product")?.getJSONArray("cities_tags")?.getString(0)

                // Getting the product's city information with geocode
                val adressInfo = getGeocodeInfo(product.packerCity)

                product.lat = adressInfo[0].latitude
                product.long = adressInfo[0].longitude

                // Updating the view
                Handler(Looper.getMainLooper()).post(Runnable {
                    productName.text = product.name
                    brandName.text = product.brand

                    // Using picasso to change the product image
                    Picasso.get()
                        .load(product.imageUrl)
                        .into(productImage);
                })

            }
        })

    }

    // When the user click on validate
    protected fun handleValidateResponse(map: GoogleMap) =// If the first marker wasn't placed
        if(myMarkers.getOrNull(0) == null) {
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage(R.string.dialog_no_marker_message)?.setTitle(R.string.dialog_no_marker_title)
                builder.create()
                builder.show()
            }
        }else{
            // Addin response marker
            val newMarkerCoordinate = LatLng(product.lat as Double, product.long as Double)

            val newMarker = map.addMarker(
                MarkerOptions()
                    .position(newMarkerCoordinate)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );

            // Putting marker in the list
            myMarkers.add(1, newMarker)

            // Creating a new LatLngBounds with both markers to center the view
            val newPosition =
                LatLngBounds.builder().include(myMarkers[0].position).include(myMarkers[1].position).build()
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(newPosition, 200))

            // Adding a red path between the two markers
            mMap.addPolyline(
                PolylineOptions()
                    .add(myMarkers[0].position, myMarkers[1].position)
                    .width(5F)
                    .color(Color.RED)
            )

            // Calculating the km between the two markers
            distance = getDistanceFromLatLonInkm(
                myMarkers[0].position.latitude,
                myMarkers[0].position.longitude,
                myMarkers[1].position.latitude,
                myMarkers[1].position.longitude
            ).toInt()

            val points = 1000 - distance

            estimationText.setText("Votre estimation est à $distance km du lieu d'origine du produit scanné !")
            winPointsText.text = "Vous avez gagné $points points !"
            responseLayout.visibility = View.VISIBLE

            validateResponse.text = "Terminé"
            validateResponse.setOnClickListener {
                val intent = Intent(this@GameActivity, MainActivity::class.java);
                startActivity(intent);
            }
        }

    protected fun getGeocodeInfo(adress: String?): List<android.location.Address>{
        val TAG = "Error finding the city"
        val geocoder = Geocoder(this, Locale.getDefault())
        var errorMessage = ""
        var addresses: List<android.location.Address> = emptyList()

        try {
            addresses = geocoder.getFromLocationName(adress, 1)
        } catch (ioException: IOException) {
            errorMessage = "Service unavailable"
            Log.e(TAG, errorMessage, ioException)
        } catch (illegalArgumentException: IllegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "Invalid city"
            Log.e(TAG, "$errorMessage. City = $adress", illegalArgumentException)
        }

        return addresses
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val paris = LatLng(48.8534, 2.3488)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 6.0F))

        mMap.setOnMapClickListener {
            mMap.clear()
            val newMarker = mMap.addMarker(MarkerOptions().position(it))
            myMarkers.clear()
            myMarkers.add(0, newMarker)
        }

        validateResponse.setOnClickListener {
            handleValidateResponse(mMap)
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

    fun getDistanceFromLatLonInkm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double{
        // Radius of the earth
        val R = 6371
        var dLat = deg2rad(lat2-lat1)
        val dlon = deg2rad(lon2-lon1)
        val a = sin(dLat/2) * sin(dLat/2) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * sin(dlon/2) * sin(dlon/2)
        val b = 2 * atan2(sqrt(a), sqrt(1-a))
        val c = R * b

        return c
    }

    fun deg2rad(deg: Double): Double{
        return deg * (PI/180)
    }

}
