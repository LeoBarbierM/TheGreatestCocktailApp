package com.example.thegreatestcocktailapp

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.thegreatestcocktailapp.network.DrinkModel
import com.example.thegreatestcocktailapp.network.Drinks
import com.example.thegreatestcocktailapp.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.thegreatestcocktailapp.ui.theme.AppColors

@Composable
fun SearchScreen() {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<DrinkModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val isGridMode by AppPreferences.isGridMode

    LaunchedEffect(searchQuery) {
        if (searchQuery.length >= 3) {
            isLoading = true
            NetworkManager.api.searchDrinks(searchQuery).enqueue(object : Callback<Drinks> {
                override fun onResponse(call: Call<Drinks>, response: Response<Drinks>) {
                    if (response.isSuccessful) searchResults = response.body()?.drinks ?: emptyList()
                    isLoading = false
                }
                override fun onFailure(call: Call<Drinks>, t: Throwable) { isLoading = false }
            })
        } else {
            searchResults = emptyList()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)),
            placeholder = { Text("Rechercher un cocktail...", color = AppColors.SecondaryText) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AppColors.PrimaryText) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, contentDescription = null, tint = AppColors.PrimaryText) }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = AppColors.CardSurface,
                unfocusedContainerColor = AppColors.CardSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = AppColors.PrimaryText,
                unfocusedTextColor = AppColors.PrimaryText
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AppColors.PrimaryText)
            }
        } else if (searchQuery.length >= 3 && searchResults.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Aucun résultat trouvé", color = AppColors.PrimaryText)
            }
        } else {
            LazyVerticalGrid(
                columns = if (isGridMode) GridCells.Fixed(2) else GridCells.Fixed(1),
                verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 120.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(searchResults) { drink ->
                    val onDrinkClick = {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra("EXTRA_DRINK_ID", drink.id)
                        context.startActivity(intent)
                    }
                    if (isGridMode) DrinkGridItemCard(drink = drink, onClick = onDrinkClick)
                    else DrinkListItemCard(drink = drink, onClick = onDrinkClick)
                }
            }
        }
    }
}
