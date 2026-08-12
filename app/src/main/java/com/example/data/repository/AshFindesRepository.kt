package com.example.data.repository

import android.content.Context
import android.net.Uri
import com.example.data.local.AshFindesDao
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatSessionEntity
import com.example.data.local.GeneratedImageEntity
import com.example.data.local.UserSettingsEntity
import com.example.data.model.AgentRegistry
import com.example.data.remote.GeminiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

class AshFindesRepository(
    private val dao: AshFindesDao,
    private val geminiService: GeminiService
) {

    val allSessions: Flow<List<ChatSessionEntity>> = dao.getAllSessions()
    val userSettings: Flow<UserSettingsEntity?> = dao.getUserSettings()
    val allGeneratedImages: Flow<List<GeneratedImageEntity>> = dao.getAllGeneratedImages()

    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessageEntity>> {
        return dao.getMessagesForSession(sessionId)
    }

    suspend fun createNewSession(
        type: String = "SEARCH",
        title: String = "New Search",
        agentId: String? = null
    ): String = withContext(Dispatchers.IO) {
        val id = UUID.randomUUID().toString()
        val session = ChatSessionEntity(
            id = id,
            title = title,
            type = type,
            agentId = agentId,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        dao.insertSession(session)
        id
    }

    suspend fun sendMessage(
        sessionId: String,
        prompt: String,
        imageUri: Uri? = null
    ): String = withContext(Dispatchers.IO) {
        // 1. Insert user message
        val userMsg = ChatMessageEntity(
            sessionId = sessionId,
            sender = "USER",
            text = prompt,
            imageUri = imageUri?.toString(),
            timestamp = System.currentTimeMillis()
        )
        dao.insertMessage(userMsg)

        // 2. Get session & history
        val session = dao.getSessionById(sessionId)
        val historyEntities = dao.getMessagesListForSession(sessionId)
        val historyList = historyEntities.map { Pair(it.sender, it.text) }

        // Update session title if default
        if (session != null && (session.title == "New Search" || session.title == "New Chat")) {
            val autoTitle = if (prompt.length > 28) prompt.take(28) + "..." else prompt
            dao.renameSession(sessionId, autoTitle)
        }

        // Determine system instruction
        val systemInstruction = when (session?.type) {
            "AGENT" -> AgentRegistry.getAgentById(session.agentId).systemPrompt
            "VISION" -> "You are Ash Vision. Analyze the uploaded image and user questions with precision."
            else -> "You are Ash Findes, an elite AI assistant. Provide direct, natural, accurate responses."
        }

        // 3. Generate response
        val responseText = if (imageUri != null) {
            geminiService.analyzeImage(imageUri, prompt)
        } else {
            geminiService.generateChatResponse(systemInstruction, historyList, prompt)
        }

        // 4. Save Ash message
        val ashMsg = ChatMessageEntity(
            sessionId = sessionId,
            sender = "ASH",
            text = responseText,
            timestamp = System.currentTimeMillis()
        )
        dao.insertMessage(ashMsg)

        // Update session timestamp
        if (session != null) {
            dao.updateSession(session.copy(updatedAt = System.currentTimeMillis()))
        }

        responseText
    }

    suspend fun renameSession(sessionId: String, title: String) = withContext(Dispatchers.IO) {
        dao.renameSession(sessionId, title)
    }

    suspend fun deleteSession(sessionId: String) = withContext(Dispatchers.IO) {
        dao.deleteMessagesForSession(sessionId)
        dao.deleteSession(sessionId)
    }

    suspend fun togglePinSession(sessionId: String, currentIsPinned: Boolean) = withContext(Dispatchers.IO) {
        dao.setPinned(sessionId, !currentIsPinned)
    }

    suspend fun saveGeneratedImage(
        prompt: String,
        style: String,
        aspectRatio: String,
        imageUrl: String
    ) = withContext(Dispatchers.IO) {
        val entity = GeneratedImageEntity(
            id = UUID.randomUUID().toString(),
            prompt = prompt,
            style = style,
            aspectRatio = aspectRatio,
            imageUrl = imageUrl,
            createdAt = System.currentTimeMillis()
        )
        dao.insertGeneratedImage(entity)
    }

    suspend fun deleteGeneratedImage(imageId: String) = withContext(Dispatchers.IO) {
        dao.deleteGeneratedImage(imageId)
    }

    suspend fun ensureDefaultUser() = withContext(Dispatchers.IO) {
        val current = dao.getUserSettingsOnce()
        if (current == null) {
            val owner = UserSettingsEntity(
                id = 1,
                userName = "Ash Owner",
                email = "ashbuild55@gmail.com",
                isOwner = true,
                isPremium = true,
                tierName = "Lifetime Owner",
                usageCount = 0
            )
            dao.insertUserSettings(owner)
        }
    }

    suspend fun updateUserSettings(settings: UserSettingsEntity) = withContext(Dispatchers.IO) {
        dao.insertUserSettings(settings)
    }
}
