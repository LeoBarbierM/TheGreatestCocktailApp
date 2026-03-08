package com.example.thegreatestcocktailapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

object AppColors {
    val Background: Color
        @Composable get() = if (isSystemInDarkTheme()) Color.Black else Color(0xFFEFE8DE)

    val CardSurface: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1C1C1E) else Color(0xFFD6CEC3)

    val PrimaryText: Color
        @Composable get() = if (isSystemInDarkTheme()) Color.White else Color(0xFF1D1B20)

    val SecondaryText: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFFA0A0A5) else Color(0xFF49454F)

    val NavBarBackground: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1C1C1E) else Color(0xFFD6CEC3)

    val NavBarContent: Color
        @Composable get() = if (isSystemInDarkTheme()) Color.White else Color(0xFF1C1C1E)

    val ChipBlue: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF30405B) else Color(0xFFA5B4D4)

    val ChipRed: Color
        @Composable get() = if (isSystemInDarkTheme()) Color(0xFF5C2A2D) else Color(0xFFEAA2A6)

    val Danger: Color = Color(0xFFFF3B30)
}