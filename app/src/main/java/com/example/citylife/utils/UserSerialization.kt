package com.example.citylife.utils

import android.location.Location
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User

class UserSerialization {

    fun serialize(user: User): String {
        var userSerialized = ""

        userSerialized += user.username + "\n"
        userSerialized += user.distance.toString() + "\n"
        userSerialized += locationToString(user) + "\n"
        userSerialized += user.reportPreferences.toString()

        return userSerialized
    }

    fun locationToString(user: User) =
        user.strLatitude(user.location) + " - " + user.strLongitude(user.location)

    fun deserialize(userSerialized: String): User {

        val username = userSerialized.split("\n")[0]
        val distance = userSerialized.split("\n")[1].toFloat()
        val location = composeLocation(userSerialized.split("\n")[2])
        val reportPreferences = composeReportPreferences(userSerialized.split("\n")[3])

        return User(username, distance, location, reportPreferences)
    }

    private fun composeLocation(locationString: String): Location {
        val latitude = locationString.split(" - ")[0].toDouble()
        val longitude = locationString.split(" - ")[1].toDouble()

        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude

        return location
    }

    private fun composeReportPreferences(reportPreferencesString: String): MutableList<ReportType> {
        var reportPreferencesList = emptyList<ReportType>().toMutableList()

        if (reportPreferencesString == "[]") {
            reportPreferencesList = emptyList<ReportType>().toMutableList()
        } else {
            var reportPreferences = reportPreferencesString.drop(1)
            reportPreferences = reportPreferences.dropLast(1)
            reportPreferences.split(",").forEach {
                element -> reportPreferencesList.add(ReportType.valueOf(element.uppercase().trim()))
            }
        }
        return reportPreferencesList
    }
}