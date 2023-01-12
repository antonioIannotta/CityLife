package com.example.citylife.signIn

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.http.models.UserDB
import com.example.citylife.httpHandler.HttpHandler
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.Base64

class SignIn(val userEmail: String, val userPassword: String) {

    val httpHandlerReference: HttpHandler = HttpHandler()

    /**
     *Funzione che effettua il login dell'utente
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun signIn(): User {

        val hashedPassword = Base64.getEncoder().encode(userPassword.toByteArray()).toString()

        val userDB: UserDB = httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/signInUser")
                contentType(ContentType.Application.Json)
                setBody(AccessInformation(userEmail, userPassword))
                //parameters.append("email", userEmail)
                //parameters.append("password", userPassword)

            }
        }.body()

        return User(userDB.username, userDB.distance.toFloat(),
            returnLocation(userDB.location), returnReportPreferences(userDB.reportPreference))

    }

    private fun returnLocation(userLocation: String): Location {
        if (userLocation == "") {
            return Location("")
        }
        val latitude = userLocation.split(" - ")[0]
        val longitude = userLocation.split(" - ")[1]
        val location = Location("")
        location.latitude = latitude.toDouble()
        location.longitude = longitude.toDouble()

        return location
    }

    private fun returnReportPreferences(reportPreferencesString: String): MutableList<ReportType> {
        val reportPreferencesList: MutableList<ReportType>

        if (reportPreferencesString == "[]") {
            reportPreferencesList = emptyList<ReportType>().toMutableList()
        } else {
            var reportPreferences = reportPreferencesString.drop(1).dropLast(1)
            reportPreferencesList = emptyList<ReportType>().toMutableList()
            reportPreferences.split(",").forEach { element ->
                reportPreferencesList.add(ReportType.valueOf(element.uppercase().trim()))
            }
        }
        return reportPreferencesList
    }
}
