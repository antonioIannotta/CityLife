package com.example.citylife.signIn

import android.location.Location
import com.example.citylife.http.models.UserDB
import com.example.citylife.httpHandler.HttpHandler
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

class SignIn(val userEmail: String, val userPassword: String) {

    val httpHandlerReference: HttpHandler = HttpHandler()

    /**
     *Funzione che effettua il login dell'utente
     */
    suspend fun signIn(): User {

        val userDB: UserDB = httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/signInUser")

                parameters.append("email", userEmail)
                parameters.append("password", userPassword)

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
        lateinit var reportPreferencesList: MutableList<ReportType>

        if (reportPreferencesString == "[]") {
            reportPreferencesList = emptyList<ReportType>().toMutableList()
        } else {
            var reportPreferences = reportPreferencesString.drop(1).dropLast(1)
            reportPreferencesList = emptyList<ReportType>().toMutableList()
            reportPreferences.split(",").forEach { element ->
                reportPreferencesList.add(ReportType.valueOf(element.toString().uppercase()))
            }
        }
        return reportPreferencesList
    }
}
