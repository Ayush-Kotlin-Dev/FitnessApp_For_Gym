package ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary Colors
val Blue = Color(0xFF1E80F8)
val Red = Color(0xFFFF0000)

// Black & Gray Shades
val Black = Color(0xFF000000)
val DarkGray = Color(0xFF18191A)
val MediumGray = Color(0xFF999A9A)
val LightGray = Color(0xFF8A8A8D)
val Black54 = Color(0xFF373B3F)
val Black24 = Color(0xFF242526)

// White & Light Shades
val White = Color(0xFFFFFFFF)
val White87 = Color(0xFFE2E2E2)
val White36 = Color(0xFFE5E5E5)
val White76 = Color(0xFFF5F5F5)
val ShimmerLightGray = Color(0xFFF1F1F1)
val ShimmerMediumGray = Color(0xFFE3E3E3)
val ShimmerDarkGray = Color(0xFF1D1D1D)
val LightRed = Color(0xFFF57D88)
val LightGrayBackground = Color(0xFFF3F3F4)

// Faded Black for Background
val FadedBlack = Color(0xFF1A1A1A)

// Dark Color Scheme
internal val DarkColors = darkColorScheme(
    primary = Red,
    background = Black.copy(alpha = 0.6f),
    surface = DarkGray,
    error = Red,
    onPrimary = White,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
    onError = White
)
