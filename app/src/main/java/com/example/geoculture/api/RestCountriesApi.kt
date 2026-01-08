package com.example.geoculture.api

import com.example.geoculture.data.Country
import retrofit2.http.GET

interface RestCountriesApi {
    @GET("all?fields=name,flags,translations")
    suspend fun getAllCountries(): List<ApiCountry>
}

// Pour mapper l’API JSON
data class ApiCountry(
    val name: Name,
    val flags: Flags,
    val translations: Map<String, Translation>
)
data class Translation(
    val official: String,
    val common: String
)
data class Name(val common: String)
data class Flags(val png: String)
