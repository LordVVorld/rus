package com.example.snackbar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snackbar.AccompaniedApplication.Companion.api
import com.example.snackbar.AccompaniedApplication.Companion.db
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class WeatherViewModel : ViewModel() {
    private val weatherData = MutableLiveData<List<Weather>>()
    private val requestResult = SingleLiveEvent<Any>()
    init { loadWeatherData() }

    fun getWeatherData(): MutableLiveData<List<Weather>> {
        return weatherData
    }

    fun getRequestResult(): SingleLiveEvent<Any> {
        return requestResult
    }

    private fun loadWeatherData() = GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = api.getWeatherList()
            if (response.isSuccessful) {
                val listOfWeather = response.body()!!.toListOfWeather()
                //dbComparison(listOfWeather, db.weatherDao().getAll())

                weatherData.postValue(listOfWeather)
                requestResult.postValue("Request successful.")
            }
            else {
                requestResult.postValue("Request failed. Gain data from database.")
                weatherData.postValue(db.weatherDao().getAll())
            }

        }
        catch(exception: Exception) {
            requestResult.postValue("Request failed. Gain data from database.")
            weatherData.postValue(db.weatherDao().getAll())
        }
    }

    private suspend fun dbComparison (oldList: List<Weather>, newList: List<Weather>) {
        for (item in newList) {
            if (!oldList.contains(item)) {
                db.weatherDao().insert(item)
            }
        }
    }

    private fun DataWeather.toListOfWeather(): List<Weather> {
        val listOfWeather = mutableListOf<Weather>()
        for (item in this.list){
            listOfWeather += Weather(
                item.main.temp,
                item.dt_txt,
                item.weather?.get(0)!!.description
            )
        }
        return listOfWeather
    }
}