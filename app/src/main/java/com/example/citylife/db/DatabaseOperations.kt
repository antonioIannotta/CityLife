package com.example.citylife.db

import android.location.Location
import com.mongodb.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import org.bson.Document

class DatabaseOperations {

    val db_address = "127.0.0.1"
    val port = 27017
    val databaseName = "CityLife"


    fun getDatabaseConnection(): MongoClient = MongoClient(db_address, port)

    fun getCollectionFromDatabase(collectionName: String): MongoCollection<Document> =
        getDatabaseConnection().getDatabase(databaseName).getCollection(collectionName)

    fun insertUser(collectionName: String, documentComponents: Map<String, String>, username: String) {
        val document = Document(username, documentComponents)
        getCollectionFromDatabase(collectionName).insertOne(document)
    }
    
    fun readDocument(key: String, collectionName: String): Document? =
        getCollectionFromDatabase(collectionName).find().first { Document().keys.contains(key) }

    fun readAllDocuments(collectionName: String) = getCollectionFromDatabase(collectionName).find()

    fun insertLocation(collectionName: String, username: String, locationAndDistance: Map<String, Any>) =
        getCollectionFromDatabase(collectionName)
            .insertOne(Document(username, locationAndDistance))

}