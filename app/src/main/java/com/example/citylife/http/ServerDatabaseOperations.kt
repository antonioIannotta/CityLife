package com.example.citylife.http

import com.example.citylife.model.report.ServerReport
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.bson.Document
import org.json.JSONObject

class ServerDatabaseOperations {

    //Indirizzo del database
    private val dbAddress =
        "mongodb+srv://admin:Antonio-26@sctm.p6dkpwo.mongodb.net/?retryWrites=true/"
    //Nome del database
    private val dbName = "CityLife"
    //Nome della collezione
    private val serverCollection = "Server collection"

    /**
     * Ritorna la collezione relativa ai dati che vengono inseriti dal server nel database
     */
    fun getServerCollection() =
        MongoClient(MongoClientURI(dbAddress))
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