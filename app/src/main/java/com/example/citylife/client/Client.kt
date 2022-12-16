package com.example.citylife.client

import com.example.citylife.model.report.Report
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.json.JSONObject

class Client {

    /*val serverUrl = "127.0.0.1:8080/req"

    suspend fun sendReport(report: Report) {

        val client = HttpClient(CIO)

        val jsonReport = createJson(report)

        client.post {
            url(serverUrl)
            contentType(ContentType.Application.Json)
            setBody(jsonReport)
        }
    }

    suspend fun receiveReport(): Report {
        val client = HttpClient(CIO)
        val httpResponse = client.get(serverUrl)
        val jsonReport = httpResponse.body<JSONObject>()

        return composeReport(jsonReport)
    }



    private fun createJson(report: Report): JSONObject {
        val jsonReport = JSONObject()

        jsonReport.put("Type", report.type)
        jsonReport.put("Location", report.location)
        jsonReport.put("LocalDateTime", report.localDateTime)
        jsonReport.put("Username", report.username)
        jsonReport.put("Text", report.text)

        return jsonReport
    }

    private fun composeReport(jsonReport: JSONObject): Report {
        val type = jsonReport["Type"].toString()
        val location = jsonReport["Location"].toString()
        val localDateTime = jsonReport["LocalDateTime"].toString()
        val text = jsonReport["Text"].toString()
        val username = jsonReport["Username"].toString()

        return Report(type, location, localDateTime, text, username)
    }*/
}