package com.example.citylife.model.report

import android.location.Location
import java.time.LocalDateTime

data class Report(val type: ReportType, val location: Location,
                  val localDateTime: LocalDateTime, val text: String, val username: String) {}
