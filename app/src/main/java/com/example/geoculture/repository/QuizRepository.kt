package com.example.geoculture.repository

import com.example.geoculture.api.RetrofitInstance
import com.example.geoculture.data.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class QuizRepository {


    suspend fun get10RandomCountries(): List<Country> = withContext(Dispatchers.IO) {
        val allCountries = RetrofitInstance.api.getAllCountries()

        // Mélanger la liste et prendre 10
        val shuffled = allCountries.shuffled(Random(System.currentTimeMillis()))
        shuffled.take(10).map { apiCountry ->
            val frenchName = apiCountry.translations?.get("fra")?.common
                ?: apiCountry.name.common

            Country(
                name = frenchName,
                capital = null, // ou apiCountry.capital si tu veux plus tard
                flagUrl = apiCountry.flags.png
            )
        }
    }
}
