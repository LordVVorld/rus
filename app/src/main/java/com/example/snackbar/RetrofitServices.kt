package com.example.snackbar

import retrofit2.Response
import retrofit2.http.GET

interface RetrofitServices {
    @GET("forecast?q=Shklov&appid=9bd98d0696500560ef3206451b125490&units=metric&lang=ru")
    suspend fun getWeatherList(): Response<DataWeather>
}