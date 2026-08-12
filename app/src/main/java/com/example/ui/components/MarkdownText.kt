package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FormattedMarkdownText(
    text: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val parts = text.split("```")

    Column(modifier = modifier) {
        parts.forEachIndexed { index, part ->
            if (index % 2 == 1) {
                // Code Block
                val codeLines = part.lines()
                val lang = codeLines.firstOrNull()?.trim() ?: "code"
                val codeContent = if (codeLines.size > 1) codeLines.drop(1).joinToString("\n") else part

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ObsidianBg)
                        .border(1.dp, GlassBorder, RoundedCornerShape(12.dp))
                ) {
                    Column {
                        // Language Header + Copy Button
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkNavyBg)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = lang.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow
                            )
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Code", codeContent)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy code",
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Code Body
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(12.dp)
                        ) {
                            Text(
                                text = codeContent,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            } else {
                // Standard Text with Bold / Bullet parsing
                if (part.isNotBlank()) {
                    val annotatedString = buildAnnotatedString {
                        var cursor = 0
                        val boldRegex = "\\*\\*(.*?)\\*\\*".toRegex()
                        val matches = boldRegex.findAll(part)

                        for (match in matches) {
                            val start = match.range.first
                            val end = match.range.last + 1
                            if (start > cursor) {
                                append(part.substring(cursor, start))
                            }
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = CyanGlow)) {
                                append(match.groupValues[1])
                            }
                            cursor = end
                        }
                        if (cursor < part.length) {
                            append(part.substring(cursor))
                        }
                    }

                    Text(
                        text = annotatedString,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}
