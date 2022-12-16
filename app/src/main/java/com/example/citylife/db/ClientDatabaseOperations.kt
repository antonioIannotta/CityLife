package com.example.citylife.db

import com.example.citylife.model.report.Report
import com.mongodb.MongoClient
import org.bson.Document
import org.json.JSONObject

class ClientDatabaseOperations {

    private val dbAddress = "10.0.2.2"
    private val port = 27017
    private val dbName = "CityLife"
    private val clientCollection = "Client collection"

    private fun getClientCollection() = MongoClient(dbAddress, port)
        .getDatabase(dbName).getCollection(clientCollection)

    fun insertReport(report: Report) = getClientCollection().insertOne(createReportDocument(report))

    private fun createReportDocument(report: Report): Document = Document.parse(createJson(report).toString())

    private fun createJson(report: Report): JSONObject {
        val jsonReport = JSONObject()

        jsonReport.put("Type", report.type)
        jsonReport.put("Location", report.location)
        jsonReport.put("LocalDateTime", report.localDateTime)
        jsonReport.put("Username", report.username)
        jsonReport.put("Text", report.text)

        return jsonReport
    }
}