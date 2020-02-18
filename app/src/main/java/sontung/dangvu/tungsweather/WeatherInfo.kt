package sontung.dangvu.tungsweather

class WeatherInfo(
    val day : String,
    val time : String,
    val summary : String,
    val icon : String,
    val temperature : Double,
    val apparentTemperature : Double


) {
    override fun toString(): String {
        return "WeatherInfo(day='$day', time='$time', summary='$summary', icon='$icon', temperature=$temperature, apparentTemperature=$apparentTemperature)"
    }
}