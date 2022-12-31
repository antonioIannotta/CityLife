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
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.network.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.security.MessageDigest

data class SignUp(val name: String, val surname: String,
                  val email: String, val password: String) {

    /**
     *Funzione che crea lo username a partire da nome, cognome ed email
     */
    private val username =  MessageDigest.getInstance("MD5")
        .digest((name + surname + email).toByteArray()).toString()

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
    var userDB = UserDB(
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

        client.get {
            url {
                protocol = URLProtocol.HTTP
                host = "10.0.2.2"
                port = 5000
                path("/users/insertUser")
                parameters.append("name", userDB.name)
                parameters.append("surname", userDB.surname)
                parameters.append("username", userDB.username)
                parameters.append("email", userDB.email)
                parameters.append("password", userDB.password)
                parameters.append("distance", userDB.distance)
                parameters.append("location", userDB.location)
                parameters.append("reportPreference", userDB.reportPreference)

            }
        }

        client.get {
            url {
                protocol = URLProtocol.HTTP
                host = "10.0.2.2:5000"
                path("/location/insertLocationAndDistance")
                parameters.append("username", locationDB.username)
                parameters.append("location", locationDB.location)
                parameters.append("distance", locationDB.distance)
            }
        }

        println("insert user finito")

        return User(username, 0.0f, Location(""),
            emptyList<ReportType>().toMutableList())
    }
}