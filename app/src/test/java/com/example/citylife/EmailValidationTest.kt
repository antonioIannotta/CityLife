package com.example.citylife

import org.junit.Test
import org.junit.Assert.*

class EmailValidationTest {

    //TODO: effettuare test con le regex

    @Test
    fun verifyCorrectEmail() {
        val email = "antonio.iannotta@gmail.com"
        assertTrue(!email.startsWith(Regex("[1-9]").toString()))
        assertTrue(email.contains("@gmail.com"))
    }

    fun verifyWrongEmail() {
        val email = "1antonio.iannotta@outlook.it"
        assertTrue(email.startsWith(Regex("[1-9]").toString()))
        assertTrue(!email.contains("@gmail.com"))
    }

}