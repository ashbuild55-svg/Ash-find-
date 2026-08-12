package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AshFindesDao {
    // Chat Sessions
    @Query("SELECT * FROM chat_sessions ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllSessions(): Flow<List<ChatSessionEntity>>

    @Query("SELECT * FROM chat_sessions WHERE id = :sessionId LIMIT 1")
    suspend fun getSessionById(sessionId: String): ChatSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ChatSessionEntity)

    @Update
    suspend fun updateSession(session: ChatSessionEntity)

    @Query("DELETE FROM chat_sessions WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Query("UPDATE chat_sessions SET isPinned = :isPinned WHERE id = :sessionId")
    suspend fun setPinned(sessionId: String, isPinned: Boolean)

    @Query("UPDATE chat_sessions SET title = :title WHERE id = :sessionId")
    suspend fun renameSession(sessionId: String, title: String)

    // Messages
    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    suspend fun getMessagesListForSession(sessionId: String): List<ChatMessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages WHERE sessionId = :sessionId")
    suspend fun deleteMessagesForSession(sessionId: String)

    // Generated Images
    @Query("SELECT * FROM generated_images ORDER BY createdAt DESC")
    fun getAllGeneratedImages(): Flow<List<GeneratedImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedImage(image: GeneratedImageEntity)

    @Query("DELETE FROM generated_images WHERE id = :imageId")
    suspend fun deleteGeneratedImage(imageId: String)

    // User Settings
    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    fun getUserSettings(): Flow<UserSettingsEntity?>

    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    suspend fun getUserSettingsOnce(): UserSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserSettings(settings: UserSettingsEntity)
}
