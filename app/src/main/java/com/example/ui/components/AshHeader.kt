package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorderActive
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AshHeader(
    userName: String = "Ash User",
    isOwner: Boolean = true,
    tierName: String = "Lifetime Owner",
    onAdminClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(CyanGlow.copy(alpha = 0.4f), ElectricBlue.copy(alpha = 0.2f))
                        )
                    )
                    .border(1.5.dp, CyanPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_icon_1786512018855),
                    contentDescription = "Ash Finds Logo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ash Findes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(CyanPrimary.copy(alpha = 0.15f))
                            .border(1.dp, GlassBorderActive, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (isOwner) "PRO" else "AI",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanGlow
                        )
                    }
                }

                Text(
                    text = "Hello, $userName",
                    fontSize = 13.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (isOwner && onAdminClick != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(CyanPrimary.copy(alpha = 0.2f), ElectricBlue.copy(alpha = 0.2f))
                        )
                    )
                    .border(1.dp, CyanGlow.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Owner Shield",
                        tint = CyanGlow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Owner",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CyanGlow
                    )
                }
            }
        }
    }
}
