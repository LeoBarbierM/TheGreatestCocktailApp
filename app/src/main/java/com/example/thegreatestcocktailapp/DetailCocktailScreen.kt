package com.example.thegreatestcocktailapp

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.thegreatestcocktailapp.network.DrinkModel
import com.example.thegreatestcocktailapp.ui.theme.AppColors

data class IngredientDisplay(val name: String, val measure: String)

@Composable
fun DetailCocktailScreen(
    cocktail: DrinkModel,
    showBackButton: Boolean = false,
    categoryName: String? = null,
    onBackClick: () -> Unit = {},
    onRefresh: (() -> Unit)? = null
) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(AppPreferences.isFavorite(cocktail.id)) }

    val ingredientsList = listOfNotNull(
        if (!cocktail.ingredient1.isNullOrBlank()) IngredientDisplay(
            cocktail.ingredient1,
            cocktail.measure1 ?: ""
        ) else null,
        if (!cocktail.ingredient2.isNullOrBlank()) IngredientDisplay(
            cocktail.ingredient2,
            cocktail.measure2 ?: ""
        ) else null,
        if (!cocktail.ingredient3.isNullOrBlank()) IngredientDisplay(
            cocktail.ingredient3,
            cocktail.measure3 ?: ""
        ) else null,
        if (!cocktail.ingredient4.isNullOrBlank()) IngredientDisplay(
            cocktail.ingredient4,
            cocktail.measure4 ?: ""
        ) else null,
        if (!cocktail.ingredient5.isNullOrBlank()) IngredientDisplay(
            cocktail.ingredient5,
            cocktail.measure5 ?: ""
        ) else null,
        if (!cocktail.ingredient6.isNullOrBlank()) IngredientDisplay(
            cocktail.ingredient6,
            cocktail.measure6 ?: ""
        ) else null
    )

    val displayTitle = categoryName ?: cocktail.category ?: "Cocktail"

    Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Background) {

        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (showBackButton) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, "Retour", tint = AppColors.PrimaryText)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = displayTitle,
                        color = AppColors.PrimaryText,
                        fontSize = if (showBackButton) 24.sp else 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onRefresh != null) {
                        IconButton(onClick = { onRefresh() }) {
                            Icon(Icons.Default.Refresh, "Rafraîchir", tint = AppColors.PrimaryText)
                        }
                    }
                    IconButton(onClick = {
                        val isAdded = AppPreferences.toggleFavorite(cocktail)
                        isFavorite = isAdded
                        Toast.makeText(
                            context,
                            if (isAdded) "❤️ Ajouté aux favoris" else "💔 Retiré des favoris",
                            Toast.LENGTH_SHORT
                        ).show()
                    }) {
                        Icon(
                            if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            "Favoris",
                            tint = AppColors.PrimaryText
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                AsyncImage(
                    model = cocktail.imageURL,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(1.dp, AppColors.CardSurface, CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    cocktail.name ?: "",
                    color = AppColors.PrimaryText,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Row(modifier = Modifier.padding(16.dp)) {
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                cocktail.category ?: "Classic",
                                color = AppColors.PrimaryText
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LocalOffer,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = AppColors.PrimaryText
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(containerColor = AppColors.ChipBlue),
                        border = BorderStroke(0.dp, Color.Transparent)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    val isAlcoholic = cocktail.alcoholic == "Alcoholic"
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                cocktail.alcoholic ?: "Non-Alcoholic",
                                color = AppColors.PrimaryText
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.WineBar,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = AppColors.PrimaryText
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(containerColor = if (isAlcoholic) AppColors.ChipRed else AppColors.ChipBlue),
                        border = BorderStroke(0.dp, Color.Transparent)
                    )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.FormatListBulleted,
                                contentDescription = "Ingrédients",
                                tint = AppColors.PrimaryText,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Ingrédients",
                                color = AppColors.PrimaryText,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        ingredientsList.forEach { item ->
                            Row(
                                Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = AppColors.SecondaryText,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(item.name, color = AppColors.PrimaryText)
                                Spacer(Modifier.weight(1f))
                                Text(item.measure, color = AppColors.SecondaryText)
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 120.dp),
                    colors = CardDefaults.cardColors(containerColor = AppColors.CardSurface)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Description,
                                contentDescription = "Recette",
                                tint = AppColors.PrimaryText,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Recette",
                                color = AppColors.PrimaryText,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            cocktail.instructions ?: "",
                            color = AppColors.SecondaryText,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}