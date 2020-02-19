package sontung.dangvu.tungsweather


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

private const val TAG = "CurrentFragment"
private const val VALUE_LIST_INFO = "list_info"

class CurrentFragment(private val mContext : Context) : Fragment() {

    private var data = ""
    private var displayedCurrentTemp = ""
    private var summaryNow = ""

    private lateinit var recyclerView: RecyclerView
    private lateinit var cityName : TextView
    private lateinit var bigTemperature : TextView
    private lateinit var currentSummary : TextView
    private lateinit var mainContainer : ConstraintLayout
    private lateinit var adapter : WeatherAdapter

    private val model: WeatherViewModel by activityViewModels()

    private val mHandler = Handler{
        val bundleData = it.data
        val weatherInfos = bundleData.getSerializable(VALUE_LIST_INFO)!! as ArrayList<WeatherInfo>
        adapter = WeatherAdapter(weatherInfos, this.mContext)
        this.recyclerView.adapter = adapter

        val currentInfo = adapter.getList()[0]
        displayedCurrentTemp = "${currentInfo.temperature.roundToInt()}\u2103"
        summaryNow = currentInfo.summary

        setBackGroundColor(currentInfo.icon)

        bigTemperature.text = displayedCurrentTemp
        currentSummary.text = summaryNow
        true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getWeatherInfos().observe(this.viewLifecycleOwner, Observer<List<WeatherInfo>> {
            //            adapter.setList(model.getWeatherInfos().value!!)
//            adapter.notifyDataSetChanged()
        })

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_current, container, false)
        mainContainer = view.findViewById(R.id.main_container)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this.mContext, LinearLayoutManager.HORIZONTAL, false)
        cityName = view.findViewById(R.id.city_name)
        bigTemperature = view.findViewById(R.id.big_temperature)
        currentSummary = view.findViewById(R.id.current_summary)
        val bundle = this.arguments!!
        data = bundle.getString(VALUE_LOADED_DATA)!!
        cityName.text = bundle.getString(VALUE_CITY_NAME)!!
//        processData()

        adapter = WeatherAdapter(model.getWeatherInfos().value!!, mContext)
        this.recyclerView.adapter = adapter

        val currentInfo = adapter.getList()[0]
        displayedCurrentTemp = "${currentInfo.temperature.roundToInt()}\u2103"
        summaryNow = currentInfo.summary

        setBackGroundColor(currentInfo.icon)

        bigTemperature.text = displayedCurrentTemp
        currentSummary.text = summaryNow

        return view
    }

//    private fun processData() {
//        Log.d(TAG, "processData( called")
//        Thread(Runnable {
//            parseData(data)
//        }).start()
//
//    }
//
//    private fun parseData(data : String) {
//        Log.d(TAG, "parseData() called with $data")
//        val parser = JsonDataParser()
//        val weatherInfos = parser.weatherInfoList(data)
//        val bundle = Bundle()
//        bundle.putSerializable(VALUE_LIST_INFO, weatherInfos)
//        val msg = Message()
//        msg.data = bundle
//        mHandler.sendMessage(msg)
//
//    }

    private fun setBackGroundColor(icon : String){
        when (icon) {
            "mostly-cloudy" -> mainContainer.setBackgroundColor(resources.getColor(R.color.grey, null))
            "overcast" -> mainContainer.setBackgroundColor(resources.getColor(R.color.dark_grey, null))
            "partly-cloudy" -> mainContainer.setBackgroundColor(resources.getColor(R.color.light_blue, null))
            "clear-day" -> mainContainer.setBackgroundColor(resources.getColor(R.color.light_sky_blue, null))
//            "clear-night" -> mainContainer.setBackgroundColor()
//            "partly-cloudy-night" -> mainContainer.setBackgroundColor()
//            "cloudy" -> mainContainer.setBackgroundColor()
//            "wind" -> mainContainer.setBackgroundColor()
//            "fog" -> mainContainer.setBackgroundColor()
//            "rain" -> mainContainer.setBackgroundColor()
//            "snow" -> mainContainer.setBackgroundColor()
//            "sleet" -> mainContainer.setBackgroundColor()
        }

    }
}
