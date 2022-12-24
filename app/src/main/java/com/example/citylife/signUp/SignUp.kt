package com.example.citylife.signUp

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.db.DatabaseOperations
import com.example.citylife.model.report.ReportType
import com.example.citylife.model.user.User
import java.security.MessageDigest
import java.time.LocalDate

data class SignUp(val name: String, val surname: String,
                  val email: String, val password: String) {

    /**
     *Funzione che crea lo username a partire da nome, cognome ed email
     */
    private val username =  MessageDigest.getInstance("MD5")
        .digest((name + surname + email).toByteArray()).toString()

    /**
     *Funzione che mappa dei valori che vengono memorizzati all'interno della collezione User
     */
    private val signUpMapOfValues = mapOf<String, String>(
        "Name" to name,
        "Username" to username,
        "Distance" to 0f.toString(),
        "Location" to "",
        "Surname" to surname,
        "Email" to email,
        "Password" to password,
        "ReportPreference" to "[]"
    )

    /**
     *Funzioen che si occupa della registrazione e memorizzazione dei dati nel DB
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun signUp(): User? {

        var user: User? = null

        if (isEmailUnique(email)) {
            //FirebaseEmailOperations().sendEmail(email)
            //if (FirebaseEmailOperations().completeSignUp(email) == "OK") {
                DatabaseOperations()
                    .insertUser(signUpMapOfValues)
                user = User(username, 0.0f, Location(""), emptyList<ReportType>().toMutableList())
            }
        return user
    }

    /**
     *Funzione che verifica che la mail inserita non sia già stata utilizzata
     */
    fun isEmailUnique(email: String): Boolean {
        return check(email)
    }

    /**
     *Funzione che effettua la verificad ella presenza all'interno
     * della collezione di un valore passato come parametro
     */
    fun check(value: String): Boolean {
        return DatabaseOperations().readAllUsers().count {
                document -> document.entries.toString().contains(value)
        } == 0
    }
}