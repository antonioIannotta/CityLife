package com.example.citylife.model.user

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.db.ClientDatabaseOperations
import com.example.citylife.db.DatabaseOperations
import com.example.citylife.db.ServerDatabaseOperations
import com.example.citylife.model.report.Report
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.report.ServerReport
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import java.time.LocalDateTime
import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 * classe che rappresenta uno specifico utente.
 */
data class User(val username: String, var distance: Float = 0.0f,
                var location: Location = Location(""),
                var reportPreferences: MutableList<ReportType> = emptyList<ReportType>().toMutableList()
) {

    //Testo per la segnalazione
    private var textForReport: String = ""
    //Ultima segnalazione ricevuta
    var lastReceivedReport = ServerReport("", "", "", "", "")
    //Lista delle notifiche che sono di interesse per l'utente
    var notificationList = emptyList<Report>().toMutableList()

    /**
     *Funzione che consente di modificare la distanza di interesse.
     */
    fun changeDistance(newDistance: Float) {
        distance = newDistance
        DatabaseOperations().getCollectionFromDatabase("User").updateOne(
            Filters.eq("Username", this.username),
            Updates.set("Distance", this.distance.toString())
        )
    }

    /**
     *Funzione che ritorna la distanza
     */
    @JvmName("getDistance1")
    fun getDistance() =
        distance

    /**
     *Funzione che aggiunge una tipologia di segnalazione a quelle di interesse
     */
    fun addReportToPreferences(report: ReportType) {

        reportPreferences.add(report)
        DatabaseOperations().getCollectionFromDatabase("User").updateOne(
            Filters.eq("Username", this.username),
            Updates.set("ReportPreferences", this.reportPreferences.toString())
        )
    }

    /**
     *Funzione che ritorna le tipologie di segnalazioni a cui l'utente è effettivamente interessato
     */
    @JvmName("getReportPreferences1")
    fun getReportPreferences() =
        reportPreferences

    /**
     *Funzione che seleziona una specifica tipologia di
     * segnalazione tra quelle a cui l'utente è interessato
     */
    fun getSpecificReportPreference(specificReportType: String = "") =
        getReportPreferences().filter {
                reportType -> reportType.name == specificReportType
        }.first()

    /**
     *Funzione che rimuove una tipologia di segnalazione
     * tra quelle a cui l'utente è effettivamente interessato
     */
    fun removeReportTypeFromPreferences(report: ReportType) =
        reportPreferences.remove(report)

    /**
     *Funzione che imposta la nuova posizione per l'utente
     */
    @JvmName("setLocation1")
    fun setLocation(location: Location)  {
        this.location = location
        val locationString = strLatitude(location) + " - " + strLongitude(location)
        DatabaseOperations().getCollectionFromDatabase("User").updateOne(
            Filters.eq("Username", this.username),
            Updates.set("Location", locationString)
        )
    }

    /**
     * Funzione che ritorna la posizione dell'utente
     */
    @JvmName("getLocation1")
    fun getLocation() =
        this.location

    /**
     * Funzione che imposta il testo da inserire all'interno della segnalazione
     */
    fun setTextForReport(newText: String) {
        this.textForReport = newText
    }

    /**
     * Funzione che ritorna il testo inserito all'interno della segnalazione
     */
    fun getTextForReport() =
        this.textForReport

    /**
     * Funzione che aggiorna la posizione dell'utente e la distanza
     * per la quale è interessato a ricevere le segnalazioni
     */
    fun updateLocationOnDB() =
        DatabaseOperations()
            .insertOrUpdateLocationAndDistance(this.username, mapOf(
                "Location" to strLatitude(getLocation()) + "-" + strLongitude(getLocation()),
                "Distance" to getDistance().toString()
            ))


    /**
     * Funzione che converte la longitudine in stringa
     */
    fun strLongitude(location: Location): String =
        Location.convert(location.longitude, Location.FORMAT_DEGREES)


    /**
     * Funzione che converte la latitudine in stringa
     */
    fun strLatitude(location: Location): String =
        Location.convert(location.latitude, Location.FORMAT_DEGREES)

    /**
     * Funzione factory per la creazione di una nuova segnalazione
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun newReport() =
        Report(getSpecificReportPreference().toString(),
            this.location.toString(),
            LocalDateTime.now().toString(),
            textForReport,
            this.username)

    /**
     * Funzione che invia una segnalazione al server.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendReport() =
        ClientDatabaseOperations()
            .insertReport(newReport())

    /**
     * Funzione che si occupa di recuperare le notifiche di interesse per l'utente
     */
    fun receiveReport() {
        val workerPool = Executors.newSingleThreadExecutor()
        workerPool.submit(Callable {
            while (true) {
                var lastReportInDB = ServerDatabaseOperations().composeReportFromDocument(
                    ServerDatabaseOperations().getServerCollection().find().first()
                )

                if (lastReceivedReport.equals(lastReportInDB)) {
                    continue
                } else {
                    if (lastReportInDB.listOfUsername.contains(this.username)) {
                        lastReceivedReport = lastReportInDB
                        if (reportPreferences.contains(ReportType.valueOf(lastReportInDB.type))) {
                            notificationList.add(lastReceivedReport.toReport(this.username))
                        }
                    }
                }
            }
        })
    }
}