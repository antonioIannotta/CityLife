package com.example.citylife.signUp

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import com.example.citylife.http.models.UserDB
import com.example.citylife.http.models.LocationDB
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.security.MessageDigest

data class SignUp(val name: String, val surname: String,
                  val email: String, val password: String) {

    /**
     *Funzione che crea lo username a partire da nome, cognome ed email
     */
    private val username =  MessageDigest.getInstance("MD5")
        .digest((name + surname + email).toByteArray()).toString()

    val client = HttpClient(CIO)
    val userDB = UserDB(
        name,
        surname,
        username,
        email,
        password,
        0.0f.toString(),
        "",
        "[]"
    )
    val locationDB = LocationDB(
        username,
        0.0f.toString(),
        ""
    )

    /**
     *Funzioen che si occupa della registrazione e memorizzazione dei dati nel DB
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun signUp(): User {

        val httpRequestBuilder = HttpRequestBuilder()
        httpRequestBuilder.method = HttpMethod.Post
        val url = URLBuilder()
        url.host = "10.0.2.2"
        url.port = 5000
        url.path("/users/insertUser")
        httpRequestBuilder.url(url.build())
        httpRequestBuilder.setBody(userDB)

        client.post(httpRequestBuilder)

        httpRequestBuilder.url("127.0.0.1:5000/location/insertLocation")
        httpRequestBuilder.setBody(locationDB)

        client.post(httpRequestBuilder)

        return User(username, 0.0f, Location(""),
            emptyList<ReportType>().toMutableList())
    }
}