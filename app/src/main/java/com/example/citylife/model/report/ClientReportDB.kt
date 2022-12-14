package com.example.citylife.model.report

/**
 * Classe che si occupa di comporre una segnalazione, composta da:
 * tipologia di segnalazione,
 * posizione dell'utente che la invia,
 * data e ora dell'invio della segnalazione,
 * testo,
 * username dell'utente che la invia
 */
@kotlinx.serialization.Serializable
data class ClientReportDB(val type: String, val location: String,
                          val localDateTime: String, val text: String, val username: String) {}
