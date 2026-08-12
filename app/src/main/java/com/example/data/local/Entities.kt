package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_sessions")
data class ChatSessionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // "SEARCH", "VISION", "AGENT"
    val agentId: String? = null,
    val isPinned: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val sender: String, // "USER" or "ASH"
    val text: String,
    val imageUri: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "generated_images")
data class GeneratedImageEntity(
    @PrimaryKey val id: String,
    val prompt: String,
    val style: String,
    val aspectRatio: String,
    val imageUrl: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Ash User",
    val email: String = "ashbuild55@gmail.com",
    val isOwner: Boolean = true,
    val isPremium: Boolean = true,
    val tierName: String = "Lifetime Owner",
    val usageCount: Int = 0
)
