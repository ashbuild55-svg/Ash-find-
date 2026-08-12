package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserSettingsEntity
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
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AccountScreen(
    viewModel: MainViewModel,
    onOpenAdmin: () -> Unit
) {
    val context = LocalContext.current
    val userSettings by viewModel.userSettings.collectAsState()

    var nameInput by remember(userSettings) { mutableStateOf(userSettings?.userName ?: "Ash Owner") }
    var emailInput by remember(userSettings) { mutableStateOf(userSettings?.email ?: "ashbuild55@gmail.com") }

    val isOwner = emailInput.trim().equals("ashbuild55@gmail.com", ignoreCase = true) || userSettings?.isOwner == true

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBg)
            .padding(bottom = 80.dp)
    ) {
        // Title Header
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
                        imageVector = Icons.Default.Person,
                        contentDescription = "Account",
                        tint = CyanGlow,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "User Profile & Account",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Manage credentials, subscription & admin settings",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        // Profile Card
        item {
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                GlassCard(
                    cornerRadius = 24.dp,
                    glowColor = CyanGlow.copy(alpha = 0.25f)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(CyanGlow, ElectricBlue)
                                        )
                                    )
                                    .border(2.dp, CyanGlow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = nameInput.take(1).uppercase(),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = nameInput,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    if (isOwner) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.Shield,
                                            contentDescription = "Owner",
                                            tint = CyanGlow,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = emailInput,
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(CyanPrimary.copy(alpha = 0.15f))
                                        .border(1.dp, GlassBorderActive, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isOwner) "Lifetime Owner (Unlimited)" else "Free Tier",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CyanGlow
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Edit Name / Email fields
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Display Name") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("account_name_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanPrimary,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Address") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("account_email_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyanPrimary,
                                unfocusedBorderColor = GlassBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val current = userSettings ?: UserSettingsEntity()
                                viewModel.updateUserSettings(
                                    current.copy(
                                        userName = nameInput.ifBlank { "Ash User" },
                                        email = emailInput.ifBlank { "ashbuild55@gmail.com" },
                                        isOwner = isOwner,
                                        isPremium = isOwner || current.isPremium,
                                        tierName = if (isOwner) "Lifetime Owner" else "Free Tier"
                                    )
                                )
                                Toast.makeText(context, "Account updated successfully!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_account_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CyanPrimary
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Save Account Settings", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Owner Admin Panel Entry Card (Only for Owner ashbuild55@gmail.com)
        if (isOwner) {
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                    GlassCard(
                        cornerRadius = 24.dp,
                        glowColor = CyanGlow,
                        borderColor = CyanGlow,
                        onClick = onOpenAdmin
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                                colors = listOf(CyanGlow, ElectricBlue)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AdminPanelSettings,
                                        contentDescription = "Admin Panel",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = "Owner Admin Panel",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Analytics, user management & connector links",
                                        fontSize = 11.sp,
                                        color = CyanGlow
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Open Admin",
                                tint = CyanGlow,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Subscription Plans Section
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                Text(
                    text = "Subscription Plans",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = "Unlock priority response speed & unlimited image generations",
                    fontSize = 12.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Monthly Card
                GlassCard(
                    cornerRadius = 20.dp,
                    glowColor = ElectricBlue.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Monthly Premium",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "₹300 / month",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyanGlow
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Unlimited AI Queries\n• Priority Gemini 3.5\n• Fast Image Studio",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(CardSurfaceElevated)
                                .border(1.dp, GlassBorder, RoundedCornerShape(14.dp))
                                .clickable {
                                    Toast.makeText(context, "Payment prepared for Stripe / Razorpay integration", Toast.LENGTH_LONG).show()
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Subscribe",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CyanGlow
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Yearly Card
                GlassCard(
                    cornerRadius = 20.dp,
                    glowColor = CyanGlow.copy(alpha = 0.3f),
                    borderColor = GlassBorderActive
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Yearly Pro",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SuccessGreen.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("SAVE 50%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                                }
                            }
                            Text(
                                text = "₹1800 / year",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyanGlow
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• All Monthly Features\n• Full Multimodal Vision\n• Exclusive AI Agents",
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(CyanPrimary, ElectricBlue)
                                    )
                                )
                                .clickable {
                                    Toast.makeText(context, "Payment prepared for Stripe / Razorpay integration", Toast.LENGTH_LONG).show()
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Subscribe",
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
