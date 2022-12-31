package com.example.citylife.http.models

@kotlinx.serialization.Serializable

data class LocationDB(
    var username: String,
    var distance: String,
    var location: String
)
