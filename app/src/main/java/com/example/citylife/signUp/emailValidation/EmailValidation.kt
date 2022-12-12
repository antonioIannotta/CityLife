package com.example.citylife.signUp.emailValidation

import androidx.core.text.isDigitsOnly

class EmailValidation {

    /**
     * effettua una varifica della validità della mail inserita
     */
    fun validateEmail(email: String): Boolean {
        return email.endsWith("@gmail.com") &&
                !email.take(1).isDigitsOnly()
    }
}