package com.example.thegreatestcocktailapp
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil3.compose.AsyncImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.thegreatestcocktailapp.network.DrinkModel
import com.example.thegreatestcocktailapp.network.Drinks
import com.example.thegreatestcocktailapp.network.NetworkManager
import com.example.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme
import com.example.thegreatestcocktailapp.ui.theme.AppColors

class DrinksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryName = intent.getStringExtra("EXTRA_CATEGORY_NAME") ?: "Cocktail"

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
                var showingDrinksDetail by remember { mutableStateOf(true) }

                Scaffold(
                    modifier = Modifier.fillMaxSize().background(AppColors.Background),
                    containerColor = Color.Transparent,
                    bottomBar = {
                        BottomNavigationBar(
                            selectedIndex = selected,
                            onItemSelected = { newIndex: Int ->
                                if (newIndex == 1) showingDrinksDetail = false
                                selected = newIndex
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding())) {
                        when (selected) {
                            0 -> ALaUneScreen()
                            1 -> {
                                if (showingDrinksDetail) DrinksListContent(categoryName = categoryName)
                                else CategoriesScreen()
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

@Composable
fun DrinksListContent(categoryName: String) {
    val context = LocalContext.current
    var drinksList by remember { mutableStateOf<List<DrinkModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val isGridMode by AppPreferences.isGridMode

    LaunchedEffect(categoryName) {
        NetworkManager.api.getDrinksByCategory(categoryName).enqueue(object : Callback<Drinks> {
            override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                if (response.isSuccessful) drinksList = response.body()?.drinks ?: emptyList()
                isLoading = false
            }
            override fun onFailure(call: Call<Drinks>, t: Throwable) { isLoading = false }
        })
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = AppColors.PrimaryText)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = categoryName, color = AppColors.PrimaryText, fontWeight = FontWeight.Bold, fontSize = 26.sp)
            }

            IconButton(onClick = { AppPreferences.toggleGridMode() }) {
                Icon(if (isGridMode) Icons.Default.ViewList else Icons.Default.GridView, contentDescription = "Changer", tint = AppColors.PrimaryText)
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppColors.PrimaryText)
            }
        } else {
            LazyVerticalGrid(
                columns = if (isGridMode) GridCells.Fixed(2) else GridCells.Fixed(1),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(drinksList) { drink ->
                    val onDrinkClick = {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("EXTRA_DRINK_ID", drink.id)
                        intent.putExtra("EXTRA_CATEGORY_NAME", categoryName)
                        context.startActivity(intent)
                    }

                    if (isGridMode) DrinkGridItemCard(drink = drink, onClick = onDrinkClick)
                    else DrinkListItemCard(drink = drink, onClick = onDrinkClick)
                }
            }
        }
    }
}

@Composable
fun DrinkListItemCard(drink: DrinkModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(84.dp).clip(RoundedCornerShape(12.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                AsyncImage(model = drink.imageURL, contentDescription = drink.name, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = drink.name ?: "Inconnu", color = AppColors.PrimaryText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DrinkGridItemCard(drink: DrinkModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(0.85f).clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface)
    ) {
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))) {
                AsyncImage(model = drink.imageURL, contentDescription = drink.name, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
            Text(text = drink.name ?: "Inconnu", color = AppColors.PrimaryText, fontSize = 15.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
        }
    }
}