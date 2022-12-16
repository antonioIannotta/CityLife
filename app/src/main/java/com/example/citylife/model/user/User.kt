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
import java.time.LocalDateTime
import java.util.concurrent.Callable
import java.util.concurrent.Executors

/**
 * classe che rappresenta uno specifico utente.
 */
data class User(val username: String) {

    //Distanza selezionata dall'utente per la quale è interessato a ricevere segnalazioni
    private var distance: Float = 0.0f
    //Tipologie di segnalazione alle quali l'utente è interessato
    private var reportPreferences = mutableListOf<ReportType>()
    //Posizione dell'utente
    private var location = Location("")
    //Testo per la segnalazione
    private var textForReport: String = ""
    //Ultima segnalazione ricevuta
    var lastReceivedReport = ServerReport("", "", "", "", "")
    //Lista delle notifiche che sono di interesse per l'utente
    lateinit var notificationList: MutableList<Report>

    /**
     *Funzione che consente di modificare la distanza di interesse.
     */
    fun changeDistance(newDistance: Float) {
        distance = newDistance
    }

    /**
     *Funzione che ritorna la distanza
     */
    fun getDistance() =
        distance

    /**
     *Funzione che aggiunge una tipologia di segnalazione a quelle di interesse
     */
    fun addReportToPreferences(report: ReportType) =
        reportPreferences.add(report)

    /**
     *Funzione che ritorna le tipologie di segnalazioni a cui l'utente è effettivamente interessato
     */
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
    }

    /**
     * Funzione che ritorna la posizione dell'utente
     */
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
        DatabaseOperations().insertOrUpdateLocationAndDistance(this.username, mapOf(
            "Location" to strLatitude(getLocation()) + "-" + strLongitude(getLocation()),
            "Distance" to getDistance().toString()
        ))

    fun strLongitude(location: Location): String =
        Location.convert(location.longitude, Location.FORMAT_DEGREES)

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
        ClientDatabaseOperations().insertReport(newReport())

    /**
     * Funzione che si occupa di recuperare le notifiche di interesse per l'utente
     */
    fun receiveReport() {
        val workerPool = Executors.newSingleThreadExecutor()
        workerPool.submit(Callable {
            while (true) {
                val lastReportInDB = ServerDatabaseOperations().composeReportFromDocument(
                    ServerDatabaseOperations().getServerCollection().find().first())

                if (lastReceivedReport.equals(lastReportInDB)) {
                    continue
                } else {
                    if (lastReportInDB.listOfUsername.contains(this.username)) {
                        lastReceivedReport = lastReportInDB
                        notificationList.add(lastReceivedReport.toReport(this.username))
                    }
                }
            }
        })
    }







}