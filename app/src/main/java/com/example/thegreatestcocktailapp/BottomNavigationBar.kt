package com.example.thegreatestcocktailapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thegreatestcocktailapp.ui.theme.AppColors

@Composable
fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.95f).height(72.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(40.dp))
                    .background(AppColors.NavBarBackground),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavItem(icon = Icons.Default.AutoAwesome, label = "À la une", isSelected = selectedIndex == 0, onClick = { onItemSelected(0) })
                NavItem(icon = Icons.Default.FormatListBulleted, label = "Catégories", isSelected = selectedIndex == 1, onClick = { onItemSelected(1) })
                NavItem(icon = Icons.Default.Favorite, label = "Favoris", isSelected = selectedIndex == 2, onClick = { onItemSelected(2) })
            }

            Spacer(modifier = Modifier.width(12.dp))


            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(if (selectedIndex == 3) AppColors.NavBarBackground.copy(alpha = 0.6f) else AppColors.NavBarBackground)
                    .clickable { onItemSelected(3) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Search, contentDescription = "Recherche", tint = AppColors.NavBarContent, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
fun RowScope.NavItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (isSelected) AppColors.NavBarContent.copy(alpha = 0.15f) else Color.Transparent
    val contentColor = if (isSelected) AppColors.NavBarContent else AppColors.NavBarContent.copy(alpha = 0.5f)

    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .padding(4.dp)
            .clip(RoundedCornerShape(36.dp))
            .background(backgroundColor)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, color = contentColor, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}