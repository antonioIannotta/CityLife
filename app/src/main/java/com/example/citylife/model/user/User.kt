package com.example.citylife.model.user

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.db.DatabaseOperations
import com.example.citylife.model.report.Report
import com.example.citylife.model.report.ReportOperations
import com.example.citylife.model.report.ReportType
import java.time.LocalDateTime

/**
 * classe che rappresenta uno specifico utente.
 */
data class User(val username: String) {

    private var distance: Float = 0.0f //distanza selezionata dall'utente per la quale è interessato a ricevere segnalazioni
    private var reportPreferences = mutableListOf<ReportType>() //tipologie di segnalazione alle quali l'utente è interessato
    private var location = Location("") //posizione dell'utente
    private var textForReport: String = "" //testo per la segnalazione

    /**
     * consente di modificare la distanza di interesse.
     */
    fun changeDistance(newDistance: Float) {
        distance = newDistance
    }

    /**
     * ritorna la distanza
     */
    fun getDistance() = distance

    /**
     * aggiunge una tipologia di segnalazione a quelle di interesse
     */
    fun addReportToPreferences(report: ReportType) = reportPreferences.add(report)

    /**
     * ritorna le tipologie di segnalazioni a cui l'utente è effettivamente interessato
     */
    fun getReportPreferences() = reportPreferences

    /**
     * seleziona una specifica tipologia di segnalazione tra quelle a cui l'utente è interessato
     */
    fun getSpecificReportPreference(specificReportType: String = "") = getReportPreferences().filter {
        reportType -> reportType.name == specificReportType
    }.first()

    /**
     * rimuove una tipologia di segnalazione tra quelle a cui l'utente è effettivamente interessato
     */
    fun removeReportTypeFromPreferences(report: ReportType) = reportPreferences.remove(report)

    /**
     * imposta la nuova posizione per l'utente
     */
    @JvmName("setLocation1")
    fun setLocation(location: Location)  {
        this.location = location
    }

    /**
     * ritorna la posizione dell'utente
     */
    fun getLocation() = this.location

    /**
     * imposta il testo da inserire all'interno della segnalazione
     */
    fun setTextForReport(newText: String) {
        this.textForReport = newText
    }

    /**
     * ritorna il testo inserito all'interno della segnalazione
     */
    fun getTextForReport() = this.textForReport

    /**
     * aggiorna la posizione dell'utente e la distanza per la quale è interessato a ricevere le
     * segnalazioni
     */
    fun updateLocationOnDB() =
        DatabaseOperations().insertLocationAndDistance(this.username, mapOf(
            "Location" to strLatitude(getLocation()) + "-" + strLongitude(getLocation()),
            "Distance" to getDistance().toString()
        ))

    fun strLongitude(location: Location): String =
        Location.convert(location.longitude, Location.FORMAT_DEGREES)

    fun strLatitude(location: Location): String =
        Location.convert(location.latitude, Location.FORMAT_DEGREES)

    /**
     * metodo factory per la creazione di una nuova segnalazione
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun newReport() =
        Report(getSpecificReportPreference(), this.location, LocalDateTime.now(), textForReport, this.username)

    /**
     * metodo che invia una segnalazione al server.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendReport() = ReportOperations().sendReport(newReport())

    //TODO: implementare la ricezione delle notifiche da parte dell'utente e la memorizzazione delle notifiche nel DB






}