package com.example.citylife.utils

import android.location.Location
import com.example.citylife.model.user.User

class UserSerialization {

    fun serialize(user: User): String {
        var userSerialized = ""

        userSerialized += user.username + "\n"
        userSerialized += locationToString(user) + "\n"
        userSerialized += user.distance.toString() + "\n"
        userSerialized += user.reportPreferences.toString()

        return userSerialized
    }

    fun locationToString(user: User) =
        user.strLatitude(user.location) + " - " + user.strLongitude(user.location)

}