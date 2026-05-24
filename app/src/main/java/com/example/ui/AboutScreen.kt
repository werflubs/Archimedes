package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CosmicBackground
import com.example.ui.theme.CosmicPrimary
import com.example.ui.theme.CosmicSurface
import com.example.ui.theme.CosmicSurfaceVariant
import com.example.ui.theme.CosmicText
import com.example.ui.theme.CosmicTextSecondary
import com.example.ui.theme.CosmicTextVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen() {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("О программе", color = CosmicText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CosmicBackground)
            )
        },
        containerColor = CosmicBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(28.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                "ABOUT PROGRAM",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CosmicPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "v1.0.0 • Kotlin • Jetpack Compose",
                                fontSize = 10.sp,
                                color = CosmicTextSecondary
                            )
                        }
                        
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(CosmicPrimary, com.example.ui.theme.CosmicOnPrimary)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text("ARC", fontSize = 8.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        }
                    }
                    
                    val annotatedString = buildAnnotatedString {
                        append(" Built with ")
                        withStyle(style = SpanStyle(color = CosmicPrimary)) {
                            append("Gemini 3.1 Pro Preview")
                        }
                        append(". Licensed under MIT.\nAuthored by ")
                        // we push a string annotation to track clicks.
                        pushStringAnnotation(tag = "URL", annotation = "https://github.com/werflubs")
                        withStyle(style = SpanStyle(color = CosmicPrimary, textDecoration = TextDecoration.Underline)) {
                            append("werflub")
                        }
                        pop()
                        append(".")
                    }
                    
                    ClickableText(
                        text = annotatedString,
                        style = androidx.compose.ui.text.TextStyle(
                            color = CosmicTextVariant,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        ),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                .firstOrNull()?.let { annotation ->
                                    uriHandler.openUri(annotation.item)
                                }
                        }
                    )
                }
            }
        }
    }
}
