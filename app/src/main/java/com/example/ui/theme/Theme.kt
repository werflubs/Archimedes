package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
      primary = CosmicPrimary,
      onPrimary = CosmicOnPrimary,
      secondary = CosmicSecondary,
      onSecondary = CosmicOnSecondary,
      tertiary = CosmicTertiary,
      onTertiary = CosmicOnTertiary,
      background = CosmicBackground,
      surface = CosmicSurface,
      surfaceVariant = CosmicSurfaceVariant,
      onBackground = CosmicText,
      onSurface = CosmicText,
      onSurfaceVariant = CosmicTextSecondary
  )

@Composable
fun MyApplicationTheme(
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    val context = LocalContext.current
    val dynamicScheme = dynamicDarkColorScheme(context)
    dynamicScheme.copy(
      background = CosmicBackground,
      surface = CosmicSurface,
      surfaceVariant = CosmicSurfaceVariant,
      onBackground = CosmicText,
      onSurface = CosmicText,
      onSurfaceVariant = CosmicTextSecondary
    )
  } else {
    DarkColorScheme
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
