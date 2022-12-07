package com.example.citylife.signupVerification

import java.util.Date

data class SignUpVerification(val name: String, val surname: String, val dateOfBirth: Date,
                              val cityOfBirth: String, val countryOfBirth: String,
                              val fiscalCode: String, val email: String, val password: String) {

}
