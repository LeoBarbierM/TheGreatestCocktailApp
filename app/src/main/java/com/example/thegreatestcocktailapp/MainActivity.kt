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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import com.example.thegreatestcocktailapp.ui.theme.AppColors

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreferences.init(applicationContext)
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

                var selected by remember { mutableIntStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize().background(AppColors.Background),
                    containerColor = Color.Transparent,
                    bottomBar = {
                        BottomNavigationBar(
                            selectedIndex = selected,
                            onItemSelected = { newIndex: Int -> selected = newIndex }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding())) {
                        when (selected) {
                            0 -> ALaUneScreen()
                            1 -> CategoriesScreen()
                            2 -> FavoritesScreen()
                            3 -> SearchScreen()
                        }
                    }
                }
            }
        }
    }
}