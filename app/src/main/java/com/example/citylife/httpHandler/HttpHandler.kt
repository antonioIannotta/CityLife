package com.example.citylife.httpHandler

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class HttpHandler() {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val host = "10.0.2.2"
    private val port = 5000

    fun getClient(): HttpClient = client
    fun getHost(): String = host
    fun getPort(): Int = port

}