package sontung.dangvu.tungsweather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.Response
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.roundToInt

private const val TAG = "MainActivity"

private const val URL = "https://api.darksky.net/forecast/"
private const val API = "8a82e5e36287b864d19a71f08b022e27"

const val VALUE_LATITUDE = "latitude"
const val VALUE_LONGITUDE = "longitude"
const val VALUE_LOADED_DATA = "loaded_data"
const val VALUE_CITY_NAME = "city_name"

class MainActivity : AppCompatActivity() {

    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var cityName = ""
    private var requestURL = "$URL$API"

    private var loadedData = ""

    private val mHandler = object : Handler(){
        override fun handleMessage(msg: Message) {
            val bundle = Bundle()
            bundle.putDouble(VALUE_LATITUDE, latitude)
            bundle.putDouble(VALUE_LONGITUDE, longitude)
            bundle.putString(VALUE_LOADED_DATA, loadedData)
            bundle.putString(VALUE_CITY_NAME, cityName)
            Log.d(TAG, "data is $loadedData")
            val currentFragment = CurrentFragment(applicationContext)
            currentFragment.arguments = bundle

            supportFragmentManager.beginTransaction().add(R.id.fragment_holder, currentFragment , null).commit()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }
        getLocation()
        getData()



    }

    private fun getLocation() {
        Log.d(TAG, "Location get started")
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            latitude = location!!.latitude
            longitude = location.longitude

            val geocoder = Geocoder(this, Locale("vi", "VIETNAM"))


            val address = geocoder.getFromLocation(latitude!!, longitude!!,1)
            if (address != null) {
                cityName = address[0].adminArea
            }

            Log.d(TAG, "latitude is $latitude")
            Log.d(TAG, "longitude is $longitude")

            requestURL= "$requestURL/$latitude,$longitude"
            Log.d(TAG, requestURL)

        } catch (e : SecurityException){
            Log.d(TAG, "$e")
        }

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    private fun getData() {
        Log.d(TAG, "getData called()")
        Thread(Runnable {
            loadContent()
        }).start()

    }

    private fun loadContent() {
        Log.d(TAG, "loadContent() starts")
        val queue = Volley.newRequestQueue(this)
        val url = requestURL

        val stringRequest =
            StringRequest(Request.Method.GET, url, Response.Listener<String> {
                Log.d(TAG, "success")
                Log.d(TAG, "Result is $it")
                loadedData = it

                val msg = Message();
                msg.obj = loadedData
                mHandler.sendMessage(msg)

            }, Response.ErrorListener {
                Log.d(TAG, "failed")
            })
        queue.add(stringRequest)

        Log.d(TAG, "getting data")
    }


}
