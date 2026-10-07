package com.example.ui.calai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FlashOn
import androidx.compose.material.icons.rounded.FlipCameraIos
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AiScanResult
import com.example.model.MealType
import com.example.model.SampleFoodPreset
import com.example.model.sampleFoodDatabase
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentGreen
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentRed
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun CalAiCameraScreen(
    isScanning: Boolean,
    scanResult: AiScanResult?,
    onTriggerScan: (SampleFoodPreset?) -> Unit,
    onPortionChange: (Float) -> Unit,
    onLogScan: () -> Unit,
    onDismissScan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPreset by remember { mutableStateOf<SampleFoodPreset?>(sampleFoodDatabase.first()) }
    var flashActive by remember { mutableStateOf(false) }

    // Animated laser scanner beam
    val infiniteTransition = rememberInfiniteTransition(label = "laser_transition")
    val laserOffsetFraction by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_sweep"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F14))
            .testTag("cal_ai_camera_screen")
    ) {
        // Futuristic Camera Viewfinder Canvas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 120.dp, top = 20.dp, start = 20.dp, end = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            // Viewfinder Frame
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF22232E), Color(0xFF14141A))
                        )
                    )
                    .border(1.5.dp, Color(0x33FFFFFF), RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Moving laser scanner line
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(3.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (380 * laserOffsetFraction).dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, Color(0xFF5856D6), Color(0xFF00C6FF), Color(0xFF5856D6), Color.Transparent)
                            )
                        )
                )

                // Corner Reticle Brackets
                CornerReticles()

                // Center food focus helper
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFA5A3FF),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = if (isScanning) "AI Analyzing Nutrition..." else "Point Camera at Food",
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = selectedPreset?.name ?: "Align meal inside box",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    if (isScanning) {
                        Spacer(modifier = Modifier.height(16.dp))
                        CircularProgressIndicator(
                            color = AccentPurple,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }

        // Camera Top Controls (Flash, Switch, Gallery)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { flashActive = !flashActive },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (flashActive) Color.White else Color(0x33000000))
            ) {
                Icon(
                    imageVector = Icons.Rounded.FlashOn,
                    contentDescription = "Flash",
                    tint = if (flashActive) Color.Black else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x66000000))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Cal AI Vision 2.0",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            IconButton(
                onClick = { /* Switch camera */ },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0x33000000))
            ) {
                Icon(
                    imageVector = Icons.Rounded.FlipCameraIos,
                    contentDescription = "Flip Camera",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Bottom Controls: Preset Carousel + Big Shutter Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sample Food Presets (Quick select to simulate real scan test)
            Text(
                text = "Tap a dish to scan or take photo:",
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sampleFoodDatabase) { preset ->
                    val isSelected = preset == selectedPreset
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) AccentPurple else Color(0x33FFFFFF))
                            .border(1.dp, if (isSelected) Color.White else Color.Transparent, RoundedCornerShape(16.dp))
                            .clickable {
                                selectedPreset = preset
                                onTriggerScan(preset)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = preset.name,
                            fontFamily = InterFontFamily,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Shutter Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Gallery button
                IconButton(
                    onClick = { onTriggerScan(selectedPreset) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoLibrary,
                        contentDescription = "Gallery",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Shutter Circle Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(AccentPurple)
                        .clickable(enabled = !isScanning) {
                            onTriggerScan(selectedPreset)
                        }
                        .testTag("camera_shutter_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "Capture & Analyze",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.size(48.dp))
            }
        }

        // AI Scan Result Pop-up Card (Slides in from bottom over the camera)
        AnimatedVisibility(
            visible = scanResult != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            scanResult?.let { result ->
                ScanResultCard(
                    result = result,
                    onPortionChange = onPortionChange,
                    onLogClick = onLogScan,
                    onDismiss = onDismissScan
                )
            }
        }
    }
}

@Composable
fun CornerReticles() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Top Left
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(24.dp)
                .border(width = 3.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(topStart = 8.dp))
        )
        // Top Right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .border(width = 3.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(topEnd = 8.dp))
        )
        // Bottom Left
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .size(24.dp)
                .border(width = 3.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(bottomStart = 8.dp))
        )
        // Bottom Right
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(24.dp)
                .border(width = 3.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(bottomEnd = 8.dp))
        )
    }
}

@Composable
fun ScanResultCard(
    result: AiScanResult,
    onPortionChange: (Float) -> Unit,
    onLogClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val currentCalories = (result.calories * result.portionMultiplier).toInt()
    val currentProtein = (result.protein * result.portionMultiplier).toInt()
    val currentCarbs = (result.carbs * result.portionMultiplier).toInt()
    val currentFat = (result.fat * result.portionMultiplier).toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            )
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(Color.White)
            .padding(24.dp)
            .testTag("ai_scan_result_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AccentGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "✓ ${result.confidence}% Match",
                            fontFamily = InterFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentGreen
                        )
                    }
                    Text(
                        text = "Cal AI Vision",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Identified Dish Name & Big Calories
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result.dishName,
                        fontFamily = InterFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMain
                    )
                    Text(
                        text = "${(result.servingSizeGrams * result.portionMultiplier).toInt()}g estimated portion",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(AccentPurple.copy(alpha = 0.12f))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "$currentCalories kcal",
                        fontFamily = InterFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Macro Breakdown Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScanMacroBox("Protein", "${currentProtein}g", AccentBlue, Modifier.weight(1f))
                ScanMacroBox("Carbs", "${currentCarbs}g", AccentOrange, Modifier.weight(1f))
                ScanMacroBox("Fats", "${currentFat}g", AccentRed, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Portion Multiplier Options (0.5x, 1x, 1.5x, 2x)
            Text(
                text = "Adjust Portion Size:",
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextMain
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(0.5f, 1.0f, 1.5f, 2.0f).forEach { mult ->
                    val isSelected = result.portionMultiplier == mult
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) AccentPurple else Color(0xFFF2F2F7))
                            .clickable { onPortionChange(mult) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${mult}x",
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextMain
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Log Meal Button
            Button(
                onClick = onLogClick,
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("log_scanned_meal_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White)
                    Text(
                        text = "Log Meal to Today",
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ScanMacroBox(name: String, amount: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF9F9FB))
            .border(0.5.dp, Color(0x14000000), RoundedCornerShape(14.dp))
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = name, fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
            Text(text = amount, fontFamily = InterFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
