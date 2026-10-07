package com.example.ui.calai

import android.graphics.Bitmap
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.rounded.CameraAlt
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import com.example.ui.theme.BorderColor
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
    var capturedPhoto by remember { mutableStateOf<Bitmap?>(null) }

    // Real Camera Launchers
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            capturedPhoto = bitmap
            onTriggerScan(selectedPreset)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            // Fallback to sample preset if camera permission denied
            onTriggerScan(selectedPreset)
        }
    }

    fun launchRealCamera() {
        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

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
        // Camera Viewfinder Canvas
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
                // If real photo was captured, show it directly in the viewfinder!
                if (capturedPhoto != null) {
                    Image(
                        bitmap = capturedPhoto!!.asImageBitmap(),
                        contentDescription = "Captured Meal",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(32.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Moving laser scanner line
                if (isScanning) {
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
                }

                // Corner Reticle Brackets
                CornerReticles()

                // Center food focus helper
                if (capturedPhoto == null && !isScanning) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(68.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.08f))
                                .clickable { launchRealCamera() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CameraAlt,
                                contentDescription = "Open Real Camera",
                                tint = Color(0xFFA5A3FF),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Tap to open camera & scan",
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Hold still over your plate for auto macro detection",
                            fontFamily = InterFontFamily,
                            fontSize = 11.5.sp,
                            color = Color(0xFF8E8E93),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                // Scanning Progress HUD
                if (isScanning) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xD9000000))
                            .border(1.dp, Color(0x33A5A3FF), RoundedCornerShape(20.dp))
                            .padding(horizontal = 24.dp, vertical = 18.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = Color(0xFFA5A3FF),
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "AI Analyzing Nutrients...",
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Estimating calories, protein, carbs & fat",
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                color = Color(0xFFA0A0AB)
                            )
                        }
                    }
                }
            }
        }

        // Top Controls: Flash, Model Badge, Flip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
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
                    text = "Cal AI Vision Camera",
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            IconButton(
                onClick = { launchRealCamera() },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0x33000000))
            ) {
                Icon(
                    imageVector = Icons.Rounded.FlipCameraIos,
                    contentDescription = "Switch Camera",
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
            // Sample Food Presets (Quick selection)
            Text(
                text = "Tap to open camera or select food dish:",
                fontFamily = InterFontFamily,
                fontSize = 11.sp,
                color = Color(0xFF8E8E93),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp)
            ) {
                items(sampleFoodDatabase) { preset ->
                    val isSelected = selectedPreset?.name == preset.name
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) AccentPurple else Color(0x22FFFFFF))
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.White.copy(alpha = 0.5f) else Color(0x22FFFFFF),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                selectedPreset = preset
                                onTriggerScan(preset)
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = preset.name,
                            fontFamily = InterFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            // Real Camera Shutter Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Open Camera Button
                IconButton(
                    onClick = { launchRealCamera() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CameraAlt,
                        contentDescription = "Open Real Camera",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Shutter Circle Button (Opens camera & triggers AI scan)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .padding(6.dp)
                        .clip(CircleShape)
                        .background(AccentPurple)
                        .clickable(enabled = !isScanning) {
                            launchRealCamera()
                        }
                        .testTag("camera_shutter_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = "Capture Photo",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Preset scan fallback button
                IconButton(
                    onClick = { onTriggerScan(selectedPreset) },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0x22FFFFFF))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.PhotoLibrary,
                        contentDescription = "Analyze Preset",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
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
                    onRetakeClick = {
                        onDismissScan()
                        launchRealCamera()
                    }
                )
            }
        }
    }
}

@Composable
fun ScanResultCard(
    result: AiScanResult,
    onPortionChange: (Float) -> Unit,
    onLogClick: () -> Unit,
    onRetakeClick: () -> Unit
) {
    val multiplier = result.portionMultiplier
    val displayCalories = (result.calories * multiplier).toInt()
    val displayProtein = (result.protein * multiplier).toInt()
    val displayCarbs = (result.carbs * multiplier).toInt()
    val displayFat = (result.fat * multiplier).toInt()
    val displayGrams = (result.servingSizeGrams * multiplier).toInt()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(Color.White)
            .padding(24.dp)
            .testTag("scan_result_card")
    ) {
        Column {
            // Drag / Close bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(AccentGreen.copy(alpha = 0.12f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Confidence: ${result.confidence}%",
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentGreen
                    )
                }

                IconButton(onClick = onRetakeClick, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Rounded.Close, contentDescription = "Close", tint = TextMuted)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dish Name & Calorie Hero
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
                        text = "$displayGrams g serving • ${result.suggestedMealType.displayName}",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$displayCalories",
                        fontFamily = InterFontFamily,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                    Text(
                        text = "kcal",
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Macro Breakdown Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScanMacroItem("Protein", "${displayProtein}g", AccentRed, Modifier.weight(1f))
                ScanMacroItem("Carbs", "${displayCarbs}g", AccentPurple, Modifier.weight(1f))
                ScanMacroItem("Fat", "${displayFat}g", AccentGreen, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Portion Multiplier Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Portion Size",
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMain
                )
                Text(
                    text = "${String.format(Locale.US, "%.1f", multiplier)}x ($displayGrams g)",
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentPurple
                )
            }

            Slider(
                value = multiplier,
                onValueChange = onPortionChange,
                valueRange = 0.5f..2.5f,
                steps = 7,
                colors = SliderDefaults.colors(
                    thumbColor = AccentPurple,
                    activeTrackColor = AccentPurple,
                    inactiveTrackColor = Color(0xFFF2F2F7)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onRetakeClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2F2F7)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text("Retake", fontFamily = InterFontFamily, color = TextMain, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onLogClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111115)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(2f)
                        .height(50.dp)
                ) {
                    Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Log to Today", fontFamily = InterFontFamily, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ScanMacroItem(name: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF9F9FB))
            .border(0.5.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = name, fontFamily = InterFontFamily, fontSize = 11.sp, color = TextMuted)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontFamily = InterFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun CornerReticles() {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
                .size(24.dp)
                .border(width = 2.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(topStart = 8.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .size(24.dp)
                .border(width = 2.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(topEnd = 8.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
                .size(24.dp)
                .border(width = 2.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(bottomStart = 8.dp))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .size(24.dp)
                .border(width = 2.dp, color = Color(0xFFA5A3FF), shape = RoundedCornerShape(bottomEnd = 8.dp))
        )
    }
}
