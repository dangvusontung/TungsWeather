package sontung.dangvu.tungsweather

import android.annotation.SuppressLint
import android.app.Application
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

private const val TAG = "WeatherViewModel"

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val weatherInfos: MutableLiveData<List<WeatherInfo>> by lazy {
        MutableLiveData<List<WeatherInfo>>().also {
            loadData()
        }
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {

            weatherInfos.value = msg.obj as List<WeatherInfo>
        }
    }

    fun getWeatherInfos(): LiveData<List<WeatherInfo>> {
        Log.d(TAG, "getWeatherInfo called")
//        Log.d(TAG, weatherInfos.value!![0].toString())
        return weatherInfos
    }

    fun getOnlineData(requestURL: String) {
        Thread(Runnable {
            Log.d(TAG, "loadContent() starts")
            val queue = Volley.newRequestQueue(getApplication())
            val url = requestURL

            val stringRequest =
                StringRequest(Request.Method.GET, url, Response.Listener<String> {
                    Log.d(TAG, "success")
                    Log.d(TAG, "Result is $it")
                    val loadedData = it
//
//                    val weatherInfoObjects = JsonDataParser().weatherInfoList(it)
//
//                    weatherInfos.value = weatherInfoObjects
//                    Log.d(TAG, weatherInfos.value!![0].toString())

                    val msg = Message()
                    msg.obj = JsonDataParser().weatherInfoList(it)
                    mHandler.sendMessage(msg)


                }, Response.ErrorListener {
                    Log.d(TAG, "failed")
                })
            queue.add(stringRequest)

            Log.d(TAG, "getting data")
        }).start()


    }

    private fun loadData() {

    }
}