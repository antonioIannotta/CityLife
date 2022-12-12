package com.example.citylife.signUp

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.db.DatabaseOperations
import com.example.citylife.model.user.User
import com.example.citylife.signUp.firebase.FirebaseEmailOperations
import java.security.MessageDigest
import java.time.LocalDate

data class SignUp(val name: String, val surname: String, val dateOfBirth: LocalDate,
                  val email: String, val password: String) {

    /**
     * crea lo username a partire da nome, cognome ed email
     */
    private val username =  MessageDigest.getInstance("MD5")
        .digest((name + surname + email).toByteArray()).toString()

    /**
     * mappa dei valori che vengono memorizzati all'interno della collezione User
     */
    private val signUpMapOfValues = mapOf<String, String>(
        "Name" to name,
        "Surname" to surname,
        "Email" to email,
        "Password" to password
    )

    /**
     * metodo per la registrazione e memorizzazione dei dati nel DB
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun signUp(): User? {

        var user: User? = null

        if (isEmailUnique(email) && checkAge()) {
            FirebaseEmailOperations().sendEmail(email)
            if (FirebaseEmailOperations().completeSignUp(email) == "OK") {
                DatabaseOperations()
                    .insertUser( signUpMapOfValues, username)
                user = User(username)
            }
        }
        return user
    }

    /**
     * verifica che la mail inserita non sia già stata utilizzata
     */
    fun isEmailUnique(email: String): Boolean {
        return check(email)
    }

    /**
     * effettua la verificad ella presenza all'interno della collezione di un valore passato come
     * parametro
     */
    fun check(value: String): Boolean {
        return DatabaseOperations().readAllUsers().count {
            document -> document.values.contains(value)
        } == 0
    }

    /**
     * verifica l'età
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAge(): Boolean {
        return (LocalDate.now().year - this.dateOfBirth.year) >= 16
    }
}

//TODO: TEST + cifrare la password
