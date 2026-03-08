package com.example.thegreatestcocktailapp.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    fun getRandomCocktail(): Call<Drinks>

    @GET("list.php?c=list")
    fun getCategories(): Call<Drinks>

    @GET("filter.php")
    fun getDrinksByCategory(@Query("c") category: String): Call<Drinks>

    @GET("lookup.php")
    fun getDrinkDetails(@Query("i") drinkId: String): Call<Drinks>

    // Dans ton fichier ApiService.kt
    @GET("search.php")
    fun searchDrinks(@Query("s") query: String): Call<Drinks>
}
