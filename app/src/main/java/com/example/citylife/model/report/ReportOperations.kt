package com.example.citylife.model.report

import com.example.citylife.client.Client

class ReportOperations {

    /**
     * Invia una segnalazione al server
     */
    suspend fun sendReport(report: Report) = Client().sendReport(report)
    /**
     * Riceve eventuali segnalazioni dal server e si occupa di ricostruire i report che poi verranno
     * visualizzati dall'utente
     */
    suspend fun receiveReport(): Report = Client().receiveReport()
}