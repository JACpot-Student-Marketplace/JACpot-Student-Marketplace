package com.example.multiplatformapplication

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// John Abbott College colours. Yellow lives in `tertiary` only: use it for AI labels
// and small highlights, never for buttons or large fills.
private val JacBlue = Color(0xFF00529E)
private val JacYellow = Color(0xFFF7E701)
private val Cream = Color(0xFFF3F0E7)
private val Ink = Color(0xFF1C1B18)

// shortcut: light theme only, add a darkColorScheme when dark mode is scheduled.
private val JACpotColors =
    lightColorScheme(
        primary = JacBlue,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFD3E3FF),
        onPrimaryContainer = Color(0xFF001C3A),
        secondary = Color(0xFF5E5B4F),
        onSecondary = Color.White,
        secondaryContainer = Color(0xFFE8E4D8),
        onSecondaryContainer = Ink,
        tertiary = JacYellow,
        onTertiary = Color(0xFF1F1C00),
        tertiaryContainer = Color(0xFFFFF9B0),
        onTertiaryContainer = Color(0xFF1F1C00),
        background = Cream,
        onBackground = Ink,
        surface = Color.White,
        onSurface = Ink,
        surfaceVariant = Color(0xFFE8E4D8),
        onSurfaceVariant = Color(0xFF4A473F),
        outline = Color(0xFF7C786C),
        outlineVariant = Color(0xFFCDC9BC),
        // Cards, dialogs and nav bars pick these by default: keep them white on the cream background.
        surfaceContainerLowest = Color.White,
        surfaceContainerLow = Color.White,
        surfaceContainer = Color.White,
        surfaceContainerHigh = Color(0xFFF7F5EF),
        surfaceContainerHighest = Color(0xFFE8E4D8),
    )

// Default font (Roboto on Android/desktop/web): only the hierarchy is tuned.
private val JACpotTypography =
    Typography(
        headlineMedium = TextStyle(fontSize = 28.sp, lineHeight = 36.sp, fontWeight = FontWeight.Bold), // screen titles
        titleLarge = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.Bold), // prices
        titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.SemiBold), // item titles
        bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp),
        labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium), // condition, course code, time
    )

private val JACpotShapes =
    Shapes(
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(12.dp), // cards, fields
        large = RoundedCornerShape(16.dp), // dialogs
    )

@Composable
fun JACpotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = JACpotColors,
        typography = JACpotTypography,
        shapes = JACpotShapes,
        content = content,
    )
}
