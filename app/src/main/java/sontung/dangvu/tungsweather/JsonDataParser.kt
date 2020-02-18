package sontung.dangvu.tungsweather

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "JsonDataParser"
class JsonDataParser {

    lateinit var timeZone : String

    fun weatherInfoList(json : String) : ArrayList<WeatherInfo> {
        Log.d(TAG, "weatherInfoList() called")
        val weatherList = ArrayList<WeatherInfo>()



        try {
            val jsonData = JSONObject(json)
            val timeZone = jsonData.getString("timezone")
            this.timeZone = timeZone
            val currently = jsonData.getJSONObject("currently")
            val time = currently.getLong("time") * 1000
            val summary = currently.getString("summary")
            val icon = currently.getString("icon")
            val temperature = currently.getDouble("temperature")
            val apparentTemperature = currently.getDouble("apparentTemperature")

            val weatherInfo = WeatherInfo(convertTimeToDate(time),convertTimeToHour(time), summary, icon, toCelsius(temperature), toCelsius(apparentTemperature))
            weatherList.add(weatherInfo)

            val hourly = jsonData.getJSONObject("hourly")

            val data = hourly.getJSONArray("data")

            for (i in 1 until 24) {
                val hourly1 = data.getJSONObject(i)
                val time1 = hourly1.getLong("time") * 1000
                Log.d(TAG, "Time1 is $time1")
                val summary1 = hourly1.getString("summary")
                val icon1 = hourly1.getString("icon")
                val temperature1 = hourly1.getDouble("temperature")
                val apparentTemperature1 = hourly1.getDouble("apparentTemperature")

                val weatherInfo1 = WeatherInfo(convertTimeToDate(time1),convertTimeToHour(time1), summary1, icon1, toCelsius(temperature1), toCelsius(apparentTemperature1))
                weatherList.add(weatherInfo1)
                convertTimeToHour(time1)
            }


        } catch (exception : JSONException) {
            Log.d(TAG, "JSONException : $exception")
        }

        Log.d(TAG, "There are ${weatherList.size} items in the list")

        for (i in 0 until weatherList.size) {
            Log.d(TAG, weatherList[i].toString())
        }

        return weatherList
    }

    private fun toCelsius(f : Double) : Double {
        return (f - 32) / 1.8
    }

    private fun convertTimeToHour(time : Long) : String{
        Log.d(TAG, "converting time with time = $time")
        try {
            val sdf = SimpleDateFormat("HH",Locale("vi", "VIETNAM"))
            return sdf.format(Date(time))
        } catch (e : Exception) {
            Log.d(TAG, "$e")
        }
        return ""
    }

    private fun convertTimeToDate(time : Long) : String{
        Log.d(TAG, "converting time with time = $time")
        try {
            val sdf = SimpleDateFormat("dd/MM", Locale("vi", "VIETNAM"))
            return sdf.format(Date(time))

        } catch (e : Exception) {
            Log.d(TAG, "$e")
        }
        return ""
    }
}