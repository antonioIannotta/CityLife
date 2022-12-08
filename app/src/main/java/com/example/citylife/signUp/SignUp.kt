package com.example.citylife.signUp

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.citylife.signUp.fiscalCodeGenerator.FiscalCodeGeneration
import java.time.LocalDate

data class SignUp(val name: String, val surname: String, val dateOfBirth: LocalDate,
                  val cityOfBirth: String, val countryOfBirth: String,
                  val email: String, val password: String) {

    @RequiresApi(Build.VERSION_CODES.O)
    val fiscalCode = FiscalCodeGeneration(name, surname, dateOfBirth, countryOfBirth, cityOfBirth)
        .generateFiscalCode()

    //TODO
    //send email, password and fiscalCode to DB
}
