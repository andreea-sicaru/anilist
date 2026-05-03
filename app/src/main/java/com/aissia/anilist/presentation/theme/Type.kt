package com.aissia.anilist.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aissia.anilist.R

/**
 * Fonts
 */
val Merriweather = FontFamily(
    Font(R.font.merriweather_regular, FontWeight.Normal),
)

val Mullish = FontFamily(Font(R.font.mulish_regular, FontWeight.Normal))

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Mullish,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),

    titleMedium = TextStyle(
        fontFamily = Merriweather,
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
    ),

    titleSmall = TextStyle(
        fontFamily = Mullish,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    ),

    labelSmall = TextStyle(
        fontFamily = Mullish,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),

    labelMedium = TextStyle(
        fontFamily = Mullish,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */

)

