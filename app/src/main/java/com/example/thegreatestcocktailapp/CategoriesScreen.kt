package com.example.thegreatestcocktailapp

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.thegreatestcocktailapp.network.Drinks
import com.example.thegreatestcocktailapp.network.NetworkManager
import com.example.thegreatestcocktailapp.ui.theme.AppColors

private val emojiMap = mapOf("Ordinary Drink" to "🍹", "Cocktail" to "🍸", "Shake" to "🧋", "Other / Unknown" to "❓", "Cocoa" to "🍫", "Shot" to "🥃", "Coffee / Tea" to "☕", "Homemade Liqueur" to "🍶", "Punch / Party Drink" to "🥣", "Beer" to "🍺", "Soft Drink" to "🧃")
@Composable
fun CategoriesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val isGridMode by AppPreferences.isGridMode
    var categoriesList by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        NetworkManager.api.getCategories().enqueue(object : Callback<Drinks> {
            override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                if (response.isSuccessful) categoriesList = response.body()?.drinks?.mapNotNull { it.category } ?: emptyList()
                isLoading = false
            }
            override fun onFailure(call: Call<Drinks>, t: Throwable) { isLoading = false }
        })
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Categories", color = AppColors.PrimaryText, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { AppPreferences.toggleGridMode() }) {
                Icon(if (isGridMode) Icons.Default.ViewList else Icons.Default.GridView, "Change view", tint = AppColors.PrimaryText)
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppColors.PrimaryText)
            }
        } else {
            LazyVerticalGrid(
                columns = if (isGridMode) GridCells.Fixed(2) else GridCells.Fixed(1),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categoriesList) { categoryName ->
                    val emoji = emojiMap[categoryName] ?: "🍷"
                    val onCategoryClick = {
                        val intent = Intent(context, DrinksActivity::class.java)
                        intent.putExtra("EXTRA_CATEGORY_NAME", categoryName)
                        context.startActivity(intent)
                    }
                    if (isGridMode) CategoryGridItem(categoryName, emoji, onCategoryClick)
                    else CategoryListItem(categoryName, emoji, onCategoryClick)
                }
            }
        }
    }
}

@Composable
fun CategoryListItem(categoryName: String, emoji: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface)
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = categoryName, color = AppColors.PrimaryText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CategoryGridItem(categoryName: String, emoji: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(16.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                Text(text = emoji, fontSize = 32.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = categoryName, color = AppColors.PrimaryText, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}