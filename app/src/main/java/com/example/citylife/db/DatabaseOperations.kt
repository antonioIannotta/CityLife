package com.example.citylife.db

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import org.bson.Document

class DatabaseOperations {

    val db_address = "127.0.0.1" //indirizzo del DB
    val port = 27017 //porta a cui è connesso il servizio
    val databaseName = "CityLife" //nome del database
    val userCollection = "User" //nome della collezione degli utenti all'interno del DB
    val locationCollection = "Location"


    /**
     * Ritorna la connessione al database
     */
    fun getDatabaseConnection(): MongoClient = MongoClient(db_address, port)

    /**
     * Ritorna la collezione dal DB passata come argomento
     */
    fun getCollectionFromDatabase(collectionName: String): MongoCollection<Document> =
        getDatabaseConnection().getDatabase(databaseName).getCollection(collectionName)

    /**
     * Consente di inserire un utente all'interno della collezione degli utenti!
     */
    fun insertUser(documentComponents: Map<String, String>, username: String) {
        val document = Document(username, documentComponents)
        getCollectionFromDatabase(userCollection).insertOne(document)
    }

    /**
     * Trova all'interno della collezione degli utenti l'utente con lo specifico username
     */
    fun readUserByUsername(username: String): Document? =
        getCollectionFromDatabase(userCollection).find().first {
                document -> document.keys.contains(username)
        }

    /**
     * Ritorna tutti i documenti presenti all'interno della collezione degli utenti
     */
    fun readAllUsers() = getCollectionFromDatabase(userCollection).find()

    /**
     * Inserisce la posizione e la distanza di interesse all'interno della collezione Location
     */
    fun insertLocationAndDistance(username: String, locationAndDistance: Map<String, String>) =
        getCollectionFromDatabase(locationCollection)
            .insertOne(Document(username, locationAndDistance))

}