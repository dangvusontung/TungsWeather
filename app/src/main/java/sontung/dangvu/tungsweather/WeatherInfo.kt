package sontung.dangvu.tungsweather

import java.io.Serializable

class WeatherInfo(
    val day : String,
    val time : String,
    val summary : String,
    val icon : String,
    val temperature : Double,
    val apparentTemperature : Double


) : Serializable {
    override fun toString(): String {
        return "WeatherInfo(day='$day', time='$time', summary='$summary', icon='$icon', temperature=$temperature, apparentTemperature=$apparentTemperature)"
    }
}