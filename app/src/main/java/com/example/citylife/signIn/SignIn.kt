package com.example.citylife.signIn

import com.example.citylife.db.DatabaseOperations
import com.example.citylife.model.User

class SignIn(val email: String, val password: String) {

    fun signIn(): User {
        var retrievedUsername = ""
        if (checkEmailExists(email) && checkPasswordWithEmail(email, password)) {
            retrievedUsername = retrieveUsername(email, password)
        }
        return User(retrievedUsername)
    }

    private fun checkPasswordWithEmail(email: String, password: String): Boolean {
        return DatabaseOperations().readAllDocuments("User")
            .count { document -> document.contains(email) && document.contains(password)} == 1
    }

    private fun checkEmailExists(email: String): Boolean =
        DatabaseOperations().readAllDocuments("User")
            .count { document -> document.values.contains(email) } == 1

    private fun retrieveUsername(email: String, password: String): String =
        DatabaseOperations().readAllDocuments("User")
            .filter {
                    document -> document.values.contains(email) && document.values.contains(password)
            }.first().keys.first()
}

//TODO: effettuare il recupero dei documenti per verificare se effettivamente presente quella mail