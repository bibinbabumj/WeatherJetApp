package com.bb.software.solution.weatherapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bb.software.solution.weatherapp.model.WeatherModel
import com.bb.software.solution.weatherapp.network.NetworkResponse

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val context = LocalContext.current
    val weather = viewModel.weather.observeAsState()
    val keyBordController = LocalSoftwareKeyboardController.current



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f), value = city,
                label = { Text("Search for any location") },
                onValueChange = {
                    city = it
                }, singleLine = true
            )

            IconButton(onClick = {
                if (!mCheckEmpty(city)) {
                    keyBordController?.hide()
                    viewModel.getWeather(city)
                } else {
                    Toast.makeText(context, "Please enter city name", Toast.LENGTH_SHORT).show()
                }

            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search city"
                )
            }

        }

        when (val result = weather.value) {
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }

            NetworkResponse.Loading -> {
                CircularProgressIndicator()

            }

            is NetworkResponse.Success -> {
                WeatherCard(weather = result.data)

            }

            null -> {

            }
        }


    }


}

@Composable
fun WeatherCard(weather: WeatherModel) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = weather.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = weather.location.country, fontSize = 20.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = weather.current.temp_c + "°C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center

        )
        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = "https:${weather.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "condition image",
            modifier = Modifier.size(150.dp)
        )
        Text(
            text = weather.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray,

            )

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherCardData(weather.current.humidity+"%","Humidity")
                    WeatherCardData(weather.current.wind_kph+" kph","Wind Speed")


                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherCardData( weather.current.uv,"UV")
                    WeatherCardData(weather.current.precip_mm+" mm","Participation")


                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherCardData( weather.location.localtime.split(" ")[1],"Local Time")
                    WeatherCardData(weather.location.localtime.split(" ")[0],"Local Date")


                }
            }

        }
    }

}

@Composable
fun WeatherCardData(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = key , fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }

}


fun mCheckEmpty(city: String): Boolean {
    return city.isEmpty()
}
