package com.example.citylife.signUp.fiscalCodeGenerator

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate


class FiscalCodeGeneration(val name: String, val surname: String,
                                val dateOfBirth: LocalDate,
                                val countryOfBirth: String, val cityOfBirth: String) {

    val vowels = "AEIOU"

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateFiscalCode(): String =
        generalPortion(surname) + namePortion(name) + yearPortion(dateOfBirth) + monthPortion(dateOfBirth) +
                dayPortion(dateOfBirth) + codeByCountryOrCity(cityOfBirth, countryOfBirth) +
                finalCode()


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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthPortion(dateOfBirth: LocalDate): String {
        val monthLetters = listOf<String>("A", "B", "C", "D", "E", "H", "L", "M", "P", "R", "S", "T")
        return monthLetters[dateOfBirth.monthValue]
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dayPortion(dateOfBirth: LocalDate): String {
        return dateOfBirth.dayOfMonth.toString()
    }

    fun codeByCountryOrCity(cityOfBirth: String, countryOfBirth: String): String {
        var codeByCityOrCountry = ""

        if (countryOfBirth.uppercase() == "ITALY") {
            codeByCityOrCountry = computeItalianCityCode(cityOfBirth)
        } else {
            codeByCityOrCountry = computeForeignCityCode(countryOfBirth)
        }

        return codeByCityOrCountry
    }

    private fun computeForeignCityCode(countryOfBirth: String): String {
        //TODO
        return ""
    }

    private fun computeItalianCityCode(cityOfBirth: String): String {
        return ItalianCities().returnItalianCodes().filter { x -> x.startsWith(cityOfBirth) }.toString().trim().takeLast(3)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun finalCode(): String {
        val stringToComputeFinalCode: String =
            generalPortion(surname) + namePortion(name) + yearPortion(dateOfBirth) + monthPortion(dateOfBirth) +
                    dayPortion(dateOfBirth) + codeByCountryOrCity(cityOfBirth, countryOfBirth)


        val odd_position_value = compute_odd_value(stringToComputeFinalCode)
        val even_position_value = compute_even_value(stringToComputeFinalCode)

        val numberToObtainLetter = (odd_position_value + even_position_value) % 26

        return computeLastLetter(numberToObtainLetter)


    }

    private fun compute_odd_value(string: String): Int {
        var result = 0

        string.uppercase().withIndex().filter { x -> x.index % 2 != 0 }.map { x -> x.value.toString() }
            .toList().forEach { x ->
                if (x == "0" || x == "A") result += 1
                if (x == "1" || x == "B") result += 0
                if (x == "2" || x == "C") result += 5
                if (x == "3" || x == "D") result += 7
                if (x == "4" || x == "E") result += 9
                if (x == "5" || x == "F") result += 13
                if (x == "6" || x == "G") result += 15
                if (x == "7" || x == "H") result += 17
                if (x == "8" || x == "I") result += 19
                if (x == "9" || x == "J") result += 21
                if (x == "K") result += 2
                if (x == "L") result += 4
                if (x == "M") result += 18
                if (x == "N") result += 20
                if (x == "O") result += 11
                if (x == "P") result += 3
                if (x == "Q") result += 6
                if (x == "R") result += 8
                if( x == "S") result += 12
                if (x == "T") result += 14
                if (x == "U") result += 16
                if (x == "V") result += 10
                if (x == "W") result += 22
                if (x == "X") result += 25
                if (x == "Y") result += 24
                if (x == "Z") result += 23
            }

        return result

    }

    private fun compute_even_value(string: String): Int {
        var result = 0

        string.uppercase().withIndex().filter { x -> x.index % 2 == 0 }.map { x -> x.value.toString() }
            .toList().forEach{ x ->
                if (x.toInt() in (1..9)) result += x.toInt()
                if (x == "A") result += 0
                if (x == "B") result += 1
                if (x == "C") result += 2
                if (x == "D") result += 3
                if (x == "E") result += 4
                if (x == "F") result += 5
                if (x == "G") result += 6
                if (x == "H") result += 7
                if (x == "I") result += 8
                if (x == "J") result += 9
                if (x == "K") result += 10
                if (x == "L") result += 11
                if (x == "M") result += 12
                if (x == "N") result += 13
                if (x == "O") result += 14
                if (x == "P") result += 15
                if (x == "Q") result += 16
                if (x == "R") result += 17
                if (x == "S") result += 18
                if (x == "T") result += 19
                if (x == "U") result += 20
                if (x == "V") result += 21
                if (x == "W") result += 22
                if (x == "X") result += 23
                if (x == "Y") result += 24
                if (x == "Z") result += 25
            }

        return result
    }

    private fun computeLastLetter(x: Int): String {

        var result = ""

        if (x == 0) result = "A"
        if (x == 1) result = "B"
        if (x == 2) result = "C"
        if (x == 3) result = "D"
        if (x == 4) result = "E"
        if (x == 5) result = "F"
        if (x == 6) result = "G"
        if (x == 7) result = "H"
        if (x == 8) result = "I"
        if (x == 9) result = "J"
        if (x == 10) result = "K"
        if (x == 11) result = "L"
        if (x == 12) result = "M"
        if (x == 13) result = "N"
        if (x == 14) result = "O"
        if (x == 15) result = "P"
        if (x == 16) result = "Q"
        if (x == 17) result = "R"
        if (x == 18) result = "S"
        if (x == 19) result = "T"
        if (x == 20) result = "U"
        if (x == 21) result = "V"
        if (x == 22) result = "W"
        if (x == 23) result = "X"
        if (x == 24) result = "Y"
        if (x == 25) result = "Z"

        return result
    }
}
