package com.example.citylife.signIn

import com.example.citylife.db.DatabaseOperations
import com.example.citylife.model.user.User

class SignIn(val email: String, val password: String) {

    /**
     *Funzione che effettua il login dell'utente
     */
    fun signIn(): User {
        var retrievedUsername = ""
        if (checkEmailExists(email) && checkPasswordWithEmail(email, password)) {
            retrievedUsername = retrieveUsername(email)
        }

        return User(retrievedUsername)
    }

    /**
     *Funzione che verifica che la password inserita sia effettivamente quella associata all'utente
     */
    private fun checkPasswordWithEmail(email: String, password: String): Boolean {
        return DatabaseOperations().readAllUsers().count { 
            document -> document.entries.toString().contains(email) && 
                document.entries.toString().contains(password)
        } == 1
    }

    /**
     *Funzione che verifica che l'email inserita esista
     */
    private fun checkEmailExists(email: String): Boolean =
        DatabaseOperations().readAllUsers()
            .count { document -> document.entries.toString().contains(email) } == 1

    /**
     *Funzione che recupera lo username con una certa email
     */
    private fun retrieveUsername(email: String): String =
        DatabaseOperations().readAllUsers()
            .filter {
                    document -> document.entries.toString().contains(email)
            }.first().keys.last()
}
