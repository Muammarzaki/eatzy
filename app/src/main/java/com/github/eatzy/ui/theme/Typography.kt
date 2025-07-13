package com.github.eatzy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.github.eatzy.R

val Inter18Font = FontFamily(
    Font(R.font.inter_18pt_regular, FontWeight.Normal),
    Font(R.font.inter_18pt_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.inter_18pt_thin, FontWeight.Thin),
    Font(R.font.inter_18pt_thinitalic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.inter_18pt_extralight, FontWeight.ExtraLight),
    Font(R.font.inter_18pt_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.inter_18pt_light, FontWeight.Light),
    Font(R.font.inter_18pt_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.inter_18pt_medium, FontWeight.Medium),
    Font(R.font.inter_18pt_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.inter_18pt_semibold, FontWeight.SemiBold),
    Font(R.font.inter_18pt_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.inter_18pt_bold, FontWeight.Bold),
    Font(R.font.inter_18pt_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.inter_18pt_extrabold, FontWeight.ExtraBold),
    Font(R.font.inter_18pt_extrabolditalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.inter_18pt_black, FontWeight.Black),
    Font(R.font.inter_18pt_blackitalic, FontWeight.Black, FontStyle.Italic),
)

val AppTypography = Typography(
    // Default text style
    bodyMedium = TextStyle(
        fontFamily = Inter18Font,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter18Font,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Inter18Font,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter18Font,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Inter18Font,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp
    ),
    displayLarge = TextStyle(
        fontFamily = Inter18Font,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp
    )
)

