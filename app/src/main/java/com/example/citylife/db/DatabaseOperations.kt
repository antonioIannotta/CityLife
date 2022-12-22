package com.example.citylife.db

import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.conversions.Bson

class DatabaseOperations {

    //indirizzo del DB
    private val db_address = "10.0.2.2"
    //Porta per la connessione al database
    private val port = 27017
    //Nome del database
    private val databaseName = "CityLife"
    //Collezione degli utenti nel database
    private val userCollection = "User"
    //Collezione per l'aggiornamento della posizione nel database
    private val locationCollection = "Location"

    /**
     * Ritorna la collezione passata come argomento
     */
    fun getCollectionFromDatabase(collectionName: String): MongoCollection<Document> =
        MongoClient(db_address, port).getDatabase(databaseName).getCollection(collectionName)

    /**
     * Inserisce un utente nella collezione degli utenti
     */
    fun insertUser(documentComponents: Map<String, String>) =
        getCollectionFromDatabase(userCollection)
            .insertOne(Document(documentComponents))

    /**
     * Ritorna tutti i documenti presenti all'interno della collezione degli utenti
     */
    fun readAllUsers() =
        getCollectionFromDatabase(userCollection).find()

    /**
     * Inserisce o aggiorna la posizione e la distanza di interesse per un certo utente
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

    fun updateLocationInUserCollection(collectionName: String, username: String, location: String){

    }


}