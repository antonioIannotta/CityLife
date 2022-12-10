package com.example.citylife.model.user

import android.location.Location
import com.example.citylife.model.report.ReportType

data class User(val username: String) {

    private var distance: Float = 0.0f
    private var reportPreferences = mutableListOf<ReportType>()
    private var location = Location("")

    fun changeDistance(newDistance: Float) {
        distance = newDistance
    }

    fun getDistance() = distance

    fun addReportToPreferences(report: ReportType) = reportPreferences.add(report)

    fun getReportPreferences() = reportPreferences

    fun removeReportFromPreferences(report: ReportType) = reportPreferences.remove(report)

    @JvmName("setLocation1")
    fun setLocation(location: Location)  {
        this.location = location
    }

    fun getLocation() = this.location







}