package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Agent
import com.example.data.model.AgentRegistry
import com.example.data.viewmodel.MainViewModel
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

@Composable
fun AgentsScreen(
    viewModel: MainViewModel
) {
    val activeSessionId by viewModel.activeSessionId.collectAsState()
    val allSessions by viewModel.allSessions.collectAsState()
    val activeMessages by viewModel.activeMessages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()

    val activeSession = allSessions.find { it.id == activeSessionId }

    // If an agent chat is currently open, render ChatView overlay
    if (activeSessionId != null && activeSession != null) {
        ChatView(
            title = activeSession.title,
            messages = activeMessages,
            isGenerating = isGenerating,
            onSendMessage = { text, uri ->
                viewModel.sendUserPrompt(text, uri)
            },
            onNewChat = {
                viewModel.startNewSearch(type = "AGENT", agentId = activeSession.agentId)
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(bottom = 80.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CyanPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "AI Agents",
                        tint = CyanGlow,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "AI Specialized Agents",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Domain experts tailored with dedicated system prompts",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        items(AgentRegistry.agents) { agent ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                GlassCard(
                    cornerRadius = 20.dp,
                    glowColor = CyanGlow.copy(alpha = 0.15f),
                    onClick = {
                        viewModel.startNewSearch(
                            initialPrompt = null,
                            type = "AGENT",
                            agentId = agent.id
                        )
                    }
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(CyanPrimary.copy(alpha = 0.18f))
                                        .border(1.dp, GlassBorderActive, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = agent.icon,
                                        contentDescription = agent.name,
                                        tint = CyanGlow,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = agent.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = agent.description,
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Launch Agent",
                                tint = CyanGlow,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Suggested Prompt:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val samplePrompt = agent.suggestedPrompts.firstOrNull() ?: ""
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CardSurface)
                                .clickable {
                                    viewModel.startNewSearch(
                                        initialPrompt = samplePrompt,
                                        type = "AGENT",
                                        agentId = agent.id
                                    )
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Prompt",
                                    tint = CyanGlow,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = samplePrompt,
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
