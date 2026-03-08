package com.example.thegreatestcocktailapp

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thegreatestcocktailapp.ui.theme.AppColors

private val emojiMap = mapOf("Ordinary Drink" to "🍹", "Cocktail" to "🍸", "Shake" to "🧋", "Other / Unknown" to "❓", "Cocoa" to "🍫", "Shot" to "🥃", "Coffee / Tea" to "☕", "Homemade Liqueur" to "🍶", "Punch / Party Drink" to "🥣", "Beer" to "🍺", "Soft Drink" to "🧃")

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val isGridMode by AppPreferences.isGridMode
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val favoriteCocktails = AppPreferences.favorites
    var showClearDialog by remember { mutableStateOf(false) }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = AppColors.Background,
            titleContentColor = AppColors.PrimaryText,
            textContentColor = AppColors.SecondaryText,
            title = { Text("Vider les favoris", fontWeight = FontWeight.Bold) },
            text = { Text("Êtes-vous sûr de vouloir supprimer tous vos cocktails favoris ? Cette action est irréversible.") },
            confirmButton = {
                TextButton(onClick = {
                    AppPreferences.clearFavorites()
                    showClearDialog = false
                    selectedCategory = null
                }) { Text("Oui, vider", color = AppColors.Danger, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Annuler", color = AppColors.PrimaryText) }
            }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedCategory != null) {
                    IconButton(onClick = { selectedCategory = null }) { Icon(Icons.Default.ArrowBack, "Retour", tint = AppColors.PrimaryText) }
                }
                if (favoriteCocktails.isNotEmpty()) {
                    IconButton(onClick = { showClearDialog = true }) { Icon(Icons.Default.DeleteSweep, "Vider", tint = AppColors.Danger) }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = selectedCategory ?: "Mes Favoris", color = AppColors.PrimaryText, fontSize = if (selectedCategory == null) 28.sp else 24.sp, fontWeight = FontWeight.Bold)
            }
            IconButton(onClick = { AppPreferences.toggleGridMode() }) {
                Icon(if (isGridMode) Icons.Default.ViewList else Icons.Default.GridView, "Changer", tint = AppColors.PrimaryText)
            }
        }

        if (favoriteCocktails.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucun cocktail en favori \uD83D\uDC94", color = AppColors.PrimaryText, fontSize = 16.sp)
            }
        } else if (selectedCategory == null) {
            val categories = favoriteCocktails.mapNotNull { it.category }.distinct().sorted()
            LazyVerticalGrid(
                columns = if (isGridMode) GridCells.Fixed(2) else GridCells.Fixed(1),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { categoryName ->
                    val emoji = emojiMap[categoryName] ?: "🍷"
                    val onCategoryClick = { selectedCategory = categoryName }
                    if (isGridMode) CategoryGridItem(categoryName, emoji, onCategoryClick)
                    else CategoryListItem(categoryName, emoji, onCategoryClick)
                }
            }
        } else {
            val drinksInCategory = favoriteCocktails.filter { it.category == selectedCategory }.sortedBy { it.name }
            LazyVerticalGrid(
                columns = if (isGridMode) GridCells.Fixed(2) else GridCells.Fixed(1),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(drinksInCategory) { drink ->
                    val onDrinkClick = {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("EXTRA_DRINK_ID", drink.id)
                        intent.putExtra("EXTRA_CATEGORY_NAME", selectedCategory) // 🌟 Envoi de la catégorie
                        context.startActivity(intent)
                    }
                    if (isGridMode) DrinkGridItemCard(drink, onDrinkClick)
                    else DrinkListItemCard(drink, onDrinkClick)
                }
            }
        }
    }
}