package com.example.citylife.signIn

@kotlinx.serialization.Serializable
data class AccessInformation(val userEmail: String, val userPassword: String) {}