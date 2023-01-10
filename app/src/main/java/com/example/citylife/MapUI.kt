package com.example.citylife

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.citylife.http.models.UserDB
import com.example.citylife.httpHandler.HttpHandler
import com.example.citylife.model.report.ClientReportDB
import com.example.citylife.model.user.User
import com.example.citylife.utils.UserSerialization
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.google.maps.android.heatmaps.HeatmapTileProvider
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun String.substringBefore(delimiter: Char, missingDelimiterValue: String = this): String {
    val index = indexOf(delimiter)
    return if (index == -1) missingDelimiterValue else substring(0, index)
}

fun String.substringAfter(delimiter: Char, missingDelimiterValue: String = this): String {
    val index = indexOf(delimiter)
    return if (index == -1) missingDelimiterValue else substring(index + 1, length)
}

val httpHandler = HttpHandler()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MapUI(serializedUser: String, context: Context) {

    val user: User = UserSerialization().deserialize(serializedUser)
    val userLocation = LatLng(user.location.latitude, user.location.longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 19f)
    }

    var notificationList: MutableList<ClientReportDB> by remember {
        mutableStateOf(user.notificationList)
    }

    var properties by remember {
        mutableStateOf(MapProperties(
            mapType = MapType.NORMAL,
            isMyLocationEnabled = true,
            isTrafficEnabled = true)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 60.dp)
    ) {
        Row(
        ) {
            /*HeatMap(
                locationsList =
                    listOf(
                        LatLng(37.782551, -122.435147),
                        LatLng(37.782745, -122.435715)
                    ),
                context = context,
            )*/
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = properties,
                cameraPositionState = cameraPositionState,
            ) {
                runBlocking {
                    notificationList = user.receiveReport()
                }

                notificationList.forEach() { notification ->
                    val location = LatLng(
                        notification.location.substringBefore(" -", notification.location).toDouble(),
                        notification.location.substringAfter("- ", notification.location).toDouble()
                    )
                    val timeStamp:String = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).dropLast(1)
                    val dateTime = parse(notification.localDateTime)
                    val dateTimeNow = parse(timeStamp)

                    val reportPostingTime = when (dateTime.until(dateTimeNow, ChronoUnit.HOURS).toInt()) {
                        0 -> "${dateTime.until(dateTimeNow, ChronoUnit.MINUTES)}min"
                        in 1..23 -> "${dateTime.until(dateTimeNow, ChronoUnit.HOURS)}h"
                        else -> "${dateTime.until(dateTimeNow, ChronoUnit.DAYS)}d"
                    }

                    Marker (
                        state = MarkerState(position = location),
                        title = notification.type,
                        snippet = "$reportPostingTime - ${notification.text}"
                    )
                }

               var userList = emptyList<UserDB>().toMutableList()

                runBlocking {
                    userList = httpHandler.getClient().get {
                        url {
                            protocol = URLProtocol.HTTP
                            host = httpHandler.getHost()
                            port = httpHandler.getPort()
                            path("/users")
                        }
                    }.body()
                }

            }
        }
    }
}

/*
@Composable
fun HeatMap(locationsList: List<LatLng>?, context: Context) {

    var mapView = MapView(context)
    val googleMap = mapView.getMapAsync {


        //var properties = mapProperties
        //var cameraPositionState = userLocation

        val heatmapTileProvider = HeatmapTileProvider.Builder()
            .data(locationsList)
            .build()

        val tileOverlay = it.addTileOverlay(
            TileOverlayOptions()
                .tileProvider(heatmapTileProvider)
                .visible(true)
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .then(Modifier.padding(16.dp))
    ) {

        AndroidView(
            factory = {
                fragment
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
*/