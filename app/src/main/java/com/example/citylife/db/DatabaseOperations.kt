package com.example.citylife.db

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.conversions.Bson

class DatabaseOperations {

    val db_address = "10.0.2.2" //indirizzo del DB
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
     * Inserisce o aggiorna la posizione e la distanza di interesse all'interno della collezione Location
     */
    fun insertOrUpdateLocationAndDistance(username: String, locationAndDistance: Map<String, String>) {
        val filter = Filters.eq("Username", username)
        var updates = emptyList<Bson>().toMutableList()
        updates.add(Updates.set("Username", username))
        locationAndDistance.forEach {
            entry -> updates.add(Updates.set(entry.key, entry.value))
        }
        val options = UpdateOptions().upsert(true)


        getCollectionFromDatabase(locationCollection)
            .updateOne(filter, updates, options)

    }

}