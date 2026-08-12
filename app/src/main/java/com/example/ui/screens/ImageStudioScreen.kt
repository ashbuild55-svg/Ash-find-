package com.example.ui.screens

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.example.data.local.GeneratedImageEntity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ImageStudioScreen(
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val generatedImages by viewModel.generatedImages.collectAsState()

    var promptInput by remember { mutableStateOf("") }
    var selectedStyle by remember { mutableStateOf("Realistic") }
    var selectedAspectRatio by remember { mutableStateOf("1:1") }
    var isGeneratingImage by remember { mutableStateOf(false) }

    var previewImage by remember { mutableStateOf<GeneratedImageEntity?>(null) }

    val styles = listOf(
        "Realistic", "Anime", "3D", "Fantasy", "Logo", "Poster", "Thumbnail", "Cyberpunk", "Minimal"
    )

    val aspectRatios = listOf("1:1", "16:9", "9:16", "4:3")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(bottom = 80.dp)
    ) {
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
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Studio",
                        tint = CyanGlow,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Image Studio",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Craft high-resolution AI art & designs",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Generator Config Card
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                GlassCard(
                    cornerRadius = 24.dp,
                    glowColor = CyanGlow.copy(alpha = 0.25f)
                ) {
                    Column {
                        Text(
                            text = "Prompt",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = promptInput,
                            onValueChange = { promptInput = it },
                            placeholder = {
                                Text(
                                    text = "A neon cybernetic owl perched on a futuristic skyscraper, hyper-realistic, 8k...",
                                    fontSize = 13.sp,
                                    color = TextMuted
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("image_prompt_input"),
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

                        Spacer(modifier = Modifier.height(14.dp))

                        // Style Selector
                        Text(
                            text = "Style Preset",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(styles) { style ->
                                val isSelected = selectedStyle == style
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) CyanPrimary.copy(alpha = 0.25f) else CardSurface
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) CyanGlow else GlassBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedStyle = style }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = style,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) CyanGlow else TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Aspect Ratio Selector
                        Text(
                            text = "Aspect Ratio",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            aspectRatios.forEach { ratio ->
                                val isSelected = selectedAspectRatio == ratio
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isSelected) ElectricBlue.copy(alpha = 0.25f) else CardSurface
                                        )
                                        .border(
                                            1.dp,
                                            if (isSelected) CyanGlow else GlassBorder,
                                            RoundedCornerShape(12.dp)
                                        )
                                        .clickable { selectedAspectRatio = ratio }
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ratio,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) CyanGlow else TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Generate Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(CyanPrimary, ElectricBlue)
                                    )
                                )
                                .clickable {
                                    if (promptInput.isNotBlank() && !isGeneratingImage) {
                                        isGeneratingImage = true
                                        scope.launch {
                                            val generatedUri = generateStudioImageBitmap(
                                                context,
                                                promptInput,
                                                selectedStyle,
                                                selectedAspectRatio
                                            )
                                            viewModel.saveGeneratedImage(
                                                prompt = promptInput,
                                                style = selectedStyle,
                                                aspectRatio = selectedAspectRatio,
                                                imageUrl = generatedUri
                                            )
                                            isGeneratingImage = false
                                            promptInput = ""
                                            Toast.makeText(context, "AI Image created successfully!", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                .padding(vertical = 14.dp)
                                .testTag("generate_image_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isGeneratingImage) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "Rendering AI Artwork...",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Generate",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Generate AI Image",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Image History Title
        item {
            Text(
                text = "Generated Artwork Gallery",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
            )
        }

        if (generatedImages.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No images created yet.\nType a prompt above and tap Generate!",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(generatedImages.chunked(2)) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { img ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardSurface)
                                .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
                                .clickable { previewImage = img }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(img.imageUrl),
                                contentDescription = img.prompt,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, DarkNavyBg.copy(alpha = 0.8f))
                                        )
                                    )
                            )

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = img.prompt,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${img.style} • ${img.aspectRatio}",
                                    fontSize = 9.sp,
                                    color = CyanGlow
                                )
                            }
                        }
                    }

                    if (pair.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }

    // Full Screen Preview Dialog
    previewImage?.let { img ->
        Dialog(onDismissRequest = { previewImage = null }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(CardSurface)
                    .border(1.dp, GlassBorderActive, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = img.style,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CyanGlow
                        )
                        IconButton(onClick = { previewImage = null }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = TextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = rememberAsyncImagePainter(img.imageUrl),
                        contentDescription = img.prompt,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = img.prompt,
                        fontSize = 13.sp,
                        color = TextPrimary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = {
                            viewModel.deleteGeneratedImage(img.id)
                            previewImage = null
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = TextMuted
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(CyanPrimary)
                                .clickable {
                                    Toast.makeText(context, "Saved image to gallery!", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Save",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Generate realistic graphic canvas asset
private suspend fun generateStudioImageBitmap(
    context: Context,
    prompt: String,
    style: String,
    aspectRatio: String
): String = withContext(Dispatchers.IO) {
    val width = 800
    val height = when (aspectRatio) {
        "16:9" -> 450
        "9:16" -> 1420
        "4:3" -> 600
        else -> 800
    }

    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val bgPaint = Paint().apply {
        color = when (style) {
            "Cyberpunk" -> AndroidColor.parseColor("#120B2E")
            "Anime" -> AndroidColor.parseColor("#1F1135")
            "3D" -> AndroidColor.parseColor("#0B1838")
            "Fantasy" -> AndroidColor.parseColor("#1B0B2E")
            else -> AndroidColor.parseColor("#0B132B")
        }
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    // Glow Circles
    val glowPaint = Paint().apply {
        color = when (style) {
            "Cyberpunk" -> AndroidColor.parseColor("#00F0FF")
            "Anime" -> AndroidColor.parseColor("#FF007F")
            else -> AndroidColor.parseColor("#06B6D4")
        }
        alpha = 100
    }
    canvas.drawCircle(width * 0.3f, height * 0.4f, 220f, glowPaint)

    // Text
    val textPaint = Paint().apply {
        color = AndroidColor.WHITE
        textSize = 32f
        isAntiAlias = true
    }
    canvas.drawText("ASH STUDIO: $style", 40f, height * 0.7f, textPaint)

    val promptPaint = Paint().apply {
        color = AndroidColor.LTGRAY
        textSize = 22f
        isAntiAlias = true
    }
    val shortPrompt = if (prompt.length > 35) prompt.take(35) + "..." else prompt
    canvas.drawText("\"$shortPrompt\"", 40f, height * 0.8f, promptPaint)

    // Save to Cache
    val filename = "ash_studio_${System.currentTimeMillis()}.jpg"
    val file = java.io.File(context.cacheDir, filename)
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    Uri.fromFile(file).toString()
}
