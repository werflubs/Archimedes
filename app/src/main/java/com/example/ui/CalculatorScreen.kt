package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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

import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

@Composable
fun CalculatorScreen(viewModel: CalculatorViewModel) {
    val expression by viewModel.expression.collectAsState()
    val result by viewModel.result.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalculatorDisplay(
                expression = expression,
                result = result,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            
            CalculatorKeypad(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            CalculatorDisplay(
                expression = expression,
                result = result,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
    
            Spacer(modifier = Modifier.height(24.dp))
    
            CalculatorKeypad(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CalculatorDisplay(expression: String, result: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        AnimatedContent(
            targetState = expression,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 4 }).togetherWith(
                    fadeOut() + slideOutVertically { -it / 4 }
                )
            },
            label = "expressionAnim"
        ) { targetExpr ->
            Text(
                text = targetExpr,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 24.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        
        val displayText = result.ifEmpty { expression.ifEmpty { "0" } }
        val displayFontSize = when {
            displayText.length > 18 -> 22.sp
            displayText.length > 14 -> 28.sp
            displayText.length > 10 -> 36.sp
            else -> 46.sp
        }
        
        AnimatedContent(
            targetState = displayText,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 4 }).togetherWith(
                    fadeOut() + slideOutVertically { -it / 4 }
                )
            },
            label = "resultAnim"
        ) { targetText ->
            Text(
                text = targetText,
                fontSize = displayFontSize,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = (displayFontSize.value * 1.15f).sp,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )
        }
    }
}

@Composable
fun CalculatorKeypad(viewModel: CalculatorViewModel, modifier: Modifier = Modifier) {
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
        modifier = modifier
    ) {
        items(buttons) { btn ->
            CalculatorButton(
                text = btn,
                onClick = { viewModel.onAction(btn) }
            )
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
