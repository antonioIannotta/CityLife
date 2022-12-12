package com.example.citylife.model.user

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.db.DatabaseOperations
import com.example.citylife.model.report.Report
import com.example.citylife.model.report.ReportOperations
import com.example.citylife.model.report.ReportType
import java.time.LocalDateTime

data class User(val username: String) {

    private var distance: Float = 0.0f
    private var reportPreferences = mutableListOf<ReportType>()
    private var location = Location("")
    private var textForReport: String = ""

    fun changeDistance(newDistance: Float) {
        distance = newDistance
    }

    fun getDistance() = distance

    fun addReportToPreferences(report: ReportType) = reportPreferences.add(report)

    fun getReportPreferences() = reportPreferences

    fun getSpecificReportPreference(specificReportType: String = "") = getReportPreferences().filter {
        reportType -> reportType.name == specificReportType
    }.first()

    fun removeReportFromPreferences(report: ReportType) = reportPreferences.remove(report)

    @JvmName("setLocation1")
    fun setLocation(location: Location)  {
        this.location = location
    }

    fun getLocation() = this.location

    fun setTextForReport(newText: String) {
        this.textForReport = newText
    }

    fun getTextForReport() = this.textForReport

    fun updateLocationOnDB() =
        DatabaseOperations().insertLocation("Location", this.username, mapOf(
            "Location" to getLocation(),
            "Distance" to getDistance()
        ))

    @RequiresApi(Build.VERSION_CODES.O)
    fun newReport() =
        Report(getSpecificReportPreference(), this.location, LocalDateTime.now(), textForReport, this.username)

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendReport() = ReportOperations().sendReport(newReport())

    //TODO: implementare la ricezione delle notifiche da parte dell'utente e la memorizzazione nel DB






}