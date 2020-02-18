package sontung.dangvu.tungsweather

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import java.util.*
import kotlin.math.roundToInt

private const val TAG = "MainActivity"
private const val URL = "https://api.darksky.net/forecast/"
private const val API = "8a82e5e36287b864d19a71f08b022e27"


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var cityName : TextView
    private lateinit var bigTemperature : TextView
    private lateinit var currentSummary : TextView
    private lateinit var mainContainer : ConstraintLayout
    private var requestURL = "$URL$API"

    private lateinit var adapter : WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainContainer = findViewById(R.id.main_container)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        cityName = findViewById(R.id.city_name)
        bigTemperature = findViewById(R.id.big_temperature)
        currentSummary = findViewById(R.id.current_summary)

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
            val latitude = location?.latitude
            val longitude = location?.longitude

            val geocoder = Geocoder(this, Locale("vi", "VIETNAM"))


            val address = geocoder.getFromLocation(latitude!!, longitude!!,1)
            if (address != null) {
                val cityName = address[0].adminArea
                this.cityName.text = cityName
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
        var data = ""

        val queue = Volley.newRequestQueue(this)
        val url = requestURL

        val stringRequest =
            StringRequest(Request.Method.GET, url, Response.Listener<String> {
                Log.d(TAG, "success")
                Log.d(TAG, "Result is $it")
                parseData(it)

            }, Response.ErrorListener {
                Log.d(TAG, "failed")
            })
        queue.add(stringRequest)

        Log.d(TAG, "getting data with $data")
    }


    private fun parseData(data : String) {
        Log.d(TAG, "parseData() called with $data")
        val parser = JsonDataParser()

        adapter = WeatherAdapter(parser.weatherInfoList(data), this)
        recyclerView.adapter = adapter

        val currentInfo = adapter.getList()[0]
        val displayedCurrentTemp = "${currentInfo.temperature.roundToInt()}\u2103"
        bigTemperature.text = displayedCurrentTemp
        currentSummary.text = currentInfo.summary

        setBackGroundColor(currentInfo.icon)

    }

    private fun setBackGroundColor(icon : String){
        when (icon) {
            "mostly-cloudy" -> mainContainer.setBackgroundColor(resources.getColor(R.color.grey, null))
            "overcast" -> mainContainer.setBackgroundColor(resources.getColor(R.color.dark_grey, null))
            "partly-cloudy" -> mainContainer.setBackgroundColor(resources.getColor(R.color.light_blue, null))
            "clear-day" -> mainContainer.setBackgroundColor(resources.getColor(R.color.light_sky_blue, null))
        }

    }
}
