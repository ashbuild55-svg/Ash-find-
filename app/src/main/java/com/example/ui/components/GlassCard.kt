package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.GlassBorder

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    borderColor: Color = GlassBorder,
    glowColor: Color = Color.Unspecified,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    var boxModifier = modifier
        .fillMaxWidth()
        .shadow(
            elevation = 12.dp,
            shape = shape,
            ambientColor = glowColor.takeIf { it != Color.Unspecified } ?: Color.Black,
            spotColor = CyanGlow
        )
        .clip(shape)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    CardSurfaceElevated.copy(alpha = 0.85f),
                    CardSurface.copy(alpha = 0.95f)
                )
            )
        )
        .border(
            width = 1.dp,
            brush = Brush.horizontalGradient(
                colors = listOf(
                    borderColor,
                    borderColor.copy(alpha = 0.15f)
                )
            ),
            shape = shape
        )

    if (onClick != null) {
        boxModifier = boxModifier.clickable { onClick() }
    }

    Box(
        modifier = boxModifier.padding(16.dp),
        content = content
    )
}
