package com.example.flocka.ui.components

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.flocka.R

val sansationFontFamily = FontFamily(
    Font(R.font.sansation_bold, FontWeight.Bold),
    Font(R.font.sansation_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.sansation_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.sansation_light, FontWeight.Light),
    Font(R.font.sansation_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.sansation_regular, FontWeight.Normal)
)

val alexandriaFontFamily = FontFamily(
    Font(R.font.alexandria_bold, FontWeight.Bold),
    Font(R.font.alexandria_light, FontWeight.Light),
    Font(R.font.alexandria_regular, FontWeight.Normal),
    Font(R.font.alexandria_semibold, FontWeight.SemiBold)
)
