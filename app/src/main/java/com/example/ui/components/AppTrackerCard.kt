package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AvTimer
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppTracker
import com.example.model.TrackerIconType
import com.example.ui.theme.BorderColor
import com.example.ui.theme.CardBackground
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.ProgressTrackColor
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextMuted

@Composable
fun AppTrackerCard(
    tracker: AppTracker,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 3D Physical tactile press animations
    val animatedScale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 420f),
        label = "card_scale"
    )
    val animatedElevation by animateFloatAsState(
        targetValue = if (isPressed) 2f else 12f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "card_elevation"
    )
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isPressed) 3.5f else 0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 420f),
        label = "card_offset_y"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .offset(y = animatedOffsetY.dp)
            .scale(animatedScale)
            // 3D tactile layered shadow
            .shadow(
                elevation = animatedElevation.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x18000000),
                spotColor = Color(0x24000000)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White,
                        Color(0xFFFAFAFD)
                    )
                )
            )
            // 3D dual-layer crisp edge
            .border(0.75.dp, Color(0x1C000000), RoundedCornerShape(22.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = tracker.progressColor.copy(alpha = 0.18f)),
                onClick = onClick
            )
            .padding(12.dp)
            .testTag("app_card_${tracker.id}")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon Box with 3D bevel effect
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = Color(0x0E000000),
                        spotColor = Color(0x18000000)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(tracker.iconBgLight, Color.White)
                        )
                    )
                    .border(0.5.dp, Color(0x14000000), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                val iconVector = when (tracker.iconType) {
                    TrackerIconType.CALORIE -> Icons.Rounded.LocalFireDepartment
                    TrackerIconType.HABIT -> Icons.Rounded.Check
                    TrackerIconType.SLEEP -> Icons.Rounded.Bedtime
                    TrackerIconType.WATER -> Icons.Rounded.WaterDrop
                    TrackerIconType.WORKOUT -> Icons.Rounded.FitnessCenter
                    TrackerIconType.FASTING -> Icons.Rounded.AvTimer
                    TrackerIconType.MEDITATION -> Icons.Rounded.SelfImprovement
                }
                Icon(
                    imageVector = iconVector,
                    contentDescription = tracker.title,
                    tint = tracker.iconColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            // App Name - Extra Bold & prominent for high readability
            Text(
                text = tracker.title,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF0F0F14),
                lineHeight = 14.sp,
                letterSpacing = (-0.3).sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Stat & Progress Bar - Bold & distinct
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F0F14),
                                fontSize = 11.5.sp
                            )
                        ) {
                            append(tracker.currentFormatted)
                        }
                        append(" ")
                        withStyle(
                            SpanStyle(
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF6B7280),
                                fontSize = 10.sp
                            )
                        ) {
                            append(tracker.targetFormatted)
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 3D Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(ProgressTrackColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(tracker.progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(tracker.progressColor)
                    )
                }
            }
        }
    }
}
