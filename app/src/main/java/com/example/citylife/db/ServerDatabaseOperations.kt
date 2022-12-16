package com.example.citylife.db

import com.example.citylife.model.report.Report
import com.example.citylife.model.report.ServerReport
import com.mongodb.MongoClient
import org.bson.Document
import org.json.JSONObject

class ServerDatabaseOperations {

    //Indirizzo del database
    private val dbAddress = "10.0.2.2"
    //Porta per la connessione al database
    private val port = 27017
    //Nome del database
    private val dbName = "CityLife"
    //Nome della collezione
    private val serverCollection = "Server collection"

    /**
     * Ritorna la collezione relativa ai dati che vengono inseriti dal server nel database
     */
    fun getServerCollection() = MongoClient(dbAddress, port)
        .getDatabase(dbName).getCollection(serverCollection)

    /**
     * Compone un report generato dal server a partire da un documento
     */
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