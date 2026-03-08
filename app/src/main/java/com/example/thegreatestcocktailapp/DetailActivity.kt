package com.example.thegreatestcocktailapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.thegreatestcocktailapp.network.DrinkModel
import com.example.thegreatestcocktailapp.network.Drinks
import com.example.thegreatestcocktailapp.network.NetworkManager
import com.example.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import com.example.thegreatestcocktailapp.ui.theme.AppColors

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkId = intent.getStringExtra("EXTRA_DRINK_ID")
        val categoryName = intent.getStringExtra("EXTRA_CATEGORY_NAME")

        setContent {
            TheGreatestCocktailAppTheme {

                val view = LocalView.current
                val darkTheme = isSystemInDarkTheme()
                val bgColor = AppColors.Background.toArgb()

                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        window.statusBarColor = bgColor
                        window.navigationBarColor = bgColor
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
                    }
                }

                var selected by remember { mutableIntStateOf(1) }
                var showingDetail by remember { mutableStateOf(true) }
                var cocktailDetails by remember { mutableStateOf<DrinkModel?>(null) }
                var isLoading by remember { mutableStateOf(true) }

                LaunchedEffect(drinkId) {
                    if (drinkId != null) {
                        NetworkManager.api.getDrinkDetails(drinkId).enqueue(object : Callback<Drinks> {
                            override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                                if (response.isSuccessful) {
                                    cocktailDetails = response.body()?.drinks?.firstOrNull()
                                }
                                isLoading = false
                            }
                            override fun onFailure(call: Call<Drinks>, t: Throwable) { isLoading = false }
                        })
                    } else { isLoading = false }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize().background(AppColors.Background),
                    containerColor = Color.Transparent,
                    bottomBar = {
                        BottomNavigationBar(
                            selectedIndex = selected,
                            onItemSelected = { newIndex: Int ->
                                if (newIndex == 1) {
                                    showingDetail = false
                                }
                                selected = newIndex
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding())) {
                        when (selected) {
                            0 -> ALaUneScreen()
                            1 -> {
                                if (showingDetail) {
                                    if (isLoading) {
                                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            CircularProgressIndicator(color = AppColors.PrimaryText)
                                        }
                                    } else {
                                        cocktailDetails?.let { DetailCocktailScreen(cocktail = it, showBackButton = true, categoryName = categoryName, onBackClick = { finish() }) }
                                    }
                                } else CategoriesScreen()
                            }
                            2 -> FavoritesScreen()
                            3 -> SearchScreen()
                        }
                    }
                }
            }
        }
    }
}