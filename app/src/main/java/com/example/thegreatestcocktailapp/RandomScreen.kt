package com.example.thegreatestcocktailapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.thegreatestcocktailapp.network.DrinkModel
import com.example.thegreatestcocktailapp.network.Drinks
import com.example.thegreatestcocktailapp.network.NetworkManager
import com.example.thegreatestcocktailapp.ui.theme.AppColors

@Composable
fun RandomScreen() {
    var randomCocktail by remember { mutableStateOf<DrinkModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var refreshTrigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(refreshTrigger) {
        isLoading = true
        NetworkManager.api.getRandomCocktail().enqueue(object : Callback<Drinks> {
            override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                if (response.isSuccessful && response.body() != null) {
                    randomCocktail = response.body()?.drinks?.firstOrNull()
                }
                isLoading = false
            }
            override fun onFailure(call: Call<Drinks>, t: Throwable) {
                isLoading = false
            }
        })
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = AppColors.PrimaryText)
        }
    } else {
        randomCocktail?.let { cocktail ->
            DetailCocktailScreen(
                cocktail = cocktail,
                showBackButton = false,
                onRefresh = { refreshTrigger++ }
            )
        }
    }
}