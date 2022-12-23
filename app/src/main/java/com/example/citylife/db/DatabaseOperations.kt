package com.example.citylife.db

import android.location.Location
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import org.bson.Document
import org.bson.conversions.Bson

class DatabaseOperations {

    //indirizzo del DB
    private val db_address =
        "mongodb+srv://admin:admin@sctm.p6dkpwo.mongodb.net/?retryWrites=true&w=majority"
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
        MongoClient(MongoClientURI(db_address)).getDatabase(databaseName).getCollection(collectionName)

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

    fun retrieveUser(username: String): User {
        val userDocument = getCollectionFromDatabase(userCollection)
            .find().filter(Filters.eq("Username", username)).first()
        return composeUser(userDocument)
    }

    private fun composeUser(userDocument: Document): User {
        val username = userDocument["Username"].toString()
        val distance = userDocument["Distance"].toString().toFloat()
        val location = returnLocation(userDocument["Location"].toString())
        val reportPreferences = returnReportPreferences(userDocument["ReportPreference"].toString())

        return User(username, distance, location, reportPreferences)
    }

    private fun returnReportPreferences(reportPreferencesString: String): MutableList<ReportType> {
        lateinit var reportPreferencesList: MutableList<ReportType>

        if (reportPreferencesString == "[]") {
            reportPreferencesList = emptyList<ReportType>().toMutableList()
        }else {
            var reportPreferences = reportPreferencesString.drop(1)
            reportPreferences = reportPreferencesString.dropLast(1)
            reportPreferencesList = emptyList<ReportType>().toMutableList()
            reportPreferences.split(",").forEach {
                    element -> reportPreferencesList.add(ReportType.valueOf(element.toString().uppercase()))
            }
        }
        return reportPreferencesList
    }

    private fun returnLocation(userLocation: String): Location {
        if (userLocation == "") {
            return Location("")
        }
        val latitude = userLocation.split(" - ")[0]
        val longitude = userLocation.split(" - ")[1]
        val location = Location("")
        location.latitude = latitude.toDouble()
        location.longitude = longitude.toDouble()

        return location
    }
}