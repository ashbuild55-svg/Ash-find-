package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.data.local.ChatMessageEntity
import com.example.ui.components.FormattedMarkdownText
import com.example.ui.components.TypingIndicator
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
fun ChatView(
    title: String,
    messages: List<ChatMessageEntity>,
    isGenerating: Boolean,
    onSendMessage: (String, Uri?) -> Unit,
    onNewChat: () -> Unit,
    onBack: () -> Unit,
    onRenameChat: (String) -> Unit,
    onDeleteChat: () -> Unit,
    onTogglePin: () -> Unit,
    isPinned: Boolean = false
) {
    var textInput by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf(title) }

    val listState = rememberLazyListState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .imePadding()
    ) {
        // Chat Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkNavyBg)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("chat_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(CyanPrimary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Ash AI",
                        tint = CyanGlow,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        maxLines = 1
                    )
                    Text(
                        text = "Ash Findes Assistant",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }

            Row {
                IconButton(onClick = onTogglePin) {
                    Icon(
                        imageVector = if (isPinned) Icons.Default.PushPin else Icons.Default.Pin,
                        contentDescription = "Pin",
                        tint = if (isPinned) CyanGlow else TextMuted
                    )
                }
                IconButton(onClick = { showRenameDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Rename",
                        tint = TextMuted
                    )
                }
                IconButton(onClick = onDeleteChat) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = TextMuted
                    )
                }
                IconButton(onClick = onNewChat, modifier = Modifier.testTag("new_chat_button")) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Chat",
                        tint = CyanGlow
                    )
                }
            }
        }

        // Messages List
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            if (messages.isEmpty() && !isGenerating) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Start",
                            tint = CyanPrimary.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Start chatting with Ash Findes",
                            fontSize = 15.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(8.dp)) }

                    items(messages) { msg ->
                        val isUser = msg.sender == "USER"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            if (!isUser) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(CyanPrimary.copy(alpha = 0.2f))
                                        .border(1.dp, CyanGlow, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Ash Avatar",
                                        tint = CyanGlow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 18.dp,
                                            topEnd = 18.dp,
                                            bottomStart = if (isUser) 18.dp else 4.dp,
                                            bottomEnd = if (isUser) 4.dp else 18.dp
                                        )
                                    )
                                    .background(
                                        if (isUser) {
                                            Brush.linearGradient(
                                                colors = listOf(CyanPrimary, ElectricBlue)
                                            )
                                        } else {
                                            Brush.verticalGradient(
                                                colors = listOf(CardSurfaceElevated, CardSurface)
                                            )
                                        }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isUser) GlassBorderActive else GlassBorder,
                                        shape = RoundedCornerShape(18.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Column {
                                    if (msg.imageUri != null) {
                                        Image(
                                            painter = rememberAsyncImagePainter(msg.imageUri),
                                            contentDescription = "Attached photo",
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp)
                                                .clip(RoundedCornerShape(12.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    if (isUser) {
                                        Text(
                                            text = msg.text,
                                            color = Color.White,
                                            fontSize = 14.sp,
                                            lineHeight = 20.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    } else {
                                        FormattedMarkdownText(text = msg.text)
                                    }
                                }
                            }
                        }
                    }

                    if (isGenerating) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(CyanPrimary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Thinking",
                                        tint = CyanGlow,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TypingIndicator()
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(12.dp)) }
                }
            }
        }

        // Selected Image Preview Thumbnail
        AnimatedVisibility(visible = selectedImageUri != null) {
            selectedImageUri?.let { uri ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkNavyBg)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected image preview",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Image attached for Vision query",
                        fontSize = 12.sp,
                        color = CyanGlow,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { selectedImageUri = null }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove photo",
                            tint = TextMuted
                        )
                    }
                }
            }
        }

        // Fixed Chat Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkNavyBg)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { photoPickerLauncher.launch("image/*") },
                modifier = Modifier.testTag("attach_photo_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AddPhotoAlternate,
                    contentDescription = "Attach image",
                    tint = CyanGlow
                )
            }

            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = {
                    Text(
                        text = if (selectedImageUri != null) "Ask about this photo..." else "Type message...",
                        fontSize = 14.sp,
                        color = TextMuted
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_text_input"),
                shape = RoundedCornerShape(24.dp),
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

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if ((textInput.isNotBlank() || selectedImageUri != null) && !isGenerating) {
                        onSendMessage(textInput.trim(), selectedImageUri)
                        textInput = ""
                        selectedImageUri = null
                    }
                },
                enabled = (textInput.isNotBlank() || selectedImageUri != null) && !isGenerating,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if ((textInput.isNotBlank() || selectedImageUri != null) && !isGenerating) {
                            CyanPrimary
                        } else {
                            CardSurfaceElevated
                        }
                    )
                    .testTag("send_message_button")
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = CyanGlow,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    // Rename Dialog
    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text("Rename Conversation", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyanPrimary,
                        unfocusedBorderColor = GlassBorder,
                        focusedTextColor = TextPrimary
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (renameText.isNotBlank()) {
                            onRenameChat(renameText.trim())
                        }
                        showRenameDialog = false
                    }
                ) {
                    Text("Rename", color = CyanGlow, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            },
            containerColor = CardSurface
        )
    }
}
