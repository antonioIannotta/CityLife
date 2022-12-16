package com.example.citylife.db

import com.example.citylife.model.report.Report
import com.example.citylife.model.report.ServerReport
import com.mongodb.MongoClient
import org.bson.Document
import org.json.JSONObject

class ServerDatabaseOperations {

    private val dbAddress = "10.0.2.2"
    private val port = 27017
    private val dbName = "CityLife"
    private val serverCollection = "Server collection"

    fun getServerCollection() = MongoClient(dbAddress, port)
        .getDatabase(dbName).getCollection(serverCollection)

    fun composeReportFromDocument(document: Document): ServerReport {


        val jsonDocument = JSONObject().getJSONObject(document.toJson())
        val type = jsonDocument["Type"].toString()
        val location = jsonDocument["Location"].toString()
        val localDateTime = jsonDocument["LocalDateTime"].toString()
        val text = jsonDocument["Text"].toString()
        val listOfUsername = jsonDocument["ListOfUsername"].toString()

        return ServerReport(type, location, localDateTime, text, listOfUsername)
    }
}