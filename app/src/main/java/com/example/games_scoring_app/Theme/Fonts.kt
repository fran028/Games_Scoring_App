package com.example.games_scoring_app.Theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.games_scoring_app.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("League Gothic")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider,
        weight = FontWeight.Normal

    )
)

val LeagueGothic = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.league_gothic, FontWeight.Normal)
)

val RobotoCondensed = FontFamily(
    androidx.compose.ui.text.font.Font(R.font.robotocondense, FontWeight.Normal)
)