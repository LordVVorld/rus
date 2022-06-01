package com.example.snackbar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snackbar.AppWithCompanion.Companion.api
import com.example.snackbar.AppWithCompanion.Companion.db
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class MyViewModel : ViewModel() {
    private val weatherData = MutableLiveData<List<Weather>>()
    private val response = SingleLiveEvent<Any>()
    init { loadWeatherData() }

    fun getWeatherData(): MutableLiveData<List<Weather>> {
        return weatherData
    }

    fun getRequestResult(): SingleLiveEvent<Any> {
        return response
    }

    private fun loadWeatherData() = GlobalScope.launch(Dispatchers.IO) {
        try {
            val result = api.getWeatherList()
            if (!result.isSuccessful) {
                response.postValue("Fail to connect to server. Data gained from database.")
                weatherData.postValue(db.weatherDao().getAll())
                return@launch
            }
            val fetchedList = result.body()!!.toWeatherList()
            actualize(fetchedList, db.weatherDao().getAll())

            weatherData.postValue(fetchedList)
            response.postValue("Successful connection. Data gained from server.")
        }
        catch(e: Exception) {
            response.postValue("Fail to connect to server. Data gained from database.")
            weatherData.postValue(db.weatherDao().getAll())
        }
    }

    private fun DataWeather.toWeatherList(): List<Weather> {
        var list = listOf<Weather>()
        this.list.forEach { weatherObject ->
            list = list + Weather(
                weatherObject.main.temp,
                weatherObject.dt_txt,
                weatherObject.weather?.get(0)!!.description
            )
        }
        return list
    }

    private suspend fun actualize(oldList: List<Weather>, newList: List<Weather>) {
        for (item in newList) {
            if (!oldList.contains(item)) {
                db.weatherDao().insertAll(item)
            }
        }
    }
}