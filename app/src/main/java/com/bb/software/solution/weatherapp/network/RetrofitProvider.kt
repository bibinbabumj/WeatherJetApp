package com.bb.software.solution.weatherapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {

    private const val BASE_URL = "https://api.weatherapi.com/"

    //private const val BASE_URL = "https://www.googleapis.com/books/v1/"
    operator fun invoke(): NetworkApi =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(NetworkApi::class.java)
}