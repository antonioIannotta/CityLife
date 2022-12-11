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

    private val username =  MessageDigest.getInstance("MD5")
        .digest((name + surname + email).toByteArray()).toString()

    private val signUpMapOfValues = mapOf<String, String>(
        "Name" to name,
        "Surname" to surname,
        "Email" to email,
        "Password" to password
    )

    fun signUp(): User? {

        var user: User? = null

        if (isEmailUnique(email)) {
            FirebaseEmailOperations().sendEmail(email)
            if (FirebaseEmailOperations().completeSignUp(email) == "OK") {
                DatabaseOperations()
                    .insertUser("User", signUpMapOfValues, username)
                user = User(username)
            }
        }
        return user
    }

    fun isEmailUnique(email: String): Boolean {
        return check(email)
    }

    fun check(value: String): Boolean {
        return DatabaseOperations().readAllDocuments("User").count {
            document -> document.values.contains(value)
        } == 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkAge(): Boolean {
        return (LocalDate.now().year - this.dateOfBirth.year) >= 16
    }
}

//TODO: TEST + cifrare la password
