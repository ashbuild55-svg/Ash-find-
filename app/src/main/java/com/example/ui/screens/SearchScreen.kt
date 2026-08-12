package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.local.ChatSessionEntity
import com.example.data.viewmodel.AppTab
import com.example.data.viewmodel.MainViewModel
import com.example.ui.components.AshHeader
import com.example.ui.components.GlassCard
import com.example.ui.theme.CardSurface
import com.example.ui.theme.CardSurfaceElevated
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.DarkNavyBg
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.GlassBorder
import com.example.ui.theme.GlassBorderActive
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

private data class QuickActionItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)

@Composable
fun SearchScreen(
    viewModel: MainViewModel,
    onNavigateTab: (AppTab) -> Unit,
    onOpenAdmin: () -> Unit
) {
    val activeSessionId by viewModel.activeSessionId.collectAsState()
    val allSessions by viewModel.allSessions.collectAsState()
    val activeMessages by viewModel.activeMessages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val historySearchQuery by viewModel.historySearchQuery.collectAsState()

    var homeSearchText by remember { mutableStateOf("") }
    var attachedVisionUri by remember { mutableStateOf<Uri?>(null) }

    val activeSession = allSessions.find { it.id == activeSessionId }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        attachedVisionUri = uri
    }

    // If active session is open, render ChatView overlay
    if (activeSessionId != null && activeSession != null) {
        ChatView(
            title = activeSession.title,
            messages = activeMessages,
            isGenerating = isGenerating,
            onSendMessage = { text, uri ->
                viewModel.sendUserPrompt(text, uri)
            },
            onNewChat = {
                viewModel.startNewSearch()
            },
            onBack = {
                viewModel.closeActiveSession()
            },
            onRenameChat = { newTitle ->
                viewModel.renameSession(activeSession.id, newTitle)
            },
            onDeleteChat = {
                viewModel.deleteSession(activeSession.id)
            },
            onTogglePin = {
                viewModel.togglePinSession(activeSession.id, activeSession.isPinned)
            },
            isPinned = activeSession.isPinned
        )
        return
    }

    // Home Search Screen View
    val suggestedPrompts = listOf(
        "Explain Quantum Computing & AI chips simply",
        "Build a modern Jetpack Compose dark layout",
        "Analyze market growth trends in AI SaaS 2026",
        "Draft a compelling executive elevator pitch",
        "Debug Kotlin coroutines exception propagation"
    )

    val quickActions = listOf(
        QuickActionItem("Web Search", Icons.Default.Language, CyanGlow) {
            if (homeSearchText.isNotBlank()) viewModel.startNewSearch(homeSearchText, "SEARCH")
        },
        QuickActionItem("Research", Icons.Default.TravelExplore, ElectricBlue) {
            viewModel.startNewSearch("Deep research summary: " + homeSearchText.ifBlank { "Latest AI breakthroughs" }, "SEARCH")
        },
        QuickActionItem("Image Studio", Icons.Default.Palette, Color(0xFFEC4899)) {
            onNavigateTab(AppTab.STUDIO)
        },
        QuickActionItem("AI Vision", Icons.Default.Visibility, Color(0xFF10B981)) {
            photoPickerLauncher.launch("image/*")
        },
        QuickActionItem("AI Agents", Icons.Default.Psychology, Color(0xFF8B5CF6)) {
            onNavigateTab(AppTab.AGENTS)
        }
    )

    val filteredSessions = allSessions.filter {
        if (historySearchQuery.isBlank()) true
        else it.title.contains(historySearchQuery, ignoreCase = true)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(bottom = 80.dp)
    ) {
        // Top Header
        item {
            AshHeader(
                userName = userSettings?.userName ?: "Ash User",
                isOwner = userSettings?.isOwner == true,
                tierName = userSettings?.tierName ?: "Lifetime Owner",
                onAdminClick = onOpenAdmin
            )
        }

        // Hero Search Section
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "What would you like to search today?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Large Glass Search Card Box
                GlassCard(
                    cornerRadius = 24.dp,
                    glowColor = CyanGlow.copy(alpha = 0.3f)
                ) {
                    Column {
                        OutlinedTextField(
                            value = homeSearchText,
                            onValueChange = { homeSearchText = it },
                            placeholder = {
                                Text(
                                    text = "Ask anything, code, math, research or vision...",
                                    fontSize = 14.sp,
                                    color = TextMuted
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("home_search_box"),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanPrimary,
                                unfocusedBorderColor = GlassBorder,
                                focusedContainerColor = CardSurfaceElevated,
                                unfocusedContainerColor = CardSurface,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { photoPickerLauncher.launch("image/*") },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(CardSurfaceElevated)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddPhotoAlternate,
                                        contentDescription = "Attach Vision Photo",
                                        tint = if (attachedVisionUri != null) CyanGlow else TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                if (attachedVisionUri != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Photo attached",
                                        fontSize = 11.sp,
                                        color = CyanGlow,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(CyanPrimary, ElectricBlue)
                                        )
                                    )
                                    .clickable {
                                        if (homeSearchText.isNotBlank() || attachedVisionUri != null) {
                                            viewModel.startNewSearch(
                                                initialPrompt = homeSearchText.ifBlank { "Analyze this image" },
                                                type = if (attachedVisionUri != null) "VISION" else "SEARCH"
                                            )
                                            homeSearchText = ""
                                            attachedVisionUri = null
                                        }
                                    }
                                    .padding(horizontal = 18.dp, vertical = 10.dp)
                                    .testTag("search_submit_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Search AI",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Search",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Action Buttons
        item {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Quick Actions",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item { Spacer(modifier = Modifier.width(20.dp)) }
                    items(quickActions) { action ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardSurface)
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                                .clickable { action.onClick() }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = action.icon,
                                    contentDescription = action.title,
                                    tint = action.color,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = action.title,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.width(20.dp)) }
                }
            }
        }

        // Suggested Prompts
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text(
                    text = "Suggested Prompts",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                suggestedPrompts.forEach { prompt ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(CardSurfaceElevated.copy(alpha = 0.6f))
                            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                            .clickable {
                                viewModel.startNewSearch(prompt, "SEARCH")
                            }
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Prompt",
                                    tint = CyanGlow,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = prompt,
                                    fontSize = 13.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Run",
                                tint = TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }

        // Search History Section
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Searches",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    IconButton(
                        onClick = { viewModel.startNewSearch() },
                        modifier = Modifier.testTag("new_search_fab")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "New Search",
                            tint = CyanGlow
                        )
                    }
                }

                // History Search Filter Field
                if (allSessions.isNotEmpty()) {
                    OutlinedTextField(
                        value = historySearchQuery,
                        onValueChange = { viewModel.setHistorySearchQuery(it) },
                        placeholder = { Text("Filter history...", fontSize = 12.sp, color = TextMuted) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search history",
                                tint = TextMuted,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyanPrimary,
                            unfocusedBorderColor = GlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        singleLine = true
                    )
                }
            }
        }

        if (filteredSessions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No search conversations yet.\nType a query above to start!",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(filteredSessions) { session ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            if (session.isPinned) CyanPrimary.copy(alpha = 0.1f) else CardSurface
                        )
                        .border(
                            1.dp,
                            if (session.isPinned) GlassBorderActive else GlassBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable {
                            viewModel.openSession(session.id)
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (session.isPinned) Icons.Default.PushPin else Icons.Default.Search,
                                contentDescription = "Search item",
                                tint = if (session.isPinned) CyanGlow else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = session.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = session.type,
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Row {
                            IconButton(
                                onClick = { viewModel.togglePinSession(session.id, session.isPinned) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (session.isPinned) Icons.Default.PushPin else Icons.Default.Pin,
                                    contentDescription = "Pin",
                                    tint = if (session.isPinned) CyanGlow else TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            IconButton(
                                onClick = { viewModel.deleteSession(session.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
