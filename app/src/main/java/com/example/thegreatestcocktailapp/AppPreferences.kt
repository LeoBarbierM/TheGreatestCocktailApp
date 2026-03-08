package com.example.thegreatestcocktailapp

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.thegreatestcocktailapp.network.DrinkModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AppPreferences {
    private const val PREFS_NAME = "cocktail_prefs"
    private const val FAVORITES_KEY = "favorites_list"
    private const val GRID_MODE_KEY = "is_grid_mode"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    val favorites = mutableStateListOf<DrinkModel>()

    var isGridMode = mutableStateOf(false)

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        isGridMode.value = prefs.getBoolean(GRID_MODE_KEY, false)

        val favoritesJson = prefs.getString(FAVORITES_KEY, null)
        if (favoritesJson != null) {
            val type = object : TypeToken<List<DrinkModel>>() {}.type
            val savedFavorites: List<DrinkModel> = gson.fromJson(favoritesJson, type)
            favorites.clear()
            favorites.addAll(savedFavorites)
        }
    }


    fun toggleGridMode() {
        isGridMode.value = !isGridMode.value
        prefs.edit().putBoolean(GRID_MODE_KEY, isGridMode.value).apply()
    }

    fun isFavorite(drinkId: String?): Boolean {
        return favorites.any { it.id == drinkId }
    }

    fun toggleFavorite(cocktail: DrinkModel): Boolean {
        val existing = favorites.find { it.id == cocktail.id }
        val isAdded = if (existing != null) {
            favorites.remove(existing)
            false
        } else {
            favorites.add(cocktail)
            true
        }


        val json = gson.toJson(favorites)
        prefs.edit().putString(FAVORITES_KEY, json).apply()

        return isAdded
    }

    fun clearFavorites() {
        favorites.clear()
        prefs.edit().remove(FAVORITES_KEY).apply()
    }
}