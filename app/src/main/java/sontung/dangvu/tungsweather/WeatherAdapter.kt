package sontung.dangvu.tungsweather

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class WeatherAdapter(
    private val listInfo : ArrayList<WeatherInfo>,
    private val context: Context
) : RecyclerView.Adapter<WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listInfo.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val dec = DecimalFormat("#.##")
        val weatherInfo = listInfo[position]
        holder.summary.text = weatherInfo.summary
        holder.temperature.text = dec.format(weatherInfo.temperature)
        holder.apparentlyTemperature.text = dec.format(weatherInfo.apparentTemperature)
        when (weatherInfo.icon) {
            "partly-cloudy-day" -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.cloudy_1, null))
            "cloudy" -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.cloud, null))
            "mostly-cloudy-day" -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.cloudy, null))
            "overcast" -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.cloudy_2, null))
            "partly-cloudy-night" -> holder.icon.setImageDrawable(context.resources.getDrawable(R.drawable.cloudy_night, null))
        }

        holder.hour.text = weatherInfo.time
        holder.date.text = weatherInfo.day
    }

    fun getList() : ArrayList<WeatherInfo> {
        return listInfo
    }
}

class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val date : TextView = itemView.findViewById(R.id.day)
    val hour : TextView = itemView.findViewById(R.id.hour)
    val icon : ImageView = itemView.findViewById(R.id.icon)
    val summary : TextView = itemView.findViewById(R.id.summary)
    val temperature : TextView = itemView.findViewById(R.id.temperature)
    val apparentlyTemperature : TextView = itemView.findViewById(R.id.apparentTemperature)
}