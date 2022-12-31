package com.example.citylife.signIn

import android.location.Location
import com.example.citylife.http.models.UserDB
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

class SignIn(val userEmail: String, val userPassword: String) {

    val httpClient = HttpClient(CIO)

    /**
     *Funzione che effettua il login dell'utente
     */
    suspend fun signIn(): User {

        val userDB = httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = "10.0.2.2:5000"
                path("/users")
                parameters.append("email", userEmail)
                parameters.append("password", userPassword)
            }
        }.body<UserDB>()

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
            var reportPreferences = reportPreferencesString.drop(1)
            reportPreferences = reportPreferencesString.dropLast(1)
            reportPreferencesList = emptyList<ReportType>().toMutableList()
            reportPreferences.split(",").forEach { element ->
                reportPreferencesList.add(ReportType.valueOf(element.toString().uppercase()))
            }
        }
        return reportPreferencesList
    }
}
