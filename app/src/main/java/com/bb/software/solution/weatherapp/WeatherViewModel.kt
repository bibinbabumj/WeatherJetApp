package com.bb.software.solution.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bb.software.solution.weatherapp.Constants.API_KEY
import com.bb.software.solution.weatherapp.model.WeatherModel
import com.bb.software.solution.weatherapp.network.NetworkResponse
import com.bb.software.solution.weatherapp.network.RetrofitProvider
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApi = RetrofitProvider.invoke()
    private val _weather = MutableLiveData<NetworkResponse<WeatherModel>>()
    public val weather: LiveData<NetworkResponse<WeatherModel>> = _weather


    fun getWeather(city: String) {
        _weather.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val weatherResult = weatherApi.mGetWeather(API_KEY, city)
                if (weatherResult.isSuccessful) {
                    weatherResult.body()?.let {
                        _weather.value = NetworkResponse.Success(it)
                    }

                } else {
                    _weather.value = NetworkResponse.Error(weatherResult.message())
                }
            } catch (e: Exception) {
                _weather.value = NetworkResponse.Error(e.message.toString())
            }


        }


    }
}