package com.example.citylife.signUp

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import com.example.citylife.http.models.UserDB
import com.example.citylife.http.models.LocationDB
import com.example.citylife.httpHandler.HttpHandler
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.network.*
import java.security.MessageDigest

data class SignUp(val name: String, val surname: String,
                  val email: String, val password: String) {

    /**
     *Funzione che crea lo username a partire da nome, cognome ed email
     */
    private val username =  MessageDigest.getInstance("MD5")
        .digest((name + surname + email).toByteArray()).toString()

    val httpHandlerReference: HttpHandler = HttpHandler()

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


    /* TODO: non viene inserito "reportPreference" nel db, dipende da chiamata app o da web service? */
    /**
     *Funzione che si occupa della registrazione e memorizzazione dei dati nel DB
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun signUp(): User {

        httpHandlerReference.getClient().post {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/insertUser")
            }
            contentType(ContentType.Application.Json)
            setBody(userDB)
        }

        httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/location/insertLocationAndDistance")
                parameters.append("username", locationDB.username)
                parameters.append("location", locationDB.location)
                parameters.append("distance", locationDB.distance)
            }
        }

        return User(username, 0.0f, Location(""),
            emptyList<ReportType>().toMutableList())
    }
}