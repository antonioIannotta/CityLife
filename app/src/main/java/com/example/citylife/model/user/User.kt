package com.example.citylife.model.user

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.httpHandler.HttpHandler
import com.example.citylife.model.report.ClientReportDB
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.report.ServerReportDB
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.time.LocalDateTime

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
    var lastReceivedReport = ServerReportDB("", "", "", "", "")
    //Lista delle notifiche che sono di interesse per l'utente
    var notificationList = emptyList<ClientReportDB>().toMutableList()
    val httpHandlerReference: HttpHandler = HttpHandler()

    /**
     *Funzione che consente di modificare la distanza di interesse.
     */
    suspend fun changeDistance(newDistance: Float) {
        distance = newDistance
        httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/updateDistance")
                parameters.append("username", username)
                parameters.append("distance", distance.toString())
            }
        }
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
    suspend fun addReportToPreferences(report: ReportType) {

        reportPreferences.add(report)

        httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/updateReportPreference")
                parameters.append("username", username)
                parameters.append("reportPreference", reportPreferences.toString())
            }
        }
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
    suspend fun removeReportTypeFromPreferences(report: ReportType) {
        reportPreferences.remove(report)

        httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/updateReportPreference")
                parameters.append("username", username)
                parameters.append("reportPreference", reportPreferences.toString())
            }
        }
    }


    /**
     *Funzione che imposta la nuova posizione per l'utente
     */
    @JvmName("setLocation1")
    suspend fun setLocation(location: Location)  {
        this.location = location
        val locationString = strLatitude(location) + " - " + strLongitude(location)

        httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/updateLocation")
                parameters.append("username", username)
                parameters.append("location", locationString)
            }
        }
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
    suspend fun updateLocationAndDistanceOnDB() {

        val locationString = strLatitude(location) + " - " + strLongitude(location)

        httpHandlerReference.getClient().get {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/location/updateLocationAndDistance")
                parameters.append("username", username)
                parameters.append("location", locationString)
                parameters.append("distance", distance.toString())
            }
        }
    }


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
        ClientReportDB(getSpecificReportPreference().toString(),
            this.location.toString(),
            LocalDateTime.now().toString(),
            textForReport,
            this.username)

    /**
     * Funzione che invia una segnalazione al server.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendReport() {

        httpHandlerReference.getClient().post {
            url {
                protocol = URLProtocol.HTTP
                host = httpHandlerReference.getHost()
                port = httpHandlerReference.getPort()
                path("/users/insertReport")
            }
            contentType(ContentType.Application.Json)
            setBody(newReport())
        }
    }

    /**
     * Funzione che si occupa di recuperare le notifiche di interesse per l'utente
     */
    suspend fun receiveReport() {
        while (true) {
            var lastReportInDB = httpHandlerReference.getClient().get {
                url {
                    protocol = URLProtocol.HTTP
                    host = httpHandlerReference.getHost()
                    port = httpHandlerReference.getPort()
                    path("/users/lastReport")
                }
            }.body<ServerReportDB>()

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
    }
}

