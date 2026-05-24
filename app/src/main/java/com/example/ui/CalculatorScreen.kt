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
                color = CosmicTextSecondary,
                lineHeight = 24.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = result.ifEmpty { expression.ifEmpty { "0" } },
                fontSize = 56.sp,
                fontWeight = FontWeight.Light,
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.End,
                letterSpacing = (-2).sp,
                modifier = Modifier.fillMaxWidth()
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
        isPrimary -> CosmicPrimary
        isSecondary -> CosmicSecondary
        isTertiary -> CosmicTertiary
        else -> CosmicSurface
    }
    
    val textColor = when {
        isPrimary -> CosmicOnPrimary
        isSecondary -> CosmicOnSecondary
        isTertiary -> CosmicOnTertiary
        else -> androidx.compose.ui.graphics.Color.White
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
