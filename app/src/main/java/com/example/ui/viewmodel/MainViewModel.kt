package com.example.data.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AshFindesDatabase
import com.example.data.local.ChatMessageEntity
import com.example.data.local.ChatSessionEntity
import com.example.data.local.GeneratedImageEntity
import com.example.data.local.UserSettingsEntity
import com.example.data.remote.GeminiService
import com.example.data.repository.AshFindesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    SEARCH, STUDIO, AGENTS, ACCOUNT
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AshFindesDatabase.getInstance(application)
    private val geminiService = GeminiService(application)
    val repository = AshFindesRepository(db.dao(), geminiService)

    private val _selectedTab = MutableStateFlow(AppTab.SEARCH)
    val selectedTab: StateFlow<AppTab> = _selectedTab.asStateFlow()

    private val _activeSessionId = MutableStateFlow<String?>(null)
    val activeSessionId: StateFlow<String?> = _activeSessionId.asStateFlow()

    private val _historySearchQuery = MutableStateFlow("")
    val historySearchQuery: StateFlow<String> = _historySearchQuery.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _visionImageUri = MutableStateFlow<Uri?>(null)
    val visionImageUri: StateFlow<Uri?> = _visionImageUri.asStateFlow()

    private val _showAdminPanel = MutableStateFlow(false)
    val showAdminPanel: StateFlow<Boolean> = _showAdminPanel.asStateFlow()

    val allSessions: StateFlow<List<ChatSessionEntity>> = repository.allSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userSettings: StateFlow<UserSettingsEntity?> = repository.userSettings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val generatedImages: StateFlow<List<GeneratedImageEntity>> = repository.allGeneratedImages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeMessages: StateFlow<List<ChatMessageEntity>> = _activeSessionId
        .flatMapLatest { id ->
            if (id != null) repository.getMessagesForSession(id) else flowOf(emptyList())
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.ensureDefaultUser()
        }
    }

    fun selectTab(tab: AppTab) {
        _selectedTab.value = tab
    }

    fun setVisionImage(uri: Uri?) {
        _visionImageUri.value = uri
    }

    fun setHistorySearchQuery(query: String) {
        _historySearchQuery.value = query
    }

    fun toggleAdminPanel(show: Boolean) {
        _showAdminPanel.value = show
    }

    fun openSession(sessionId: String) {
        _activeSessionId.value = sessionId
    }

    fun closeActiveSession() {
        _activeSessionId.value = null
    }

    fun startNewSearch(initialPrompt: String? = null, type: String = "SEARCH", agentId: String? = null) {
        viewModelScope.launch {
            val title = when (type) {
                "AGENT" -> "Agent Chat"
                "VISION" -> "AI Vision Search"
                else -> if (!initialPrompt.isNull_or_blank()) initialPrompt!!.take(24) else "New Search"
            }
            val id = repository.createNewSession(type = type, title = title, agentId = agentId)
            _activeSessionId.value = id
            if (!initialPrompt.isNull_or_blank()) {
                sendUserPrompt(initialPrompt!!, _visionImageUri.value)
            }
        }
    }

    fun sendUserPrompt(prompt: String, imageUri: Uri? = null) {
        val sessionId = _activeSessionId.value ?: return
        if (prompt.isBlank() && imageUri == null) return

        viewModelScope.launch {
            _isGenerating.value = true
            try {
                repository.sendMessage(sessionId, prompt, imageUri)
                _visionImageUri.value = null
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun renameSession(sessionId: String, title: String) {
        viewModelScope.launch {
            repository.renameSession(sessionId, title)
        }
    }

    fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            if (_activeSessionId.value == sessionId) {
                _activeSessionId.value = null
            }
            repository.deleteSession(sessionId)
        }
    }

    fun togglePinSession(sessionId: String, isPinned: Boolean) {
        viewModelScope.launch {
            repository.togglePinSession(sessionId, isPinned)
        }
    }

    fun saveGeneratedImage(prompt: String, style: String, aspectRatio: String, imageUrl: String) {
        viewModelScope.launch {
            repository.saveGeneratedImage(prompt, style, aspectRatio, imageUrl)
        }
    }

    fun deleteGeneratedImage(imageId: String) {
        viewModelScope.launch {
            repository.deleteGeneratedImage(imageId)
        }
    }

    fun updateUserSettings(settings: UserSettingsEntity) {
        viewModelScope.launch {
            repository.updateUserSettings(settings)
        }
    }

    private fun String?.isNull_or_blank(): Boolean = this == null || this.isBlank()
}
