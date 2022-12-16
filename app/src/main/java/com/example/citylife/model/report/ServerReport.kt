package com.example.citylife.model.report

data class ServerReport(val type: String, val location: String,
                  val localDateTime: String, val text: String, val listOfUsername: String) {

    fun equals(serverReport: ServerReport): Boolean {
        return this.type == serverReport.type &&
                this.location == serverReport.location &&
                this.localDateTime == serverReport.localDateTime &&
                this.text == serverReport.text &&
                this.listOfUsername == serverReport.listOfUsername
    }

    fun toReport(username: String): Report {
        return Report(this.type, this.location, this.localDateTime, this.text, username)
    }
}