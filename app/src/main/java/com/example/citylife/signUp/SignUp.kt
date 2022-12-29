package com.example.citylife.signUp

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.http.DatabaseOperations
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import com.example.citylife.http.models.UserDB
import com.example.citylife.http.models.LocationDB
import io.ktor.client.*
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
    suspend fun signUp(): User? {

        lateinit var user: User

        if (isEmailUnique(email)) {
            val httpRequestBuilder = HttpRequestBuilder()
            httpRequestBuilder.method = HttpMethod.Post
            httpRequestBuilder.url("127.0.0.1/users/insertUser")
            httpRequestBuilder.setBody(userDB)

            client.post(httpRequestBuilder)

            httpRequestBuilder.url("127.0.0.1/location/insertLocation")
            httpRequestBuilder.setBody(locationDB)

            client.post(httpRequestBuilder)

            user = User(username, 0.0f, Location(""), emptyList<ReportType>().toMutableList())
        }
        return user
    }


    //TODO: EFFETTUARE TUTTI I CONTROLLI
    /**
     *Funzione che verifica che la mail inserita non sia già stata utilizzata
     */
    private fun isEmailUnique(email: String): Boolean {
        return check(email)
    }

    /**
     *Funzione che effettua la verificad ella presenza all'interno
     * della collezione di un valore passato come parametro
     */
    private fun check(value: String): Boolean {
        return DatabaseOperations().readAllUsers().count {
                document -> document.entries.toString().contains(value)
        } == 0
    }
}