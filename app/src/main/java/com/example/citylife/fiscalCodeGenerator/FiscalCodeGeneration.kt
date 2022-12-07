package com.example.citylife.fiscalCodeGenerator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate


class FiscalCodeGeneration {

    val vowels = "AEIOU"

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateFiscalCode(name: String, surname: String, dateOfBirth: LocalDate, countryOfBirth: String, cityOfBirth: String): String =
        generalPortion(surname) + namePortion(name) + yearPortion(dateOfBirth) + ""


    private fun generalPortion(surname: String): String {
        var surnamePortion = ""
        if (consonantsOrVowelsNumber(surname, "consonants") >= 3) {
            surnamePortion += firstConsonantsOrVowels(surname, 3, "consonants")
        } else if(consonantsOrVowelsNumber(surname, "consonants") + consonantsOrVowelsNumber(surname, "vowels") >= 3){
            surnamePortion += firstConsonantsOrVowels(surname, consonantsOrVowelsNumber(surname, "consonants"), "consonants")
            surnamePortion += firstConsonantsOrVowels(surname, 3 - surnamePortion.length, "vowels")
        }else {
            surnamePortion += firstConsonantsOrVowels(surname, consonantsOrVowelsNumber(surname, "consonants"), "consonants")
            surnamePortion += firstConsonantsOrVowels(surname, consonantsOrVowelsNumber(surname, "vowels"), "vowels")
            surnamePortion += completeSurnameOrNamePortion(surnamePortion.length)
        }

        return surnamePortion
    }

    private fun namePortion(name: String): String {
        var namePortion = ""

        if (consonantsOrVowelsNumber(name, "consonants") >= 4) {
            namePortion = firstSecondAndFourthConsonantsInName(name)
        } else {
            namePortion = generalPortion(name)
        }

        return namePortion
    }

    private fun completeSurnameOrNamePortion(length: Int): String {
        return "X".repeat(3 - length)
    }

    private fun firstConsonantsOrVowels(string: String, number: Int, type: String): String {
        var firstConsonantsOrVowels = ""

        when (type) {
            "consonants" -> firstConsonantsOrVowels = string.uppercase().filter { x -> x !in vowels }.take(number)
            "vowels" -> firstConsonantsOrVowels = string.uppercase().filter { x -> x in vowels }.take(number)
        }

        return firstConsonantsOrVowels
    }

    private fun consonantsOrVowelsNumber(string: String, type: String): Int {
        var consonantsOrVowelsNumber = 0

        when (type) {
            "consonants" -> consonantsOrVowelsNumber = string.uppercase().filter { x -> x !in vowels }.length
            "vowels" -> consonantsOrVowelsNumber = string.uppercase().filter { x -> x in vowels }.length
        }

        return consonantsOrVowelsNumber
    }

    private fun firstSecondAndFourthConsonantsInName(name: String): String {
        var firstSecondAndFourthConsonantsInName = ""

        listOf<Int>(1, 2, 4).forEach {
            number -> firstSecondAndFourthConsonantsInName += name.uppercase().filter { x -> x !in vowels }[number]
        }

        return firstSecondAndFourthConsonantsInName
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun yearPortion(dateOfBirth: LocalDate): String {
        return dateOfBirth.year.toString().takeLast(2)
    }




}
