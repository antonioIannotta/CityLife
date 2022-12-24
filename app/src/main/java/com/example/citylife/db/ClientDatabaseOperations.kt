package com.example.citylife.db

import com.example.citylife.model.report.Report
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.bson.Document
import org.json.JSONObject

/**
 * Classe che gestisce le operazioni sulla collezione del DB in cui i client eseguono le operazioni
 * per inviare i report
 */
class ClientDatabaseOperations {

    //Indirizzo del database
    private val dbAddress =
        "mongodb+srv://admin:Antonio-26@sctm.p6dkpwo.mongodb.net/?retryWrites=true/"
    //Nome del database
    private val dbName = "CityLife"
    //Nome della collezione
    private val clientCollection = "Client collection"


    /**
     *Funzione che ritorna la collezione
     */
    private fun getClientCollection() = MongoClient(MongoClientURI(dbAddress))
        .getDatabase(dbName).getCollection(clientCollection)

    /**
     * Funzione che inserisce un report all'interno della collezione
     */
    fun insertReport(report: Report) =
        getClientCollection().insertOne(createReportDocument(report))

    /**
     * Funzione che crea un documento a partire da un report
     */
    private fun createReportDocument(report: Report): Document =
        Document.parse(createJson(report).toString())

    /**
     * Funzione che ritorna un JSONObject a partire da un report
     */
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