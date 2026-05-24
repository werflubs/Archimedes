package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val expression by viewModel.expression.collectAsState()
    val result by viewModel.result.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = expression,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 24.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            val displayText = result.ifEmpty { expression.ifEmpty { "0" } }
            val displayFontSize = when {
                displayText.length > 18 -> 22.sp
                displayText.length > 14 -> 28.sp
                displayText.length > 10 -> 36.sp
                else -> 46.sp
            }
            
            Text(
                text = displayText,
                fontSize = displayFontSize,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = (displayFontSize.value * 1.15f).sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        val buttons = listOf(
            "C", "⌫", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "00", "0", ".", "="
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(buttons) { btn ->
                CalculatorButton(
                    text = btn,
                    onClick = { viewModel.onAction(btn) }
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, onClick: () -> Unit) {
    val isPrimary = text in listOf("÷", "×", "-", "+", "=")
    val isSecondary = text in listOf("⌫", "%")
    val isTertiary = text == "C"

    val bgColor = when {
        isPrimary -> MaterialTheme.colorScheme.primary
        isSecondary -> MaterialTheme.colorScheme.secondary
        isTertiary -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.surface
    }
    
    val textColor = when {
        isPrimary -> MaterialTheme.colorScheme.onPrimary
        isSecondary -> MaterialTheme.colorScheme.onSecondary
        isTertiary -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            color = textColor,
            fontWeight = if (isPrimary || isSecondary || isTertiary) FontWeight.Bold else FontWeight.Medium
        )
    }
}
