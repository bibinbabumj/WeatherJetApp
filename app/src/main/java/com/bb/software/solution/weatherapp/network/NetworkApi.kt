package com.bb.software.solution.weatherapp.network

import com.bb.software.solution.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("v1/current.json")
    suspend fun mGetWeather(
        @Query("key") key: String,
        @Query("q") q: String
    ): Response<WeatherModel>
}