package com.example.citylife.signUp

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.db.DatabaseOperations
import com.example.citylife.model.User
import com.example.citylife.signUp.fiscalCodeGenerator.FiscalCodeGeneration
import java.security.MessageDigest
import java.time.LocalDate

data class SignUp(val name: String, val surname: String, val dateOfBirth: LocalDate,
                  val cityOfBirth: String, val countryOfBirth: String,
                  val email: String, val password: String) {

    @RequiresApi(Build.VERSION_CODES.O)
    val fiscalCode = FiscalCodeGeneration(name, surname, dateOfBirth, countryOfBirth, cityOfBirth)
        .generateFiscalCode()

    private val username =  MessageDigest.getInstance("MD5")
        .digest((name + surname + email).toByteArray()).toString()

    @RequiresApi(Build.VERSION_CODES.O)
    val signUpMapOfValues = mapOf<String, String>(
        "Name" to name,
        "Surname" to surname,
        "Fiscal Code" to fiscalCode,
        "Password" to password
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun signUp(): User {
        DatabaseOperations()
            .insertUser("User", signUpMapOfValues, username)
        return User(username)
    }

    fun areEmailAndFiscalCodeUnique(email: String, fiscalCode: String): Boolean {
        return check(email) && check(fiscalCode)
    }

    fun check(value: String): Boolean {
        return DatabaseOperations().readAllDocuments("User").count {
            document -> document.values.contains(value)
        } == 0
    }
}
