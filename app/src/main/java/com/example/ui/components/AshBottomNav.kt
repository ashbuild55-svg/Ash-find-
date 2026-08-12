package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.viewmodel.AppTab
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

private data class NavItemData(
    val tab: AppTab,
    val label: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun AshBottomNav(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    val items = listOf(
        NavItemData(AppTab.SEARCH, "Search", Icons.Default.Search, "nav_search"),
        NavItemData(AppTab.STUDIO, "Studio", Icons.Default.Palette, "nav_studio"),
        NavItemData(AppTab.AGENTS, "AI Agents", Icons.Default.Psychology, "nav_agents"),
        NavItemData(AppTab.ACCOUNT, "Account", Icons.Default.Person, "nav_account")
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            DarkNavyBg.copy(alpha = 0.95f),
                            CardSurface.copy(alpha = 0.98f)
                        )
                    )
                )
                .border(1.dp, GlassBorder, RoundedCornerShape(28.dp))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = selectedTab == item.tab
                val iconTint by animateColorAsState(
                    targetValue = if (isSelected) CyanGlow else TextMuted,
                    animationSpec = tween(250), label = "iconTint"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) TextPrimary else TextMuted,
                    animationSpec = tween(250), label = "textColor"
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (isSelected) CyanPrimary.copy(alpha = 0.15f) else Color.Transparent
                        )
                        .clickable { onTabSelected(item.tab) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag(item.tag),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}
