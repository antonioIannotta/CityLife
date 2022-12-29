package com.example.citylife.http.models

data class UserDB(
    var name: String,
    var surname: String,
    var username: String,
    var email: String,
    val password: String,
    val distance: String,
    val location: String,
    val reportPreference: String
)
